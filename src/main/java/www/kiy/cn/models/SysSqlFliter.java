package www.kiy.cn.models;

import java.io.Serializable;
import java.util.Date;

public class SysSqlFliter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getStrFliterParam() {
		return strFliterParam;
	}
	public void setStrFliterParam(String strFliterParam) {
		this.strFliterParam = strFliterParam;
	}
	public String getStrValue() {
		return strValue;
	}
	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}
	public String getUidVouch() {
		return uidVouch;
	}
	public void setUidVouch(String uidVouch) {
		this.uidVouch = uidVouch;
	}
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	public String getStrRule() {
		return strRule;
	}
	public void setStrRule(String strRule) {
		this.strRule = strRule;
	}
	public String getStrColumn() {
		return strColumn;
	}
	public void setStrColumn(String strColumn) {
		this.strColumn = strColumn;
	}
	public boolean isbNoUsed() {
		return bNoUsed;
	}
	public void setbNoUsed(boolean bNoUsed) {
		this.bNoUsed = bNoUsed;
	}
	public Date getdCreateTime() {
		return dCreateTime;
	}
	public void setdCreateTime(Date dCreateTime) {
		this.dCreateTime = dCreateTime;
	}
	public boolean isbIsNotNull() {
		return bIsNotNull;
	}
	public void setbIsNotNull(boolean bIsNotNull) {
		this.bIsNotNull = bIsNotNull;
	}
	private String uid;
	private String strFliterParam;
	private String strValue;
	private String uidVouch;
	private String strName;
	private String strRule;
	private String strColumn; 
	private boolean bNoUsed;
	private Date dCreateTime;
	private boolean bIsNotNull;  
}
