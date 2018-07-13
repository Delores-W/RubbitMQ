import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RabbitConsumer {

    public static final String QUEUE_NAME = "hello";
    public static final String IP_ADDRESS = "localhost";
    public static final int PORT = 5672;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        Address[] addresses = new Address[] {
          new Address(IP_ADDRESS,PORT)
        };

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setUsername("root");
        factory.setPassword("root123");

        Connection connection = factory.newConnection(addresses);
        final Channel channel = connection.createChannel();
//        channel.queueDeclare(QUEUE_NAME,false,false, false, null);
        channel.basicQos(64);
        System.out.println(" [*] Waitting for messages.");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("[x] Received '" + message + "'");

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
        TimeUnit.SECONDS.sleep(5);
        channel.close();
        connection.close();
    }
}
