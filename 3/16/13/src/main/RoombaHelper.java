package main;

import java.util.ArrayList;
import java.util.Stack;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.navigation.DifferentialPilot;
import main.ShortGrid.Tile;

public class RoombaHelper {

	public static final int FACING_SOUTH = 0, FACING_EAST = 1,
			FACING_NORTH = 2, FACING_WEST = 3, FACING_SP_SOUTH = 4;

	public static final int BLOCKED_NONE = 0, BLOCKED_LEFT = 1,
			BLOCKED_RIGHT = 2, BLOCKED_FRONT = 3, BLOCKED_ELSE = 4;

	private static final float wheelDiameter = 4.32f;
	private static final float trackWidth = 15.25f;
	private static final DifferentialPilot pirate = new DifferentialPilot(wheelDiameter, trackWidth, Motor.B, Motor.C);
	private static final UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S4);
	private static final int n = 8;
	static ShortGrid grid = new ShortGrid(n, n);

	public static byte facing = FACING_SOUTH;
	public static byte largestx = n / 2, lowestx = n / 2, largesty = n / 2,
			lowesty = n / 2, currentx = n / 2, currenty = n / 2;
	public static int perimeterStartX = -1;
	public static int perimeterStartY = -1;

	public static double LEFT_ANGLE = 85.0d;
	public static double RIGHT_ANGLE = -85.0d;
	private static double pos = 2.0d;

	/** <code>true</code> to enable logging */
	private static final boolean logging = false;
	
	private static Pathfinder pathLocator = new Pathfinder(grid);

	private static Stack<Vertex> revisitTiles = new Stack<Vertex>();
	
	public static ArrayList<Vertex> path = new ArrayList<Vertex>();

	public static void main(String[] args) throws InterruptedException {
		System.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				super.run();
				RConsole.close();
			}
		});

		if (logging)
			RConsole.openBluetooth(7000);
		pirate.setTravelSpeed(20);
		if (logging)
			RConsole.println("Travel speed was set to: " + pirate.getTravelSpeed());
		
		grid.setStatus(currentx, currenty, 1);
		while (!Button.ESCAPE.isDown() && !isSurrounded()) {
			move();
		}
		Button.waitForAnyPress();
	}

	public static void calibrate() {
		pirate.forward();
		while (!Button.ESCAPE.isDown()) { }
		pirate.stop();
	}

	/**
	 * Returns the distance traveled since it started moving, in centimeters.
	 * @return Distance in centimeters.
	 */
	static double getLastMovedDistance() {
		return pirate.getMovementIncrement() / 10;
	}
	
	
	static boolean isSurrounded() {
		if (grid.getStatus(currentx, currenty - 1) != 0
				&& grid.getStatus(currentx, currenty + 1) != 0
				&& grid.getStatus(currentx - 1, currenty) != 0
				&& grid.getStatus(currentx + 1, currenty) != 0 
				) {
//			if (logging) {
//				RConsole.println("Suck a dick I'm done");
////				grid.printGrid();
//			}
//			
			if(revisitTiles.empty()){
				printGrid();
				return true;
			}
			else{
				while(!revisitTiles.empty()){
					System.out.println(revisitTiles.pop());
//					path = pathLocator.findPath(new Vertex(currentx,currenty), revisitTiles.pop());
//					traversePath(path);
				}
				printGrid();
				Button.waitForAnyPress();
				return true;
			}
		} else {
			if (logging)
				printGrid();
				RConsole.println("not done");

			System.out.println("not done");
			return false;
		}
	}
	
	static void traversePath(ArrayList<Vertex> p){
		for(int i=1;i<p.size();i++){
			if(p.get(i).posX()==(currentx-1)){
				turn(facing,FACING_WEST);
				currentx--;
			}else if(p.get(i).posX()==(currentx+1)){
				turn(facing,FACING_EAST);
				currentx++;
			}else if(p.get(i).posZ()==(currenty-1)){
				turn(facing,FACING_SOUTH);
				currenty--;
			}else if(p.get(i).posZ()==(currenty+1)){
				turn(facing,FACING_NORTH);
				currenty++;
			}
			pirate.forward();
			while (getLastMovedDistance() < 20.6) {}
			pirate.stop();
		}
	}
	
	public static void turn(int currentFacing, int desiredFacing){
		switch(currentFacing){
		case (FACING_SOUTH):
			if(desiredFacing==FACING_NORTH){
				pirate.rotate(LEFT_ANGLE);
				pirate.rotate(LEFT_ANGLE);
			}else if(desiredFacing==FACING_EAST){
				pirate.rotate(LEFT_ANGLE);
			}else if(desiredFacing==FACING_WEST){
				pirate.rotate(RIGHT_ANGLE);
			}
			break;
		case (FACING_EAST):
			if(desiredFacing==FACING_WEST){
				pirate.rotate(LEFT_ANGLE);
				pirate.rotate(LEFT_ANGLE);
			}else if(desiredFacing==FACING_NORTH){
				pirate.rotate(LEFT_ANGLE);
			}else if(desiredFacing==FACING_SP_SOUTH){
				pirate.rotate(RIGHT_ANGLE);
			}
			break;
		case (FACING_NORTH):
			if(desiredFacing==FACING_SP_SOUTH){
				pirate.rotate(LEFT_ANGLE);
				pirate.rotate(LEFT_ANGLE);
			}else if(desiredFacing==FACING_WEST){
				pirate.rotate(LEFT_ANGLE);
			}else if(desiredFacing==FACING_EAST){
				pirate.rotate(RIGHT_ANGLE);
			}
			break;
		case (FACING_WEST):
			if(desiredFacing==FACING_EAST){
				pirate.rotate(LEFT_ANGLE);
				pirate.rotate(LEFT_ANGLE);
			}else if(desiredFacing==FACING_SP_SOUTH){
				pirate.rotate(LEFT_ANGLE);
			}else if(desiredFacing==FACING_NORTH){
				pirate.rotate(RIGHT_ANGLE);
			}
			break;
		case (FACING_SP_SOUTH):
			if(desiredFacing==FACING_NORTH){
				pirate.rotate(LEFT_ANGLE);
				pirate.rotate(LEFT_ANGLE);
			}else if(desiredFacing==FACING_EAST){
				pirate.rotate(LEFT_ANGLE);
			}else if(desiredFacing==FACING_WEST){
				pirate.rotate(RIGHT_ANGLE);
			}
			break;
		}
		facing = (byte)desiredFacing;
	}

	private static void printGrid() {
		for(int j = n-1; j >=0 ; j--) {
			System.out.println("");
			for(int i = 0; i < n; i++) {
				System.out.print(grid.getStatus(i, j)+"_");
			}
		}
		Button.waitForAnyPress();
	}

	public static void move() {
		int clear = getClear();
		switch (facing) { // south,east,north,west, specialSouth
		case FACING_SOUTH: // south
			if (clear == BLOCKED_NONE && grid.getStatus(currentx, currenty - 1) == 0) {
				checkTileAndPushToStack();
				pirate.forward();
				while (getLastMovedDistance() < pos) {
					
				}
				pirate.stop();
				currenty--;
				grid.setStatus(currentx, currenty, 1);
				if (currenty > lowesty)
					lowesty = currenty;
			} else {
				grid.setStatus(currentx, currenty - 1, 2);
//				pirate.rotate(LEFT_ANGLE);
//				facing = FACING_EAST;
				turn(facing, FACING_EAST);
			}
			break;
		case FACING_EAST: // east
			// boner.activate()
			if (currentx + 1 == perimeterStartX && currenty == perimeterStartY) {
				getOutOfTile();
//				Sound.beepSequence();
//				pirate.rotate(LEFT_ANGLE);
//				facing = FACING_NORTH;
				turn(facing, FACING_NORTH);
			} else if (clear == BLOCKED_NONE && grid.getStatus(currentx + 1, currenty) == Tile.TRAVERSABLE) {
				checkTileAndPushToStack();
				pirate.forward();
				while (getLastMovedDistance() < pos) {
					
				}
				pirate.stop();
				currentx++;
				grid.setStatus(currentx, currenty, 1);
				if (currentx > largestx)
					largestx = currentx;
				pirate.rotate(RIGHT_ANGLE);
				clear = checkTile();
				if (clear == BLOCKED_NONE && grid.getStatus(currentx, currenty - 1) == Tile.TRAVERSABLE) {
					facing = FACING_SOUTH;
				} else {
					grid.setStatus(currentx, currenty - 1, 2);
					pirate.rotate(LEFT_ANGLE);
				}
			} else {
				grid.setStatus(currentx + 1, currenty, 2);
				pirate.rotate(LEFT_ANGLE);
				if (currentx >= largestx && currentx != perimeterStartX) {
					perimeterStartX = currentx;
					perimeterStartY = currenty;
					System.out.println("Perimeter: " + perimeterStartX + ", "
							+ perimeterStartY);
					printGrid();
//					Sound.beepSequenceUp();
				}
				facing = FACING_NORTH;
			}
			break;
		case FACING_NORTH: // north
			if (clear == BLOCKED_NONE && grid.getStatus(currentx, currenty + 1) == Tile.TRAVERSABLE) {
				checkTileAndPushToStack();
				pirate.forward();
				while (getLastMovedDistance() < pos) {
				}
				pirate.stop();
				currenty++;
				grid.setStatus(currentx, currenty, 1);
				if (currenty > largesty)
					largesty = currenty;
				pirate.rotate(RIGHT_ANGLE);
				clear = checkTile();
				if (clear == BLOCKED_NONE && grid.getStatus(currentx + 1, currenty) == Tile.TRAVERSABLE) {
					facing = FACING_EAST;
				} else {
					grid.setStatus(currentx + 1, currenty, 2);
					pirate.rotate(LEFT_ANGLE);
				}
			} else {
				grid.setStatus(currentx, currenty + 1, 2);
//				pirate.rotate(LEFT_ANGLE);
//				facing = FACING_WEST;
				turn(facing,FACING_WEST);
			}
			break;
		case FACING_WEST: // west
			if (clear == BLOCKED_NONE && grid.getStatus(currentx - 1, currenty) == Tile.TRAVERSABLE) {
				checkTileAndPushToStack();
				pirate.forward();
				while (getLastMovedDistance() < pos) {
				}
				pirate.stop();
				currentx--;
				grid.setStatus(currentx, currenty, 1);
				if (currentx < lowestx)
					lowestx = currentx;
				pirate.rotate(RIGHT_ANGLE);
				clear = checkTile();
				if (clear == BLOCKED_NONE
						&& grid.getStatus(currentx, currenty + 1) == 0)
					facing = FACING_NORTH;
				else {
					grid.setStatus(currentx, currenty + 1, 2);
					pirate.rotate(LEFT_ANGLE);
				}
			} else {
				grid.setStatus(currentx - 1, currenty, 2);
//				pirate.rotate(LEFT_ANGLE);
//				facing = FACING_SP_SOUTH;
				turn(facing, FACING_SP_SOUTH);
			}
			break;
		case FACING_SP_SOUTH:
			if (clear == BLOCKED_NONE
					&& grid.getStatus(currentx, currenty - 1) == 0) {
				checkTileAndPushToStack();
				pirate.forward();
				while (getLastMovedDistance() < pos) {
				}
				pirate.stop();
				currenty--;
				grid.setStatus(currentx, currenty, 1);
				if (currenty > lowesty)
					lowesty = currenty;
				pirate.rotate(RIGHT_ANGLE);
				clear = checkTile();
				if (clear == BLOCKED_NONE
						&& grid.getStatus(currentx - 1, currenty) == 0)
					facing = FACING_WEST;
				else {
					grid.setStatus(currentx, currenty + 1, 2);
					pirate.rotate(LEFT_ANGLE);
				}
			} else {
				grid.setStatus(currentx, currenty - 1, 2);
//				pirate.rotate(LEFT_ANGLE);
//				facing = FACING_EAST;
				turn(facing,FACING_EAST);
			}
			break;
		}
	}

	/**
	 * Checks if the tile that the robot is in has corners that hasn't been scanned yet. <br />
	 * If there are unexplored corners, it pushes the tile to the stack indicating it needs further attention. 
	 * 
	 * 
	 */
	private static void checkTileAndPushToStack() {
		// TODO Auto-generated method stub
		if(grid.needsAttention(currentx, currenty,n,facing)){
//			Button.waitForAnyPress();
			Vertex current = new Vertex(currentx,currenty);
			revisitTiles.push(current);
			System.out.println("Pushing " + current);
		}
	}

	/**
	 * Checks around the robot.
	 * 
	 * @return Status code: <br />
	 *         0 - All clear <br />
	 *         1 - Something on the left <br />
	 *         2 - Something on the right <br />
	 *         3 - Something in front <br />
	 *         4 - Something was detected
	 */
	private static int checkTile() {
		boolean left = true;
		boolean right = true;
		boolean front = true;
		// String logMsg = "";
		Motor.A.rotate(-90);
		/*
		 * if (logging) logMsg += (" 180deg: " + sonic.getDistance());
		 */
		if (sonic.getDistance() < 8) {
			left = false;
		}
		Motor.A.rotate(30);
		/*
		 * if (logging) logMsg += (" 150deg: " + sonic.getDistance());
		 */
//		Sound.playTone(sonic.getDistance() * 20, 100);
		if (sonic.getDistance() < 12) {
			left = false;
		}
		Motor.A.rotate(30);
		/*
		 * if (logging) logMsg += (" 120deg: " + sonic.getDistance());
		 */
//		Sound.playTone(sonic.getDistance() * 20, 100);
		if (sonic.getDistance() < 24) {
			left = false;
		}
		Motor.A.rotate(30);
		/*
		 * if (logging) logMsg += ("  90deg: " + sonic.getDistance());
		 */
//		Sound.playTone(sonic.getDistance() * 20, 100);
		if (sonic.getDistance() < 23) {
			front = false;
		}
		Motor.A.rotate(30);
		/*
		 * if (logging) logMsg += ("  60deg: " + sonic.getDistance());
		 */
//		Sound.playTone(sonic.getDistance() * 20, 100);
		if (sonic.getDistance() < 24) {
			right = false;
		}
		Motor.A.rotate(30);
		/*
		 * if (logging) logMsg += ("  30deg: " + sonic.getDistance());
		 */
//		Sound.playTone(sonic.getDistance() * 20, 100);
		if (sonic.getDistance() < 12) {
			right = false;
		}
		Motor.A.rotate(30);
		/*
		 * if (logging) logMsg += ("   0deg: " + sonic.getDistance());
		 */
//		Sound.playTone(sonic.getDistance() * 20, 100);
		if (sonic.getDistance() < 8) {
			right = false;
		}
		Motor.A.rotate(-90);
		/*
		 * if (logging) logMsg += ("  90deg: " + sonic.getDistance());
		 */
//		Sound.playTone(sonic.getDistance() * 20, 100);

		/*
		 * if (logging) RConsole.println(logMsg);
		 */

		if (left && right && front) {
			return BLOCKED_NONE;
		} else if (!left) {
			// getOutOfTile();
			return BLOCKED_LEFT;
		} else if (!right) {
			// getOutOfTile();
			return BLOCKED_RIGHT;
		} else if (!front) {
			// getOutOfTile();
			return BLOCKED_FRONT;
		} else {
			// getOutOfTile();
			return BLOCKED_ELSE;
		}

	}

	/**
	 * Checks around the robot.
	 * 
	 * @return Status code: <br />
	 *         0 - All clear <br />
	 *         1 - Something on the left <br />
	 *         2 - Something on the right <br />
	 *         3 - Something in front <br />
	 *         4 - Something was detected
	 */
	private static int getClear() {
		int check;
		// String logMsg = "";
		if (checkfront() == BLOCKED_FRONT) {
			return BLOCKED_FRONT;
		}

		check = checkTile();
		if (check == 0) {
			return BLOCKED_NONE;
		} else {
			getOutOfTile();
			return check;
		}
	}

	private static int checkfront() {
		if (sonic.getDistance() > 15) {
			getInTile();
		} else {
//			Sound.buzz();
			if (logging)
				RConsole.println("Buzz!: " + sonic.getDistance());
			return BLOCKED_FRONT;
		}
		return currentx;

	}

	private static void getInTile() {
		pirate.forward();
		while (getLastMovedDistance() < 0.65) {
		}
		pirate.stop();
	}

	static void getOutOfTile() {
		pirate.backward();
		while (getLastMovedDistance() > -0.65) {
			// do nothing, just continue to move backwards
		}
		pirate.stop();
	}

}
