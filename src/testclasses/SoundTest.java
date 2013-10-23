package testclasses;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.SoundSensor;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;


public class SoundTest {

	/**
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int max = 0; 
		
		SoundSensor sound = new SoundSensor(SensorPort.S4, true);
		TouchSensor touch = new TouchSensor(SensorPort.S3);
		LightSensor light = new LightSensor(SensorPort.S2, true);
		UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S1);
		
		while(!Button.ESCAPE.isPressed()){
			Motor.A.forward();
			Motor.B.forward();
			System.out.println("Sound "+ sound.readValue());
//			if(touch.isPressed())
//			{
//				break; 
//			}
//			if(max < sound.readValue()){
//				max = sound.readValue();
//			}
//			System.out.println("Sound at "+ max);
//			if(max > 30){
//				System.out.println("Done");
//				break;
//			}
		} 
		
		
	}

}
