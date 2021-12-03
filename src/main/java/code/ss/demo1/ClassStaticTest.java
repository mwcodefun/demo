package code.ss.demo1;

public class ClassStaticTest {

    public static   Integer i = 0;

    {
        i = 10;
        System.out.println("C1 init");
    }

     class C2 extends ClassStaticTest{
        {
            i = 20;
            System.out.println("C2 init");
        }
    }



}
