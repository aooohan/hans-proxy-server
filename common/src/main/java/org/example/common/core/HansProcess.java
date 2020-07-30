package org.example.common.core;

import io.netty.channel.ChannelHandlerContext;
import org.example.common.protocol.HansMessage;

/**
 * @author ：lihan
 * @description：
 * @date ：2020/7/29 9:26
 */
public interface HansProcess {
    /**
     * 注册操作
     * @param ctx
     * @param msg
     */
    void doRegister(ChannelHandlerContext ctx, HansMessage msg);

    /**
     * 注册回应操作
     *
     * @param msg
     */
    void doRegisterResp(ChannelHandlerContext ctx, HansMessage msg);
    /**
     * 专门处理连接操作
     * @param ctx
     * @param msg
     */
    void doConnect(ChannelHandlerContext ctx, HansMessage msg);

    /**
     * 处理断开连接
     * @param ctx
     * @param msg
     */
    void doDisConnect(ChannelHandlerContext ctx, HansMessage msg);

    /**
     * 数据转发
     * @param ctx
     * @param msg
     */
    void doForward(ChannelHandlerContext ctx, HansMessage msg);

    /**
     * 心跳检测
     * @param ctx
     * @param msg
     */
    void doHeart(ChannelHandlerContext ctx, HansMessage msg);

}
