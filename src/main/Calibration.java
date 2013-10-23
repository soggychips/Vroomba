package main;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class Calibration {

	// global variables
	private DifferentialPilot pilot;
	private UltrasonicSensor sonar;
	private TouchSensor touch;
	private NXTRegulatedMotor motor;

	// some constants
	private final float wheelDiameter = 4.32f;
	private final float trackWidth = 15.25f;

	// constructor
	public Calibration() {

		// initialization
		pilot = new DifferentialPilot(wheelDiameter, trackWidth, Motor.B,
				Motor.C);
		sonar = new UltrasonicSensor(SensorPort.S4);
		touch = new TouchSensor(SensorPort.S1);
		motor = Motor.A;

		// final travel speed = 20 cm/s
		// tile distance = 30.5 to 31.0
		

		
		
		
//		double tileDist = calibrateTileDistace();
//		System.out.println("Tile Distance = " + tileDist);
//		Button.waitForAnyPress();
//		
//		pilot.setTravelSpeed(20);
//		pilot.travel(5*tileDist);	
				
	}	
	
	/**
	 * This method calibrates the tile distance.
	 * 
	 * Make sure the robot runs for 10 tiles and press 
	 * the ORANGE button when at the 10th tile.
	 * 
	 * This method wait on another button press to 
	 * continue the rest of the prgram.
	 *   
	 * @return the average tile distance
	 */
	private double calibrateTileDistace() {
		double dist = 0.0;
		pilot.setTravelSpeed(20);
		pilot.forward();
		
		while(!Button.ENTER.isDown())
			dist = pilot.getMovement().getDistanceTraveled();
		
		pilot.stop();	
		Button.waitForAnyPress();
		return dist/10;		
	}

	// main
	public static void main(String[] args) {
		new Calibration();
	}

}
