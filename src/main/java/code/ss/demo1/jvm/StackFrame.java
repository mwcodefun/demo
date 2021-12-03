package code.ss.demo1.jvm;

import code.ss.demo1.jvm.utils.Bytes;

import java.util.Stack;

public class StackFrame {

    final byte[] localVirables;

    final Stack<Object> operandStack;

    public StackFrame(int slot) {
        localVirables = new byte[slot * 4];
        operandStack = new Stack<>();
    }


    /**
     * 将positon_1的局部变量放入栈顶
     */
    public void iload_1() {
        int intVirable = getIntVirable(0);
        operandStack.push(intVirable);
    }

    public void iload_n(int n) {

    }

    private int getIntVirable(int index) {
        int i = Bytes.toInt(localVirables[index], localVirables[index + 1], localVirables[index + 2], localVirables[index + 3]);
        return i;
    }



}
