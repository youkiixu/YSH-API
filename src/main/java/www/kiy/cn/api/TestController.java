package www.kiy.cn.api;
 
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import www.kiy.cn.HotKey.eSqlType;
import www.kiy.cn.service.SaaSService;
import www.kiy.cn.service.QueryService;
import www.kiy.cn.service.TestService;
import www.kiy.cn.youki.CacheInfo;
import www.kiy.cn.youki.JMap;
import www.kiy.cn.youki.Pub;
import www.kiy.cn.youki.SetLog;

@RestController
@RequestMapping("/tt")
public class TestController {
	@Autowired
	private SaaSService saas;
	
	@Autowired
	 private ApplicationContext applicationContext; 
	
	TestService testService;
	@Autowired
	CacheInfo cacheInfo;
	public static final Logger log = LoggerFactory.getLogger(TestController.class);
	@RequestMapping("/test")
	 public String Index() {
		log.info("msg");
		SetLog.logInfo("testAAAAAAAAAA");  
		 try { 
			 
 		 try { 
 			 
 			JMap LogisticsOrder = new JMap();
 			LogisticsOrder.put("a", "123");
 			LogisticsOrder.put("b", "123");
 			LogisticsOrder.put("c", "123");
 			LogisticsOrder.put("UserId", "123");
 			LogisticsOrder.put("ContactName", "33333");
 			LogisticsOrder.put("ContactAddress", "广东省 佛山市 南海区 桂城街道 大龙天空");
 			LogisticsOrder.put("ReceiveName", "徐玉基");
 			LogisticsOrder.put("CollectionMoney", "11");
 			LogisticsOrder.put("NeedingAttention", "123");
 			LogisticsOrder.put("CreateDate", "2011-11-11");
 			LogisticsOrder.put("ModifyerId", "0");
 			LogisticsOrder.put("ContactTelePhone", "13538969547");
 			LogisticsOrder.put("ReceiveTelePhone", "111111133333");
 			LogisticsOrder.put("ReceiveRegionId", 1);
 			LogisticsOrder.put("ReceiveAddress", "广东省 佛山市 禅城区 石湾街道 小星星行");
 			LogisticsOrder.put("OrderForm", 1);
 			LogisticsOrder.put("ThirdPlatformOrderNo", 11111);
 			LogisticsOrder.put("StartDate", "2011-12-11");
 			
 			
 			LogisticsOrder.put("DistributorName", "test");
 			LogisticsOrder.put("Id", "1");
 			
 			
 			JMap LogisticsOrder_List = new JMap();
 			LogisticsOrder_List.put("a", "123");
 			LogisticsOrder_List.put("LogisticsNo_P", "111");
 			LogisticsOrder_List.put("LogisticsNo_Key", "111");
 			LogisticsOrder_List.put("UserID", "10095");
 			LogisticsOrder_List.put("Logistics_status", "50");
 			LogisticsOrder_List.put("InsertTime", "2018-11-07 17:23:12.123");
 			
 			
 			
 			JMap LogisticsOrder_List1 = new JMap();
 			LogisticsOrder_List1.put("a", "222");
 			LogisticsOrder_List1.put("LogisticsNo_P", "222");
 			LogisticsOrder_List1.put("LogisticsNo_Key", "222");
 			LogisticsOrder_List1.put("UserID", "11");
 			LogisticsOrder_List1.put("Id", "1");
 			
 			JMap LogisticsOrder_List2 = new JMap();
 			LogisticsOrder_List2.put("a", "222");
 			LogisticsOrder_List2.put("LogisticsNo_P", "333");
 			LogisticsOrder_List2.put("LogisticsNo_Key", "333");
 			LogisticsOrder_List2.put("UserID", "11");
 			LogisticsOrder_List2.put("Id", "2");
 			
 			JMap LogisticsOrder_List3 = new JMap();
 			LogisticsOrder_List3.put("InsertTime", "2018-11-07 17:25:12.123");
 			LogisticsOrder_List3.put("a", "123");
 			LogisticsOrder_List3.put("LogisticsNo_P", "444");
 			LogisticsOrder_List3.put("LogisticsNo_Key", "444");
 			LogisticsOrder_List3.put("UserID", "10095");
 			LogisticsOrder_List3.put("Logistics_status", "50");
 			
 			
 			JMap LogisticsRecords = new JMap();
 			
 			LogisticsRecords.put("Enabled", true);
 			LogisticsRecords.put("CreateDate", "2018-11-07 17:25:12.123");
 			LogisticsRecords.put("Remark", "333");
 			LogisticsRecords.put("Logistics_status", "50");
 			LogisticsRecords.put("OrderID_P", "1");
 			LogisticsRecords.put("OrderId", "1"); 
 			LogisticsRecords.put("AdminId", "123");
 			
 			
 			JMap LogisticsRecords1 = new JMap();
 			LogisticsRecords1.put("a", "123");
 			LogisticsRecords1.put("OrderID_P", "2");
 			LogisticsRecords1.put("Id", "124");
 			LogisticsRecords1.put("Logistics_status", "50");
 			
 			
 			
 			JMap LogisticsRecords2 = new JMap();
 			LogisticsRecords2.put("a", "123");
 			LogisticsRecords2.put("b", "123");
 			LogisticsRecords2.put("Id", "123");
 			LogisticsRecords2.put("OrderID_P", "2");
 			LogisticsRecords2.put("Logistics_status", "50");

 			JMap  data  = new JMap();
 			List<JMap> lstLogisticsOrder = new ArrayList<JMap>();
 			lstLogisticsOrder.add(LogisticsOrder);
 			data.put("LogisticsOrder",lstLogisticsOrder); 
 			
 			List<JMap> lstLogisticsOrder_List= new ArrayList<JMap>();
 			lstLogisticsOrder_List.add(LogisticsOrder_List);
 			lstLogisticsOrder_List.add(LogisticsOrder_List1);
 			lstLogisticsOrder_List.add(LogisticsOrder_List2);
 			lstLogisticsOrder_List.add(LogisticsOrder_List3);
 			data.put("LogisticsOrder_List",lstLogisticsOrder_List); 
 			
 			List<JMap> lstLogisticsRecords= new ArrayList<JMap>();
 			lstLogisticsRecords.add(LogisticsRecords);
 			lstLogisticsRecords.add(LogisticsRecords1);
 			lstLogisticsRecords.add(LogisticsRecords2);
 			data.put("LogisticsRecords",lstLogisticsRecords);  
 			Object obj=saas.tbSaaSSave("YSH00000003", "LogisticsOrder", data, "LogisticsOrder", "LogisticsOrder_List:LogisticsNO_PKey-LogisticsRecords:OrderID_P");
 		//	Object obj=saas.tbSaaSSaveForList("YSH00000003", data, eSqlType.Jdbc); 
				 //Object obj= Pub.getInstance().InvokeMethod(applicationContext,null,"www.kiy.cn.service.impl", "TestServiceImpl", "tbSaveTest", new JMap());
				System.out.println(obj);
 				
 			} catch (Exception e) {
 				// TODO Auto-generated catch block
 					e.printStackTrace();
 			}
//			 
			   JMap map = new JMap();  
			   map.put("@IsHidden",0);
			 System.out.println("data="+saas.getDataSetByMethod("YSH00000007", "GetProductComments",map));
			   
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		 cacheInfo.getString("a");  
//		 
//		 
//		cacheInfo.putString("kk", "222");
//		
//		cacheInfo.getString("kk");
//		
//		JMap m = new JMap();
//		m.put("1231", "111");
//		m.put("a", 222);
//		
//		JMap m1 = new JMap();
//	    m1.put("7", "77");
//		
//		m.put("m1", m1);
//		
//		cacheInfo.putJMap("m",m);
//		cacheInfo.getMap("m"); 
		
		return "xxx";
	}
	
	
	

}
