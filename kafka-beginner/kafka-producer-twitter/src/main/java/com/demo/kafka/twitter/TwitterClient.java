package com.demo.kafka.twitter;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;


import java.util.List;
import java.util.concurrent.BlockingQueue;


public class TwitterClient {
	
	public static Client createTwitterClient(BlockingQueue<String> msgQueue){
		
		// use your credentials 
		String consumerKey = "JNmeomCvwEgHe0XXo16zZ3Psz";
		String consumerSecret = "hoK4blM3OMPnNuZ1xlnnM8kP1TrYZPL8R0v840SKei1ZFTycvE";
		String token = "2274362136-AL6ldGoPejPRcEw21d2mgeRmFR8qrWUgZT7gLqn";
		String secret = "68PE8ulF4BH7qFzqdkGqxDtqU3NGhmYQdrG3ifXxvbswX";
		
		// terms to look for in twitter feed
		List<String> terms = Lists.newArrayList("bitcoin", "usa", "politics", "sport", "soccer");

		/** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
		Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
		StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

		hosebirdEndpoint.trackTerms(terms);

		// These secrets should be read from a config file
		Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, token, secret);

		ClientBuilder builder = new ClientBuilder()
				.name("Hosebird-Client-01")                              // optional: mainly for the logs
				.hosts(hosebirdHosts)
				.authentication(hosebirdAuth)
				.endpoint(hosebirdEndpoint)
				.processor(new StringDelimitedProcessor(msgQueue));

		Client hosebirdClient = builder.build();
		return hosebirdClient;
	}
}