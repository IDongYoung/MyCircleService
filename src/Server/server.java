package Server;

import java.util.ArrayList;

import database.MyDate;
public class server {
	
	//注册   参数(name email phone address date) 返回值:1(注册成功),2(邮箱已经被注册),3(发送邮件失败)
	public String regist(String name,String email,String phone,String address,String date)
	{
		System.out.println("go to server_regist");
		if (MyDate.has_this("user", "email", email, true)) return 1+"|"+2;
		else
		{
			int x=0;
	    	while (true)
	    	{
	    		x=(int)(Math.random()*1000000);
	    		if (x>100000&&x<1000000) break;
	    	}
			if(SendEmail.send(email, x+""))
			{
				MyDate.add_user(name, email, x+"",phone, address, date);
				return 1+"|"+1;
			}
			else return 1+"|"+3;
		}
	}
	
	//登陆  参数(email password) 返回值:1(成功),-1（失败）
	public String login(String email,String password)
	{
		return 2+"|"+MyDate.login(email, password);
	}
	//创建班级    参数(user_id password class_name information date)返回值:1(创建成功) -1(创建失败) 
	public String create_class(String user_id,String password,String class_name,String information,String date)
	{
		return 3+"|"+MyDate.create_class(user_id, password, class_name, information,date);
	}
	//搜索班级    参数(user_id password class_number) 返回值:-1（不存在）("name|the_value|creater_name|the_value"(名称),(创建者名字));
	public String serch_class(String user_id,String password,String class_number)
	{
		return 4+"|"+MyDate.serch_class(user_id, password, class_number);
	}
	//加入班级    参数(user_id password class_number) 返回值:1(申请成功) -1(申请失败)
	public String join_class(String user_id,String password,String class_number)
	{
		return 5+"|"+MyDate.join_class(user_id, password, class_number);
	}
	//退出班级    参数(user_id password class_number) 返回值:1(退出成功) -1(退出失败)
	public String exit_class(String user_id,String password,String class_number)
	{
		return 6+"|"+MyDate.exit_class(user_id, password, class_number);
	}		
	//同意加入    参数(user_id password class_number  an_user_id) 返回值:1(同意成功) -1(同意失败)
	public String agree_join(String user_id,String password,String class_number,String an_user_id)
	{
		return 7+"|"+MyDate.agree_join(user_id, password, class_number, an_user_id);
	}		
	//获得班级列表    参数(user_id password) 返回值:1(获取成功) -1(获取失败) "num"(班级数量) "name0"(第一个班级名) "id0"(第一个班级id)...
	public String get_class_list(String user_id,String password)
	{
		return 8+"|"+MyDate.get_class_list(user_id, password);
	}	
	//获得成员列表    参数(user_id password class_number) 返回值:1(获取成功) -1(获取失败) "num"(成员数量) "name0"(第一个成员名) "phone0"(第一个成员手机号码) "address0"(第一个成员地理位置) "date0"(第一个成员信息更新时间)...
	public String get_persons_list(String user_id,String password,String class_number)
	{
		return 9+"|"+MyDate.get_persons_list(user_id, password, class_number);
	}	
	//创建人权限移交    参数(user_id password class_number an_user_id) 返回值:1(移交成功) -1(移交失败)
	public String founder_hand_over(String user_id,String password,String class_number,String an_user_id)
	{
		return 10+"|"+MyDate.founder_hand_over(user_id, password, class_number, an_user_id);
	}		
	//创建人解散班群    参数(user_id password class_number) 返回值:1(解散成功) -1(解散失败) 
	public String destory_class(String user_id,String password,String class_number)
	{
		return 11+"|"+MyDate.destory_class(user_id, password, class_number);
	}	
	//添加群名片    参数(user_id password class_number an_name) 返回值:1(添加成功) -1(添加失败)
	public String add_new_name(String user_id,String password,String class_number,String an_name)
	{
		return 12+"|"+MyDate.add_new_name(user_id, password, class_number, an_name);
	}		
	//获得成员信息    参数(user_id password an_user_id) 返回值:1(获取成功) -1(获取失败) "name"(成员名) "phone"(成员手机号码) "address"(成员地理位置) 
	public String get_person_information(String user_id,String password,String an_user_id)
	{
		return 13+"|"+MyDate.get_person_information(user_id, password, an_user_id);
	}
	//修改密码    参数(user_id password new_password) 返回值:1(修改成功) -1(修改失败)
	public String update_password(String user_id,String password,String new_password)
	{
		return 14+"|"+MyDate.update_password(user_id, password, new_password);
	}
	//踢掉某人    参数(user_id password class_number an_user_id) 返回值:1(踢出成功) -1(踢出失败)
	public String delte_person(String user_id,String password,String class_number,String an_user_id)
	{
		return 15+"|"+MyDate.delte_person(user_id, password, class_number, an_user_id);
	}
	//获得班级信息    参数(user_id password class_number) 返回值:1(获取成功) -1(获取失败)
	public String get_class_information(String user_id,String password,String class_number)
	{
		return 16+"|"+MyDate.get_class_information(user_id, password, class_number);
	}
	//修改个人信息    参数(user_id password name) 返回值:1(修改成功) -1(修改失败)
	public String update_person_information(String user_id,String password,String name)
	{
		return 17+"|"+MyDate.update_person_information(user_id, password, name);
	}
	//修改班级信息    参数(user_id password class_number name information) 返回值:1(修改成功) -1(修改失败)                     
	public String update_class_information(String user_id,String password,String class_number,String name,String information)
	{
		return 18+"|"+MyDate.update_class_information(user_id, password, class_number, name, information);
	}
	public String uploaddata(String id,String password)
	{
		return 19+"|"+MyDate.uploaddate(id, password);
	}
	public String updatephone(String id,String password,String phone)
	{
		return 20+"|"+MyDate.updatePhone(id, password, phone);
	}
	public String getCreaterId(String class_number)
	{
		return MyDate.getCreaterId(class_number);
	}
	public String getUserNameById(String user_id)
	{
		return MyDate.getUserNameById(user_id);
	}
	public String getClassNameById(String class_id)
	{
		return MyDate.getClassNameById(class_id);
	}
	public String updateWaitMe(String id,String password)
	{
		return 24+"|"+MyDate.getAllWaitMeAgree(id, password);
	}
	public ArrayList<String> getAllFriends(String id)
	{
		return MyDate.getAllFriends(id);
	}
}
