package data.structure;

public class Vertex {
    private String label;
    private ListLinked<Edge> edges;
    private State state;
    private int jumps;
    private Vertex parent;
    private int d;
    private int f;
    private Vertex achievablePrevius;
    private int treeDegree;
    private TypeVertex type;

    public Vertex(String label) {
        this.label = label;
        this.jumps = 0;
        this.d=0;
        this.f=0;
        this.treeDegree=0;
        edges = new ListLinked<>();
        state = State.NO_VISITADO;
        this.type=TypeVertex.NONE;
    }
    public void setType(TypeVertex type) {
        this.type = type;
    }

    public TypeVertex getType() {
        return type;
    }
    public void setTreeDegree(int treeDegree) {
        this.treeDegree = treeDegree;
    }
    public void setAchievablePrevius(Vertex achievablePrevius) {
        this.achievablePrevius = achievablePrevius;
    }

    public Vertex getAchievablePrevius() {
        return achievablePrevius;
    }
    public int getTreeDegree() {
        return treeDegree;
    } 
    public void setParent(Vertex parent) {
        this.parent = parent;
    }

    public Vertex getParent() {
        return parent;
    }
    public void setF(int f) {
        this.f = f;
    }

    public int getF() {
        return f;
    } 
    public void setD(int d) {
        this.d = d;
    }

    public int getD() {
        return d;
    }
    public int getJumps() {
        return jumps;
    }

    public void setJumps(int jumps) {
        this.jumps = jumps;
    }

    public void setStatus(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void addEdge(Vertex v1, Vertex v2, double weight) {
        Edge edge = new Edge(v1, v2, weight);
        edges.add(edge);
    }

    public String getLabel() {
        return label;
    }

    public ListLinked<Edge> getEdges() {
        return edges;
    }

    public String toString() {
        return "Vertex={label={" + label + "},edges={" + edges + "}}";
    }
}
