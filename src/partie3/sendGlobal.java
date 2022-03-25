package partie3;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory ; 
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
public class sendGlobal {
	private final static String QUEUE_NAME = "textGlobal" ;
	
	
	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try(Connection connection = factory.newConnection(); 
			Channel channel = connection.createChannel()) 
		{
			channel.queueDeclare(QUEUE_NAME,false,false, false,null);
			JSONObject obj = new JSONObject();
		      obj.put("Text", "message");
		      obj.put("Section",1);
			
			channel.basicPublish("",QUEUE_NAME,null,obj.toJSONString().getBytes());
			System.out.println(" [x] Sent Globaly '"+ obj.toJSONString()+"'");
		}
	}

	private static Object String(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}