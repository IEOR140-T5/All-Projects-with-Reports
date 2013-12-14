package milestone1;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import robot.*;

/**
 * @author Khoa Tran, Phuoc Nguyen
 */
public class Milestone1 {

	/**
	 * Main code for Milestone 1
	 * @param args - command line arguments
	 */	
	public static void main(String args[]) {
		// Pilot-related variables
		float wheelDiameter = 5.38f;
		float trackWidth = 11.2f;
		DifferentialPilot pilot = new DifferentialPilot(wheelDiameter,
				trackWidth, Motor.A, Motor.C);
		
		// Sensor, Scanner, and Racer
		NXTRegulatedMotor sensorMotor = Motor.B;
		Scanner scanner = new Scanner(sensorMotor, new LightSensor(SensorPort.S2));
		Racer racer = new Racer(scanner, pilot);
		
		// Wait for a press, and GO GO GO
		Button.waitForAnyPress();
		racer.toLight();
	}
}
