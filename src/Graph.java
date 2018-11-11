import java.util.*;
public class Graph<T> {
    private int size;
    private ArrayList<Node> nodes;
    public class Node {
        private T value;
        private ArrayList<Node> connections;
        public Node (T value) {
            this.value = value;
            connections = new ArrayList<Node>();
        }
        public T getValue() {
            return this.value;
        }
        public void setValue(T newValue) {
            this.value = newValue;
        }
        public void connect(Node from, Node to) {
            from.connections.add(to);    
        }
        public void doubleConnect(Node from, Node to) {
            connect(from,to);
            connect(to,from);
        }
        public ArrayList<Node> getOutbound() {
            ArrayList<Node> n = new ArrayList<Node>();
            for(int i = 0; i<connections.size(); i++) {
                n.add(connections.get(i));
            }
            return n;
        }
    }


    public <T> Graph () {
        this.size = 0;
    }
    public void newNode(T value) {
        nodes.add(new Node(value));
        this.size++;
    }
    private int indexOf(T value) {
        if(this.size == 0) { return -1; }
        for(int i = 0; i<nodes.size(); i++) {
            if(nodes.get(i).equals(value)) {
                return i;
            }
        }
        return -1;
    }
    public boolean contains(T value) {
        return indexOf(value)!=-1;
    }
}
