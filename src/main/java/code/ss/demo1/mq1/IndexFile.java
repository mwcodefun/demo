package code.ss.demo1.mq1;

import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

public class IndexFile {

    MappedByteBuffer mappedByteBuffer;
    ByteBuffer headerBuffer;

    public IndexFile() {
//        this.mappedByteBuffer = new MappedByteBuffer();


    }


    private static int HEADER_SIZE = 40;




    static class MapValue{

    }



    public void putVal(String key, MapValue value) {

    }

    public void increIndexCount() {
    }

}
