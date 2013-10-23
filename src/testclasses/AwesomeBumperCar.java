package testclasses;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.objectdetection.Feature;
import lejos.robotics.objectdetection.FeatureDetector;
import lejos.robotics.objectdetection.FeatureListener;
import lejos.robotics.objectdetection.RangeFeatureDetector;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class AwesomeBumperCar {

	private static float wheelDiameter = 4.32f;
	private static float trackWidth = 13.60f;
	private static DifferentialPilot pilot = new DifferentialPilot(wheelDiameter,
			trackWidth, Motor.B, Motor.C);

	public static final int MAX_DETECT = 80;
	private static final double reverseDistance = -5.0;
	private static final int frontClearance = 20;
	
	private static final NXTRegulatedMotor motor = Motor.A;
	private static final  UltrasonicSensor sonar = new UltrasonicSensor(SensorPort.S4);
	private static final  RangeFeatureDetector fd = new RangeFeatureDetector(sonar, MAX_DETECT, 200);
	
	
	public static void main(String[] args) {
		Behavior b1 = new DriveForward();
		Behavior b2 = new HitWall();
		Behavior[] bArray = { b1, b2 };
		Arbitrator arby = new Arbitrator(bArray);
		arby.start();
	}
	
	
	public static class DriveForward implements Behavior {

		private boolean _suppressed = false;

		public boolean takeControl() {
			return true; // This bitch always wants control.
		}

		public void action() {
			_suppressed = false;
			pilot.forward();
			while (!_suppressed) {
				Thread.yield(); //Don't stop till you get enough
				System.out.println(Runtime.getRuntime().freeMemory());
			}
			pilot.stop();
		}

		public void suppress() {
			_suppressed = true;
		}
	}
	
	public static class HitWall implements Behavior, FeatureListener {
		
		private int range, rightDistance, leftDistance;
		
		public HitWall() {
			fd.addListener(this);
		}


		public boolean takeControl() {
			return range < frontClearance;
		}
		
		/**
		 * What to do when a wall is hit/detected
		 */
		public void action() {
			// Back up:
			pilot.travel(reverseDistance);
			
			// points the ultrasonic sensor to the right
			motor.rotateTo(90);
			// gets the right distance reading
			rightDistance = range;
			// points the ultrasonic sensor to the left
			motor.rotateTo(-90);
			// gets the left distance reading
			leftDistance = range;
			// points the ultrasonic sensor to the front
			motor.rotateTo(0);
			
			// if right has more room, turn right
			if (rightDistance > leftDistance)
				pilot.rotate(-90.0);
			// otherwise turn left
			else
				pilot.rotate(90.0);
		}

		public void suppress() {
			//Never called.
		}

		@Override
		public void featureDetected(Feature feature, FeatureDetector detector) {
			range = (int)feature.getRangeReading().getRange();
		}
		
	}

}