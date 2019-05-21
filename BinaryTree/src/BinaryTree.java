public class BinaryTree<T extends Comparable<T>> {
    Node<T> root;
    public BinaryTree(){
        root = null;
    }
    public void insert(T value){
        root = insert(root, value);
    }

    public void delete(T value){
        root = delete(root, value);
    }

    private Node<T> search(Node<T> node, T value){
        if(node == null)
            return null;
        int comparison = value.compareTo(node.value);
        if(comparison<0){
            return search(node.left, value);
        }
        else if(comparison>0){
            return search(node.right, value);
        }
        else{
            return node;
        }
    }

    public Node<T> search(T value){
        return search(root, value);
    }

    private Node<T> insert(Node<T> node, T value){
        //jesteśmy na niesitniejącym dziecku liścia
        if(node==null){
            node = new Node<>(value);
        }
        //jesteśmy na liściu
        else{
            int comparison = value.compareTo(node.value);
            //value jest mniejsza
            if(comparison<0){
                node.left = insert(node.left, value);
            }
            //value jest wieksza
            else if(comparison>0){
                node.right = insert(node.right, value);
            }
        }
        return node;
    }

    private Node<T> delete(Node<T> node, T value){
        if(node==null)
            return null;
        int comparison = value.compareTo(node.value);
        if(comparison<0){
            node.left = delete(node.left, value);
        }
        else if(comparison>0){
            node.right =  delete(node.right, value);
        }
        //trafilismy
        else{
            //zero lub jedno dziecko
            if(node.left == null){
                Node<T> temp = node.right;
                node.right = null;
                return temp;
            }
            else if(node.right == null){
                Node<T> temp = node.left;
                node.left = null;
                return temp;
            }
            //dwoje dzieci
            else{
                //znajdz najmniejszą wartosc sposrod prawych potomkow
                Node<T> temp = minimumValue(node.right);
                //zastap wartosc wiercholka najmniejszą
                node.value = temp.value;
                //usun najmniejszą wartosc
                node.right = delete(node.right, temp.value);
            }
        }
        return node;
    }

    public void draw(){
        inOrder(root);
    }

    private Node<T> minimumValue(Node<T> node){
        Node<T> current = node.right;
        while(current != null && current.left != null)
        {
            current = current.left;
        }
        return current;
    }

    private void inOrder(Node<T> node){
        if(node != null){
            inOrder(node.left);
            System.out.print(node.value+" ");
            inOrder(node.right);
        }
    }
}
