package code.ss.demo1.jvm;

import code.ss.demo1.jvm.utils.Bytes;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class classload1 {

    int _cur;


    public byte read_u1(ByteBuffer byteBuffer) {
        byte b = byteBuffer.get(_cur);
        _cur++;
        return b;
    }


    public byte[] read_u2(ByteBuffer buffer) {
        byte[] bytes = new byte[2];
        bytes[0] = buffer.get(_cur++);
        bytes[1] = buffer.get(_cur++);
//        buffer.get(bytes, _cur, bytes.length);
//        _cur = _cur + 2;
        return bytes;
    }

    private byte[] read_magic(ByteBuffer byteBuffer) {
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = read_u1(byteBuffer);
        }
        return bytes;
    }

    public void parseClass(String className) throws IOException, ClassNotFoundException {
        String fileName = toFileName(className);
        URL resource = getClass().getClassLoader().getResource(fileName);
        String file1 = resource.getFile();
        File file = new File(file1);
        System.out.println(file.getAbsolutePath());
        if (!file.exists()) {
            throw new ClassNotFoundException();
        }
        RandomAccessFile rw = new RandomAccessFile(file, "rw");
        FileChannel channel = rw.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024 * 1024);

        int read = channel.read(byteBuffer);
        if (read >= byteBuffer.capacity()) {
            throw new IOException();
        }
        byte[] bytes = read_magic(byteBuffer);
        int i = 0xCAFEBABE;
        if (Bytes.toInt(bytes) == i) {
            System.out.println("yes it's magic code");
        }else{
            throw new RuntimeException();
        }
        System.out.println(bytesToHexString(bytes));
        byte[] minorVersion = read_u2(byteBuffer);
        byte[] mainVersion = read_u2(byteBuffer);
        System.out.println("minorVersion:" + Bytes.toFitInt(minorVersion));
        System.out.println("mainVersion:" + Bytes.toFitInt(mainVersion));

    }
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    private String toFileName(String className) {

        return className.replaceAll("//.", File.separator) + ".class";
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        classload1 classload1 = new classload1();
        Class<?> demo = classload1.getClass().getClassLoader().loadClass("Demo");
        classload1.parseClass("Demo");
    }


}
