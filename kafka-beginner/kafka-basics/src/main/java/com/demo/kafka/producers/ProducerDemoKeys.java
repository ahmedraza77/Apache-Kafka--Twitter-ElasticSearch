package com.demo.kafka.producers;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProducerDemoKeys {
	static Logger logger = LoggerFactory.getLogger(ProducerDemoKeys.class);
	
	public static void main(String[] args) {
		String bootstrapServer = "localhost:9092";

		//Create Producer Protperties
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		//Create Producer
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

		for(int i=0; i<10; i++) {
			String topic = "first_topic";
			String value = "hello world " + Integer.toString(i);
			String key = "id_" + Integer.toString(i);
			
			//Create Producer Record
			ProducerRecord<String, String> record = 
					new ProducerRecord<String, String>(topic, key, value);

			logger.info("key: " + key);
			
			//Send Data -asynchronous (cos of this data is never send, program exits)
			producer.send(record, new Callback() {		
				public void onCompletion(RecordMetadata metadata, Exception exception) {
					// execute every time a record is successfully send or exception thrown
					if(exception==null) {
						//Record was successfully send
						logger.info("Recieved new metaData: \n"+ 
								"Topic: "+metadata.topic() + "\n" +
								"Partition: "+ metadata.partition() + "\n" +
								"Offsets: " + metadata.offset() + "\n" +
								"Timestamp: " + metadata.timestamp());
					}
					else {
						//handle exception
						logger.error("Error while producing data: ", exception);
					}
				}
			});

		}
		//flush data     //to wait for the data to be produced
		producer.flush();

		//flush and close
		producer.close();
	}

}
