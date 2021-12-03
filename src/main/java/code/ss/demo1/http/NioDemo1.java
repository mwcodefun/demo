package code.ss.demo1.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import static java.nio.channels.SelectionKey.*;

import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class NioDemo1 {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress(9000);
        serverSocketChannel.socket().bind(inetSocketAddress);

        serverSocketChannel.register(selector, OP_ACCEPT);
        while (true) {
            int selectedNum = selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                if ((next.readyOps() & SelectionKey.OP_ACCEPT) == OP_ACCEPT) {
                    ServerSocketChannel channel = (ServerSocketChannel) next.channel();
                    SocketChannel accept = channel.accept();
                    accept.configureBlocking(false);
                    accept.register(selector, OP_READ);
                    accept.write(ByteBuffer.wrap("Welcome\r\n".getBytes()));
                    iterator.remove();
                } else if ((next.readyOps() & OP_READ) == OP_READ) {
                    SocketChannel channel = (SocketChannel) next.channel();
                    ByteBuffer allocate = ByteBuffer.allocate(100);
                    channel.read(allocate);
                    allocate.put("fllow u.\r\n".getBytes());
                    allocate.flip();
                    channel.write(allocate);
                }

            }
        }
    }
}
