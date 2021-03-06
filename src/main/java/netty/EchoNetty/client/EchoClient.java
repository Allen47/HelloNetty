package netty.EchoNetty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author Msq
 * @date 2021/3/16 - 18:52
 */
public class EchoClient {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8686"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    public static void main(String[] args) throws InterruptedException {

        // configure the client
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());
                            p.addLast(new EchoClientHandler());
                        }
                    });

            // start the client
            ChannelFuture f = b.connect(HOST, PORT).sync();

            // send data to Server
            String cli_msg = "Hello EchoNetty!";
            f.channel().writeAndFlush(cli_msg); // 第一条消息
            f.channel().writeAndFlush(
                    Unpooled.copiedBuffer("Hello world!", CharsetUtil.UTF_8)); // 第二条消息

            // wait until connection is closed
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
