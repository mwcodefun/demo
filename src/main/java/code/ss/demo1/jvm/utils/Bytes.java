package code.ss.demo1.jvm.utils;

public class Bytes {

    public static int toInt(byte[] bytes) {
        return (((bytes[0]       ) << 24) |
                ((bytes[1] & 0xff) << 16) |
                ((bytes[2] & 0xff) <<  8) |
                ((bytes[3] & 0xff)      ));
    }

    public static int toInt(byte b1, byte b2, byte b3, byte b4) {
        return (((b1       ) << 24) |
                ((b2 & 0xff) << 16) |
                ((b3 & 0xff) <<  8) |
                ((b4 & 0xff)      ));

    }

    public static int toFitInt(byte[] bytes) {
        int a = 0;
        for (int i = 0; i < bytes.length; i++) {
            a = a | ((bytes[i] & 0xff) << ((bytes.length - i - 1) * 8));
        }
        return a;


    }

}
