package com.github.kastkest.cloud.netty.serialization;

import com.github.kastkest.cloud.model.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileHandler extends SimpleChannelInboundHandler<AbstractMassage> {

    private final Path serverDir = Paths.get(".", "server_files");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush((new ListMessage(serverDir)));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractMassage msg) throws Exception {
        log.info("received: {} message", msg.getMessageType().getName());

        if (msg instanceof FileMessage file) {
            Files.write(serverDir.resolve(file.getName()), file.getBytes());
            ctx.writeAndFlush(new ListMessage(serverDir));
        }
        if (msg instanceof DownloadMessage dm) {
            ctx.writeAndFlush(new FileMessage(serverDir.resolve(dm.getName())));
        }
        if (msg instanceof DeleteMessage dm) {
            Files.delete(serverDir.resolve(dm.getName()));
            ctx.writeAndFlush(new ListMessage(serverDir));
        }
    }
}
