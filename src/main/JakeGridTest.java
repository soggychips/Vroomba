package main;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;
public class JakeGridTest {

	private static float wheelDiameter = 4.32f;
	private static float trackWidth = 15.25f;
	private static DifferentialPilot pirate = 
			new DifferentialPilot(wheelDiameter, trackWidth, Motor.B, Motor.C);
	private static double LEFT_ANGLE = 85.0d;
	private static double RIGHT_ANGLE = -85.0d;
	private static double pos=2.8d;
	
	private static final boolean logging = false;
	
	
	/**
	 * @param args
	 */
	static double getLastMovedDistance() {
		return pirate.getMovementIncrement()/10;
	}
	
	public static class Direction {
		public static int EAST = 0;
		public static int WEST = 1;
		public static int NORTH = 2;
		public static int SOUTH = 3;
	}
	
	public static void main(String[] args) {
		ShortGrid grid = new ShortGrid(5,5);
		int curx = 0; int cury=0;
		boolean complete=false;
		int facing=0;
		
		pirate.setTravelSpeed(20);
		grid.setStatus(0,0,1);
		while(!complete){
			//assume facing +x direction
			switch(facing) {
			case 0:
				if(curx<2){
					moveOneTile();
					grid.setStatus(curx,cury,1);
					curx++;
				}else {
					pirate.rotate(LEFT_ANGLE);
					facing++;
				}
				break;
			case 1:
				if(cury<2){
					moveOneTile();
					grid.setStatus(curx,cury,1);
					cury++;
				}else {
					pirate.rotate(LEFT_ANGLE);
					facing++;
				}
				break;
			case 2:
				if(curx>0){
					moveOneTile();
					grid.setStatus(curx,cury,1);
					curx--;
				}else {
					pirate.rotate(LEFT_ANGLE);
					facing++;
				}
				break;
			case 3:
				if(cury>0){
					moveOneTile();
					
					grid.setStatus(curx,cury,1);
					cury--;
				}else {
					pirate.rotate(LEFT_ANGLE);
					facing++;
					complete=true;
				}
				break;
			}
			
			
		}
		grid.printGrid();
	}


	private static void moveOneTile() {
		// TODO Auto-generated method stub
		pirate.forward();
		while( getLastMovedDistance() < pos) {
		}
		pirate.stop();
	}
	
}
