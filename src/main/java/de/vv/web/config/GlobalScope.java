package de.vv.web.config;

import java.util.ArrayList;
import java.util.List;

import de.vv.web.db.DBCon;

public class GlobalScope {
	public static List<String> isins;
	public static boolean running = true;
	// 1000 <- one second, * 60 <- one minute, * 60 <- one hour, * 12 <- half
	// day
	// this means schedule sleeps half a day, then fetches data
	// normally a day would be enough, since only once per day new data gets
	// inserted
	// there should be a function to trigger it manually though
	// also better purpose would be to time schedule this event on an suitable
	// timeframe
	public static long scheduleSleepTime = 12 * 60 * 60 * 1000;

	public static void fetchIsins() {
		isins = DBCon.getIsinList();
	}

	public static List<String> getLikeIsin(String isin, int amt) {
		int count = 0;
		if (isin != null && isin.length() <= 12) {
			List<String> il = new ArrayList<String>(); // isin list
			for (String s : isins) {
				if (s.toLowerCase().contains(isin)) {
					il.add(s);
					if (count++ >= amt)
						return il;
				}
			}
			return il;
		}
		return null;
	}
}
