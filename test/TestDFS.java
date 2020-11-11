package test;

import data.structure.Graph;
//import jdk.javadoc.internal.doclets.formats.html.SourceToHTMLConverter;

public class TestDFS {

    static void testDFS() {
        Graph graph = new Graph(false);
        graph.readFileInput("test01.txt");
        graph.printGraphDFS();
        graph.DFS();
        System.out.println("El vertice de articulacion es:"+graph.getVertexArticulation());       
    }

    public static void main(String[] args) {
        testDFS();
    }
}