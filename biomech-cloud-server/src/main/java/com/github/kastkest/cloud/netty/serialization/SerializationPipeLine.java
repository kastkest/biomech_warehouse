package com.github.kastkest.cloud.netty.serialization;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;


// implements Serializable

public class SerializationPipeLine extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(
                new ObjectDecoder(500*1024*1024,ClassResolvers.cacheDisabled(null)),
                new ObjectEncoder(),
                new FileHandler()
        );
    }
}
