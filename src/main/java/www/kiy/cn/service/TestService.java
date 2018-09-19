package www.kiy.cn.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import www.kiy.cn.youki.JMap;

public interface TestService {
	
	
	JMap tbSaveTest();
	
	
}
