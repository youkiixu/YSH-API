package www.kiy.cn.service.impl;

import java.sql.Connection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import www.kiy.cn.configs.DBDataSourceConfig;
import www.kiy.cn.service.MssqlPoolService;
import www.kiy.cn.youki.Convert;
import www.kiy.cn.youki.SetLog; 

@Service 
public class MssqlPoolServiceImpl implements MssqlPoolService {
	
	private String strServerName, strDBName, strUserID, strPassword;  
	/**
	 * 
	 * 注意pool.getConnection()，都是先从threadlocal里面拿的，如果threadlocal里面有，则用，保证线程里的多个dao操作，用的是同一个connection，以保证事务
	 */
	
	//最大连接数
	@Value("${spring.datasource.max-active}")
	private int maxActive;
	

	 // 空闲连接  
    private List<Connection> free= new Vector<Connection>();  
    // 活动连接  
    private List<Connection> active= new Vector<Connection>();  

    // 将线程和连接绑定，保证事务能统一执行
    private  ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();  
    
	private DBDataSourceConfig db; 

	@Autowired
	public MssqlPoolServiceImpl(DBDataSourceConfig db) {
		this.db= db;
	}
	
	public MssqlPoolServiceImpl( String strServerName,String strDBName,String strUserID,String strPassword){
		this.strServerName=strServerName; 
		this.strDBName = strDBName;
		this.strUserID = strUserID;
		this.strPassword= strPassword;
		CheckPool(); 
		this.db= new  DBDataSourceConfig();
	}
	
    
	
       
    /* (non-Javadoc)
	 * @see www.kiy.cn.service.impl.ConnectPoolService#isClose(java.sql.Connection)
	 */
    @Override
	public void isClose(Connection cn)  {
        isClose(cn,false);
    }
    /**
     * 
     *bClose判断是否需要  关闭链接
     */
    private boolean isClose(Connection cn,boolean bDestory)  { 
        try{
            if(bDestory){
                cn.close();
                cn=null;
                return true;
            }

            if(cn==null || cn.isClosed()){
                return true;
            }
            
        }catch(Exception ex){
            SetLog.Info("JdbcDaoEception", ex.toString());
        }
        if(bDestory){
            cn=null;
        }
        return false;
    }
   
    /* (non-Javadoc)
	 * @see www.kiy.cn.service.impl.ConnectPoolService#destory()
	 */
    @Override
	public void destory()  {

        for (Connection cn : free ) {
            isClose(cn,true);
        }

        for (Connection cn : active ) {
            isClose(cn,true);
        }

        threadLocal.remove();
        
    }
    
    
    public synchronized Connection getConnection() throws Exception{ 
    	System.out.println("当前活跃线程数:"+ Convert.ToMssqlJdbcUrl(this.strServerName, this.strDBName));
    	System.out.println("当前空闲线程数:"+free.size());
    	System.out.println("当前活跃线程数:"+active.size());
    	System.out.println("当前总线程数:"+maxActive);
        if(free.size()>0){
            Connection cn  =free.get(0);
            threadLocal.set(cn);
            active.add(cn);
            free.remove(cn);
            return cn;
        }
        try{
            if(free.size()==0 || active.size() <=maxActive  ){
            	//每调取一次getConnection 会创建新的Connection
        		//并不能满足高并发情况。因为connection不是线程安全的
        		//用线程池进行
                Connection cn=getDriverManagerDataSource().getConnection();
                active.add(cn); 
                threadLocal.set(cn);
                return cn; 
            }
            else{
            	
                wait(1000);
                return getConnection(); 
            } 
        }catch(Exception ex){
            throw new Exception(ex.toString());
        } 
    }
    /* (non-Javadoc)
	 * @see www.kiy.cn.service.impl.ConnectPoolService#releaseConnection(java.sql.Connection)
	 */
    @Override
	public synchronized void release(Connection cn)  {
       
        if(!isClose(cn,false)){
            free.add(cn);
            active.remove(cn);
            threadLocal.remove();
            notifyAll();   
        }

    }  
    /* (non-Javadoc)
	 * @see www.kiy.cn.service.impl.ConnectPoolService#getDriverManagerDataSource()
	 */
    @Override
	public DriverManagerDataSource getDriverManagerDataSource()throws Exception {
    	DriverManagerDataSource ds = db.getMssQLDataSource(strServerName, strDBName, strUserID, strPassword);
		return ds;
	}
	

	// 定时检查连接池情况
	/* (non-Javadoc)
	 * @see www.kiy.cn.service.impl.ConnectPoolService#CheckPool()
	 */
	@Override
	public void CheckPool() {
		//设定指定任务task在指定延迟delay后进行固定延迟peroid的执行  
		new Timer().scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("定时检查连接池情况");
				System.out.println("当前活跃线程数:"+ Convert.ToMssqlJdbcUrl(strServerName, strDBName));
				System.out.println("空线池连接数：" + free.size());
				System.out.println("活动连接数：：" + active.size());
				System.out.println("总的连接数：" + maxActive);
			}
		},0, 1000 * 60 * 5);//5分钟跑一次
	}
	
	/* (non-Javadoc)
	 * @see www.kiy.cn.service.impl.ConnectPoolService#getStrServerName()
	 */
	@Override
	public String getStrServerName() {
		return strServerName;
	}

	/* (non-Javadoc)
	 * @see www.kiy.cn.service.impl.ConnectPoolService#setStrServerName(java.lang.String)
	 */
	@Override
	public void setStrServerName(String strServerName) {
		this.strServerName = strServerName;
	}

	/* (non-Javadoc)
	 * @see www.kiy.cn.service.impl.ConnectPoolService#getStrDBName()
	 */
	@Override
	public String getStrDBName() {
		return strDBName;
	}

	/* (non-Javadoc)
	 * @see www.kiy.cn.service.impl.ConnectPoolService#setStrDBName(java.lang.String)
	 */
	@Override
	public void setStrDBName(String strDBName) {
		this.strDBName = strDBName;
	}

	/* (non-Javadoc)
	 * @see www.kiy.cn.service.impl.ConnectPoolService#getStrUserName()
	 */
	@Override
	public String getStrUserID() {
		return strUserID;
	}

	/* (non-Javadoc)
	 * @see www.kiy.cn.service.impl.ConnectPoolService#setStrUserName(java.lang.String)
	 */
	@Override
	public void setStrUserID(String strUserID) {
		this.strUserID = strUserID;
	}

	/* (non-Javadoc)
	 * @see www.kiy.cn.service.impl.ConnectPoolService#getStrPassword()
	 */
	@Override
	public String getStrPassword() {
		return strPassword;
	}

	/* (non-Javadoc)
	 * @see www.kiy.cn.service.impl.ConnectPoolService#setStrPassword(java.lang.String)
	 */
	@Override
	public void setStrPassword(String strPassword) {
		this.strPassword = strPassword;
	}

	/* (non-Javadoc)
	 * @see www.kiy.cn.service.impl.ConnectPoolService#setDriverManager(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void setDriverManager(String strServerName, String strDBName, String strUserID, String strPassword) {
		this.strPassword = strPassword;
		this.strDBName = strDBName;
		this.strServerName = strServerName;
		this.strUserID = strUserID;
	}
}
