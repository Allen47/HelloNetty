package netty.EchoNetty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author Msq
 * @date 2021/3/16 - 16:23
 */
public class EchoServer {

    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", "8686"));

    public static void main(String[] args) throws Exception {

        /*
        final SslContext sslctx;
        if(SSL){
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslctx = SslContextBuilder.forServer(
                    ssc.certificate(), ssc.privateKey()).build();
        }else {
            sslctx = null;
        } */

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.SO_BACKLOG, 100)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();
                        p.addLast(new StringDecoder());
                        p.addLast(new StringEncoder());
                        p.addLast(new EchoServerHandler());
                    }
                });

        ChannelFuture f = b.bind(PORT).sync();
        // wait until server-socket is close
        f.channel().closeFuture().sync();

        // shut down all event-loop to terminate all threads
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
