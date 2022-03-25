package partie3;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class redirectSections {

	private final static String QUEUE_NAME = "TextGlobal" ;

	private static JSONParser parser = new JSONParser();
	
	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection(); 
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME,false,false, false,null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		
		com.rabbitmq.client.DeliverCallback deliverCallback = (consumerTag , delivery) -> {
			String message = new String(delivery.getBody(),"UTF-8") ;
			try {
				JSONObject obj = (JSONObject) parser.parse(message);
				//System.out.println(obj);
				//System.out.println(obj.get("Section") instanceof Object);
				System.out.println(" [x] Received to transfer'"+ obj.toJSONString()+"'");
				String section = obj.get("Section").toString();
				System.out.println(section instanceof String);
				if (section.equals("1")) {
					System.out.println("hello world");
					sendMsg_Queue("MQ1",obj);
				}
				else {
					sendMsg_Queue("MQ2",obj);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		};
		channel.basicConsume(QUEUE_NAME,true,deliverCallback, consumerTag ->{});
			
	}

	static void sendMsg_Queue(String QueueName, JSONObject SentObj) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try(Connection connection = factory.newConnection(); 
			Channel channel = connection.createChannel()) 
		{
			channel.queueDeclare(QueueName,false,false, false,null);
		      
			channel.basicPublish("",QueueName,null,SentObj.toJSONString().getBytes());
			System.out.println(" [x] Sent '"+ SentObj.toJSONString()+"' to "+QueueName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	}