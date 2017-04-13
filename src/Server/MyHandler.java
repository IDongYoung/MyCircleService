package Server;
 
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import myuntil.myMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Queue;
 
public class MyHandler extends IoHandlerAdapter
{

	public HashMap<String,IoSession> AllSession = new HashMap<String,IoSession>();
    public HashMap<String,Queue> MsgQue = new HashMap<String,Queue>();
    //捕获异常
    @Override
    public void exceptionCaught(IoSession session, Throwable cause ) throws Exception
    {
        cause.printStackTrace();
    }
    //消息接收
    @Override
    public void messageReceived(IoSession session, Object message ) throws Exception
    {
        String str = message.toString();
        System.out.println("得到消息："+str);
        myMessage m = new myMessage(str);
        String[] mymessage = m.decodeMessage();
        AllSession.put("test", session);
        if(mymessage[0].equals("0")) // 请求连接服务器
        {
        	AllSession.put(mymessage[1], session); // 以id为键值 存储session
        }
        else if(mymessage[0].equals("1")) // 注册
        {
        	IoSession s = session;
        	server myserver = new server();
        	String name = mymessage[1];    // 姓名
        	String email = mymessage[2];   // 邮箱
        	String phone = mymessage[3];   // 电话
        	String address = mymessage[4]; // 地址
        	String date = mymessage[5];    // 日期
        	String r = myserver.regist(name, email, phone, address, date);
        	s.write(r);
        }
        else if(mymessage[0].equals("2")) // 登陆
        {
        	IoSession s = session;
        	server myserver = new server();
        	String email = mymessage[1];     // 邮箱
        	String password = mymessage[2];  // 密码
        	String r = myserver.login(email, password);
        	String[] h = r.split("\\|");
        	if (h.length>2)
        		AllSession.put(h[2],session);
        	s.write(r);
        	
        }
        else if(mymessage[0].equals("3")) // 创建班级
        {
        	IoSession s = session;
        	server myserver = new server();
        	String user_id = mymessage[1]; 
        	String password = mymessage[2]; 
        	String class_name = mymessage[3]; 
        	String information = mymessage[4]; 
        	String date = mymessage[5];
        	String r = myserver.create_class(user_id, password, class_name, information, date);
        	s.write(r);
        }
        else if(mymessage[0].equals("4")) // 搜索班级
        {
        	IoSession s = session;
        	server myserver = new server();
        	String user_id = mymessage[1]; 
        	String password = mymessage[2]; 
        	String class_number = mymessage[3];
        	String r = myserver.serch_class(user_id, password, class_number);
        	s.write(r);
        }
        else if(mymessage[0].equals("5")) // 加入班级
        {
        	IoSession s = session;
        	server myserver = new server();
        	String user_id = mymessage[1];
        	String password = mymessage[2]; 
        	String class_number = mymessage[3];
        	String r = myserver.join_class(user_id, password, class_number);
        	s.write(r);
        	String[] R = r.split("\\|");
        	if (R[1].equals("1"))
        	{
        		String id = myserver.getCreaterId(class_number);
            	String user_name = myserver.getUserNameById(user_id);
            	String class_name = myserver.getClassNameById(class_number);
            	System.out.println("开始给发送方"+id+"消息");
            	String msg = 23+"|"+user_id+"|"+user_name+"|"+class_number+"|"+class_name;
            	reposed(id,msg);
            	System.out.println("成功给发送方消息");
        	}
        }
        else if(mymessage[0].equals("6")) // 退出班级
        {
        	IoSession s = session;
        	server myserver = new server();
        	String user_id = mymessage[1]; 
        	String password = mymessage[2]; 
        	String class_number = mymessage[3];
        	String r = myserver.exit_class(user_id, password, class_number);
        	s.write(r);
        }
        else if(mymessage[0].equals("7")) // 同意加入
        {
        	IoSession s = session;
        	server myserver = new server();
        	String user_id = mymessage[1]; 
        	String password = mymessage[2];
        	String class_number = mymessage[3]; 
        	String an_user_id = mymessage[4];
        	String r = myserver.agree_join(user_id, password, class_number, an_user_id);
        	s.write(r);
        	String[] R = r.split("\\|");
        	if (R[1].equals("1"))
        	{
        		System.out.println("开始给申请方"+an_user_id+"消息");
            	String msg = 25+"|"+class_number;
            	reposed(an_user_id,msg);
            	System.out.println("成功给申请方消息");
        	}
        }
        else if(mymessage[0].equals("8")) // 获得班级列表
        {
        	IoSession s = session;
        	server myserver = new server();
        	String user_id = mymessage[1]; 
        	String password = mymessage[2];
        	String r = myserver.get_class_list(user_id, password);
        	s.write(r);
        }
        else if(mymessage[0].equals("9")) // 获得成员列表
        {
        	IoSession s = session;
        	server myserver = new server();
        	String user_id = mymessage[1];  
        	String password = mymessage[2]; 
        	String class_number = mymessage[3];
        	String r = myserver.get_persons_list(user_id, password, class_number);
        	s.write(r);
        }
        else if(mymessage[0].equals("10")) // 创建人权限移交
        {
        	IoSession s = session;
        	server myserver = new server();
        	String user_id = mymessage[1]; 
        	String password = mymessage[2]; 
        	String class_number = mymessage[3]; 
        	String an_user_id = mymessage[4];
        	String r = myserver.founder_hand_over(user_id, password, class_number, an_user_id);
        	s.write(r);
        }
        else if(mymessage[0].equals("11")) // 创建人解散班群
        {
        	IoSession s = session;
        	server myserver = new server();
        	String user_id = mymessage[1]; 
        	String password = mymessage[2]; 
        	String class_number = mymessage[3];
        	String r = myserver.destory_class(user_id, password, class_number);
        	s.write(r);
        }
        else if(mymessage[0].equals("12")) // 添加群名片
        {
        	IoSession s = session;
        	server myserver = new server();
        	String user_id = mymessage[1]; 
        	String password = mymessage[2]; 
        	String class_number = mymessage[3]; 
        	String an_name = mymessage[4];
        	String r = myserver.add_new_name(user_id, password, class_number, an_name);
        	s.write(r);
        }
        else if(mymessage[0].equals("13")) // 获得成员信息
        {
        	IoSession s = session;
        	server myserver = new server();
        	String user_id = mymessage[1];
        	String password = mymessage[2]; 
        	String an_user_id = mymessage[3];
        	String r = myserver.get_person_information(user_id, password, an_user_id);
        	s.write(r);
        }
        else if(mymessage[0].equals("14")) // 修改密码
        {
        	IoSession s = session;
        	server myserver = new server();
        	String user_id = mymessage[1];
        	String password = mymessage[2]; 
        	String new_password = mymessage[3];
        	String r = myserver.update_password(user_id, password, new_password);
        	s.write(r);
        }
        else if(mymessage[0].equals("15")) // 踢掉某人
        {
        	IoSession s = session;
        	server myserver = new server();
        	String user_id = mymessage[1]; 
        	String password = mymessage[2]; 
        	String class_number = mymessage[3]; 
        	String an_user_id = mymessage[4];
        	String r = myserver.delte_person(user_id, password, class_number, an_user_id);
        	s.write(r);
        }
        else if(mymessage[0].equals("16")) // 获得班级信息
        {
        	IoSession s = session;
        	server myserver = new server();
        	String user_id = mymessage[1]; 
        	String password = mymessage[2]; 
        	String class_number = mymessage[3];
        	String r = myserver.get_class_information(user_id, password, class_number);
        	s.write(r);
        }
        else if(mymessage[0].equals("17")) // 修改个人信息
        {
        	IoSession s = session;
        	server myserver = new server();
        	String user_id = mymessage[1]; 
        	String password = mymessage[2]; 
        	String name = mymessage[3];
        	String r = myserver.update_person_information(user_id, password, name);
        	s.write(r);
        }
        else if(mymessage[0].equals("18")) // 修改班级信息
        {
        	IoSession s = session;
        	server myserver = new server();
        	String user_id = mymessage[1]; 
        	String password = mymessage[2]; 
        	String class_number = mymessage[3]; 
        	String name = mymessage[4]; 
        	String information = mymessage[5];
        	String r = myserver.update_class_information(user_id, password, class_number, name, information);
        	s.write(r);
        }
        else if(mymessage[0].equals("19")) // 加载信息
        {
        	IoSession s = session;
        	server myserver = new server();
        	String id = mymessage[1]; 
        	String password = mymessage[2]; 
        	String r = myserver.uploaddata(id, password);
        	s.write(r);
        }
        else if(mymessage[0].equals("20")) // 更新号码
        {
        	IoSession s = session;
        	server myserver = new server();
        	String id = mymessage[1]; 
        	String password = mymessage[2];
        	String phone = mymessage[3];
        	String r = myserver.updatephone(id, password, phone);
        	s.write(r);
        	String[] R = r.split("\\|");
        	if (R[1].equals("1"))
        	{
        		ArrayList<String> AL = myserver.getAllFriends(id);
            	new Thread(new Runnable()
            	{
            		@Override
    				public void run() 
            		{
            			String msg = "26|"+id+"|"+phone;
    					for(String f_id : AL)
    					{
    						reposed(f_id,msg);
    					}
    				}
            	}).start();
        	}	
        }
        else if (mymessage[0].equals("21")) //  lianjie  21|myid
        {
        	String id = mymessage[1];
        	AllSession.put(id, session);
        	
        	new Thread(new Runnable()
        	{
        		@Override
				public void run() 
        		{
        			Queue<String> temp = MsgQue.get(id);
        			IoSession s = AllSession.get(id);
                	if(temp!=null&&!temp.isEmpty())
                	{
                		while(!temp.isEmpty())
                		{
                			String msg = temp.poll();
                			s.write(msg);
                		}
                	}	
				}
        		
        	 }).start();;
        }
        else if (mymessage[0].equals("22"))  // faxiaoxi 22|myid|oid|mm
        {
        	String oid = mymessage[2];
        	IoSession my = AllSession.get(oid);
        	if(my != null)
        	{
        		my.write(mymessage[1]+mymessage[3]);
        	}
        	else
        	{
        		session.write("fail!");
        	}
        }
        else if (mymessage[0].equals("24"))  // faxiaoxi 22|myid|oid|mm
        {
        	IoSession s = session;
        	server myserver = new server();
        	String id = mymessage[1]; 
        	String password = mymessage[2];
        	String r = myserver.updateWaitMe(id, password);
        	s.write(r);
        }
        else 
        {
        	IoSession s = session;
        	s.write("hello 其他");
        }
    }
    public void reposed(String id,String msg)
    {
    	IoSession s = AllSession.get(id);
    	if(s!=null)
    	{
    		s.write(msg);
    	}
    	else
    	{
    		Queue<String> que = MsgQue.get(id);
    		if(que==null)
    		{
    			Queue<String> q = new LinkedList<String>();
    			q.offer(msg);
    			MsgQue.put(id, q);
    		}
    		else
    		{
    			que.offer(msg);
    		}
    	}
    }
    public void test(IoSession session)
    {
        Collection<IoSession> sessions = session.getService().getManagedSessions().values();

        for (IoSession s : sessions) 
        {
           System.out.println(s.getId());
        }
    }
    
    //会话空闲
    @Override
    public void sessionIdle(IoSession session, IdleStatus status ) throws Exception
    {
        System.out.println("IDLE" + session.getIdleCount(status));
    }
}
