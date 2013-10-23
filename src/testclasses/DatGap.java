package testclasses;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.SoundSensor;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;

//dfh/df/hdfhdf/hdf/df/hdfh
public class DatGap {
	
	private static SoundSensor sound;
	private static TouchSensor touch;
	private final static int START_SOUND = 35;
	private static boolean allStop = false;
	private static LightSensor light;
	private static boolean parked = false; 

	public static void main(String[] args) throws InterruptedException {
		 
		sound = new SoundSensor(SensorPort.S4, true);
		touch = new TouchSensor(SensorPort.S3);
		light = new LightSensor(SensorPort.S2, true);

		System.out.println("Clap to make me go...");
		Sound.playNote(Sound.PIANO, 700, 300);
		Sound.playNote(Sound.PIANO, 400, 500);
//		findGap(43.2, 12.0, Motor.A, Motor.B);
		
		while(!Button.ESCAPE.isPressed()){
//			findGap(43.2, 12.0, Motor.A, Motor.B);
			
			//System.out.println("Sound at "+ max);
			if(sound.readValue() > START_SOUND){
				
				//Victory Doughnut 
				if(sound.readValue() > START_SOUND && parked){
					haveFun(43.2, 12.0, Motor.A, Motor.B);
					break;
				}
				
				System.out.println("Looking for parking...");
				findGap(43.2, 12.0, Motor.A, Motor.B);
				
				if(allStop){
					LCD.clear();
					System.out.println("OOPS... Call 911");
					Sound.buzz();
					Sound.buzz();
					Thread.sleep(10000);
					break;
				}
				Sound.playNote(Sound.PIANO, 400, 350);
				Sound.playNote(Sound.PIANO, 700, 500);
				LCD.clear();
				System.out.println("Finished Parking");
				System.out.println("Have a nice day. ");
//				Thread.sleep(5000);
//				break;
			}
			
		}
		
	}
	
	private static void haveFun(double wheelDiameter, double trackWidth, NXTRegulatedMotor... motor) {
		// TODO Auto-generated method stub
		DifferentialPilot pilot = new DifferentialPilot(wheelDiameter, trackWidth, motor[0], motor[1]);
		
		pilot.setTravelSpeed(200);
		Motor.C.rotate(-90);
		pilot.backward();
		double current = pilot.getMovement().getDistanceTraveled()/10;
		double pos = current - 25;
		while((pilot.getMovement().getDistanceTraveled()/10) > pos){
			
		}
//		pilot.stop();
		Motor.C.rotate(160);
//		current = pilot.getMovement().getDistanceTraveled()/10;
//		pos = current - 60;
		while(!Button.ESCAPE.isPressed()){
			pilot.backward();
		}
		pilot.stop();
	
		
	}

	static int startReading, endReading;
//	private static int minLimit;
	private static final int PARKING_LIMIT = 21;
	public static final int threshold = 4;
	
	//good speeds for testing.
//	public static final double parking_speed = 6.0;
	public static final double parking_speed2 = 130.0;
	
	
	public static void findGap(double wheelDiameter, double trackWidth, NXTRegulatedMotor... motor) {
		final UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S1);
		
		DifferentialPilot pilot = new DifferentialPilot(wheelDiameter, trackWidth, motor[0], motor[1]);
		
		pilot.setTravelSpeed(parking_speed2);
		pilot.backward();
//		PARKING_LIMIT = 23;
//		minLimit = startReading - threshold;
		
		while(true) {
			//Find the spot

//			Allstop. Stops everything. 
			if(allStop){
				break;
			}
			if(findSpot(pilot,sonic,PARKING_LIMIT)){
				System.out.println("Parking...");
				getInPark(pilot);
				break;
			}
			
		}
		
//		System.out.println("Moved " + (pilot.getMovement().getDistanceTraveled()/10) + "cm.");
	}


	private static boolean findSpot(DifferentialPilot pilot, UltrasonicSensor sonic, int maxLimit2) {
		// TODO Auto-generated method stub
		final double MIN_SPOT = 12; 
		double currentLoc = 0.0; 
		boolean foundSpot = false; 
		boolean gapStarted = false; 
		while(!foundSpot){
			//Car hits something. 
			if(touch.isPressed()){
				pilot.stop();
				allStop = true; 
				return false;
			}
			
			//Light sensor stops when it sees a shade of green. 
//			if(light.readNormalizedValue() > 370 && light.readNormalizedValue() < 420){
//				pilot.stop();
//				allStop = true; 
//				return false;
//			}
			
			if(gapStarted)
			{
				
//				System.out.println("Gap started " + currentLoc);
							 
			}
			else
			{
				gapStarted = gapPresent(sonic,maxLimit2);
//				System.out.println("No gap");
				currentLoc = pilot.getMovement().getDistanceTraveled()/10;
			}
			
			if(gapStarted && gapPresent(sonic,maxLimit2) && ( pilot.getMovement().getDistanceTraveled()/10 <= (currentLoc - MIN_SPOT))){
				foundSpot  = true; 
//				System.out.println("Found Spot");
			}
			else if(!gapPresent(sonic,maxLimit2) && gapStarted)
			{
//				System.out.println("GAP ENDED: "+ (currentLoc));
//				System.out.println("Ending Value: " + ((currentLoc - MIN_SPOT)));
				gapStarted = false; 
				currentLoc = 0; 
			}
		}
		
		
		
		return foundSpot; 
	}


	private static boolean gapPresent(UltrasonicSensor sonic, int maxLimit2) {
		// TODO Auto-generated method stub
		int reading = sonic.getDistance();
		if(reading > maxLimit2){
			return true; 
		}
		else {
			return false; 
		}
	}


	private static void getInPark(DifferentialPilot pilot) {
		// TODO Auto-generated method stub
		double current = pilot.getMovement().getDistanceTraveled()/10;
		double pos = current - 5;
		while((pilot.getMovement().getDistanceTraveled()/10) > pos){
			
		}
		pilot.stop();
		Motor.C.rotate(75);
//		System.out.println("C"+ (pilot.getMovement().getDistanceTraveled()/10));
		pilot.forward();
//		System.out.println(" p"+ pos);
		pos = (pilot.getMovement().getDistanceTraveled()/10) +6;
		while((pilot.getMovement().getDistanceTraveled()/10) < pos){
			//first back
			Sound.playNote(Sound.FLUTE, 600, 50);
		}
		
		
//		pilot.stop();
//		Sound.twoBeeps();
		Sound.playNote(Sound.FLUTE, 600, 20);
		Motor.C.rotate(-160);
		pilot.forward();
//		System.out.println(" p"+ pos);
		Sound.playNote(Sound.FLUTE, 600, 20);
		pos = (pilot.getMovement().getDistanceTraveled()/10) +0.5;
		while((pilot.getMovement().getDistanceTraveled()/10) < pos){
			//second back
			Sound.playNote(Sound.FLUTE, 600, 20);
		}
		pilot.stop();
		
//		Motor.C.rotate(95);
		Motor.C.rotate(140);
		pilot.backward();
		pos = (pilot.getMovement().getDistanceTraveled()/10) -0.9;
		while((pilot.getMovement().getDistanceTraveled()/10) > pos){
			//front 1
		}
		pilot.stop();
		Motor.C.rotate(-75);
		parked=true;
//		Motor.C.rotate(-130);
//		pilot.backward();
//		pos = (pilot.getMovement().getDistanceTraveled()/10) -0.05;
//		while((pilot.getMovement().getDistanceTraveled()/10) > pos){
//			//front 2
//		}
//		pilot.stop();
//		Motor.C.rotate(75);
//		pilot.forward();
//		pos = (pilot.getMovement().getDistanceTraveled()/10) +0.7;
//		while((pilot.getMovement().getDistanceTraveled()/10) < pos){
//			
//		}
//		pilot.stop();
		
		
//		System.out.println("a"+ (pilot.getMovement().getDistanceTraveled()/10));
	}
}
