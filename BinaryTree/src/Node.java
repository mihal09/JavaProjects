public class Node<T> {
    public Node<T> left, right;
    public T value;
    public Node(T value){
        left = null;
        right = null;
        this.value = value;
    }
}
