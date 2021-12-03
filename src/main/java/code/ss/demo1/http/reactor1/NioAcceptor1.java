package code.ss.demo1.http.reactor1;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NioAcceptor1 extends Thread {

    final ServerSocketChannel serverSocketChannel;
    final Selector selector;
    public NioAcceptor1(ServerSocketChannel serverSocketChannel, Selector selector1) {
        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector1;
    }

    @Override
    public void run() {
        try {
            SocketChannel accept = serverSocketChannel.accept();
            new IOHandler(selector, accept);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
