package www.kiy.cn.service.impl;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import www.kiy.cn.dao.TestDao;
import www.kiy.cn.service.TestService;
import www.kiy.cn.youki.JMap;
@Service(value="TestService")
public class TestServiceImpl implements TestService  {

	
	@Resource
	TestDao testDao;
	
	@Transactional(propagation=Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	@Override
   	public JMap tbSaveTest() {
		// TODO Auto-generated method stub
		JMap m = new JMap();
		m.put("a", "This is java Test"); 
		JMap m2 = new JMap();
		m2.put("a", "This is java Test1");   
		
		int obj=	testDao.tbSaveTest(m);
		obj +=	testDao.tbSaveTest(m2); 
		
	    System.out.println(obj);
	    System.out.println(m);
	    System.out.println(m2);
		
	    JMap m3 = new JMap();
		m3.put("b", "This is java Test");
		m3.put("idA", m.get("id")); 
		
		JMap m4 = new JMap();
		m4.put("a", "This is java Test1    1");   
		
		obj=	testDao.tbSaveTestB(m3);
		obj +=	testDao.tbSaveTestB(m4); 
	    
	    return null;
	}

}
