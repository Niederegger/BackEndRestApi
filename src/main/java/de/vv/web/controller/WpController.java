package de.vv.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.vv.web.config.GlobalScope;
import de.vv.web.db.DBC_WP;
import de.vv.web.functions.BasicFunctions;
import de.vv.web.model.FieldModel;
import de.vv.web.model.Strings2;
import de.vv.web.model.ContainerForOldThoughts.IsinData;
import de.vv.web.model.ContainerForOldThoughts.MasterValue;
import de.vv.web.model.ContainerForOldThoughts.UploadContainer;
import de.vv.web.model.ContainerForOldThoughts.WPDModel;
import de.vv.web.model.UpdateIsinHistory.HistoryIsinUpdatesContainer;
import de.vv.web.model.maininfo.*;
import de.vv.web.model.stammdaten.QuellenSet;

@RestController
@RequestMapping("/api/wp")
public class WpController {

	//--------------------------------------------------------------------------------------------------------------------
	// In Betrieb
	//--------------------------------------------------------------------------------------------------------------------

	@RequestMapping("/isinOfWkn")
	public String isinAC(@RequestParam(value = "v", required = true) String v) {
		return BasicFunctions.isinOfWkn(v);
	}
	
	/**
	 * isinAC - ISINs AutoComplete This Interface is used to offer an
	 * autocomplete function when looking for ISINs,
	 * based on contains
	 * 
	 * @param v
	 *          ISIN
	 * @param amt
	 *          Amount
	 * @return a List of similar ISINs
	 */
	@RequestMapping("/isinAC")
	public List<String> isinAC(@RequestParam(value = "v", required = true) String v,
			@RequestParam(value = "amt", required = true) int amt) {
		if (amt < 0)
			return null;
		return GlobalScope.getLikeIsin(v.toLowerCase(), amt);
	}

	/**
	 * fetches most recent Information of this ISIN with all mentioned Sources
	 * 
	 * @param v
	 *          ISIN
	 * @return QuellenMap
	 */
	@RequestMapping("/quellen")
	public QuellenMap getQuellen(@RequestParam(value = "v") String v) {
		v = BasicFunctions.isinOfWkn(v);
		if (v != null) {
			return DBC_WP.getStammdaten(BasicFunctions.isinOfWkn(v)).toQuellenContainer();
		}
		return null;
	}
	
	/**
	 * fetches most recent Information of this ISIN with all mentioned Sources
	 * 
	 * @param v
	 *          ISIN
	 * @return QuellenMap
	 */
	@RequestMapping("/quellenSet")
	public QuellenSet getQuellenSet(@RequestParam(value = "v") String v) {
		v = BasicFunctions.isinOfWkn(v);
		if (v != null) {
			return DBC_WP.getStammdaten(BasicFunctions.isinOfWkn(v)).toQuellenSet();
		}
		return null;
	}


	@RequestMapping(value = "pushData", method = { RequestMethod.POST })
	public String pushData(HttpServletRequest request, @RequestBody FieldModel info) {
		printRequestInfo(request);
		if (request != null) {
			String ip = request.getHeader("X-Forwarded-For") != null ? request.getHeader("X-Forwarded-For")
					: request.getRemoteAddr();
			if(info != null){

				String isin = info.getIsin().trim();
				if(isin != null  && isin.length() == 12){
//					public static int patchData(FieldModel fm, String isin, String ip, String mic, String comment, String srcID, String origin) {
					int i = DBC_WP.patchData(info, isin, ip, null, "changed by User", "User", "unregistered User");
					if(i >= 0) return "success";
				}
			}
			return "error";
		} else
			return "Are you a Hacker?";
	}
	
	/**
	 * @param v
	 *          ISINs
	 * @param s
	 *          SequenceNum
	 * @return MainInfo2[]
	 */
	@RequestMapping("/quellenAnsehen")
	public List<MainInfo2> getQuellenAnsehen(@RequestParam(value = "v") String v, @RequestParam(value = "s") int s) {
		v = BasicFunctions.isinOfWkn(v);
		if (v != null) {
			return DBC_WP.getFieldValueSours(BasicFunctions.isinOfWkn(v), s).container;
		}
		return null;
	}

	/**
	 * returns a single QuellEntry
	 * 
	 * @param v
	 *          ISIN
	 * @param sqn
	 *          SequenceNum
	 * @param srcn
	 *          SrcNum
	 * @return QuellEntry
	 */
	@RequestMapping("/quellEntry")
	public QuellElement getQuellenElement(@RequestParam(value = "v") String v, @RequestParam(value = "sqn") int sqn,
			@RequestParam(value = "srcn") int srcn) {
		v = BasicFunctions.isinOfWkn(v);
		if (v != null) {
			return DBC_WP.getStammdaten(BasicFunctions.isinOfWkn(v)).toQuellElement(sqn, srcn);
		}
		return null;
	}
	
	@RequestMapping("/getFields")
	public FieldModel getQuellenElement() {
		return DBC_WP.getFieldList(null);
	}

	@RequestMapping(value = "editInfo2", method = { RequestMethod.POST })
	public QuellenMap editMainInfo2(HttpServletRequest request, @RequestBody QuellenMap info) {
		printRequestInfo(request);
		if (request != null) {
			UploadContainer uc = info.toUploadContainer();
			String source = request.getHeader("X-Forwarded-For") != null ? request.getHeader("X-Forwarded-For")
					: request.getRemoteAddr();
			uc.setData("User", "User", source, "changed by User", ""); // source, origin, comment, mic) 
			DBC_WP.uplaodData(uc);
			System.out.println("Data inserted by: " + source);
			return info;
		} else
			System.out.println();
		return null;
	}
	
	//--------------------------------------------------------------------------------------------------------------------
	// Funktionen
	//--------------------------------------------------------------------------------------------------------------------

	void printRequestInfo(HttpServletRequest r) {
		System.out.println("rUser: " + r.getRemoteUser());
		System.out.println("rAddr: " + r.getRemoteAddr());
		System.out.println("rHost: " + r.getRemoteHost());
		System.out.println("XForward: " + r.getHeader("X-Forwarded-For"));
	}

	//--------------------------------------------------------------------------------------------------------------------
	// ab hier spare ich mir das Kommentieren, da diese Funktionen wieder rausgenommen wurden aus der WebSite
	// zum teil Funktionieren diese trotzdem, und sich immernoch offen in der RestApi
	//--------------------------------------------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------------------------------------------
	// funktioniert, nicht In Betrieb
	//--------------------------------------------------------------------------------------------------------------------

	@RequestMapping("/Stammdaten")
	public MainInfoContainer getStammdaten(@RequestParam(value = "v") String v) {
		v = BasicFunctions.isinOfWkn(v);
		if (v != null) {
			return DBC_WP.getStammdaten(BasicFunctions.isinOfWkn(v)).getStammdaten();
		}
		return null;
	}

	@RequestMapping("isin")
	public IsinData isinData(@RequestParam(value = "v", required = true) String v) {
		v = BasicFunctions.isinOfWkn(v);
		if (v != null) { // check ob die isin wirklich 12 characters lang ist
			System.out.println("called for isin: " + v);
			return DBC_WP.getIsinInfo(v); // return eine Lister der Daten als Json Object
		} // falls die isin nicht 12 Characters hat wird null returned
		return null;
	}

	@RequestMapping("isinExist")
	public boolean isinExist(@RequestParam(value = "v", required = true) String v) {
		if (v.length() <= 12) { // check ob die isin wirklich 12 characters lang ist
			System.out.println("isinExist called: " + v);
			return DBC_WP.doesIsinExist(v); // return eine Lister der Daten als Json Object
		} // falls die isin niccht 12 Characters hat wird null returned
		return false;
	}

	@RequestMapping("info")
	public MainInfoContainer mainInfo(@RequestParam(value = "v", required = true) String v) {
		v = BasicFunctions.isinOfWkn(v);
		if (v != null) { // check ob die isin wirklich 12 characters lang ist
			System.out.println("MainInfo called: " + v);
			return DBC_WP.getMainInfo(v); // return eine Lister der Daten als Json Object
		} // falls die isin niccht 12 Characters hat wird null returned
		return null;
	}

	@RequestMapping(value = "editInfo", method = { RequestMethod.POST })
	public MainInfoContainer editMainInfo(HttpServletRequest request, @RequestBody MainInfoContainer info) {
		printRequestInfo(request);
		if (request != null) {
			UploadContainer uc = info.toUploadContainer();
			String source = request.getHeader("X-Forwarded-For") != null ? request.getHeader("X-Forwarded-For")
					: request.getRemoteAddr();
			uc.setData("User", "User", source, "changed by User", ""); // source, origin, comment, mic) 
			DBC_WP.uplaodData(uc);
			System.out.println("Data inserted by: " + source);
			return info;
		} else
			System.out.println();
		return null;
	}

	@RequestMapping("/distinctSources")
	public HistoryIsinUpdatesContainer distinctSources(@RequestParam(value = "v") String v) {
		v = BasicFunctions.isinOfWkn(v);
		if (v != null) {
			return DBC_WP.getSources(BasicFunctions.isinOfWkn(v));
		}
		return null;
	}

	//--------------------------------------------------------------------------------------------------------------------
	// veraltet
	//--------------------------------------------------------------------------------------------------------------------

	@RequestMapping("/wpd")
	public List<WPDModel> wpd(@RequestParam(value = "isin", required = true) String isin) {
		isin = BasicFunctions.isinOfWkn(isin);
		if (isin != null) { // check ob die isin wirklich 12 characters lang ist
			List<WPDModel> list = DBC_WP.getWpd(isin); // fetche die Daten aus der Db
			return list; // return eine Lister der Daten als Json Object
		} // falls die isin niccht 12 Characters hat wird null returned
		return null;
	}

	//--------------------------------------------------------------------------------------------------------------------
	// funktioniert nicht
	//--------------------------------------------------------------------------------------------------------------------

	@RequestMapping("/isin2")
	public List<MasterValue> isin(@RequestParam(value = "isin", required = true) String isin) {
		if (isin.length() == 12) { // check ob die isin wirklich 12 characters lang ist
			List<MasterValue> lmv = DBC_WP.getIsinData(isin); // fetche die Daten aus der Db
			return lmv; // return eine Lister der Daten als Json Object
		} // falls die isin nicht 12 Characters hat wird null returned
		return null;
	}

}
