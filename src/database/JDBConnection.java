package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBConnection {
	private final static String url ="jdbc:mysql://589b3dff3a97e.gz.cdb.myqcloud.com:15656/mytest?user=root&password=LIdongyang2012&useUnicode=true&characterEncoding=utf8";
	private final static String dbDriver = "com.mysql.jdbc.Driver";
    private Connection con = null;
    static 
    {
    	try 
    	{
    		Class.forName(dbDriver).newInstance();
    	} 
    	catch (Exception ex) 
    	{
    		System.out.println("驱动加载失败！");
    	}
    }

    public boolean creatConnection() 
    {
    	try 
    	{
    		con = DriverManager.getConnection(url);
    		con.setAutoCommit(true);
    	} 
    	catch (SQLException e) 
    	{
    		System.out.println(e.getMessage());
    		System.out.println("creatConnectionError!");
    	}
    	return true;
    }

    public boolean executeUpdate(String sql) 
    {
    	if (con == null)
    		creatConnection();
    	try 
    	{
    		Statement stmt = con.createStatement();
    		int iCount = stmt.executeUpdate(sql);
    		return true;
    	} 
    	catch (SQLException e) 
    	{
    		System.out.println(e.getMessage());
    		return false;
    	}
    }

    public ResultSet executeQuery(String sql) 
    {
    	ResultSet rs;
    	try 
    	{
    		if (con == null)
    			creatConnection();
    		Statement stmt = con.createStatement();
    		try 
    		{
    			rs = stmt.executeQuery(sql);
    		} 
    		catch (SQLException e) 
    		{
    			System.out.println(e.getMessage());
    			return null;
    		}
    	} 
    	catch (SQLException e) 
    	{
    		return null;
    	}
    	return rs;
    }
    
    public void close(ResultSet rs) 
    {
    	if(rs!= null)
    	{
    		try 
    		{
    			rs.close();
    		} 
    		catch (SQLException e) 
    		{
    			e.printStackTrace();
    		}
    	}
    	if(con!= null)
    	{
    		try 
    		{
    			con.close();
    			con=null;
    		} 
    		catch (SQLException e) 
    		{
    			e.printStackTrace();
    		}
    	}
    }
    
    public void close2()
    {
    	if(con != null)
    	{
		   try 
		   {
			   con.close();
			   con=null;
		   } 
		   catch (SQLException e) 
		   {
			   e.printStackTrace();
		   }
		}
    }
    public void test()
    {
    	String sql = "select * from user;";
    	ResultSet r = this.executeQuery(sql);
    	try {
			while(r.next())
			{
				System.out.println(r.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public static void main(String[] avg)
    {
    	String sql = "select * from user;";
    	JDBConnection j = new JDBConnection();
    	//j.executeQuery(sql);
    	j.test();
    }
}