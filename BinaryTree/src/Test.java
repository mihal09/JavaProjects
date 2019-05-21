public class Test {
    public static void main(String[] args){
        BinaryTree<String> tree = new BinaryTree<>();
        tree.insert("aaa");
        tree.insert("aba");
        tree.insert("aab");
        tree.insert("baa");
        tree.insert("abb");
        tree.insert("bba");
        tree.delete("aab");
        tree.draw();
    }
}
