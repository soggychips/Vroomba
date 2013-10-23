package testclasses;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;


public class MotorTest {
	private static float wheelDiameter = 4.32f;
	private static float trackWidth = 15.25f;
	private static DifferentialPilot pirate = 
			new DifferentialPilot(wheelDiameter, trackWidth, Motor.B, Motor.C);

	public static void main(String[] args) throws InterruptedException {
		pirate.setTravelSpeed(20);
		while(!Button.ESCAPE.isDown()){
			if(Button.RIGHT.isDown()){
				pirate.rotate(-85.0d);
			}
			else if(Button.LEFT.isDown()){
				pirate.rotate(84.9d);
			}
			
		}
		
//		Motor.C.rotate(100);
//		Thread.sleep(0);
	}
}
