package code.ss.demo1.wal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class LogFile {

    private final File file;
    private MappedByteBuffer mappedByteBuffer;
    private static final int FILE_HEADER_LENGTH = 16;

    private  ByteBuffer header;

    private int writeOffset = 0;

    public LogFile(File file,int size) {
        this.file = file;
        try {
            RandomAccessFile rwfile = new RandomAccessFile(file, "rw");
            mappedByteBuffer = rwfile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, size);
            header = mappedByteBuffer.slice();
            this.writeOffset = header.getInt() == 0 ? FILE_HEADER_LENGTH : header.getInt();
            this.readOffset = FILE_HEADER_LENGTH;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void appendLog(String log) {
        this.mappedByteBuffer.position(writeOffset);
        byte[] bytes = log.getBytes();
        this.mappedByteBuffer.putInt(bytes.length);
        mappedByteBuffer.put(bytes);
        writeOffset = writeOffset + 4 + bytes.length;
        this.header.position(0);
        this.header.putInt(writeOffset);
    }

    int readOffset;

    String read() {
        int length = readStringlength();
        System.out.println(length);
        return readStringBylength(length);
    }

    String readStringBylength(int length) {
        byte[] bytes = new byte[length];
        mappedByteBuffer.position(readOffset);
        mappedByteBuffer.get(bytes, 0, length);
        readOffset = readOffset + length;
        return new String(bytes);
    }

    int readStringlength(){
        int anInt = mappedByteBuffer.getInt(readOffset);
        readOffset = readOffset + 4;
        return anInt;
    }

    private void sortArr(int[] arr){

    }
    /**
     * Given a list of {@link Integer}s, remove the duplicates
     * and return the result sorted by value.
     */

    public static void main(String[] args) throws IOException {
        File file = new File("1.log");
        if (!file.exists()) {
            file.createNewFile();
        }
        int fileSize = 1024 * 1024 * 1024;
        LogFile logFile = new LogFile(file, fileSize);
        logFile.appendLog("set a=1");
        String line = null;
        while ((line = logFile.read()) != null) {
            System.out.println(line);
            line = logFile.read();
        }
        System.out.println("Read log finished");
    }

}
