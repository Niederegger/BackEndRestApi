package de.vv.web.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.vv.web.db.DBCon;
import de.vv.web.model.MasterValue;

@RestController
public class IsinController {

	/**
	 * Get Controller requesting Isin MasterValues
	 * 
	 * @param isin
	 * @param model
	 * @return
	 */
	@RequestMapping("/isin")
	public List<MasterValue> isin(@RequestParam(value = "isin", required = true) String isin) {
		if (isin.length() == 12) { // check ob die isin wirklich 12 characters lang ist
			List<MasterValue> lmv = DBCon.getIsinData(isin); // fetche die Daten																// aus der Db
			return lmv; // return eine Lister der Daten als Json Object
		} // falls die isin niccht 12 Characters hat wird null returned
		return null;
	}

	@RequestMapping("/hi")
	public String hi() {

		return "hi";
	}
	
	/**
	 * isinAC - Isin AutoComplete This Interface is used to offer an
	 * autocomplete function when looking for isin,
	 * based on contains
	 * @param in
	 * @return
	 */
	@RequestMapping("/isinAC")
	public List<String> isinAC(@RequestParam(value = "isin", required = true) String isin) {
		return DBCon.getAllIsins(isin);
	}

}
