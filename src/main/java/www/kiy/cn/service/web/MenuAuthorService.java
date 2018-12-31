package www.kiy.cn.service.web;

import java.util.List;

import org.springframework.stereotype.Service;

import www.kiy.cn.youki.JMap;
import www.kiy.cn.youki.ListMap;

public interface MenuAuthorService {

	JMap HomeMenuAuthor(Object json) throws Exception;

	ListMap getMapRecursion(List<JMap> dt,  String strPreParentCode);

}
