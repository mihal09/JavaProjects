public class BinaryTree {
    Node root;
    public BinaryTree(){
        root = null;
    }
    public void insert(Comparable value){
        root = insert(root, value);
    }

    public void delete(Comparable value){
        root = delete(root, value);
    }

    private Node search(Node node, Comparable value){
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

    public boolean search(Comparable value){
        return search(root, value)!=null;
    }

    private Node insert(Node node, Comparable value){
        //jesteśmy na niesitniejącym dziecku liścia
        if(node==null){
            node = new Node(value);
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

    private Node delete(Node node, Comparable value){
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
                Node temp = node.right;
                node.right = null;
                return temp;
            }
            else if(node.right == null){
                Node temp = node.left;
                node.left = null;
                return temp;
            }
            //dwoje dzieci
            else{

                //znajdz najmniejszą wartosc sposrod prawych potomkow
                Node temp = minimumValue(node);
                //zastap wartosc wiercholka najmniejszą
                node.value = temp.value;
                //usun najmniejszą wartosc
                node.right = delete(node.right, temp.value);
            }
        }
        return node;
    }

    public String draw(){
        return inOrder(root);
    }

    public Node minimumValue(Node node){
        Node current = node.right;
        while(current != null && current.left != null)
        {
            current = current.left;
        }
        return current;
    }

    private String inOrder(Node node){
        String result = "";
        if(node != null){
            result += inOrder(node.left);
            result += node.value+" ";
            result += inOrder(node.right);
        }
        return result;
    }
}
