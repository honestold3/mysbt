package org.wq.kafka;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafkaProducer {
	public static void main(String[] args) throws Exception{
		Properties props = new Properties();
		//
		//props.put("metadata.broker.list", "192.168.1.170:9092,192.168.1.171:9092,192.168.1.172:9092");
        props.put("metadata.broker.list", "localhost:9092");
		//
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("zookeeper.connection.timeout.ms", 999999999);
		ProducerConfig config = new ProducerConfig(props);
		//
		Producer<String, String> producer = new Producer<String, String>(config);
		
		//KeyedMessage<String, String> message = new KeyedMessage<String, String>("test", "hello you");
		for (int i = 0; i < 10; i++) {
			//
			KeyedMessage<String, String> message = new KeyedMessage<String, String>("kankan", (i%2)+"", i+"");
			//
			producer.send(message);
		}
		producer.close();
	}
}
