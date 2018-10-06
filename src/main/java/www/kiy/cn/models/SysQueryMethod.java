package www.kiy.cn.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SysQueryMethod implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public int getIntSysProject() {
		return intSysProject;
	}
	public void setIntSysProject(int intSysProject) {
		this.intSysProject = intSysProject;
	}
	public int getIntDBConfig() {
		return intDBConfig;
	}
	public void setIntDBConfig(int intDBConfig) {
		this.intDBConfig = intDBConfig;
	}
	public String getStrCon() {
		return strCon;
	}
	public void setStrCon(String strCon) {
		this.strCon = strCon;
	}
	public String getStrMethod() {
		return strMethod;
	}
	public void setStrMethod(String strMethod) {
		this.strMethod = strMethod;
	}
	public String getStrText() {
		return strText;
	}
	public void setStrText(String strText) {
		this.strText = strText;
	}
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	public String getStrGroupBy() {
		return strGroupBy;
	}
	public void setStrGroupBy(String strGroupBy) {
		this.strGroupBy = strGroupBy;
	}
	public String getStrOrderBy() {
		return strOrderBy;
	}
	public void setStrOrderBy(String strOrderBy) {
		this.strOrderBy = strOrderBy;
	}
	public boolean isbProcedurce() {
		return bProcedurce;
	}
	public void setbProcedurce(boolean bProcedurce) {
		this.bProcedurce = bProcedurce;
	}
	public boolean isbGetInfo() {
		return bGetInfo;
	}
	public void setbGetInfo(boolean bGetInfo) {
		this.bGetInfo = bGetInfo;
	}
	public boolean isbPage() {
		return bPage;
	}
	public void setbPage(boolean bPage) {
		this.bPage = bPage;
	}
	public boolean isbNonQuery() {
		return bNonQuery;
	}
	public void setbNonQuery(boolean bNonQuery) {
		this.bNonQuery = bNonQuery;
	}
	public boolean isbAppend() {
		return bAppend;
	}
	public void setbAppend(boolean bAppend) {
		this.bAppend = bAppend;
	}
	public Date getdCreateTime() {
		return dCreateTime;
	}
	public void setdCreateTime(Date dCreateTime) {
		this.dCreateTime = dCreateTime;
	}
	public String getStrFrom() {
		return strFrom;
	}
	public void setStrFrom(String strFrom) {
		this.strFrom = strFrom;
	}
	public String getStrCode() {
		return strCode;
	}
	public void setStrCode(String strCode) {
		this.strCode = strCode;
	}
	public String getStrParentCode() {
		return strParentCode;
	}
	public void setStrParentCode(String strParentCode) {
		this.strParentCode = strParentCode;
	}
	public List<SysSqlFliter> getSysSqlFliter() {
		return sysSqlFliter;
	}
	public void setSysSqlFliter(List<SysSqlFliter> sysSqlFliter) {
		this.sysSqlFliter = sysSqlFliter;
	} 
	private String uid;
	private int intSysProject;
	private int intDBConfig;
	private String strCon;
	private String strMethod;
	private String strText;
	private String strName;
	private String strGroupBy;
	private String strOrderBy;
	private boolean bProcedurce;
	private boolean bGetInfo;
	private boolean bPage;
	private boolean bNonQuery;
	private boolean bAppend;
	private Date dCreateTime;
	private String strFrom;
	private String strCode;
	private String strParentCode;   
	private List<SysSqlFliter> sysSqlFliter; 
}
