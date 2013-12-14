package milestone5;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import milestone4.*;
import tracker.*;

import java.io.IOException;

public class BTShortestPath extends ShortestPath {

	private BTCommunicator btc;

	/**
	 * 
	 * @param tracker
	 * @param usensor
	 * @param x
	 * @param y
	 * @param btc
	 */
	public BTShortestPath(Tracker tracker, UltrasonicSensor usensor, int x,
			int y, BTCommunicator btc) {
		super(tracker, usensor, x, y);
		this.btc = btc;
	}

	/**
	 * Sets the destination using Bluetooth
	 */
	public void updateDestination() {
		try {
			int[] xy = btc.receive();
			super.setDestinationCoord(xy[0], xy[1]);
			LCD.clear();
			LCD.drawString("destination set", xy[0], xy[1]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Blocks the specified node, and tells the computer that the node has been
	 * blocked
	 */
	public void updateObstaclePosition(Node obstacleNode) {
		obstacleNode.blocked();
		btc.send(1, obstacleNode.getX(), obstacleNode.getY());
	}

	/**
	 * Updates the robot position, and tells the computer the robot's new
	 * position.
	 */
	public void updateRobotPosition(Node destination) {
		currentPosition = destination;
		btc.send(0, currentPosition.getX(), currentPosition.getY());
	}
	
	/**
	 * Finds any unblocked node by the ultrasonic sensor, and blocks them
	 * @return true if node is blocked, false otherwise
	 */
	protected boolean isBlocked() {
		int distance = usensor.getDistance();
		Node inFront = currentPosition.neighbor(heading);
		if (distance > MIN_DIST || inFront == null || inFront.isBlocked()) {
			return false;
		}
		updateObstaclePosition(inFront);
		grid.recalc();
		return true;
	}
	
	/**
	 * Drives the tracker to the destination set on the grid
	 */
	public void toDestination() {
		grid.recalc();
		while (currentPosition.getDistance() > 0) { // while we're not at destination
			do {
				turnToBestDirection();
			} while (isBlocked()); // try finding the best direction while we're still blocked
			tracker.trackLine();
			currentPosition = currentPosition.neighbor(heading);
			updateRobotPosition(currentPosition);
		}
	}
	
	/**
	 * Carries out the mission
	 */
	public void go() {
		tracker.calibrate();
		heading = 0;
		while (true) {
			updateDestination();
			toDestination();
			Sound.beepSequence();
			LCD.drawString("(" + currentPosition.getX() + "," + currentPosition.getY() + ")", 0, 0);
		}
	}
}
