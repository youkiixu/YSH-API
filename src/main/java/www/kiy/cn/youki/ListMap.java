package www.kiy.cn.youki;
  
import java.util.ArrayList;

public class ListMap extends ArrayList<JMap> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public JMap get(int index ){
		if(super.size()-1<index)
			return null; 
		return	SetLog.ObjectToMap(super.get(index)); 
	}
}
