package code.ss.demo1.http.reactor1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NioReactor1 extends Thread {
    final Selector selector;
    final ServerSocketChannel serverSocketChannel;

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 4, 100L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    public NioReactor1(int port) throws IOException {
         selector = Selector.open();
         serverSocketChannel = ServerSocketChannel.open();
         serverSocketChannel.configureBlocking(false);
         InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
         serverSocketChannel.socket().bind(inetSocketAddress);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public static void main(String[] args) throws IOException {
        new NioReactor1(9000).start();
        for (; ; ) {

        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey selectionKey : selectionKeys) {
                if (selectionKey.isAcceptable()) {
                    new NioAcceptor1(serverSocketChannel, selector).run();
                }else{
                    IOHandler attachment = (IOHandler) selectionKey.attachment();
                    if (attachment.selectionKey.isReadable()) {
//                            attachment.socketChannel.read();
                    }else{
//                            attachment.socketChannel.write();
                    }
                    //会导致读写事件并发
                    executor.execute(() -> {
                        ((IOHandler)selectionKey.attachment()).run();
                    });
                }
            }
            selectionKeys.clear();
        }
    }
}
