import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitProducer {

    public static final String EXCHANGE_NAME = "exchange_demo";
    public static final String ROUTING_KEY = "routingkey_demo";
    public static final String QUEUE_NAME = "hello";
    public static final String IP_ADDRESS = "localhost";
    public static final int PORT = 5672;

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setUsername("root");
        factory.setPassword("root123");
        Connection connection = factory.newConnection();
        Channel channel1 = connection.createChannel();

        channel1.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
        // Specify a queue
        channel1.queueDeclare(QUEUE_NAME,false,false, false, null);
        channel1.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        String message = "Hello World!!!";
        channel1.basicPublish(EXCHANGE_NAME,ROUTING_KEY,MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
        System.out.println(" [x] Sent '"+ message +"'");

        channel1.close();
        connection.close();
    }
}
