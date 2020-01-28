package com.demo.kafka.consumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemo {

	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(ConsumerDemo.class.getName());

		String bootstrapServer = "localhost:9092";
		String groupId = "my-group-two";
		String topic = "first_topic";
		
		//Create Consumer Configs
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		
		//Create Consumer
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
		
		//Subscribe Consumers To Our Topic(s)
		consumer.subscribe(Arrays.asList(topic));       //Subscribe to One Topic
		//consumer.subscribe(Arrays.asList("first_list", "second_list"));          //Subscribe To More Topics
		
		//Poll For New Data
		 while(true){
	            ConsumerRecords<String, String> records =
	                    consumer.poll(Duration.ofMillis(100)); // new in Kafka 2.0.0

	            for (ConsumerRecord<String, String> record : records){
	                logger.info("Key: " + record.key() + ", Value: " + record.value());
	                logger.info("Partition: " + record.partition() + ", Offset:" + record.offset());
	            }
	        }

		


	}

}
