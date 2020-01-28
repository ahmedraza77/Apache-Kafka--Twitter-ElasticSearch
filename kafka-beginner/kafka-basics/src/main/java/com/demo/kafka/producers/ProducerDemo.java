package com.demo.kafka.producers;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerDemo {
	public static void main(String[] args) {
		String bootstrapServer = "localhost:9092";
		
		//Create Producer Protperties
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		
		//Create Producer
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
		
		//Create Producer Record
		ProducerRecord<String, String> record = new ProducerRecord<String, String>("first_topic", "hello world");
		
		//Send Data -asynchronous (cos of this data is never send, program exits)
		producer.send(record);
		
		//flush data     //to wait for the data to be produced
		producer.flush();
		
		//flush and close
		producer.close();
	}

}
