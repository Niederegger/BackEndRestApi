package de.vv.web.model.ContainerForOldThoughts;

public class WPDModel {
	public String isin;
	public String url;
	public String sourceId;
	
	public WPDModel(String isin, String url, String sourceId){
		this.isin = isin;
		this.url = url;
		this.sourceId = sourceId;
	}
}
