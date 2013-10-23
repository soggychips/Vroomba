package main;
import java.util.ArrayList;


public class Pathfinder {
	ShortGrid grid;
	
	public Pathfinder(ShortGrid g) {
		this.grid = g;
	}
	
//	ArrayList<Vertex> findPath(Vertex source, Vertex dest) {
//		ArrayList<Vertex> path = new ArrayList<Vertex>();
//		path.add(new Vertex(4, 4));
//		path.add(new Vertex(4, 3));
//		
//		return path;
//		
//	}
	
	//MUST AVOID INFINITE LOOPS! MAY OCCUR WHEN ROBOT IS SURROUNDED BY 2+ WALLS -JAKE
	// returns arraylist of vertices from source to destination
	ArrayList<Vertex> findPath(Vertex source, Vertex dest) {
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		boolean pathFound = false;
		int LEFT=0, DOWN=1, RIGHT=2, UP=3; //JAKE
		int lastMove;						//JAKE
		Vertex next = new Vertex(source.posX(), source.posZ());
		
		while(!pathFound) {	
			boolean optimalMove = false;
			if(dest.posX() == next.posX() && dest.posZ() == next.posZ() ) { //robot is at destination vertex
				path.add(new Vertex(next.posX(), next.posZ()));
				pathFound = true;
				break;
			}
			else if(dest.posZ() > next.posZ() && lastMove!=DOWN && //JAKE
					grid.getStatus(next.posX(), next.posZ() + 1) ==1) { //destination is north of "next"
				next.moveNorth();
				path.add(new Vertex(next.posX(), next.posZ()));
				optimalMove = true;
			}	
			else if(dest.posX() < next.posX() && lastMove!=RIGHT && //JAKE
					grid.getStatus(next.posX() - 1, next.posZ()) ==1) { //destination is west of "next"
				next.moveWest();
				path.add(new Vertex(next.posX(), next.posZ()));
				optimalMove = true;
			}
			else if(dest.posZ() < next.posZ() && lastMove!=UP && //JAKE
					grid.getStatus(next.posX(), next.posZ() - 1) ==1) { //destination is south of "next"
				next.moveSouth();
				path.add(new Vertex(next.posX(), next.posZ()));
				optimalMove = true;
			}
			else if(dest.posX() > next.posX() && lastMove!=LEFT && //JAKE
					grid.getStatus(next.posX() + 1,  next.posZ()) ==1) { //destination is east of "next:
				next.moveEast();
				path.add(new Vertex(next.posX(), next.posZ()));
				optimalMove = true;
			}
			lastMove=-1; //resets lastMove -JAKE
			
			
				
			
			// If you couldn't move in the direction of the destination,
			// you still have to move. 
			//HOWEVER, YOU CAN'T MAKE A MOVE IN THE OPPOSITE DIRECTION OF THE LAST MOVE
			//I.E. DON'T GO RIGHT WHEN YOU JUST WENT LEFT, AND THEN GO LEFT AFTER YOU JUST WENT RIGHT, ETC.
			if(!optimalMove) {
				if(grid.getStatus(next.posX() - 1, next.posZ()) ==1) { //if tile west is open, move
					next.moveWest();
					lastMove=LEFT; //JAKE
					path.add(new Vertex(next.posX(), next.posZ()));
				}
				else if(grid.getStatus(next.posX(), next.posZ() - 1) ==1) { //if tile south is open, move
					next.moveSouth();
					lastMove=DOWN; //JAKE
					path.add(new Vertex(next.posX(), next.posZ()));
				}
				else if(grid.getStatus(next.posX() + 1,  next.posZ()) ==1) { //east
					next.moveEast();
					lastMove=RIGHT; //JAKE
					path.add(new Vertex(next.posX(), next.posZ()));
				}
				else if(grid.getStatus(next.posX(), next.posZ() + 1) ==1) { //north
					next.moveNorth();
					lastMove=UP; //JAKE
					path.add(new Vertex(next.posX(), next.posZ()));
				}
				else { //nothing?
					
				}
			}
		}	
		
		return path;
	}
}
