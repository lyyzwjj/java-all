package com.wjjzst.common.utils.id;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

/**
 * Twitter_Snowflake<br>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 0000000000 0000000000 0000000000 0000000000 00000000 - 000000 - 0000000000 <br>
 * 40位时间截(毫秒级)，注意，40位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。40位的时间截，可以使用34年，年T = (1L << 40) / (1000L * 60 * 60 * 24 * 365) = 34<br>
 * 14位的数据机器位，可以部署在16384个节点，包括8位datacenterId和6位workerId<br>
 * 10位序列，毫秒内的计数，10位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生1024个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生37.5万ID左右。
 */
public class SnowflakeIdWorker {

    // ==============================Fields===========================================
    /**
     * 开始时间截 (2021-01-01)
     */
    private static final long TWEPOCH = 1609430400000L;

    /**
     * 机器id所占的位数
     */
    private static final long WORKERIDBITS = 6L;

    /**
     * 数据标识id所占的位数
     */
    private static final long DATACENTERIDBITS = 8L;

    /**
     * 支持的最大机器id，结果是63 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private static final long MAXWORKERID = ~(-1L << WORKERIDBITS);

    /**
     * 支持的最大数据标识id，结果是255
     */
    private static final long MAXDATACENTERID = ~(-1L << DATACENTERIDBITS);

    /**
     * 序列在id中占的位数
     */
    private static final long SEQUENCEBITS = 10L;

    /**
     * 机器ID向左移10位
     */
    private static final long WORKERIDSHIFT = SEQUENCEBITS;

    /**
     * 数据标识id向左移16位(10+6)
     */
    private static final long DATACENTERIDSHIFT = SEQUENCEBITS + WORKERIDBITS;

    /**
     * 时间截向左移26位(10+6+10)
     */
    private static final long TIMESTAMPLEFTSHIFT = SEQUENCEBITS + WORKERIDBITS + DATACENTERIDBITS;

    /**
     * 生成序列的掩码，这里为1023 (0b1111111111=0x3ff=1023)
     */
    private static final long SEQUENCEMASK = ~(-1L << SEQUENCEBITS);

    /**
     * 工作机器ID(0~63)
     */
    private final long workerId;

    /**
     * 数据中心ID(0~255)
     */
    private final long datacenterId;

    /**
     * 毫秒内序列(0~1023)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    /**
     * 所有分表分表的最小公倍数
     * 原本是 0 -> 4095
     * 现在是commonSharding -> 4095
     * 现在每秒最多生成id个数 4095 - commonSharding
     * 线上分表160 100 最小公倍数800
     */
    private final long commonSharding;

    //==============================Constructors=====================================

    /**
     * 构造函数
     *
     * @param workerId     工作ID (0~63)
     * @param datacenterId 数据中心ID (0~255)
     */
    public SnowflakeIdWorker(long workerId, long datacenterId, int commonSharding) {
        if (workerId > MAXWORKERID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAXWORKERID));
        }
        if (datacenterId > MAXDATACENTERID || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MAXDATACENTERID));
        }
        if (commonSharding > SEQUENCEMASK || commonSharding < 0) {
            throw new IllegalArgumentException(String.format("commonSharding Id can't be greater than %d or less than 0", SEQUENCEMASK));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.commonSharding = commonSharding;
    }

    // ==============================Methods==========================================

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCEMASK;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
                sequence = (int) (commonSharding * Math.random());
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            System.out.println("change");
            // sequence = 0;
            sequence = (int) (commonSharding * Math.random());
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - TWEPOCH) << TIMESTAMPLEFTSHIFT) //
                | (datacenterId << DATACENTERIDSHIFT) //
                | (workerId << WORKERIDSHIFT) //
                | sequence;
    }

    public static void main(String[] args) throws InterruptedException {
        // average();
        each(); // 375/ms
    }

    public static void each() {
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(31, 255, 160);
        for (int i = 0; i < 100000; i++) {
            long l = snowflakeIdWorker.nextId();
            System.out.println(Long.toBinaryString(l));
        }
    }

    public static void average() throws InterruptedException {
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(63, 255, 160);
        long size = 160L;
        Map<Long, LongAdder> map = new HashMap<>((int) size);
        for (long i = 0; i < size; i++) {
            map.put(i, new LongAdder());
        }
        int batch = 100000;
        int thread = 32;
        BigDecimal all = new BigDecimal(batch * thread);
        ExecutorService exec = Executors.newFixedThreadPool(thread);
        CountDownLatch c = new CountDownLatch(thread);
        for (int index = 0; index < thread; index++) {
            Runnable run = () -> {
                for (int i = 0; i < batch; i++) {
                    long l = snowflakeIdWorker.nextId();
                    System.out.println(Long.toBinaryString(l));
                    map.get(l % size).increment();
                }
                c.countDown();
            };
            exec.execute(run);
        }
        c.await();
        exec.shutdown();
        for (Map.Entry<Long, LongAdder> entry : map.entrySet()) {
            Long mod = entry.getKey();
            long subBatchSize = entry.getValue().longValue();
            BigDecimal divide = new BigDecimal(subBatchSize).divide(all, 16, RoundingMode.UNNECESSARY);
            System.out.printf("当前余数mod: %d,百分比为: %.6f,subBatchSize: %d\n", mod, divide, subBatchSize);
        }
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

}
