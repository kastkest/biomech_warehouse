package com.github.kastkest.cloud.netty;

import com.github.kastkest.cloud.netty.handlers.EchoHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class EchoPipeline extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(
            new StringEncoder(),
                new StringDecoder(),
                new EchoHandler()
        );
    }
}
