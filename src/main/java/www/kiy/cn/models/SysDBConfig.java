package www.kiy.cn.models;

import java.io.Serializable;

public class SysDBConfig   implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getIntSysProject() {
		return intSysProject;
	}
	public void setIntSysProject(int intSysProject) {
		this.intSysProject = intSysProject;
	}
	public String getStrAppid() {
		return strAppid;
	}
	public void setStrAppid(String strAppid) {
		this.strAppid = strAppid;
	}
	public String getStrServerName() {
		return strServerName;
	}
	public void setStrServerName(String strServerName) {
		this.strServerName = strServerName;
	}
	public String getStrDBName() {
		return strDBName;
	}
	public void setStrDBName(String strDBName) {
		this.strDBName = strDBName;
	}
	public String getStrUserID() {
		return strUserID;
	}
	public void setStrUserID(String strUserID) {
		this.strUserID = strUserID;
	}
	public String getStrPassword() {
		return strPassword;
	}
	public void setStrPassword(String strPassword) {
		this.strPassword = strPassword;
	}
	public boolean isbDefault() {
		return bDefault;
	}
	public void setbDefault(boolean bDefault) {
		this.bDefault = bDefault;
	}
	public boolean isbDecode() {
		return bDecode;
	}
	public void setbDecode(boolean bDecode) {
		this.bDecode = bDecode;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private int ID;
	private int intSysProject; 
	private String strAppid;
	private String strServerName; 
	private String strDBName;
	private String strUserID;
	private String strPassword;
	private boolean bDefault;
	private boolean bDecode;
}
