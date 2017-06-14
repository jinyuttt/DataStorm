/**    
 * 文件名：SqliteDB.java    
 *    
 * 版本信息：    
 * 日期：2017年5月27日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package DBAcess;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**    
 *     
 * 项目名称：DBAcess    
 * 类名称：SqliteDB    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2017年5月27日 上午2:53:47    
 * 修改人：jinyu    
 * 修改时间：2017年5月27日 上午2:53:47    
 * 修改备注：    
 * @version     
 *     
 */
public class SqliteDB {
    // 得到连接 会在你所填写的目录建一个你命名的文件数据库  
    Connection conn; 
    String dbfile=":memory:";
    public void setDB(String file)
    {
        dbfile=file;
    }
    public void close()
    {
        if(conn!=null)
        {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public void  open()
    {
        // 加载驱动  
        try {  
            Class.forName("org.sqlite.JDBC");  
        } catch (ClassNotFoundException e) {  
            // TODO Auto-generated catch block  
            // e.printStackTrace();  
            System.out.println("数据库驱动未找到!");  
        }  
            try {
                String pre="jdbc:sqlite:"+dbfile;
                conn = DriverManager.getConnection(pre,null,null);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }  
            // 设置自动提交为false  
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }  
    }
    public int Update(String sql)
    {
        Statement statement = null;
        try {
            statement = conn.createStatement();
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }  
        try {
         int r=   statement.executeUpdate(sql);
         return r;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        return 0;
    }
public ResultSet query(String sql)
{
    Statement stat = null;
    try {
        stat = conn.createStatement();
    } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    ResultSet rs = null;
    try {
        rs = stat.executeQuery(sql);
    } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } // 查询数据
     return rs;
}
}
