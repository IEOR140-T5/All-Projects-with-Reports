package milestone3;
import tracker.Tracker;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.util.ButtonCounter;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * input x,y from buttons, then navigates to destination Uses Tracker,
 * ButtonCounter
 */
public class GridNav0 {

	/**
	 * responsible for following the line to a marker, turning at the marker
	 */
	Tracker tracker;
	/**
	 * set by getDestinaion, used by toDestination, newHeading(); updated by
	 * toDestinatin;
	 */
	int[] _position = new int[2];
	/**
	 * setBy getDestination(); used by toDestination, newHeading()
	 */
	int[] _destination = new int[2]; // where am I going?
	/**
	 * the direction the robot is facing updated by toDestination() used by
	 * toDestination and newHeading()
	 */
	int _heading = 0; // direction I am facing
	ButtonCounter bc = new ButtonCounter();

	/**
	 * constructor uses a tracker
	 */
	public GridNav0(Tracker theTracker) {
		tracker = theTracker;
	}

	/**
	 * go to the destination uses and updates _heading and _ position; uses
	 * _destination calls nextHeading();
	 */
	void toDestination() {
		LCD.clearDisplay();
		int turnAngle;
		int nextHeading;
		while (!equals(_position, _destination)) {
			nextHeading = newHeading();
			turnAngle = nextHeading - _heading;
			
			if (turnAngle != 0) {
				if (!tracker.isMoving()) {
					tracker.turn(-turnAngle);
				}
				while (tracker.isMoving()) {
					tracker.turn(-turnAngle);
				}
				_heading = nextHeading; // return between within 0,3
			}

			tracker.trackLine();
			if (_heading < 2) {
				_position[_heading]++; // increase in x or y
			} else {
				_position[_heading - 2]--;
			}
			System.out.println("H " + _heading + " X " + _position[0] + " Y "
					+ _position[1]);
			Sound.playTone(800 + 50 * _position[0], 100);
			Sound.playTone(800 + 50 * _position[1], 100);
		}
	}

	/**
	 * returns heading between 0 and 3 uses _heading, _position, _destination in
	 * X direction first.
	 */
	private int newHeading() {
		int xDir = _destination[0] - _position[0];
		if (xDir > 0) {
			return 0;
		} else if (xDir < 0) {
			return 2;
		} else { // move in y direction
			int yDir = _destination[1] - _position[1];
			if (yDir > 0) {
				return 1;
			} else if (yDir < 0) {
				return 3;
			}
		}
		return 0;
	}

	/**
	 * sets _destination uses ButtonCounter
	 */
	public void getDestination() {
		LCD.clear();
		bc.count("Dest x,y");
		int x = bc.getLeftCount();
		int y = bc.getRightCount();
		
		if (x >= 0 && x <= 6 && y >= 0 && y <= 8) {
			_destination[0] = x;
			_destination[1] = y;
		}
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	boolean equals(int[] a, int[] b) {
		return a[0] == b[0] && a[1] == b[1];
	}

	/**
	 * carries out the mission
	 */
	public void go() {
		tracker.calibrate();
		while (true) {
			getDestination();
			toDestination();
		}
	}

	/**
	 * Main Test code for Milestone 3
	 * @param args
	 */
	public static void main(String[] args) {
		float wheelDiameter = 5.38f;
		float trackWidth = 11.2f;
		DifferentialPilot pilot = new DifferentialPilot(wheelDiameter,
				trackWidth, Motor.A, Motor.C);
		LightSensor left = new LightSensor(SensorPort.S1);
		LightSensor right = new LightSensor(SensorPort.S4);
		Tracker tracker = new Tracker(pilot, left, right);
		GridNav0 robot = new GridNav0(tracker);
		robot.go();
	}
}
