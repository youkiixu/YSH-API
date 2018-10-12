package www.kiy.cn.youki;
 
import java.io.Serializable;
import java.util.TreeMap;  
public class JMap  extends  TreeMap<String,Object> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
  
	public Object getWithRemoveKey(String key) {
		 if(super.containsKey(key)){
			Object obj= super.get(key);
			super.remove(key);
			return obj;
		 }
		 return null;
	}
	@Override
	public Object put(String key,Object value){
		if(super.containsKey(key))
			super.remove(key); 
		return super.put(key, value);
		
	}
	
	public JMap getMap(String key){
		if(key==null)
			return null;
		if(!super.containsKey(key))
			return null;
		return SetLog.ObjectToMap(super.get(key));
	}
//	private Map<String,Object> map = null;
//	public JMap( ){
//		map = new HashMap<String,Object>();
//	}
//	
//	public void clear(){
//		map.clear();
//	} 
//	public boolean containKey(String key){
//		return map.containsKey(key); 
//	}
//	public int Count(){
//		return map.size();
//	}

//	public Object get(String key){
//		if(containKey(key))
//			return map.get(key);
//		return null;
//	}
//	public void removeKey(String key){
//		if(containKey(key))
//			map.remove(key);
//	}
//	public Object getWithClearKey(String key){
//		Object object= get(key);
//		 removeKey(key);
//		 return object;
//		
//	}
//	public Map<String, Object> getMap(){
//		return map;
//	}
//	public void putAll(JMap map){
//		if(map!=null){ 
//			this.map.putAll(map.getMap()); 
//		}
//	}
}
