package de.vv.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.vv.web.config.GlobalScope;
import de.vv.web.db.DBCon;
import de.vv.web.model.*;

@RestController
@RequestMapping("/api/wp")
public class WpController {

	@RequestMapping("/isin2")
	public List<MasterValue> isin(@RequestParam(value = "isin", required = true) String isin) {
		if (isin.length() == 12) { // check ob die isin wirklich 12 characters lang ist
			List<MasterValue> lmv = DBCon.getIsinData(isin); // fetche die Daten aus der Db
			return lmv; // return eine Lister der Daten als Json Object
		} // falls die isin nicht 12 Characters hat wird null returned
		return null;
	}

	@RequestMapping("/hi")
	public String hi() {
		return "hi";
	}
	
	@RequestMapping("isin")
	public IsinData isinData(@RequestParam(value = "v", required = true) String v){
		if (v.length() <= 12) { // check ob die isin wirklich 12 characters lang ist
			System.out.println("called for isin: "+v);
			return DBCon.getIsinInfo(v); // return eine Lister der Daten als Json Object
		} // falls die isin nicht 12 Characters hat wird null returned
		return null;
	}
	
	@RequestMapping("isinExist")
	public boolean isinExist(@RequestParam(value = "v", required = true) String v){
		if (v.length() <= 12) { // check ob die isin wirklich 12 characters lang ist
			System.out.println("isinExist called: "+v);
			return DBCon.doesIsinExist(v); // return eine Lister der Daten als Json Object
		} // falls die isin niccht 12 Characters hat wird null returned
		return false;
	}
	
	@RequestMapping("info")
	public MainInfoContainer mainInfo(@RequestParam(value = "v", required = true) String v){
		if (v.length() == 12 || v.length() == 6) { // check ob die isin wirklich 12 characters lang ist
			System.out.println("MainInfo called: "+v);
			return DBCon.getMainInfo(v); // return eine Lister der Daten als Json Object
		} // falls die isin niccht 12 Characters hat wird null returned
		return null;
	}
	
	@RequestMapping(value = "editInfo", method = { RequestMethod.POST })
	public MainInfoContainer editMainInfo(HttpServletRequest request, @RequestBody MainInfoContainer info){
		printRequestInfo(request);
		if(request != null){
			UploadContainer uc = info.toUploadContainer();
			String source = request.getHeader("X-Forwarded-For") !=null ? 
					request.getHeader("X-Forwarded-For") : request.getRemoteAddr();
			uc.setData("AnonUser", source, "changed by User", ""); // source, origin, comment, mic) 
			DBCon.uplaodData(uc);
			System.out.println("Data inserted by: " + source);
			return info;
		} else System.out.println();
		return null;
	}
	
	void printRequestInfo(HttpServletRequest r){
		System.out.println("rUser: " + r.getRemoteUser());
		System.out.println("rAddr: " + r.getRemoteAddr());
		System.out.println("rHost: " + r.getRemoteHost());
		System.out.println("XForward: " +  r.getHeader("X-Forwarded-For"));
	}
	
	@RequestMapping("/wpd")
	public List<WPDModel> wpd(@RequestParam(value = "isin", required = true) String isin) {
		if (isin.length() <= 12) { // check ob die isin wirklich 12 characters lang ist
			List<WPDModel> list = DBCon.getWpd(isin); // fetche die Daten aus der Db
			return list; // return eine Lister der Daten als Json Object
		} // falls die isin niccht 12 Characters hat wird null returned
		return null;
	}
	
	/**
	 * isinAC - Isin AutoComplete This Interface is used to offer an
	 * autocomplete function when looking for isin,
	 * based on contains
	 * @param in
	 * @return
	 */
	@RequestMapping("/isinAC")
	public List<String> isinAC(@RequestParam(value = "v", required = true) String v, @RequestParam(value = "amt", required = true) int amt) {
		if(amt<0) return null;
		return GlobalScope.getLikeIsin(v.toLowerCase(), amt);
	}

}
