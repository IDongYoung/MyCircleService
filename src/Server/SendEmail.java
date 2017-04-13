package Server;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 
 * @author Qixuan.Chen
 */
public class SendEmail {

    public static final String HOST = "smtp.163.com";
    public static final String PROTOCOL = "SMTP";   
    public static final int PORT = 25;
    public static final String FROM = "lidongyang1316@163.com";//�����˵�email
    public static final String PWD = "LIdongyang1316";//����������
    /**
     * ��ȡSession
     * @return
     */
    private static Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);//���÷�������ַ
        props.put("mail.store.protocol" , PROTOCOL);//����Э��
        props.put("mail.smtp.port", PORT);//���ö˿�
        props.put("mail.smtp.auth" , true);

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PWD);
            }

        };
        Session session = Session.getDefaultInstance(props , authenticator);

        return session;
    }

    public static boolean send(String toEmail , String content) {
    	
        Session session = getSession();
        try {
            Message msg = new MimeMessage(session);          
            msg.setFrom(new InternetAddress(FROM));
            InternetAddress[] address = {new InternetAddress(toEmail)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject("注册成功：");
            msg.setSentDate(new Date());
            msg.setContent("您的密码为："+content +"为保证安全，请尽快修改！", "text/html;charset=utf-8");
            Transport.send(msg);
        }
        catch (MessagingException mex) {
            mex.printStackTrace();
            System.out.println("wrong!!");
            return false;
        }
        return true;
    }
    
    public static void main(String[] avg)
    {
    	System.out.println("hello!");
    	SendEmail.send("417020264@qq.com", "hello world!");
    	System.out.println("Over!");
    }
}