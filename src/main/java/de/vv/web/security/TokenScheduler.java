package de.vv.web.security;

import java.sql.Timestamp;

import de.vv.web.config.GlobalScope;

public class TokenScheduler implements Runnable {
	
	public void run() {
		while(GlobalScope.running){
			System.out.println("Schedule event triggered: " + (new Timestamp(System.currentTimeMillis())));
			GlobalScope.fetchIsins();
			try {
				Thread.sleep(GlobalScope.scheduleSleepTime);
			} catch (InterruptedException e) {
				System.err.println("SchulderError: "+e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
}