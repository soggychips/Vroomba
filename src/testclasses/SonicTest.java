package testclasses;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;


public class SonicTest {
	
	public static void main(String[] args) {
		UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S1);
		 while (!Button.ESCAPE.isDown()) {
			System.out.println(sonic.getDistance());
		}
	}
}
