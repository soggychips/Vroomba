package main;
import java.util.ArrayList;


public class Pathfinder {
	ShortGrid grid;
	
	public Pathfinder(ShortGrid g) {
		this.grid = g;
	}
	
	// returns arraylist of vertices from source to destination
	ArrayList<Vertex> findPath(Vertex source, Vertex dest) {
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		boolean pathFound = false;
		Vertex next = new Vertex(source.posX(), source.posZ());
		
		while(!pathFound) {	
			boolean optimalMove = false;
			if(dest.posX() == next.posX() && dest.posZ() == next.posZ() ) { //robot is at destination vertex
				path.add(new Vertex(next.posX(), next.posZ()));
				pathFound = true;
				break;
			}
			else if(dest.posX() < next.posX() && 
					grid.getStatus(next.posX() - 1, next.posZ()) != 2) { //destination is west of "next"
				next.moveWest();
				path.add(new Vertex(next.posX(), next.posZ()));
				optimalMove = true;
			}
			else if(dest.posX() > next.posX() &&
					grid.getStatus(next.posX() + 1,  next.posZ()) != 2) { //destination is east of "next:
				next.moveEast();
				path.add(new Vertex(next.posX(), next.posZ()));
				optimalMove = true;
			}
			else if(dest.posZ() < next.posZ() &&
					grid.getStatus(next.posX(), next.posZ() - 1) != 2) { //destination is south of "next"
				next.moveSouth();
				path.add(new Vertex(next.posX(), next.posZ()));
				optimalMove = true;
			}
			else if(dest.posZ() > next.posZ() &&
					grid.getStatus(next.posX(), next.posZ() + 1) != 2) { //destination is north of "next"
				next.moveNorth();
				path.add(new Vertex(next.posX(), next.posZ()));
				optimalMove = true;
			}		
			
			// If you couldn't move in the direction of the destination,
			// you still have to move
			if(!optimalMove) {
				if(grid.getStatus(next.posX() - 1, next.posZ()) != 2) { //if tile west is open, move
					next.moveWest();
					path.add(new Vertex(next.posX(), next.posZ()));
				}
				else if(grid.getStatus(next.posX(), next.posZ() - 1) != 2) { //if tile south is open, move
					next.moveSouth();
					path.add(new Vertex(next.posX(), next.posZ()));
				}
				else if(grid.getStatus(next.posX() + 1,  next.posZ()) != 2) { //east
					next.moveEast();
					path.add(new Vertex(next.posX(), next.posZ()));
				}
				else if(grid.getStatus(next.posX(), next.posZ() + 1) != 2) { //north
					next.moveNorth();
					path.add(new Vertex(next.posX(), next.posZ()));
				}
				else { //nothing?
					
				}
			}
		}	
		
		return path;
	}
}
