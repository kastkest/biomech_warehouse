package com.github.kastkest.cloud.netty;

import com.github.kastkest.cloud.netty.handlers.FirstInHandler;
import com.github.kastkest.cloud.netty.handlers.OutHandler;
import com.github.kastkest.cloud.netty.handlers.SecondInHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class EchoPipeline extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(
            new OutHandler(),
                new FirstInHandler(),
                new SecondInHandler()
        );
    }
}
