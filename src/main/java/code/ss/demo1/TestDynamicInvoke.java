package code.ss.demo1;

public class TestDynamicInvoke {





    public void doS() {
        Human human = new Man();
        human.sayHello();
    }

    public static void main(String[] args) {
        Human human = new Man();
        human.sayHello();
    }

}
