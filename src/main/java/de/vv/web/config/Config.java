package de.vv.web.config;

public class Config {
/*
{
  "user": "TestUser",
  "pw": "TestUser",
  "serverName": "DESKTOP-LNOORVK\\SQLEXPRESS",
  "dbName": "MasterData",
  "port": 1433,
  "fileLocation" : "D:\\Files\\"
}
*/
	public String user;															// Database User
	public String pw;																// Database Password
	public String serverName;												// Database Server Name
	public String dbName;														// Database DB-Name
	public int port;																// Database Port
	public String fileLocation = "D:\\Files\\";			// FileServer Path Location
	public int thisPort;														// WebSite Port
}
