package netty.Discard.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author Msq
 * @date 2021/3/16 - 16:02
 */
public class DiscardNettyClientHandler extends SimpleChannelInboundHandler<Object> {

    private ByteBuf content;
    private ChannelHandlerContext ctx;

    private final ChannelFutureListener trafficGenerator = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
            if(channelFuture.isSuccess())
                generateTraffic();
            else{ // not success
                channelFuture.cause().printStackTrace();
                channelFuture.channel().close();
            }
        }
    };

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("Client channel active...");
        this.ctx = ctx;

        // init msg
        content = ctx.alloc().directBuffer(DiscardNettyClient.SIZE)
                .writeZero(DiscardNettyClient.SIZE);

        // send the msg
        generateTraffic();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        // 本应该读取的，但是服务器不会发送数据过来，即使发送了也会被discard
    }

    public void generateTraffic(){
        // flush the outbound buffer to the socket
        // once flush, generate the same amount of traffic again
        ctx.writeAndFlush(content.retainedDuplicate())
                .addListener(trafficGenerator);
    }


}
