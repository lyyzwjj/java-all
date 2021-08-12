package com.wjjzst.designpattern._05singleton单例模式;

public class SafeFullSingletonPatternDemo {
	
	public static class Singleton {
		
		private static Singleton instance;
		
		private Singleton() {
			
		}
		
		// 不是完美的
		// 因为不同的JVM的编译器的问题，可能导致说，这个情况下，还是线程不安全的
		// 具体的我不再这儿讲，因为涉及到复杂的JVM内部的原理
		
		public static Singleton getInstance() {
			// 如果线程1和线程2都执行到了这一步，然后此时线程1判断发现还是null
			// 线程2此时判断发现instance == null，也会进去
			if(instance == null) {
				// 线程1就会进来，此时线程1停止，切换到线程2
				// 线程2也会进来，此时切换到线程1
				
				// 线程1，发现这里需要加锁， 在这里加锁，获取到了这个锁
				// 线程2过来，线程2发现说，我也想要在这里加锁，发现说这个锁被人加了，线程2挂起等待别人释放锁
				// 此时切换回线程2，线程2发现锁被释放，然后在这里加锁
				synchronized(SafeFullSingletonPatternDemo.class) {
					// 线程1就进来了，此时切换到线程2
					// 切换回线程1，线程1此时在这里，再次判断，instance == null
					// 线程2就进来了，double check，如果这里没有instance == null的判断，那么线程2就会再次创建
					// 一个实例
					// 但是这里是双重检查，线程2又判断了一下，instance == null？否，不是null
					if(instance == null) {
						// 线程1就会进来，创建一个实例
						instance = new Singleton();
					}
				}
			}
			// 这边出来以后，线程1就释放锁了
			// 线程2跳出来，直接获取一个instance返回了，这个instance就是之前线程1创建的实例
			return instance;
		}	
		
	}
	
}
