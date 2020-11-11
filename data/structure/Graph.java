package data.structure;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import jdk.javadoc.internal.doclets.formats.html.SourceToHTMLConverter;

public class Graph {
    private boolean directed;
    // private boolean weighted;
    private ListLinked<Vertex> vertexList;
    private Vertex[] vertexs;
    private int numVertexs;

    private boolean isCiclico;
    private boolean isConnected;
    private int componentConnected;
    private Stack<Integer> stack = new Stack<>();

    public Graph(boolean directed) {
        this.directed = directed;
        isCiclico=false;
        isConnected = false;
        componentConnected = 0;
        vertexList = new ListLinked<>();
    }

    public Graph(boolean directed, int numVertexs) {
        this.directed = directed;
        isConnected = false;
        isCiclico=false;
        componentConnected = 0;
        vertexs = new Vertex[numVertexs];
    }

    public boolean isConnected() {
        BFS();
        return isConnected;
    }

    public int getComponentConnected() {
        return componentConnected;
    }

    public boolean getDirected() {
        return directed;
    }

    public ListLinked<Vertex> getVertexList() {
        return vertexList;
    }

    public Vertex[] getVertexs() {
        return vertexs;
    }

    public int getNumVertexs() {
        return numVertexs;
    }

    public void addVertex(Vertex vertex) {
        vertexList.add(vertex);
    }

    public void addEdge(Vertex v1, Vertex v2, double weight) {
        Edge edge = new Edge(v1, v2, weight);
        v1.addEdge(edge);
        if (!directed) {
            Edge edge2 = new Edge(v2, v1, weight);
            v2.addEdge(edge2);
        }
    }
    public Vertex getVertexArticulation(){
        Node<Vertex> n= vertexList.getHead();
        Vertex Varticualcion=null;
      
        while(n!=null){
            if(n.getData().getLabel()==n.getData().getAchievablePrevius().getLabel())
                vertexList.removeNode(n.getData());   
            DFS();
            if(!isConnected){
                Varticualcion=n.getData();
                n=null;
            }else
                n=n.getLink();
        }
        return Varticualcion;
    }
    public void DFS() {     
         
        Node<Vertex> n= vertexList.getHead();
        stack.push(-1);
        while(n!=null){
            if(n.getData().getState()==State.NO_VISITADO)
                DFS(n.getData(),stack.pop()+1);         
            n=n.getLink();
        }
        stack.clear();
    }
    public void DFS(Vertex vertex,int d) {
       vertex.setStatus(State.VISITADO);
       vertex.setD(d);
       stack.push(d);
       Node<Edge> node=vertex.getEdges().getHead();
       vertex.setAchievablePrevius(vertex);
       while(node!=null){
 
            if(node.getData().getV2().getState()==State.NO_VISITADO){    
                node.getData().setType(TypeEdge.TREE);
                System.out.println(node.getData());
                stack.pop();
                node.getData().getV2().setParent(vertex);
                vertex.setTreeDegree(vertex.getTreeDegree()+1);
                DFS(node.getData().getV2(),d+1);
                d=stack.pop();
            }else{
                if(vertex.getParent().getLabel() != node.getData().getV2().getLabel() && vertex.getParent() != null){ 
                    node.getData().setType(TypeEdge.LATER);
                    System.out.println(node.getData());
                    isCiclico=true;
                    if (node.getData().getV2().getD() < vertex.getAchievablePrevius().getD()) 
                        vertex.setAchievablePrevius(node.getData().getV2());
                    
                }
            }
            node=node.getLink();
       }  
       ArticulationVertex(vertex);
       vertex.setStatus(State.PROCESADO);           
       vertex.setF(d+1);
       stack.push(d+1);
    }
    public void ArticulationVertex(Vertex vertex){
        if (vertex.getParent() == null) {
            if (vertex.getTreeDegree() > 1) 
                vertex.setType(TypeVertex.ROOT_CUTNODE);          
        } else{
            if (vertex.getAchievablePrevius().getLabel()==vertex.getParent().getLabel()) 
                vertex.setType(TypeVertex.PARENT_CUTNODE);
            else{
                if (vertex.getAchievablePrevius().getLabel()==vertex.getLabel()) {
                    vertex.getParent().setType(TypeVertex.BRIDGE_CUTNODE);
                    if (vertex.getTreeDegree() > 0) 
                        vertex.setType(TypeVertex.BRIDGE_CUTNODE);                   
                }
            }
        }
        if (vertex.getParent() != null) {
            if (vertex.getAchievablePrevius().getD() < vertex.getParent().getAchievablePrevius().getD()) 
                vertex.getParent().setAchievablePrevius(vertex.getAchievablePrevius());            
        }

    }
    public void BFS(Vertex vertex) {
        ListLinked<Vertex> travelBFS = new ListLinked<>();
        Queue<Vertex> queue = new LinkedList<>();// estructura de datos cola
        queue.add(vertex);
        vertex.setStatus(State.VISITADO);
        travelBFS.add(vertex);
        while (!queue.isEmpty()) {
            vertex = queue.poll();// vertice padre
            ListLinked<Edge> lEdges = vertex.getEdges();
            Node<Edge> node = lEdges.getHead();
            while (node != null) {
                Vertex opposite = node.getData().getV2();// vertices hijos
                if (opposite.getState() == State.NO_VISITADO) {
                    queue.add(opposite);
                    opposite.setStatus(State.VISITADO);
                    opposite.setParent(vertex);
                    opposite.setJumps(vertex.getJumps() + 1);
                    travelBFS.add(opposite);
                }
                node = node.getLink();
            }
            vertex.setStatus(State.PROCESADO);
        }
        
    }

    public void BFS() {
        Node<Vertex> iterator = vertexList.getHead();
        isConnected = false;
        while (iterator != null) {
            Vertex vertex = iterator.getData();
            if (vertex.getState().compareTo(State.NO_VISITADO) == 0) {
                BFS(vertex);
                componentConnected++;
                isConnected = componentConnected == 1;
            }
            iterator = iterator.getLink();
        }
    }

    public void shortesPaths() {
        BFS(vertexList.getHead().getData());
        System.out.println("Caminos cortos");
        for (int i = 0; i < vertexs.length; i++) {
            String route = printShortPath(getShortPath(vertexs[i]));
            System.out.println(vertexs[i].getLabel() + "(" + vertexs[i].getJumps() + "): " + route);
        }
    }

    public void shortPath(Vertex start, Vertex finish) {
        BFS(start);
        Stack<Vertex> shortPath = getShortPath(finish);
        printShortPath(shortPath);
    }

    public String printShortPath(Stack<Vertex> shortPath) {
        String output = "";
        if (!shortPath.isEmpty()) {
            while (!shortPath.isEmpty()) {
                output += shortPath.pop().getLabel();
                if (shortPath.size() >= 1) {
                    output += " -> ";
                }
            }
        } else {
            output += "Sin caminos cortos, vertice aislado";
        }
        // output += "\n";
        return output;
    }

    public Stack<Vertex> getShortPath(Vertex vertex) {
        Stack<Vertex> shortPath = new Stack<>();
        Vertex parent = vertex.getParent();
        if (parent != null) {
            shortPath.push(vertex);
            while (parent != null) {
                shortPath.push(parent);
                parent = parent.getParent();
            }
        }
        return shortPath;
    }

    public void printGraph() {
        ListLinked<Edge> edges;
        String output = "";
        for (int i = 0; i < vertexs.length; i++) {
            Vertex vertex = vertexs[i];
            output = output + vertex.getLabel();
            edges = vertex.getEdges();
            output = output + "(" + edges.size() + ") -> ";
            Node<Edge> temp = edges.getHead();
            while (temp != null) {
                output = output + "{" + temp.getData().getV2().getLabel() + "} ";
                temp = temp.getLink();
            }
            output = output + "\n";
        }
        System.out.println(output);
    }
    public void printGraphDFS() {
        ListLinked<Edge> edges;
        String output = "";
        for (int i = 0; i < vertexs.length; i++) {
            Vertex vertex = vertexs[i];
            output = output + vertex.getLabel();
            edges = vertex.getEdges();
            output = output + "(" + edges.size() + "){d="+vertex.getD()+"f="+vertex.getF()+"} -> ";
            Node<Edge> temp = edges.getHead();
            while (temp != null) {
                output = output + "{" + temp.getData().getV2().getLabel() +"}";
                temp = temp.getLink();
            }
            output = output + "\n";
        }
        System.out.println(output);
    }
    public void printEdges() {
        ListLinked<Edge> edges;
        String output = "";
        for (int i = 0; i < vertexs.length; i++) {
            edges = vertexs[i].getEdges();
            Node<Edge> temp = edges.getHead();
            while (temp != null) {
                output =output + "Edge={V1="+temp.getData().getV1().getLabel() + ",V2="+temp.getData().getV2().getLabel()+", Type="+temp.getData().getType()+"}"+ "\n";
                temp = temp.getLink();
            }
            output = output + "\n";
        }
        System.out.println(output);
    }
    public void readFileInput(String filename) {
        String path = System.getProperty("user.dir") + "\\input\\" + filename;
        try {

            System.out.println(path);
            File file = new File(path);
            Scanner scanner = new Scanner(file);

            String line = "";
            line = scanner.next();
            Pattern pattern;
            Matcher matcher;

            pattern = Pattern.compile("size\\s*=\\s*(\\d+)");
            matcher = pattern.matcher(line);
            matcher.find();
            String strSize = matcher.group(1);
            System.out.println(strSize);

            vertexs = new Vertex[Integer.parseInt(strSize)];
            // Obteniendo las lineas de informacion de vertices
            while (!(line = scanner.nextLine()).equals(";")) {

                pattern = Pattern.compile("(\\d+)\\s*=\\s*(.+)");
                matcher = pattern.matcher(line);
                if (matcher.find()) {
                    Vertex vertex = new Vertex(matcher.group(2));
                    addVertex(vertex);
                    vertexs[Integer.parseInt(matcher.group(1))] = vertex;
                }
                // System.out.println(line);
            }

            // Obteniendo las lineas de informacion de aristas
            while (!(line = scanner.nextLine()).equals(";")) {
                pattern = Pattern.compile("\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)");
                matcher = pattern.matcher(line);
                if (matcher.find()) {
                    int posV1 = Integer.parseInt(matcher.group(1));
                    int posV2 = Integer.parseInt(matcher.group(2));
                    Vertex v1 = vertexs[posV1];
                    Vertex v2 = vertexs[posV2];
                    double weight = Double.parseDouble(matcher.group(3));
                    addEdge(v1, v2, weight);
                }
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }

        /*
         */

    }

    public static void main(String[] args) {
        Graph graph = new Graph(false);

        Vertex LaPaz = new Vertex("La Paz");
        Vertex Cochabamba = new Vertex("Cochabamba");
        Vertex SantaCruz = new Vertex("Santa Cruz");
        Vertex Riberalta = new Vertex("Riberalta");

        LaPaz.addEdge(new Edge(LaPaz, Cochabamba));
        LaPaz.addEdge(new Edge(LaPaz, SantaCruz));
        LaPaz.addEdge(new Edge(LaPaz, Riberalta));

        graph.addVertex(LaPaz);
        graph.addVertex(Cochabamba);
        graph.addVertex(SantaCruz);
        graph.addVertex(Riberalta);

        graph.readFileInput("bolivia.txt");
    }
}
