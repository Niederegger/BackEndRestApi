package de.vv.web.functions;

import java.sql.Timestamp;

import de.vv.web.config.GlobalScope;

public class Scheduler implements Runnable {

	public void run() {
		while (GlobalScope.running) {			 // while active			

			// print TS when patching
			System.out.println("Schedule event triggered: " + (new Timestamp(System.currentTimeMillis())));

			// this is the important part:
			GlobalScope.fetchIsins();		// reFetching ISIN

			try {
				Thread.sleep(GlobalScope.scheduleSleepTime);
			} catch (InterruptedException e) {
				System.err.println("SchulderError: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
}