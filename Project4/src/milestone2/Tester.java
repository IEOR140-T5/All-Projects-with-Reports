package milestone2;
import tracker.Tracker;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

/**
 * Follow the grid
 * 
 * @author Phuoc Nguyen
 */
public class Tester {

	Tracker tracker;

	/**
	 * Testing code for Milestone 2
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
	 * @param tracker - the tracking robot from Tracker.java
	 */
	public static void go(Tracker tracker) {

		// Setup the calibration
		tracker.calibrate();

		// 1: right, -1: left - Multiply by 90
		int _turnDirection = 1;

		// 2 rounds left turns
		for (int count = 0; count < 16; count++) {
			while (true) {
				if ((tracker.getlval() < -10) | (tracker.getrval() < -10)) {
					tracker.pilot.travel(7, true);
					tracker.sleepRobot(150);
					if (count % 2 != 0) {
						tracker.sleepRobot(300);
						tracker.turn(_turnDirection * -1);
						tracker.sleepRobot(300);
					}
					break;
				} else {
					tracker.trackLine();
				}
			}
		}

		// 2 rounds right turns
		tracker.turn(_turnDirection * -1);
		for (int count = 0; count < 16; count++) {
			while (true) {
				if ((tracker.getlval() < -10) | (tracker.getrval() < -10)) {
					tracker.pilot.travel(7, true);
					tracker.sleepRobot(150);
					if (count % 2 != 0) {
						tracker.sleepRobot(300);
						tracker.turn(_turnDirection);
						tracker.sleepRobot(300);
					}
					break;
				} else {
					tracker.trackLine();
				}

			}
		}

		// 2 Shuttle between (0,2) and (0,4)
		for (int count = 0; count < 8; count++) {
			while (true) {
				if ((tracker.getlval() < -10) | (tracker.getrval() < -10)) {
					tracker.pilot.travel(7, true);
					tracker.sleepRobot(150);
					if (count % 2 != 0) {
						tracker.sleepRobot(300);
						tracker.turn(_turnDirection * 2 * -1);
						tracker.sleepRobot(300);
					}
					break;
				} else {
					tracker.trackLine();
				}
			}
		}

		// 2 Shuttle between (0,2) and (0,0)
		tracker.turn(_turnDirection * 2 * -1);
		for (int count = 0; count < 8; count++) {
			while (true) {
				if ((tracker.getlval() < -10) | (tracker.getrval() < -10)) {
					tracker.pilot.travel(7, true);
					tracker.sleepRobot(150);
					if (count % 2 != 0) {
						tracker.sleepRobot(300);
						tracker.turn(_turnDirection * 2);
						tracker.sleepRobot(300);
					}
					break;
				} else {
					tracker.trackLine();
				}
			}
		}
	}
}
