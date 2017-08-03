package de.vv.web.config;

import java.util.ArrayList;
import java.util.List;

import de.vv.web.db.DBC_WP;

// used in combination with scheduler, a Global Scope is needed to transfer Data from one thread to another
public class GlobalScope {
	public static List<String> isins;				// List of all fetched ISINs
	public static boolean running = true;		// flag, Scheduler runs aslong this is true
	/*
	 * 1000 <- one second, * 60 <- one minute, * 60 <- one hour, * 12 <- half day
	 * this means schedule sleeps half a day, then fetches data normally a day would be enough,
	 * since only once per day new data gets inserted there should be a function to trigger
	 * it manually though also better purpose would be to time schedule this event on an suitable
	 * timeframe
	 */
	public static long scheduleSleepTime = 12 * 60 * 60 * 1000;

	/**
	 * updates ISINs List
	 */
	public static void fetchIsins() {
		isins = DBC_WP.getIsinList();
	}

	/**
	 * returns a List with ISIN containing that subString
	 * 
	 * @param isin
	 *          ISIN-Substring
	 * @param amt
	 *          Amount of Items till satisfied
	 * @return
	 */
	public static List<String> getLikeIsin(String isin, int amt) {
		int count = 0;
		if (isin != null && isin.length() <= 12) {		// as long this substring is between 0 and 12 characters
			List<String> il = new ArrayList<String>();  // return Set
			for (String s : isins) { 										// traverse complete ISIN list
				if (s.toLowerCase().contains(isin)) {			// an ISIN contains this substring
					il.add(s);															// add it to return set
					if (count++ >= amt)											// increment count and check whether enough been collected
						return il;														// then return whether true
				}
			}
			return il;																	// since the amount couldn't be satisfied, just return all found ones
		}
		return null;
	}
}
