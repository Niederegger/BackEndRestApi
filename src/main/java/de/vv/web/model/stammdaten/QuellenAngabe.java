package de.vv.web.model.stammdaten;

import java.util.List;
import java.util.ArrayList;
import de.vv.web.model.Strings2;

public class QuellenAngabe {
	public String origin;
	public String source;
	public List<Strings2> values = new ArrayList<Strings2>();

	/**
	 * 
	 * @param q
	 *          str1 -> origin,
	 *          str2 -> source
	 */
	public QuellenAngabe(Strings2 q) {
		origin = q.str1;
		source = q.str2;
	}
	
	public QuellenAngabe(String o, String s) {
		origin = o;
		source = s;
	}

	@Override
	public int hashCode() {
		return origin.hashCode() * 3 + source.hashCode() * 5;
	}

	@Override
	public boolean equals(Object obj) {
		assert (obj.getClass() == this.getClass());
		QuellenAngabe o = (QuellenAngabe) obj;
		return origin.equals(o.origin) && source.equals(o.source);
	}

	public boolean containsValue(String val) {
		for (Strings2 s2 : values) {
			if (s2.str1.equals(val))
				return true;
		}
		return false;
	}

	public void insert(Strings2 s2) {
		values.add(s2);
	}

}
