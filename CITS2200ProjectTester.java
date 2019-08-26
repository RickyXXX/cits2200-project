import java.io.*;
import java.util.*;

public class CITS2200ProjectTester {
	public static void loadGraph(CITS2200Project project, String path) {
		// The graph is in the following format:
		// Every pair of consecutive lines represent a directed edge.
		// The edge goes from the URL in the first line to the URL in the second line.
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			while (reader.ready()) {
				String from = reader.readLine();
				String to = reader.readLine();
				System.out.println("Adding edge from " + from + " to " + to);
				project.addEdge(from, to);
			}
		} catch (Exception e) {
			System.out.println("There was a problem:");
			System.out.println(e.toString());
		}
	}

	public static void main(String[] args) {
		// Change this to be the path to the graph file.
		String pathToGraphFile = "/Users/ricky/eclipse-workspace/2200project/src/test.txt";
		// Create an instance of your implementation.
		CITS2200Project proj = new Project2200();
		// Load the graph into the project.
		loadGraph(proj, pathToGraphFile);
		// Write your own tests!
        
//        
//
		//int qw=proj.getShortestPath("/wiki/Australia","/wiki/United+Kingdom");
        System.out.println(" ");
        //System.out.println(qw);
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("The centers for this graph are");
        String[] x= proj.getCenters();
        for(String b: x) System.out.println(b);
        
        String[][] scc = proj.getStronglyConnectedComponents();
        System.out.println(" ");
        System.out.println("SCC's are");
        //System.out.println(" ");
        for(String[] a: scc){
            for(String b:a){
                System.out.print(b+" ");
            }
            System.out.println(" ");
        }
        
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("The hamiltonian path is");
        String[] q = proj.getHamiltonianPath();
        for(String a: q){
            System.out.println(a);
        }
    }
}
