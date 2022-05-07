package com.github.kastkest.cloud.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EchoServer {

    public static void main(String[] args) {

        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup light = new NioEventLoopGroup(1);
        EventLoopGroup hard = new NioEventLoopGroup();

        try {
            bootstrap.group(light, hard)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new EchoPipeline());

            ChannelFuture channelFuture = bootstrap.bind(8189).sync();
            channelFuture.channel()
                    .closeFuture()
                    .sync();
            log.info("Server started...");
        } catch (InterruptedException e) {
            log.error("", e);
        } finally {
            light.shutdownGracefully();
            hard.shutdownGracefully();
        }
    }
}