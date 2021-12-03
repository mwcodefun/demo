package code.ss.demo1;



public class A2 {

    private volatile int b = 0;
    private A1 a1 = new A1();

    public static void sayHello() {
        System.out.println("hello");
    }



    public int a(Integer i) {
        sayHello();
        Integer a = new Integer(10);
        Integer b = new Integer(20);
        System.out.println(a);
        a.toString();
        a1.a();
        return a + b + i;
    }

    public static void main(String[] args) {
        new A2().a(10);

        ClassLoader classLoader = A2.class.getClassLoader();
        ClassLoader parent = classLoader.getParent();
        System.out.println(A2.class.getClassLoader());
        System.out.println(parent);
        System.out.println(parent.getParent());
    }

}
