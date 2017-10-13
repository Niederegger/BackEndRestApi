package de.vv.web.model.wp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.vv.web.functions.BF;

public class MappingsModel {
	public ArrayList<Mapped> mapped = new ArrayList<Mapped>();
	public ArrayList<UnMapped> unmapped = new ArrayList<UnMapped>();

	public MappingsModel() {}

	public MappingsModel(ResultSet rs) {init(rs);}

	public void init(ResultSet rs) {
		try {
			String src, fn, mfn;
			while (rs.next()) {
				src = BF.trimming(rs.getString("MV_SOURCE_ID"));
				fn = BF.trimming(rs.getString("MV_FIELDNAME"));
				mfn = BF.trimming(rs.getString("FD_FIELDNAME"));
				
				if(mfn!=null){mapped.add(new Mapped(src,fn,mfn));
				} else {unmapped.add(new UnMapped(src,fn));}
			}
		} catch (SQLException e) {e.printStackTrace();}
	}

}
