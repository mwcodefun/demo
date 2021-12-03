package code.ss.demo1;

import java.io.IOException;
import java.io.InputStream;

public class ClassLoaderDemo extends ClassLoader{
    public Integer a = 0;

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        String classFileName = name.substring(name.lastIndexOf('.') + 1) + ".class";
        InputStream is = getClass().getResourceAsStream(classFileName);
        if (is == null) {
            return super.loadClass(name, resolve);
        }
        try {
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new ClassNotFoundException();
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ClassLoaderDemo classLoaderDemo = new ClassLoaderDemo();
        classLoaderDemo.a = 10;
        System.out.println(classLoaderDemo.a);
    }
}
