package code.ss.demo1.jvm;

import java.util.Stack;

/**
 * Tos cache demo
 * @param <E>
 */
public class TosCacheDemo1<E> {

    Stack<E> stack = new Stack<>();

    int cache = 1;

    Object[] cacheArr = new Object[3];

    int curIndex = 0;

    public static void main(String[] args) {
        TosCacheDemo1<Object> objectTosCacheDemo1 = new TosCacheDemo1<>();
        objectTosCacheDemo1.push("123");
        System.out.println(objectTosCacheDemo1.pop());
        objectTosCacheDemo1.push("123");
        objectTosCacheDemo1.push("321");
        System.out.println(objectTosCacheDemo1.pop());
        System.out.println(objectTosCacheDemo1.pop());
    }

    public void push(E e) {
        if (curIndex < cacheArr.length) {
            cacheArr[curIndex] = e;
            curIndex++;
        } else {
            stack.push(e);
        }
    }

    public Object pop() {
        if (curIndex == 0) {
            return stack.pop();
        }
        curIndex = curIndex - 1;
        return cacheArr[curIndex];
    }

}
