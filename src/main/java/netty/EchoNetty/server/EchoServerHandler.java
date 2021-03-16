package netty.EchoNetty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author Msq
 * @date 2021/3/16 - 16:49
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    int count = 0; // 消息数

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("Server channel active...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        System.out.println("Server channel read...");
        System.out.println("From client:" + msg); // 迷惑：输出了很长，且有效的消息在末尾
        String result = "server -> client";
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(result.getBytes());
        ctx.channel().writeAndFlush(buf);
        System.out.println("=========");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        System.out.println(count++ + "Server channel read complete!");
        ctx.flush();
    }
}
