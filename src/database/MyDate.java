package database;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class MyDate {
	
	Map<String,String> M = new HashMap<String,String>();
	
	public static boolean has_this(String table,String clume,String feature,boolean isString)
	{
		String sql="";
		if (isString)
			sql = "select * from "+table+" where "+clume+" = '"+feature+"';";
		else
			sql = "select * from "+table+" where "+clume+" = "+feature+";";
		JDBConnection j = new JDBConnection();
		ResultSet r = j.executeQuery(sql);
		try
		{
			if(r.next()) return true;
		}
		catch (Exception e)
		{
			
		}
		finally
		{
			j.close(r);
		}
		return false;
	}
	
	public static boolean checkID(String id,String password)
	{
		String sql = "select password from user where id = "+id+";";
		JDBConnection j = new JDBConnection();
		ResultSet r = j.executeQuery(sql);
		try
		{
			if (r.next()&&r.getString(1).equals(password)) return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			j.close(r);
		}
		return false;
	}
	
	public static void add_user(String name,String email,String password,String phone,String address,String date)
	{
		String sql = "insert into user (name,email,password,phone,address,date) " + 
				     "values('"+name+"','"+email+"','"+password+"','"+phone+"','"+address+"','"+date+"');";
		System.out.println("sql = "+sql);
		JDBConnection j = new JDBConnection();
		j.executeUpdate(sql);
		j.close2();
	}
	public static String login(String email,String password)
	{
		String sql = "select * from user where email = '"+email+"';";
		JDBConnection j = new JDBConnection();
		ResultSet r = j.executeQuery(sql);
		String result=-1+"";
		try
		{
			if (r.next())
			{
				int my_id = r.getInt(1);
				String my_name = r.getString(2);
				String my_password = r.getString(3);
				String my_email = r.getString(4);
				String my_phone = r.getString(5);
				String my_address = r.getString(6);
				if(my_password.equals(password))
				{
					result = my_id+"|"+my_name+"|"+my_password+"|"+my_email+"|"+my_phone+"|"+my_address;
					return 1+"|"+result;
				}
			}
			return result;
		}
		catch (Exception e)
		{
			return -1+"";
		}
		finally
		{
			j.close2();
		}
	}
	public static String create_class(String user_id,String password,String class_name,String information,String date)
	{
		if (checkID(user_id,password))
		{
			String sql = "insert into classes (user_id,name,information,date)"+
		                 "values('"+user_id+"','"+class_name+"','"+information+"','"+date+"');";
			JDBConnection j = new JDBConnection();
			j.executeUpdate(sql);
			j.close2();
			sql = "select c.id,u.name from classes c,user u "
				+ "where c.user_id = u.id and u.id = "+user_id+" and "
				+ "c.id not in (select cu.class_id from class_user cu,user u "
				+ "   where cu.user_id = u.id and u.id = "+user_id+");";
			j = new JDBConnection();
			ResultSet r = j.executeQuery(sql);
			String result="";
			try
			{
				while (r.next())
				{
					String class_number = r.getString(1);
					String user_name = r.getString(2);
					join_class(user_id, password, class_number);
					agree_join(user_id, password, class_number, user_id);
					result = class_number+"|"+user_id+"|"+class_name+"|"+information+"|"+date+"|"+user_name;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return -1+"";
			}
			finally
			{
				j.close(r);
			}
			return 1+"|"+result;
		}
		else
		{
			return -1+"";
		}
	}
	public static String serch_class(String user_id,String password,String class_number)
	{
		if (checkID(user_id,password))
		{
			String sql = "select classes.name,user.name from classes,user where classes.user_id = user.id and classes.id="+class_number+";";
			JDBConnection j = new JDBConnection();
			ResultSet r = j.executeQuery(sql);
			String result="";
			try
			{
				if (r.next())
				{
					result = r.getString(1)+"|"+r.getString(2);
					//("name|creater_name"(名称),(创建者名字));
				}
				else
				{
					return -1+"";
				}
			}
			catch (Exception e)
			{
				return -1+"";
			}
			finally
			{
				j.close2();
			}
			return 1+"|"+result;
		}
		else
		{
			return -1+"";
		}
	}
	
	//加入班级    参数(user_id password class_number) 返回值:1(申请成功) -1(申请失败)
	public static int join_class(String user_id,String password,String class_number)
	{
		if (checkID(user_id,password))
		{
			JDBConnection j = new JDBConnection();
			String sql1 = "select * from class_user where user_id = "+
		                   user_id+" and class_id = "+class_number+";";
			ResultSet r = j.executeQuery(sql1);
			try
			{
				if(r.next()) return 2;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				j.close(r);
				return -1;
			}
			sql1 = "select * from class_user_temp where user_id = "+
	                   user_id+" and class_id = "+class_number+";";
		    r = j.executeQuery(sql1);
		    try
		    {
		    	if(r.next()) return 3;
		    }
		    catch (Exception e)
		    {
		    	e.printStackTrace();
		    	j.close(r);
		    	return -1;
		    }
			String sql = "insert into class_user_temp values("+user_id+","+class_number+");";
			j.executeUpdate(sql);
			j.close2();
			return 1;
		}
		else
		{
			return -1;
		}
	}
	//退出班级    参数(user_id password class_number) 返回值:1(退出成功) -1(退出失败)
	public static int exit_class(String user_id,String password,String class_number)
	{
		if (checkID(user_id,password))
		{
			String sql = "delete from class_user where user_id = "+user_id+" and class_id = "+class_number+";";
			JDBConnection j = new JDBConnection();
			j.executeUpdate(sql);
			j.close2();
			return 1;
		}
		else
		{
			return -1;
		}
	}
	//同意加入    参数(user_id password class_number  an_user_id) 返回值:1(同意成功) -1(同意失败)
	public static String agree_join(String user_id,String password,String class_number,String an_user_id)
	{
		if (checkID(user_id,password)&&has_this("class_user_temp","user_id",an_user_id,false))
		{
			String sql ="select name from user where id = "+an_user_id;
			JDBConnection j = new JDBConnection();
			ResultSet r = j.executeQuery(sql);
			String name="";
			try
			{
				if (r.next())
				{
					name = r.getString(1);
					sql = "insert into class_user values("+an_user_id+","+class_number+",'"+name+"');";
					j.executeUpdate(sql);
					sql = "delete from class_user_temp where user_id = "+an_user_id+" and class_id = "+class_number+";";
					j.executeUpdate(sql);
					return 1+"";
				}
				else
				{
					return -1+"";
				}
				
			}
			catch (Exception e)
			{
				return -1+"";
			}
			finally
			{
				j.close2();
			}
		}
		else
		{
			return -1+"";
		}
	}
	//获得班级列表    参数(user_id password) 返回值:1(获取成功) -1(获取失败) "num"(班级数量) "name0"(第一个班级名) "id0"(第一个班级id)...
	public static String get_class_list(String user_id,String password)
	{
		if (checkID(user_id,password))
		{
			String sql = "select classes.name,classes.id from classes,class_user where classes.id = class_user.class_id;";
			JDBConnection j = new JDBConnection();
			int num = 0;
			ResultSet r = j.executeQuery(sql);
			String result="";
			try
			{
				while(r.next())
				{
					result += r.getString(1)+"|";	
					result += r.getInt(2)+"|";
					num++;
				}
				result = num+"|"+result;
				result = result.substring(0, result.length()-1);
			}catch(Exception e)
			{
				return -1+"";
			}
			finally
			{
				j.close2();
			}
			return result;
		}
		else
		{
			return -1+"";
		}
	}
	//获得成员列表    参数(user_id password class_number) 返回值:1(获取成功) -1(获取失败) "num"(成员数量) "name0"(第一个成员名) "phone0"(第一个成员手机号码) "address0"(第一个成员地理位置) "date0"(第一个成员信息更新时间)...
	public static String get_persons_list(String user_id,String password,String class_number)
	{
		if (checkID(user_id,password))
		{
			String sql = "select name,phone,address,date from user,class_user where user.id = class_user.user_id and class_user.class_id = "+class_number+";";
			JDBConnection j = new JDBConnection();
			int num = 0;
			ResultSet r = j.executeQuery(sql);
			String result="";
			try
			{
				while(r.next())
				{
					result += r.getString(1)+"|";
					result += r.getString(2)+"|";
					result += r.getString(3)+"|";
					result += r.getString(4)+"|";
					num++;
				}
				result = num+"|"+result;
				result = result.substring(0, result.length()-1);
			}catch(Exception e)
			{
				System.out.println(sql);
				return -1+""+sql;
			}
			finally
			{
				j.close2();
			}
			return result;
		}
		else
		{
			return -1+"验证失败！";
		}
	}
	
	/*******************************************************************
	 以下尚未完成
	 *******************************************************************/
	
	
	//创建人权限移交    参数(user_id password class_number an_user_id) 返回值:1(移交成功) -1(移交失败)
	public static int founder_hand_over(String user_id,String password,String class_number,String an_user_id)
	{
		if (checkID(user_id,password))
		{
			String sql = "select * from ";
			JDBConnection j = new JDBConnection();
			
			return 1;
		}
		else
		{
			return -1;
		}
	}
	//创建人解散班群    参数(user_id password class_number) 返回值:1(解散成功) -1(解散失败) 
	public static int destory_class(String user_id,String password,String class_number)
	{
		if (checkID(user_id,password))
		{
			String sql = "select * from ";
			JDBConnection j = new JDBConnection();
			
			return 1;
		}
		else
		{
			return -1;
		}
	}
	//添加群名片    参数(user_id password class_number an_name) 返回值:1(添加成功) -1(添加失败)
	public static int add_new_name(String user_id,String password,String class_number,String an_name)
	{
		if (checkID(user_id,password))
		{
			String sql = "update class_user set an_name='"+an_name+"' where class_id = "+class_number;
			JDBConnection j = new JDBConnection();
			j.executeUpdate(sql);
			return 1;
		}
		else
		{
			return -1;
		}
	}
	//获得成员信息    参数(user_id password an_user_id) 返回值:1(获取成功) -1(获取失败) "name"(成员名) "phone"(成员手机号码) "address"(成员地理位置) 
	public static int get_person_information(String user_id,String password,String an_user_id)
	{
		if (checkID(user_id,password))
		{
			String sql = "select * from ";
			JDBConnection j = new JDBConnection();
			
			return 1;
		}
		else
		{
			return -1;
		}
	}
	//修改密码    参数(user_id password new_password) 返回值:1(修改成功) -1(修改失败)
	public static int update_password(String user_id,String password,String new_password)
	{
		if (checkID(user_id,password))
		{
			String sql = "select * from ";
			JDBConnection j = new JDBConnection();
			
			return 1;
		}
		else
		{
			return -1;
		}
	}
	//踢掉某人    参数(user_id password class_number an_user_id) 返回值:1(踢出成功) -1(踢出失败)
	public static int delte_person(String user_id,String password,String class_number,String an_user_id)
	{
		if (checkID(user_id,password))
		{
			String sql = "select * from ";
			JDBConnection j = new JDBConnection();
			
			return 1;
		}
		else
		{
			return -1;
		}
	}
	//获得班级信息    参数(user_id password class_number) 返回值:1(获取成功) -1(获取失败)
	public static int get_class_information(String user_id,String password,String class_number)
	{
		if (checkID(user_id,password))
		{
			String sql = "select * from ";
			JDBConnection j = new JDBConnection();
			
			return 1;
		}
		else
		{
			return -1;
		}
	}
	//修改个人信息    参数(user_id password name) 返回值:1(修改成功) -1(修改失败)
	public static int update_person_information(String user_id,String password,String name)
	{
		if (checkID(user_id,password))
		{
			String sql = "select * from ";
			JDBConnection j = new JDBConnection();
			
			return 1;
		}
		else
		{
			return -1;
		}
	}
	//修改班级信息    参数(user_id password class_number name information) 返回值:1(修改成功) -1(修改失败)                     
	public static int update_class_information(String user_id,String password,String class_number,String name,String information)
	{
		if (checkID(user_id,password))
		{
			String sql = "select * from ";
			JDBConnection j = new JDBConnection();
			
			return 1;
		}
		else
		{
			return -1;
		}
	}
	public static String uploaddate(String id,String password)
	{
		if (!checkID(id,password))
		{
			return -1+"";
		}
		else
		{
			//user
			String sql = "select distinct id,name,email,phone,address,date from user u,class_user cu "
					   + "   where u.id=cu.user_id and "
					   + "         (cu.class_id in "
					   + "              (select class_id from class_user "
					   + "                      where user_id="+id+"))";
			JDBConnection j = new JDBConnection();
			j.executeQuery(sql);
			int num = 0;
			ResultSet r = j.executeQuery(sql);
			String result="";
			String temp="";
			try
			{
				while(r.next())
				{
					result += r.getInt(1)+"|";
					result += r.getString(2)+"|";
					result += r.getString(3)+"|";
					result += r.getString(4)+"|";
					result += r.getString(5)+"|";
					result += r.getString(6)+"|";
					num++;
				}
				result = num + "|" +result;
				temp = result;
			}catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(sql);
			}
			// class 
			sql = "select id,c.user_id,name,information,date "
				+ " from classes c,class_user cu "
				+ " where c.id=cu.class_id and "
				+ " cu.user_id = "+id+";";
			j = new JDBConnection();
			j.executeQuery(sql);
			num = 0;
			r = j.executeQuery(sql);
			result="";
			try
			{
				while(r.next())
				{
					result += r.getInt(1)+"|";
					result += r.getString(2)+"|";
					result += r.getString(3)+"|";
					result += r.getString(4)+"|";
					result += r.getString(5)+"|";
					num++;
				}
				result = num + "|" +result;
				temp = temp+result;
			}catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(sql);
			}
			//class_user
			sql = "select user_id,class_id,an_name from class_user "
				+ " where class_id in "
				+ "  (select class_id from class_user "
				+ "    where user_id = "+id+");";
			j = new JDBConnection();
			j.executeQuery(sql);
			num = 0;
			result="";
			r = j.executeQuery(sql);
			try
			{
				while(r.next())
				{
					result += r.getInt(1)+"|";
					result += r.getString(2)+"|";
					result += r.getString(3)+"|";
					num++;
				}
				result = num + "|" +result;
				result = result.substring(0, result.length()-1);
				temp = temp+result;
				
			}catch (Exception e)
			{
				e.printStackTrace();
				System.out.println(sql);
			}
			finally
			{
				j.close(r);
			}
			return 1+"|"+temp;
		}
	}
	
	public static String updatePhone(String id,String psd,String phone)
	{
		if(checkID(id,psd))
		{
			String sql = "update user set phone = '"+phone+"' where id = "+id+";";
			JDBConnection j = new JDBConnection();
			boolean r = j.executeUpdate(sql);
			j.close2();
			if(r)
				return 1+"";
			else
				return -1+"";
		}
		return -1+"";
	}
	public static String getCreaterId(String class_number)
	{
		String sql = "select user_id from classes where id = "+class_number+";";
		JDBConnection j = new JDBConnection();
		ResultSet r = j.executeQuery(sql);
		try
		{
			if (r.next()) return r.getInt(1)+"";
			else return -1+"";
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1+"";
		}
		finally
		{
			j.close(r);
		}	
	}
	public static String getUserNameById(String user_id)
	{
		String sql = "select name from user where id = "+user_id+";";
		JDBConnection j = new JDBConnection();
		ResultSet r = j.executeQuery(sql);
		try
		{
			if (r.next()) return r.getInt(1)+"";
			else return -1+"";
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1+"";
		}
		finally
		{
			j.close(r);
		}
	}
	public static String getClassNameById(String class_id)
	{
		String sql = "select name from classes where id = "+class_id+";";
		JDBConnection j = new JDBConnection();
		ResultSet r = j.executeQuery(sql);
		try
		{
			if (r.next()) return r.getInt(1)+"";
			else return -1+"";
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1+"";
		}
		finally
		{
			j.close(r);
		}
	}
	public static String getAllWaitMeAgree(String user_id,String password)
	{
		if (!checkID(user_id,password)) return -1+"";
		// 查找本人 群下所有申请的项
		String sql = "select cut.user_id,cut.class_id,u.name "+
				     " from class_user_temp cut,classes c,user u "+
				     " where cut.class_id = c.id and cut.user_id = u.id and c.user_id = "+user_id+";";
		JDBConnection j = new JDBConnection();
		int num=0;
		ResultSet r = j.executeQuery(sql);
		String result = "";
		String temp = "";
		try
		{
			while(r.next())
			{
				String temp_user_id = r.getString(1);
				String temp_class_id = r.getString(2);
				String temp_an_name = r.getString(3);
				num++;
				result = result+temp_user_id+"|"+temp_class_id+"|"+temp_an_name+"|";
			}
			result=num+"|"+result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1+"";
		}
		if (num==0) return 0+"";
		sql = "select * from user u "
			+ " where u.id in "
			+ " (select user_id from class_user_temp) "
			+ " and u.id not in "
			+ " (select cu.user_id from class_user cu "
			+ "    where cu.class_id in "
			+ "       (select cu.class_id from class_user cu "
			+ "           where cu.user_id = 2));";   // 查找所有申请了本人且与本人不同在任意一群的人
		 
		r = j.executeQuery(sql);
		num = 0;
		try
		{
			while(r.next())
			{
				String id = r.getInt(1)+""; 
                String name = r.getString(2);
                String email = r.getString(4);
                String phone = r.getString(5);
                String address  = r.getString(6);
                String date  = r.getString(7);
                num++;
                temp = temp+id+"|"+name+"|"+email+"|"+phone+"|"+address+"|"+date+"|";
			}
			if(temp.length()>0) 
			{
				temp = temp.substring(0, temp.length()-1);
			}
				result=result+num+"|"+temp;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1+"";
		}
		
		return 1+"|"+result;
		
	}
	public static ArrayList<String> getAllFriends(String id)
	{
		ArrayList<String> result = new ArrayList<String>();
		String sql = "select distinct cu.user_id from class_user cu where "
				   + " cu.class_id in (select cu.class_id from class_user cu where"
				   + "                   cu.user_id = "+id+")";
		JDBConnection j = new JDBConnection();
		ResultSet r = j.executeQuery(sql);
		try
		{
			while(r.next())
			{
				result.add(r.getString(1));
			}
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return result;
		}
		finally
		{
			j.close(r);
		}
	}
	public static void main(String[] avg)
	{
		//String s = MyDate.getAllWaitMeAgree("5","993620");
		//System.out.println(s);
		/*SimpleDateFormat formatter = new SimpleDateFormat("yyyy|MM|dd|HH|mm|ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        System.out.println(str);*/
		
		Queue<String> q = new LinkedList<String>();
		System.out.println(q.isEmpty()?"Yes":"No");
		q.offer("hello");
		q.offer("world");
		while(!q.isEmpty())
		{
			System.out.println(q.poll());
		}
		
		
		
		
		
		
		
	}
}
