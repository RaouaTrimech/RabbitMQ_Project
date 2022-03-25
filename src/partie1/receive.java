package partie1;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory ; 
import com.rabbitmq.client.DeliverCallback ;

// JSON librairies
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class receive {
	private final static String QUEUE_NAME = "hello" ;
	private static JSONParser parser = new JSONParser();
	
	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection(); 
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME,false,false, false,null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		
		DeliverCallback deliverCallback = (consumerTag , delivery) -> {
			String message = new String(delivery.getBody(),"UTF-8") ;
			try {
				JSONObject obj = (JSONObject) parser.parse(message);
				System.out.println(" [x] Received '"+ obj.toJSONString()+"'");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		};
			
			channel.basicConsume(QUEUE_NAME,true, deliverCallback, consumerTag ->{});
			
		
	}
	}