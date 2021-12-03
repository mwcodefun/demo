package code.ss.demo1.http.reactor1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class IOHandler implements Runnable{
    final Selector selector;
    final SocketChannel socketChannel;
    final SelectionKey selectionKey;
    final ByteBuffer writeBuffer;
    final ByteBuffer readBuffer;
    private AtomicInteger state;
    public IOHandler(Selector selector, SocketChannel socketChannel) throws IOException {
        this.selector = selector;
        this.socketChannel = socketChannel;
        state = new AtomicInteger();

        socketChannel.configureBlocking(false);
        SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
        selectionKey.attach(this);
        this.selectionKey = selectionKey;
        writeBuffer = ByteBuffer.allocateDirect(1024);
        readBuffer = ByteBuffer.allocateDirect(1024);
        writeBuffer.put("Welcome ss telnet server.\r\n".getBytes());
        writeBuffer.flip();
        doWriteData();
    }

    private void printBufferStatus(ByteBuffer buffer) {
        System.out.println(
                String.format("WriteBuffer cap:%d,position:%d,limit:%d,remain:%d", buffer.capacity(), buffer.position(), buffer.limit(), buffer.remaining()));
    }

    private void printBufferContent(ByteBuffer buffer) throws IOException {
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        System.out.write(bytes);
    }

    private void doWriteData() throws IOException {
        int writed = socketChannel.write(writeBuffer);
        printBufferStatus(writeBuffer);
//        printBufferContent(writeBuffer);
        if (writeBuffer.hasRemaining()) {
            selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
        }else{
            writeBuffer.clear();
            selectionKey.interestOps(selectionKey.interestOps() & ~SelectionKey.OP_WRITE | SelectionKey.OP_READ);
        }
    }

    private void c(String command) throws IOException {
        String[] s = command.split(" ");
        if (s != null && s.length > 1) {
            String cmd = s[0];
            if (cmd.equals("echo")) {
                System.out.println("echo:" + s[1]);
                writeBuffer.put(s[1].getBytes());
                writeBuffer.flip();
                doWriteData();
            }
            if (cmd.equals("cmd")) {
                String result = callCmd(s[1]);
                writeBuffer.put(result.getBytes());
                writeBuffer.flip();
                doWriteData();
            }
        }
    }

    public String callCmd(String cmd) {
        try {

            ProcessBuilder processBuilder = new ProcessBuilder(cmd.trim());
            Process start = processBuilder.start();
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(start.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            bufferedReader.close();
            start.waitFor();
            return stringBuilder.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    int lastReadPosition;

    private void readData() throws IOException {
        socketChannel.read(readBuffer);
    }

    /**
     * 读取完毕后bytebuffer并不会自己回收掉，需要手动管理
     *
     * @throws IOException
     */
    private  void doReadData() throws IOException {
        System.out.println("read data");
        int readSize = socketChannel.read(readBuffer);
        int position = readBuffer.position();
        printBufferStatus(readBuffer);
        byte[] bytes = new byte[readBuffer.position()];
        readBytesFromBufferPosition(bytes, 0);
        readBuffer.position(position);
        //如果一次没有读到结尾就需要保留继续读
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == 13) {
                //读到结尾
                String command = new String(bytes);
                readBuffer.clear();
                selectionKey.interestOps(selectionKey.interestOps() & ~SelectionKey.OP_READ);
                c(command);
                break;
            }
        }
    }

    private void readBytesFromBufferPosition(byte[] bytes,int position) {
        readBuffer.position(position);
        readBuffer.get(bytes);
    }

    @Override
    public void run() {
        try {
            if (selectionKey.isReadable()) {
                doReadData();
            } else if (selectionKey.isWritable()) {
                doWriteData();
            }
        } catch (Exception e) {
            e.printStackTrace();
            selectionKey.cancel();
            try {
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }


}
