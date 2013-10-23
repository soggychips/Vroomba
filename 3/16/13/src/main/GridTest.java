package main;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.navigation.DifferentialPilot;


public class GridTest {

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
		
		// TODO Auto-generated method stub
		if (logging) RConsole.openBluetooth(5000);
		
		//(0,3) = 0
		//(0,4) = 2
		//(1,0) = 2
		//(1,4) = 2
		//(2,0) = 2
		//(2,4) = 2
		//(3,0) = 2
		//(3,4) = 2
		//(4,0) = 2
		//(4,1) = 
		//(4,3) = 0
		//(4,4) = 2
		
		int startX = 1;
		int startY = 1;
		int curX = 1;
		int curY = 1;
		Grid grid = new Grid(5,5);
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				if(i ==0){
					System.out.println("In this case");
					grid.setPosition(i, j, 2);
				}
				else if( i == 1 && (j == 0 || j == 4)){
					System.out.println("In this case");
					grid.setPosition(i, j, 2);
				}
				else if( i == 2 && (j == 0 || j == 4)){
					System.out.println("In this case");
					grid.setPosition(i, j, 2);
				}
				else if( i == 3 && (j == 0 || j == 4)){
					System.out.println("In this case");
					grid.setPosition(i, j, 2);
				}
				else if( i == 4 ){
					System.out.println("In this case");
					grid.setPosition(i, j, 2);
				}
				else{
					grid.setPosition(i, j, 0);
				}
				
				
//				Button.waitForAnyPress();
				
//				if(i == 0 || i == 4 || j == 0 || j == 4) {
//					grid.setPosition(i, j, 2);
//					
//				}
//				else {
//					grid.setPosition(i, j, 0);
//					System.out.println("x: " + i);
//					System.out.println("z: " + j);
//					System.out.println("val:" + grid.getPosition(i, j));
//				}
			}
		}
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				System.out.print(grid.getPosition(i, j) + "|");
			}
			System.out.println();
		}
		Button.waitForAnyPress();
		grid.printGrid();
		
		grid.setPosition(1, 1, 1);
		boolean stillMoving = true;
		boolean started = true;
		int direction = Direction.EAST;
		while(stillMoving) {
			if(!(curX == startX && curY == startY) || started) {
				System.out.println(grid.getPosition(curX, curY));
				Button.waitForAnyPress();
				started = false;
				if(direction == Direction.EAST) {
					System.out.println("In EAST");
					System.out.println("x: " + curX);
					System.out.println("z: " + curY);
					if(grid.getPosition(curX + 1, curY) != 2) {
						upOneTile();
						grid.setPosition(curX, curY, 1);
						//System.out.println(grid.getPosition(curX, curY));
//						Button.waitForAnyPress();
						curX++;
						grid.printGrid();
						//System.out.println((curX != startX) + " and " +(curY != startY));
					}
					else {
						pirate.rotate(LEFT_ANGLE);
						direction = Direction.NORTH;
					}
				}
				else if(direction == Direction.NORTH) {
					System.out.println("In NORTH");
					System.out.println("x: " + curX);
					System.out.println("z: " + curY);
					//System.out.println(grid.getPosition(curX, curY));
//					Button.waitForAnyPress();
					if(grid.getPosition(curX, curY + 1) != 2) {
						upOneTile();
						grid.setPosition(curX, curY, 1);
						curY++;
						grid.printGrid();
					}
					else {
						pirate.rotate(LEFT_ANGLE);
						direction = Direction.WEST;
					}
				} 
				else if(direction == Direction.WEST) {
					System.out.println("In WEST");
					System.out.println("x: " + curX);
					System.out.println("z: " + curY);
//					System.out.println(grid.getPosition(curX, curY));
					Button.waitForAnyPress();
					if(grid.getPosition(curX - 1, curY) != 2 && (curX>0)) {
						System.out.println((grid.getPosition(curX - 1, curY) != 2));
						upOneTile();
						grid.setPosition(curX, curY, 1);
						curX--;
						grid.printGrid();
					}
					else {
						pirate.rotate(LEFT_ANGLE);
						direction = Direction.SOUTH;
					}
				}
				else if(direction == Direction.SOUTH) {
					System.out.println("In South");
					System.out.println(grid.getPosition(curX, curY));
//					Button.waitForAnyPress();
					if(grid.getPosition(curX, curY - 1) != 2 && (curY>0)) {
						upOneTile();
						grid.setPosition(curX, curY, 1);
						curY--;
						grid.printGrid();
					}
					else {
						pirate.rotate(LEFT_ANGLE);
						direction = Direction.EAST;
					}
				}
			}
			else {
				System.out.println("Going into else");
				System.out.println(grid.getPosition(curX, curY));
				Button.waitForAnyPress();
				stillMoving = false;
				if (logging) RConsole.println("done");
				if (logging) RConsole.close();
				break;
			}
			
		}
/*
		int curx = 0; int cury=0;
		boolean complete=false;
		int facing=0;
		
		pirate.setTravelSpeed(20);
		grid.setPosition(0,0,1);
		while(!complete){
			//assume facing +x direction
			switch(facing) {
			case 0:
				if(curx<2){
					upOneTile();
					grid.setPosition(curx,cury,1);
					curx++;
				}else {
					pirate.rotate(LEFT_ANGLE);
					facing++;
				}
				break;
			case 1:
				if(cury<2){
					upOneTile();
					grid.setPosition(curx,cury,1);
					cury++;
				}else {
					pirate.rotate(LEFT_ANGLE);
					facing++;
				}
				break;
			case 2:
				if(curx>0){
					upOneTile();
					grid.setPosition(curx,cury,1);
					curx--;
				}else {
					pirate.rotate(LEFT_ANGLE);
					facing++;
				}
				break;
			case 3:
				if(cury>0){
					upOneTile();
					
					grid.setPosition(curx,cury,1);
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
		*/
	}

	private static void upOneTile() {
		// TODO Auto-generated method stub
		pirate.forward();
		while( getLastMovedDistance() < pos) {
		}
		pirate.stop();
	}
	

}
