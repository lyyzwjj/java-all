package com.wjjzst.io.nio.netty.learn.ch6.exceptionspread;

import com.wjjzst.io.nio.netty.learn.ch6.BusinessException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author: Wjj
 * @Date: 2021/6/3 11:00 上午
 * @desc:
 */
public class ExceptionCaughtHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // ..

        if (cause instanceof BusinessException) {
            System.out.println("BusinessException");
        }
    }
}
