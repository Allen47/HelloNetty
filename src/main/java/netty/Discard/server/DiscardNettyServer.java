package netty.Discard.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author Msq
 * @date 2021/3/16 - 15:26
 */
public class DiscardNettyServer {

    // 读取property中是否有port这个变量，如果有就用;没有就用8686
    static final int PORT = Integer.parseInt(System.getProperty("port", "8686"));

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // 用于处理客户端的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // 用来处理连接后的I/O读写请求和系统任务
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast((new DiscardNettyServerHandler()));
                        }
                    });

            ChannelFuture f = b.bind(PORT).sync(); // “同步”启动方式
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }
}
