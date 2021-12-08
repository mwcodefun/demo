package code.ss.demo1.collections.tree;

public class Btree {

    public void traverse(BtreeNode btreeNode) {
        if (btreeNode == null) {
            return;
        }
        traverse(btreeNode.left);
        traverse(btreeNode.right);
        //houxu bianli weizhi
    }

}
