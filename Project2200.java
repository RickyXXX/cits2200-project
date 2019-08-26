import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.lang.Math;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;


public class Project2200 implements CITS2200Project {
	
	int ID=0;
	private ArrayList<vertex> vertices=new ArrayList<vertex>();
	private ArrayList<Set<vertex>> result = new ArrayList<Set<vertex>>();
	
	private class vertex{
		String URL;
		ArrayList<vertex> adjacents;
		int distances;
		boolean visit;
		int eccentricity;
		int SCCindex;
		int SCClowlink;
		boolean onStack;
		int id;
		
		private vertex(String a) {
			URL=a;
			adjacents = new ArrayList<vertex>();
			eccentricity=0;
			onStack=false;
			SCCindex=-1;
			SCClowlink=-1;
		}
	}
	
	
	//* Adds an edge to the Wikipedia page graph. If the pages do not
	//* already exist in the graph, they will be added to the graph.
	public void addEdge(String urlFrom, String urlTo) {
		
		//making sure the urlForm and urlTo are different
		if(!urlFrom.equals(urlTo)) { 
			boolean from = false;
			boolean to = false;
			vertex tem1 = null;
			vertex tem2 = null;
			Iterator<vertex> it = vertices.iterator();
			
			while(it.hasNext()) {
				vertex a = it.next();
				//if the urlFrom already exist
				if(a.URL.equals(urlFrom)) {
					from = true;
					tem1=a;
				}
				//if the urlTo already exist
				if(a.URL.equals(urlTo)) {
					to = true;
					tem2=a;
				}
			
			}
			//4 condition of addEdge
			if(from==false&&to==false) {
				vertex a = new vertex(urlFrom);
				a.id=ID;
				ID++;
				vertex b = new vertex(urlTo);
				b.id=ID;
				ID++;
				vertices.add(a);
				vertices.add(b);
				a.adjacents.add(b);
			}
			else if(from==true&&to==false) {
				vertex b = new vertex(urlTo);
				b.id=ID;
				ID++;
				vertices.add(b);
				tem1.adjacents.add(b);
			}
			else if(from==false&&to==true) {
				vertex a = new vertex(urlFrom);
				a.id=ID;
				ID++;
				vertices.add(a);
				a.adjacents.add(tem2);
			}
			else {
				tem1.adjacents.add(tem2);
			}
		}
		else {
			System.out.println("these two pages are same"); 
		}
	}

	/**
	 * Finds the shorest path in number of links between two pages.
	 * If there is no path, returns -1.
	 * 
	 * @param urlFrom the URL where the path should start.
	 * @param urlTo the URL where the path should end.
	 * @return the legnth of the shorest path in number of links followed.
	 */
	public int getShortestPath(String urlFrom, String urlTo) {
        if(!urlFrom.equals(urlTo)) {
            vertex start=null;
            vertex end=null;
            
            //finding the urlFrom in which vertex
            for(vertex v : vertices){
                if(v.URL.equals(urlFrom)) start=v;
                if(v.URL.equals(urlTo)) end=v;
                v.visit=false;
            }
            if(start==null) System.out.println("the graph doesn't include the starting vertex");
            if(end==null) System.out.println("the graph doesn't include the starting vertex");
           

            //BFS
            Queue<vertex> queue = new LinkedList<vertex>();
            queue.add(start);
            start.distances=0;
            

            while(!queue.isEmpty()) {
                vertex a = queue.remove();
                for(vertex b : a.adjacents) {
                    if(b.visit==false) {
                        b.visit=true;
                        b.distances=a.distances+1;
                        queue.add(b);
                    }
                }
                if(a.URL.equals(urlTo)) return a.distances;
            }

            //if urlForm cannot reach urlTo return -1
            return -1;
        }
        else return 0;
    }
	/**
	 * Finds all the centers of the page graph. The order of pages
	 * in the output does not matter. Any order is correct as long as
	 * all the centers are in the array, and no pages that aren't centers
	 * are in the array.
	 * 
	 * @return an array containing all the URLs that correspond to pages that are centers.
	 */
	public String[] getCenters() {
		//calculate each vertex's eccentricity
		int path=0;
		
		for(vertex a : vertices) {
			for(vertex b : vertices) {
				path=getShortestPath(a.URL,b.URL);
				if(path==-1) {
					//if the path is -1,it means that the distance is infinity
					//give a very big value to a
					a.eccentricity=Integer.MAX_VALUE; 
					continue;
				}
				a.eccentricity=Math.max(path, a.eccentricity);
			}
		}
		
		//find the radius
		int radius=Integer.MAX_VALUE;
		for(vertex v: vertices) {
			radius=Math.min(v.eccentricity, radius);
		}
		
		//find the center
		int num=0;
		for(vertex a: vertices) {
			if(a.eccentricity==radius) num++;
		}
		String[] center = new String[num];
		int j=0;
		for(vertex a: vertices) {
			if(a.eccentricity==radius) {
				center[j]=a.URL;
			}
		}
		return center;
	}

	/**
	 * Finds all the strongly connected components of the page graph.
	 * Every strongly connected component can be represented as an array 
	 * containing the page URLs in the component. The return value is thus an array
	 * of strongly connected components. The order of elements in these arrays
	 * does not matter. Any output that contains all the strongly connected
	 * components is considered correct.
	 * 
	 * @return an array containing every strongly connected component.
	 * */

	public String[][] getStronglyConnectedComponents() {
		// Tarjan's strongly connected components algorithm
		
		int index = 0;
		Stack<vertex> s = new Stack<vertex>();
		
		for(vertex v:vertices){
			if(v.SCCindex == -1) index=strongconnect(v, index, s);
		}
		

	//	transform the result from the type list to type string 
		int row = result.size();
        String[][] scc = new String[row][];
        
        for(int i=0;i<row;i++) {
            String[] a = new String[result.get(i).size()];
            int j=0;
            for(vertex v: result.get(i)) {
                a[j++]=v.URL;
            }
            scc[i]=a;
        }
        return scc;
	}

	private int strongconnect(vertex v, int index, Stack<vertex> s) {
		
		v.SCCindex = index;
		v.SCClowlink = index;
		index ++;
		s.push(v);
		v.onStack = true;
		
		for (vertex w:v.adjacents){
			if(w.SCCindex == -1){
				index=strongconnect(w, index, s);
				v.SCClowlink = Math.min(v.SCClowlink, w.SCClowlink);
			}else if(w.onStack){
				v.SCClowlink = Math.min(v.SCClowlink, w.SCClowlink);
			}
		}
				
		if(v.SCClowlink == v.SCCindex){
			vertex w;
			Set<vertex> set =new HashSet<vertex>();
			do{
				w = s.pop();
				w.onStack = false;
				set.add(w);
			}while (w.URL != v.URL);
			result.add(set);
		}
		return index;
	}
	/**
	 * Finds a Hamiltonian path in the page graph. There may be many
	 * possible Hamiltonian paths. Any of these paths is a correct output.
	 * This method should never be called on a graph with more than 20
	 * vertices. If there is no Hamiltonian path, this method will
	 * return an empty array. The output array should contain the URLs of pages
	 * in a Hamiltonian path. The order matters, as the elements of the
	 * array represent this path in sequence. So the element [0] is the start
	 * of the path, and [1] is the next page, and so on.
	 * 
	 * @return a Hamiltonian path of the page graph.
	 */
	public String[] getHamiltonianPath() {
		
		//the number of subsets in a set including n elements is 2^n
		//giving the number to the subset
		//find that the integer can be represented by binary number
		//for example subset=0 means empty set and subset=7 means 111
		//indicated that the subset includes 1st, 2nd and 3rd vertex
		int subset = (int) Math.pow(2,vertices.size());
		int infinity = vertices.size();
		int[][] subsetVertex = new int[subset][vertices.size()];
		int min = 0;
		int k = 0;

		for (int i = 0; i < subset; i++){
			for (int v = 0; v < vertices.size(); v++){
				if(checkbit(v,i) == false) subsetVertex[i][v] = infinity;
				else if(countSubset(i)==1) subsetVertex[i][v] = 0;
				else{
					min = infinity;
					for(int j = 0; j < vertices.size(); j ++){
						if(checkbit(j,i)){ 
							if(isEdge(vertices.get(j), vertices.get(v))){
								k = subsetVertex[(i) ^ (int)(Math.pow(2,v))][j] + 1;
								if(k<min) min = k;
							}
						}
					}
					subsetVertex[i][v]= min;
				}
			}
		}
		
		String[] path = new String[vertices.size()];
		boolean found = false;
		int endP = 0;
		//identify subset which include n elements
		int currentM = (int)Math.pow(2, vertices.size())-1;
		
		int counter = 1;
		
		while(!found && endP < vertices.size()){
			if (subsetVertex[currentM][endP] < infinity) found = true;
			else endP++;
		}
		
		if(!found) {
			System.out.println("the graph doesn't have the hamiltonian path");
			return new String[0];
		}
		else found = false;
		//insert the vertex
		path[vertices.size()-counter] = vertices.get(endP).URL;
		counter++;
		
		for(int i = 1; i < vertices.size(); i++){
			int j = 0;
			while(!found && j < vertices.size()){
				if(subsetVertex[currentM][endP] == (subsetVertex[currentM ^ (int)Math.pow(2, endP)][j] +1)){
					if(j!=endP && isEdge(vertices.get(j),vertices.get(endP))) {
						found = true;
					}
				}
				if(!found) j++;
			}
			
			if (!found) System.out.println("there is a problem!!!!");
			currentM = (currentM ^ (int)Math.pow(2, endP));
			endP = j;
			path[vertices.size()-counter] = vertices.get(endP).URL;
			counter++;
			found = false;
		}
		return path;
	}
	
	//return the number of vertices containning in the subset
	private int countSubset(int subset){
		int sum = 0;
		for(int i = 0; i < vertices.size(); i++){
			sum = sum + (1 & (subset>>i));
		}
		return sum;
	}
	
	//checked subset if contain a vertex at position n
	private boolean checkbit(int n, int subset){	
		int a = 1 & (subset>>n);
		if(a==1) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean isEdge(vertex a, vertex b){
		boolean c = a.adjacents.contains(b);
		return c;
	}
		
}
