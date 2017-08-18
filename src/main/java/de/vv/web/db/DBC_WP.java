package de.vv.web.db;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.vv.web.model.FieldModel;
import de.vv.web.model.Strings2;
import de.vv.web.model.ContainerForOldThoughts.*;
import de.vv.web.model.UpdateIsinHistory.HistoryIsinUpdatesContainer;
import de.vv.web.model.maininfo.MainInfo2;
import de.vv.web.model.maininfo.MainInfo2Container;
import de.vv.web.model.maininfo.MainInfoContainer;

// DataBaseConnection for WertPapiere
public class DBC_WP {

	//--------------------------------------------------------------------------------------------------------------------
	// In Betrieb
	//--------------------------------------------------------------------------------------------------------------------

	public static MainInfo2Container getFieldValueSours(String isin, int seqn) {
		if (isin.length() == 12) {
			String queryString = "{call vvsp_get_fieldvalue_sources (?, ?)}";
			try {
				CallableStatement cstmt = DBCon.con.prepareCall(queryString);
				cstmt.setString(1, isin);
				cstmt.setInt(2, seqn);
				ResultSet rs = cstmt.executeQuery();
				int count = 0;																		// Diese Count-geschichte kommt daher:
				while (rs == null) {															// eine StoredProcedure kann mehrere ResultSet returnen
					count++;																				// MainInfo (v1) lieferte einige Sets zureuck, von denen
					cstmt.getMoreResults();														// die ersten paar null waren, -> dadher die Iteration durch
					rs = cstmt.getResultSet();													// die sets, // mainInfoV2 liefert aktuell direkt das
					if (count++ > 100)															// gewuenschte Set und macht dadurch dieses Handling 
						return null;																	// unnoetig
				}
				return new MainInfo2Container(rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Returns first ISIN linked with this WKN
	 * 
	 * @param wkn
	 * @return
	 */
	public static String getIsinOfWkn(String wkn) {
		try {
			CallableStatement cstmt = DBCon.con.prepareCall("{call vvsp_get_isin_for_wkn (?)}");
			cstmt.setString(1, wkn);
			ResultSet rs = cstmt.executeQuery();
			while (rs.next()) {
				return rs.getString("mv_isin");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * returns MainInfo2Container with fetched data from vvsP_mainInfo_V2
	 * 
	 * @param isin
	 * @return MainInfo2Container
	 */
	public static MainInfo2Container getStammdaten(String isin) {
		if (isin.length() == 12) {
			String queryString = "{call vvsp_get_maininfoV2 (?)}";
			try {
				CallableStatement cstmt = DBCon.con.prepareCall(queryString);
				cstmt.setString(1, isin);
				ResultSet rs = cstmt.executeQuery();
				int count = 0;																		// Diese Count-geschichte kommt daher:
				while (rs == null) {															// eine StoredProcedure kann mehrere ResultSet returnen
					count++;																				// MainInfo (v1) lieferte einige Sets zureuck, von denen
					cstmt.getMoreResults();														// die ersten paar null waren, -> dadher die Iteration durch
					rs = cstmt.getResultSet();													// die sets, // mainInfoV2 liefert aktuell direkt das
					if (count++ > 100)															// gewuenschte Set und macht dadurch dieses Handling 
						return null;																	// unnoetig
				}
				return new MainInfo2Container(rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static FieldModel getFieldList(String user) {
		String queryString = "{call vvsp_get_all_fieldnames (?)}";
		try {
			CallableStatement cstmt = DBCon.con.prepareCall(queryString);
			cstmt.setString(1, user);
			ResultSet rs = cstmt.executeQuery();
			int count = 0;																		// Diese Count-geschichte kommt daher:
			while (rs == null) {															// eine StoredProcedure kann mehrere ResultSet returnen
				count++;																				// MainInfo (v1) lieferte einige Sets zureuck, von denen
				cstmt.getMoreResults();														// die ersten paar null waren, -> dadher die Iteration durch
				rs = cstmt.getResultSet();													// die sets, // mainInfoV2 liefert aktuell direkt das
				if (count++ > 100)															// gewuenschte Set und macht dadurch dieses Handling 
					return null;																	// unnoetig
			}
			return new FieldModel(rs, "FieldName");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<MainInfo2> getFields(String user) {
		String queryString = "{call vvsp_get_all_fieldnames (?)}";
		try {
			CallableStatement cstmt = DBCon.con.prepareCall(queryString);
			cstmt.setString(1, user);
			ResultSet rs = cstmt.executeQuery();
			int count = 0;																		// Diese Count-geschichte kommt daher:
			while (rs == null) {															// eine StoredProcedure kann mehrere ResultSet returnen
				count++;																				// MainInfo (v1) lieferte einige Sets zureuck, von denen
				cstmt.getMoreResults();														// die ersten paar null waren, -> dadher die Iteration durch
				rs = cstmt.getResultSet();													// die sets, // mainInfoV2 liefert aktuell direkt das
				if (count++ > 100)															// gewuenschte Set und macht dadurch dieses Handling 
					return null;																	// unnoetig
			}
			List<MainInfo2> ret = new ArrayList<MainInfo2>();
			while (rs.next()) {
				ret.add(new MainInfo2(rs.getString("FieldName").trim(), rs.getInt("Sequence_Num")));
			}
			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * supposed to fetch all isin from mastervalues which are like isin
	 * 
	 * @param isin
	 * @return
	 */
	public static List<String> getIsinList() {
		String queryString = "select distinct mv_isin from vv_mastervalues;";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			ResultSet rs = ps.executeQuery();
			List<String> isinList = new ArrayList<String>();
			while (rs.next()) {
				isinList.add(rs.getString("MV_ISIN"));
			}
			return isinList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	//info, isin, source, null, "User", "User", "changed by User"
	public static int patchData(FieldModel fm, String isin, String ip, String mic, String comment, String srcID, String origin) {
		try {
			PreparedStatement ps;
			int stmtCount;
			// patching edit
			String query = "INSERT INTO vv_mastervalues_upload"
					+ "(MVU_DATA_ORIGIN, MVU_URLSOURCE, MVU_SOURCEFILE, MVU_SOURCE_ID, MVU_ISIN, MVU_MIC, MVU_FIELDNAME, MVU_STRINGVALUE, MVU_COMMENT) VALUES"
					+ "(?,?,?,?,?,?,?,?,?);";
			ps = DBCon.con.prepareStatement(query);
			List<Strings2> sml;
			int c = -1;
			for (String k : fm.keyOrder) {
				sml = fm.container.get(k);
				if (k.toLowerCase().equals("isin") || sml == null || sml.size() == 0){
					continue;
				}
				
				for(Strings2 s2 : sml){
					System.out.println(s2);
					if(s2.str2==null||s2.str2.length()==0||s2.str2.isEmpty()||s2.str2.equals("")){
						continue;
					}
					String s1 = s2.str1;
					String fn = k;
					if(s1 != null){
						s1 = s1.trim();
						if(s1.length() != 0)  fn += "."+s1;
					}
					c++;
					stmtCount = 1;
					ps.setString(stmtCount++, origin); // Data Origin
					ps.setString(stmtCount++, ip); // url-Source
					ps.setString(stmtCount++, "none"); 				// SourceFile
					ps.setString(stmtCount++, srcID);
					ps.setString(stmtCount++, isin);
					ps.setString(stmtCount++, mic);
					ps.setString(stmtCount++, fn);
					ps.setString(stmtCount++, s2.str2);
					ps.setString(stmtCount++, comment);
					ps.executeUpdate();
				}
				
			}
			// executing upload to main table
			stmtCount = 1;
			ps = DBCon.con.prepareStatement("exec vvsp_import_uploadV3 ?, ?, ?, ?, ?;");
			ps.setString(stmtCount++, srcID); // SourceId
			ps.setString(stmtCount++, origin); // Data Origin
			ps.setString(stmtCount++, ip); // UrlSource
			ps.setString(stmtCount++, "none"); // SourceFile
			ps.setString(stmtCount++, comment); // Comment
			ps.executeUpdate();
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static void uplaodData(UploadContainer data) {
		try {
			PreparedStatement ps;
			int stmtCount;
			// patching edit
			for (int i = 0; i < data.fieldName.size(); i++) {
				if (data.values.get(i) == null || data.values.get(i) == "")
					continue;
				// (MVU_DATA_ORIGIN, MVU_SOURCE_ID, MVU_ISIN, MVU_MIC, MVU_FIELDNAME, MVU_STRINGVALUE, MVU_COMMENT)
				ps = DBCon.con.prepareStatement(data.query);
				stmtCount = 1;
				ps.setString(stmtCount++, data.origin); // Data Origin
				ps.setString(stmtCount++, data.urlSource); // url-Source
				ps.setString(stmtCount++, "none"); 				// SourceFile
				ps.setString(stmtCount++, data.source);
				ps.setString(stmtCount++, data.isin);
				ps.setString(stmtCount++, data.mic);
				ps.setString(stmtCount++, data.fieldName.get(i));
				ps.setString(stmtCount++, data.values.get(i));
				ps.setString(stmtCount++, data.comment);
				ps.executeUpdate();
			}
			// executing upload to main table
			stmtCount = 1;
			ps = DBCon.con.prepareStatement("exec vvsp_import_uploadV3 ?, ?, ?, ?, ?;");
			ps.setString(stmtCount++, data.source); // SourceId
			ps.setString(stmtCount++, data.origin); // Data Origin
			ps.setString(stmtCount++, data.urlSource); // UrlSource
			ps.setString(stmtCount++, "none"); // SourceFile
			ps.setString(stmtCount++, data.comment); // Comment
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//--------------------------------------------------------------------------------------------------------------------
	// Funktionen
	//--------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	// ab hier spare ich mir das Kommentieren, da diese Funktionen wieder rausgenommen wurden aus der WebSite
	// zum teil Funktionieren diese trotzdem, und sich immernoch offen in der RestApi
	//--------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	// funktioniert, nicht In Betrieb
	//--------------------------------------------------------------------------------------------------------------------

	public static HistoryIsinUpdatesContainer getSources(String isin) {
		try {
			PreparedStatement ps;
			ps = DBCon.con.prepareStatement("select distinct  MV_ISIN, MV_SOURCE_ID,   UPL_TIMESTAMP from vv_mastervalues"
					+ "	left outer join vv_uploads on UPL_UPLOAD_ID = MV_UPLOAD_ID"
					+ " where  MV_FIELDNAME <>'Closing Price Previous Business Day' and mv_isin=? order by UPL_TIMESTAMP desc");
			ps.setString(1, isin);
			return new HistoryIsinUpdatesContainer(ps.executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean doesIsinExist(String isin) {
		if (isin.length() == 12) {
			String queryString = "select top (1) mv_isin from vv_mastervalues where mv_isin=?;";
			try {
				PreparedStatement ps = DBCon.con.prepareStatement(queryString);
				ps.setString(1, isin);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					return (isin.toLowerCase().equals(rs.getString("mv_isin").toLowerCase()));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static MainInfoContainer getMainInfo(String isin) {
		String queryString = "exec vvsp_get_maininfo ?;";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			ps.setString(1, isin);
			System.out.println("exec vvsp_get_maininfo '" + isin + "';");
			ps.execute();
			ResultSet rs = ps.getResultSet();
			int count = 0;
			while (rs == null) {
				count++;
				ps.getMoreResults();
				rs = ps.getResultSet();
				// stored procedure return multiple result-sets,
				// therefore one has to loop through each null set,
				// finally reaching the right on, 
				// as security a lock has been set at 100 loops
				if (count++ > 100)
					return null;
			}
			MainInfoContainer mi = new MainInfoContainer();
			while (rs.next()) {
				mi.instert(rs.getString("LEVEL1"), rs.getString("LEVEL2"), rs.getString("STRINGVALUE"));
			}
			return mi;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param v
	 * @return
	 */
	public static IsinData getIsinInfo(String v) {
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(IsinData.query);
			ps.setString(1, v);
			ResultSet rs = ps.executeQuery();
			IsinData data = new IsinData();
			while (rs.next()) {
				data.add(IdRow.parse(rs));
			}
			data.sort();
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * supposed to fetch all isin from mastervalues which are like isin
	 * 
	 * @param isin
	 * @return
	 */
	public static List<String> getAllIsins(String isin) {
		String queryString = "select distinct mv_isin from vv_mastervalues where  mv_isin like ?;";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(queryString);
			ps.setString(1, "%" + isin + "%");
			ResultSet rs = ps.executeQuery();
			List<String> isinList = new ArrayList<String>();
			while (rs.next()) {
				isinList.add(rs.getString("MV_ISIN"));
			}
			return isinList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	//--------------------------------------------------------------------------------------------------------------------
	// veraltet
	//--------------------------------------------------------------------------------------------------------------------

	public static List<WPDModel> getWpd(String isin) {
		String isw = "SELECT MV_SOURCE_ID ,MV_ISIN ,MV_STRINGVALUE FROM dbo.vv_mastervalues where  mv_isin like ? and MV_FIELDNAME='hyperlink';";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(isw);
			ps.setString(1, "%" + isin + "%");
			ResultSet rs = ps.executeQuery();
			List<WPDModel> list = new ArrayList<WPDModel>();
			while (rs.next()) {
				// lade die daten aus dem ResultSet
				// die Daten werdeng etrimmed um unnoetige whitespaces zu cutten
				String mvIsin = rs.getString("MV_ISIN");
				if (mvIsin != null)
					mvIsin = mvIsin.trim();
				String mvSourceId = rs.getString("MV_SOURCE_ID");
				if (mvSourceId != null)
					mvSourceId = mvSourceId.trim();
				String mvVal = rs.getString("MV_StringVALUE");
				if (mvVal != null)
					mvVal = mvVal.trim();
				list.add(new WPDModel(mvSourceId, mvVal, mvIsin));
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	//--------------------------------------------------------------------------------------------------------------------
	// funktioniert nicht
	//--------------------------------------------------------------------------------------------------------------------

	/**
	 * fetching isin mastervalues
	 * 
	 * @param isin
	 * @return
	 */
	public static List<MasterValue> getIsinData(String isin) {
		// bei preparedStatements einfach ein ? und keine '?' <-- fehlerquelle
		String isw = "SELECT MV_SOURCE_ID ,MV_UPLOAD_ID ,MV_ISIN ,MV_MIC ,MV_AS_OF_DATE ,MV_FIELDNAME ,MV_TIMESTAMP ,MV_STRINGVALUE ,MV_DATA_ORIGIN ,MV_URLSOURCE ,MV_COMMENT FROM dbo.vv_mastervalues WHERE MV_ISIN=?";
		try {
			PreparedStatement ps = DBCon.con.prepareStatement(isw);
			ps.setString(1, isin);
			ResultSet rs = ps.executeQuery();
			List<MasterValue> lmv = new ArrayList<MasterValue>();
			while (rs.next()) {
				// lade die daten aus dem ResultSet
				// die Daten werdeng etrimmed um unnoetige whitespaces zu cutten
				String mvIsin = rs.getString("MV_ISIN");
				if (mvIsin != null)
					mvIsin = mvIsin.trim();
				String mvSourceId = rs.getString("MV_SOURCE_ID");
				if (mvSourceId != null)
					mvSourceId = mvSourceId.trim();
				String mvAOD = rs.getString("MV_AS_OF_DATE");
				if (mvAOD != null)
					mvAOD = mvAOD.trim();
				String mvFN = rs.getString("MV_FIELDNAME");
				if (mvFN != null)
					mvFN = mvFN.trim();
				String mvTS = rs.getString("MV_TIMESTAMP");
				if (mvTS != null)
					mvTS = mvTS.trim();
				String mvVal = rs.getString("MV_StringVALUE");
				if (mvVal != null)
					mvVal = mvVal.trim();
				String mvDO = rs.getString("MV_DATA_ORIGIN");
				if (mvDO != null)
					mvDO = mvDO.trim();
				String mvUS = rs.getString("MV_URLSOURCE");
				if (mvUS != null)
					mvUS = mvUS.trim();
				String mvC = rs.getString("MV_COMMENT");
				if (mvC != null)
					mvC = mvC.trim();
				String mvMic = rs.getString("MV_MIC");
				if (mvMic != null)
					mvMic = mvMic.trim();
				String mvUI = rs.getString("MV_UPLOAD_ID");
				if (mvUI != null)
					mvUI = mvUI.trim();
				lmv.add(new MasterValue(mvSourceId, mvIsin, mvAOD, mvFN, mvTS, mvVal, mvDO, mvUS, mvC, mvMic, mvUI));
			}
			return lmv;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
