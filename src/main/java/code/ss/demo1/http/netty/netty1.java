package code.ss.demo1.http.netty;

import io.netty.bootstrap.BootstrapConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class netty1 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        NamedThreadFactory worker = new NamedThreadFactory("worker");
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup(1,new NamedThreadFactory("boss"));
        NioEventLoopGroup eventExecutors1 = new NioEventLoopGroup(2, worker);

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(eventExecutors, eventExecutors1)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new EchoServerHandler());
//                        p.addLast(new MessageDecoder());
//                        p.addLast(new MessageEncoder());
                        //p.addLast(new LoggingHandler(LogLevel.INFO));
                        //p.addLast("encoder", new MessageEncoder());
                        //p.addLast("decoder", new MessageDecoder());
                        //p.addFirst(new LineBasedFrameDecoder(65535));
//                        p.addLast(new EchoServerHandler());
                    }
                });
        ChannelFuture bind = serverBootstrap.bind(9001);
        ChannelFuture sync = bind.sync();
        sync.get();
        for (; ; ) {

        }

    }
}
