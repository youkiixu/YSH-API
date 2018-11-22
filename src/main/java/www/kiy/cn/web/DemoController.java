package www.kiy.cn.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import www.kiy.cn.youki.Convert;
import www.kiy.cn.youki.JMap;
@RestController
@RequestMapping("/Demo")
public class DemoController {
	
	
	private List<JMap> userList(){
		List<JMap> lst =new ArrayList<JMap>();
    	JMap map = new JMap();
    	map.put("Id",1);
    	map.put("strName","我是 1 ");
    	lst.add(map);
    	
    	JMap map2 = new JMap();
    	map2.put("Id",2);
    	map2.put("strName","我是 2 ");
    	lst.add(map2);
    	return lst;
	}
	@RequestMapping("/test")
	 public String Index() {
		return "test Hello";
	}
	 /**
     * 查询所用用户
     * @return
     */
    @GetMapping
    public ModelAndView list(Model model) { 
    	
        model.addAttribute("userList", userList());
        model.addAttribute("title", "用户管理");
        return new ModelAndView("Demo/list", "mUser", model); 
    }
    
    /**
     * 根据id查询用户
     * @return
     */
    @GetMapping("{id}")
    public ModelAndView view(@PathVariable("id") Long id, Model model) {
    	 
    	 List<JMap> lst= userList();
    	 JMap u = null;
    	 for (int i =0 ;i<lst.size() ;i++){
    		 u =lst.get(i);
    		 if( Convert.ToInt32(u.get("Id")) ==id) 
    			 break;  
    	 }  
    	
    	 model.addAttribute("user", u);
         model.addAttribute("title", "查看用户");
    	
         return new ModelAndView("Demo/view", "mUser", model);
         
    }
    /**
     * 获取 form 表单页面
     * @return
     */
    @GetMapping("/form")
    public ModelAndView createForm(Model model) {
    	JMap m= new JMap();
    	m.put("Id", 0);
    	m.put("strName",null );
    	model.addAttribute("user", m);
        model.addAttribute("title", "创建用户");
        return new ModelAndView("Demo/form", "mUser", model);
    }

    /**
     * 新建用户
     * @param user
     * @return
     */
    @PostMapping
    public ModelAndView create(JMap user) { 
        return new ModelAndView("redirect:/Demo");
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @GetMapping(value = "delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id, Model model) {
        
        model.addAttribute("userList",this.userList());
        model.addAttribute("title", "删除用户");
        return new ModelAndView("Demo/list", "mUser", model);
    }

    /**
     * 修改用户
     */
    @GetMapping(value = "modify/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Long id, Model model) { 
    	JMap m = new JMap();
    	m.put("Id", 1);
    	m.put("strName", "mamamam");
    	
        model.addAttribute("user", m);
        model.addAttribute("title", "修改用户");
        return new ModelAndView("Demo/form", "mUser", model);
    }
}
