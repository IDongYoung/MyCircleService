package Server;
 
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
 
public class MyServer
 {
    private static final int PORT = 8888;
   
    public static void main(String[] args) 
    { 
    	IoAcceptor acceptor = new NioSocketAcceptor(); //socket接收器
        acceptor.getFilterChain().addLast("logger",new LoggingFilter()); //添加日志记录
        acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));  //添加编码解码器
        MyHandler t = new MyHandler();
        acceptor.setHandler(t); //添加处理器(用于接收数据后处理处理数据逻辑)
        acceptor.getSessionConfig().setReadBufferSize(2048 ); //设置读取数据缓存单位byte
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10 ); //设置多长时间后接收器开始空闲
        try {
            acceptor.bind(new InetSocketAddress(PORT)); //绑定某个端口，作为数据入口
            //acceptor.dispose(true);  
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}
