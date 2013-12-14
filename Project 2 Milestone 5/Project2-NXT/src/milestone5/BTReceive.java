package milestone5;

import lejos.nxt.*;
import milestone4.*;
import lejos.nxt.comm.*;
import java.io.*;

/**
 * Receive data from another NXT, a PC, a phone, or another bluetooth device.
 * 
 * Waits for a connection, receives an int and returns its negative as a reply,
 * 100 times, and then closes the connection, and waits for a new one.
 * 
 * @author Lawrie Griffiths
 * 
 */
public class BTReceive {

	public static void main(String[] args) throws Exception {
		LCD.drawString("Waiting", 0, 0);

		BTConnection btc = Bluetooth.waitForConnection();
		LCD.drawString("Connected", 0, 1);
		DataInputStream dis = btc.openDataInputStream();
		DataOutputStream dos = btc.openDataOutputStream();

		for (int i = 0; i < 100; i++) {
			int n = dis.readInt();
			LCD.drawInt(n, 7, 0, 3);
			dos.writeInt(-n);
			dos.flush();
		}
		dis.close();
		dos.close();
		Thread.sleep(100); // wait for data to drain
		LCD.drawString("closing", 0, 4);
		LCD.refresh();
		btc.close();
		Button.waitForAnyPress(0);
	}
}
