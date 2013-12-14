package milestone5;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import milestone4.ShortestPath;
import tracker.Tracker;

public class Milestone5 {

	/**
	 * Main Test code for Milestone 5
	 * @param args - command line arguments
	 */
	public static void main(String[] args) {
		float wheelDiameter = 5.38f;
		float trackWidth = 11.2f;
		DifferentialPilot pilot = new DifferentialPilot(wheelDiameter,
				trackWidth, Motor.A, Motor.C);
		LightSensor left = new LightSensor(SensorPort.S1);
		LightSensor right = new LightSensor(SensorPort.S4);
		UltrasonicSensor ussensor = new UltrasonicSensor(SensorPort.S3);
		Tracker tracker = new Tracker(pilot, left, right);
		
		// Handle Bluetooth
		BTCommunicator comm = new BTCommunicator();
		BTShortestPath robot = new BTShortestPath(tracker, ussensor, 6, 8, comm);
		
		robot.go();
	}
}
