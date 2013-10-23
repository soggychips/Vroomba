package main;
import java.util.ArrayList;
import java.util.LinkedList;


public class BFS {
	private ShortGrid grid;
	private LinkedList<Vertex> queue;	
	private int maxX;
	private int maxZ;
	
	public BFS(ShortGrid g, int maxX, int maxZ) {
		this.grid = g;
		this.maxX = maxX - 1;
		this.maxZ = maxZ - 1;
	}
	
	/*
	 * Do a breadth-first search starting at (x,z) and return
	 * when a grid position's value = 0.
	 */
	public Vertex search(int x, int z) {
		this.queue = new LinkedList<Vertex>();
		Vertex v = new Vertex(x, z);
		v.markAsVisited();
		queue.add(v);
		//System.out.println("Added and marked v at: (" + x + ", " + z + ")");
		
		while(!queue.isEmpty()) {
			Vertex t = queue.remove(0);
			if(grid.getStatus(t.posX(), t.posZ()) == 0) {
				System.out.println("Found 0 at: (" + t.posX() + ", " + t.posZ() + ")");
				return t;
			}
			
			ArrayList<Vertex> adjacents = getAdjacents(t);
			for(Vertex u : adjacents) {
				if(!u.marked()) {
					u.markAsVisited();
					queue.add(u);
					//System.out.println("Added and marked u at: (" + u.posX() + ", " + u.posZ() + ")");
				}
			}
		}
		
		return null;
	}
	
	private ArrayList<Vertex> getAdjacents(Vertex v) {
		ArrayList<Vertex> adj = new ArrayList<Vertex>();
		Vertex t;
		if(v.posX() != 0 && v.posZ() != maxZ && grid.getStatus(v.posX() - 1, v.posZ() + 1) != 2) {
			t = new Vertex(v.posX() - 1, v.posZ() + 1);
			adj.add(t);
		}
		if(v.posZ() != maxZ && grid.getStatus(v.posX(), v.posZ() + 1) != 2) {
			t = new Vertex(v.posX() + 1, v.posZ() + 1);
			adj.add(t);
		}
		if(v.posX() != maxX && v.posZ() != maxZ && grid.getStatus(v.posX() + 1, v.posZ() + 1) != 2) {
			t = new Vertex(v.posX() + 1, v.posZ() + 1);
			adj.add(t);
		}
		if(v.posX() != 0 && grid.getStatus(v.posX() - 1, v.posZ()) != 2) {
			t = new Vertex(v.posX() - 1, v.posZ());
			adj.add(t);
		}
		if(grid.getStatus(v.posX(), v.posZ()) != 2) {
			t = new Vertex(v.posX(), v.posZ());
			adj.add(t);
		}
		if(v.posX() != maxX && grid.getStatus(v.posX() + 1, v.posZ()) != 2) {
			t = new Vertex(v.posX() + 1, v.posZ());
			adj.add(t);
		}
		if(v.posX() != 0 && v.posZ() != 0 && grid.getStatus(v.posX() - 1, v.posZ() - 1) != 2) {
			t = new Vertex(v.posX() - 1, v.posZ() - 1);
			adj.add(t);
		}
		if(v.posZ() != 0 && grid.getStatus(v.posX(), v.posZ() - 1) != 2) {
			 t = new Vertex(v.posX(), v.posZ() - 1);
			adj.add(t);
		}
		if(v.posX() != maxX && v.posZ() != 0 && grid.getStatus(v.posX() + 1, v.posZ() - 1) != 2) {
			t = new Vertex(v.posX() + 1, v.posZ() - 1);
			adj.add(t);
		}
		
		return adj;
	}
}
