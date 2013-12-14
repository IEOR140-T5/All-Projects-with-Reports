package robot;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.Delay;

import java.util.Random;

/**
 * @author Khoa Tran, Phuoc Nguyen
 * 
 */
public class Avoider {

	private Racer racer;
	private Scanner scanner;
	private int _turnAngle = 30;

	/**
	 * Public constructor for Avoider class
	 */
	public Avoider(Racer racer, Scanner scanner) {
		this.racer = racer;
		this.scanner = scanner;
	}

	public void avoid(int whichIsDetected) {
		Random random = new Random();
		_turnAngle = random.nextInt(45);
		int objectDistance = scanner.getDistance();
		int objectAngle;
		racer.travel(-15);
		scanner.scanObject(racer._scanAngle);
		scanner.rotateTo(0, false);
		objectAngle = scanner.getAngle();
		

		LCD.clear();
		LCD.drawInt((int) objectAngle, 0, 0);

		boolean tooCloseToRight = objectAngle < 5;

		// This is when the ultrasonic sensor detects an object nearby
		if (whichIsDetected == 1) {
			// object is at right - beep once
			if (tooCloseToRight) {
				Sound.beep();
				scanner.rotateTo(-90, false);
				racer.turnPilot(90);				
				//racer.turnPilot(_turnAngle); // turn left
			} else { // object is at left - beep twice
				Sound.twoBeeps();
				scanner.rotateTo(90, false);
				racer.turnPilot(-90);
				//racer.turnPilot(-_turnAngle); // turn right
			}
			while(objectDistance < 60){				
				racer.toAngle(0);
				objectDistance = scanner.getDistance();
			}
			
			if(tooCloseToRight)	{ 
				racer.pilot.travelArc(-30,10 * Math.PI);
				scanner.rotateTo(0, true);
			} else {
				racer.pilot.travelArc(30,10 * Math.PI);
				scanner.rotateTo(0, true);
				
			}
		}
		
		// This is when the Left Touch Sensor detects
		if (whichIsDetected == 2) {
			Sound.beep();
			racer.turnPilot(_turnAngle); // turn left
		}
		
		// This is when the Left Touch Sensor detects
		if (whichIsDetected == 3) {
			Sound.beep();
			racer.turnPilot(-_turnAngle); // turn left
		}
		
		racer.travel(20);
	}
}
