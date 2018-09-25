package www.kiy.cn.youki;


public class YSHException extends Exception{

	private static final long serialVersionUID = 1L;

    private String strErrCode; 
    public YSHException(String message){
        super(message);
    }
    public YSHException(String errorCode, String message){
        super(message);
        this.setErrorCode(errorCode); 
    }

    public YSHException(String strErrCode, String message, Throwable cause){
        super(message, cause);
        this.setErrorCode(strErrCode); 
    } 
 
    public YSHException(String message, Throwable cause){
        super(message, cause);

    }
    public String getErrorCode(){
        return strErrCode;
    }
    public void setErrorCode(String strErrCode){
        this.strErrCode = strErrCode;
    } 
}