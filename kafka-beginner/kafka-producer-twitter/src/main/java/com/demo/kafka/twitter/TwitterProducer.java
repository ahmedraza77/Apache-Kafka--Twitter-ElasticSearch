package com.demo.kafka.twitter;


import com.twitter.hbc.core.Client;

import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TwitterProducer {

	Logger logger = LoggerFactory.getLogger(TwitterClient.class.getName());

	public TwitterProducer() {}

	public static void main(String[] args) {
		new TwitterProducer().run();
	}

	public void run(){
		logger.info("Setup");

		/** Set up blocking queues: Be sure to size these properly based on expected TPS of your stream */
		BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(60);

		// create a twitter client
		Client client = TwitterClient.createTwitterClient(msgQueue);
		// Attempts to establish a connection.
		client.connect();

		// create a kafka producer
		KafkaProducer<String, String> producer = KafkaProducerConfig.createKafkaProducer();

		// add a shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			logger.info("stopping application...");
			logger.info("shutting down client from twitter...");
			client.stop();
			logger.info("closing producer...");
			producer.close();
			logger.info("done!");
		}));

		// loop to send tweets to kafka
		// on a different thread, or multiple different threads....
		while (!client.isDone()) {
			String msg = null;
			try {
				msg = msgQueue.poll(3, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
				client.stop();
			}
			if (msg != null){
				logger.info(msg);

				// create Producer Record & send data
				producer.send(new ProducerRecord<>("twitter_tweets", null, msg), new Callback() {
					@Override
					public void onCompletion(RecordMetadata recordMetadata, Exception e) {
						if (e != null) {
							logger.error("Something bad happened", e);
						}
					}
				});
			}
		}
		logger.info("End of application");
	}

}
