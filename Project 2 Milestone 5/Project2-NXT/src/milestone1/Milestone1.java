package milestone1;
import tracker.Tracker;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * Follow the oval blue line
 * 
 * @author Phuoc Nguyen, Khoa Tran
 */
public class Milestone1 {
	/**
	 * Testing code for Milestone 1
	 * 
	 * @param args - command line arguments     
	 */
	public static void main(String[] args) {
		float wheelDiameter = 5.38f;
		float trackWidth = 11.2f;
		DifferentialPilot pilot = new DifferentialPilot(wheelDiameter,
				trackWidth, Motor.A, Motor.C);
		LightSensor left = new LightSensor(SensorPort.S1);
		LightSensor right = new LightSensor(SensorPort.S4);
		Tracker tracker = new Tracker(pilot, left, right);
		go(tracker);
	}

	/**
	 * Executes tasks for this milestone
	 */
	public static void go(Tracker tracker) {

		// Setup the calibration
		tracker.calibrate();
		int _turnDirection = 1;

		for (int count = 0; count < 16; count++) {
			// each loop take control of half the track
			while (true) {
				if ((tracker.getlval() < -10) | (tracker.getrval() < -10)) {
					// black marker detected
					break;
				} else {
					tracker.trackLine();
				}
			}
			tracker.pilot.travel(7);
			if (count == 7) {
				tracker.turn(_turnDirection * 2);
			}
		}

		// Extra credit part
		Button.waitForAnyPress();
		for (int count = 0; count < 6; count++) {
			while (true) {
				if ((tracker.getlval() < -10) | (tracker.getrval() < -10)) {
					// black marker detected
					break;
				} else {
					tracker.trackLine();
				}
			}
			tracker.pilot.travel(7);
			tracker.sleepRobot(850);
			if ((count == 0) | (count == 3) | (count == 4)) {
				tracker.turn(_turnDirection);
			} else {
				tracker.turn(-_turnDirection);
			}
			tracker.sleepRobot(850);
		}
	}
}
