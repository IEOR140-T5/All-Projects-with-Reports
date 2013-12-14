package robot;

import lejos.nxt.LCD;
import lejos.nxt.Button;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

/**
 * Racer class that implements the logic for robot going toward the light
 * 
 * @author Khoa Tran, Phuoc Nguyen, Corey Short
 */
public class Racer {

	/**
	 * Static variables
	 */
	public static final double THRESHOLD = 55;

	/**
	 * Instance variables
	 */
	private Scanner scanner;
	public DifferentialPilot pilot;
	private int distanceLimit = 25;
	public static final int _scanAngle = 45;

	/**
	 * Constructor for Racer, which takes in a Scanner and a DifferentialPilot
	 * 
	 * @param s
	 *            - Scanner equips with lights and obstacles detection
	 * @param dp
	 *            - Pilot to control the motor
	 */
	public Racer(Scanner s, DifferentialPilot dp) {
		scanner = s;
		pilot = dp;
		pilot.setTravelSpeed((double)pilot.getMaxTravelSpeed());
	}

	/**
	 * Completes two round trips for Milestone 1 by going to the lights back and
	 * forth
	 */
	public void toLight() {
		for (int i = 0; i < 2; i++) {
			while (scanner.getLight() < THRESHOLD) {
				scanner.scanTo(60);
				toAngle(scanner.getAngle());
				LCD.drawInt((int) scanner.getLight(), 0, 0);

				if (scanner.getLight() > THRESHOLD) {
					stopRobot();
					sleepRobot(500);
					break;
				}
				scanner.scanTo(-60);
				toAngle(scanner.getAngle());
				LCD.drawInt((int) scanner.getLight(), 0, 0);
				if (scanner.getLight() > THRESHOLD) {
					stopRobot();
					sleepRobot(500);
				}
			}
			turnAround();
			scanner.setLight(0);

			// Move back
			while (scanner.getLight() < THRESHOLD) {
				scanner.scanTo(60);
				toAngle(scanner.getAngle());
				LCD.drawInt((int) scanner.getLight(), 0, 0);

				if (scanner.getLight() > THRESHOLD) {
					stopRobot();
					sleepRobot(500);
					break;
				}
				scanner.scanTo(-60);
				toAngle(scanner.getAngle());
				LCD.drawInt((int) scanner.getLight(), 0, 0);
				if (scanner.getLight() > THRESHOLD) {
					stopRobot();
					sleepRobot(500);
				}
			}
			turnAround();
			scanner.setLight(0);
		}
		scanner.scanTo(0);
	}

	/**
	 * Find light for Milestone 2, since we want to find the light while
	 * detecting obstacles this time
	 */
	public void findLight() {
		scanner.rotateTo(0, true);
		Detector detector = new Detector();
		detector.start();

		// Three loops of detectors are necessary to always look out for
		// detectors while scanning twice back and forth
		while (true) {
			if (detector.isDetected) {
				whenDetected();
				detector = new Detector();
				detector.start();
			}

			// First scan
			scanner.scanTo(_scanAngle);
			toAngle(scanner.getAngle());
			LCD.drawInt((int) scanner.getLight(), 0, 0);

			if (detector.isDetected) {
				whenDetected();
				detector = new Detector();
				detector.start();
			}

			// Second scan
			scanner.scanTo(-_scanAngle);
			toAngle(scanner.getAngle());
			LCD.drawInt((int) scanner.getLight(), 0, 0);

			if (detector.isDetected) {
				whenDetected();
				detector = new Detector();
				detector.start();
			}

			// Has the robot found the light?
			if (scanner.getLight() > THRESHOLD) {
				stopRobot();
				sleepRobot(500);
				turnAround();
				break;
			}
		}
	}

	/**
	 * Find light for Milestone 3, since we want to find the light while
	 * detecting obstacles this time
	 */
	public void toLightMilestone3() {
		scanner.rotateTo(0, true);
		Detector detector = new Detector();
		Avoider avoider = new Avoider(this, scanner);
		detector.start();

		LCD.clear();
		// Three loops of detectors are necessary to always look out for
		// detectors while scanning twice back and forth
		while (true) {
			pilot.travel(2);
			while (true) {
				if (detector.isDetected) {
					if (foundLight())
						break; // detect light as obstacle
					avoider.avoid(detector.whichIsDetected);
					detector = new Detector();
					detector.start();
				}
	
				// First scan
				scanner.scanTo(_scanAngle);
				toAngle(scanner.getAngle());
				LCD.drawInt((int) scanner.getLight(), 0, 1);
	
				if (detector.isDetected) {
					if (foundLight())
						break; // detect light as obstacle
					avoider.avoid(detector.whichIsDetected);
					detector = new Detector();
					detector.start();
				}
	
				// Second scan
				scanner.scanTo(-_scanAngle);
				toAngle(scanner.getAngle());
				LCD.drawInt((int) scanner.getLight(), 0, 1);
	
				if (detector.isDetected) {
					if (foundLight())
						break; // detect light as obstacle
					avoider.avoid(detector.whichIsDetected);
					detector = new Detector();
					detector.start();
				}
	
				if (foundLight())
					break;
	
				// Has the robot found the light?
	
			}
			detector = new Detector();
			detector.start();
		}
	}

	private boolean foundLight() {
		if (scanner.getLight() > THRESHOLD) {
			stopRobot();
			Delay.msDelay(500);
			turnAround();
			return true;
		}
		return false;
	}

	public void travel(int distance) {
		pilot.travel(distance);
	}

	/**
	 * Tasks to perform when object is detected
	 */
	public void whenDetected() {
		pilot.travel(-30);
		scanner.rotateTo(0, true);
		// Button.ENTER.waitForPressAndRelease();
	}

	public void turnPilot(int degree) {
		pilot.rotate(degree, false);
	}

	/**
	 * Stops the robot
	 */
	public void stopRobot() {
		pilot.stop();
	}

	/**
	 * Sleeps the robot
	 * 
	 * @param ms
	 *            - how long to sleep
	 */
	public void sleepRobot(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}

	/**
	 * Steers to a specific angle
	 * 
	 * @param angle
	 *            - the angle to steer to
	 */
	public void toAngle(double angle) {
		pilot.steer(angle);
	}

	/**
	 * Turns the robot around at the end of every half
	 */
	private void turnAround() {
		pilot.rotate(220);
		scanner.rotateTo(0, false);
		// pilot.steer(0); // travel straight
	}

	/**
	 * Inner-class detector to make use of Thread
	 */
	class Detector extends Thread {

		/**
		 * Instance variables
		 */
		boolean isDetected = false;
		int whichIsDetected = 0; // 1 is ultra sensor detected, 2 is touch
									// sensor detected

		/**
		 * Runs the detector
		 */
		public void run() {
			while (!isDetected) {
				LCD.drawInt((int) getDistance(), 0, 2);

				if ((getDistance() < distanceLimit)) {
					whichIsDetected = 1;
					isDetected = true;
					stopRobot();
				}
				if ((isLeftTouched())) {
					whichIsDetected = 2;
					isDetected = true;
					stopRobot();
				}
				if ((isRightTouched())) {
					whichIsDetected = 3;
					isDetected = true;
					stopRobot();
				}
				Thread.yield();
			}
		}

		public int getAngle() {
			return scanner.getAngle();
		}

		/**
		 * Returns distance from the reading from Ultrasonic Sensor
		 */
		public int getDistance() {
			return scanner.getUltrasonicSensor().getDistance();
		}

		/**
		 * Returns True/False when the Left Touch Sensor is touched
		 */
		public boolean isLeftTouched() {
			return scanner.getLeftTouchSensor().isPressed();
		}

		/**
		 * Returns True/False when the Right Touch Sensor is touched
		 */
		public boolean isRightTouched() {
			return scanner.getRightTouchSensor().isPressed();
		}
	}
}
