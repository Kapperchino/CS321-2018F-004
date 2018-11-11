import java.util.*;
public class Graph<T> {
    private int size;
    private ArrayList<Node> nodes;
    public class Node {
        private T value;
        private ArrayList<Node> connections; //Outgoing connections
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
        public void connect (Node to) {
            connections.add(to);    
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
    public void add(T value) {
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
    
    public ArrayList<T> getOutbound(T value) {
        ArrayList<T> values = new ArrayList<T>();
        int x = indexOf(value);
        if(x == -1) {
            return values;
        }
        ArrayList<Node> nodes = nodes.get(x).getOutbound();
        for(int i = 0; i< nodes.size(); i++) {
            values.add(nodes.get(i).getValue());
        }
    }
    public void connect(T from, T to) {
        Node fromNode = nodes.get(indexOf(from));
        Node toNode = nodes.get(indexOf(to));
        if(fromNode==null||toNode==null) {
            return;
        }
        fromNode.connect(toNode);    
    }
}
