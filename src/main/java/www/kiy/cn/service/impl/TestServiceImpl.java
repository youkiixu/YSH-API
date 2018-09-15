package www.kiy.cn.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import www.kiy.cn.dao.SysInvokeMethodDao;
import www.kiy.cn.service.TestService;
import www.kiy.cn.youki.JMap;

@Service
public class TestServiceImpl implements TestService {

	@Resource
	private  SysInvokeMethodDao sysInvokeMethodDao;
	public void Test() {
		// TODO Auto-generated method stub
		System.out.println("MY Name Is Test !!!!");
		List<JMap> lst= sysInvokeMethodDao.getEntityByName("HomeMenuAuthor");
		System.out.println(lst);
	}

}
