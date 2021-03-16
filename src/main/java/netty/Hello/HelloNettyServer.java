package netty.Hello;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Msq
 * @date 2021/3/10 - 21:24
 */
public class HelloNettyServer {

    private int port;

    public HelloNettyServer(int port){
        this.port = port;
    }

    public void run() throws Exception{
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new HelloNettyServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f= b.bind(port).sync();
            f.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("开始建立Netty服务器...");
        int port = 8080;
        if(args.length > 0)
                port = Integer.parseInt(args[0]);
        new HelloNettyServer(port).run();
    }
}
