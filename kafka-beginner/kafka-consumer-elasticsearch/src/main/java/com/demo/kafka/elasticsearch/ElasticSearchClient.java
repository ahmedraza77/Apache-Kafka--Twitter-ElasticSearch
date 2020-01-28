package com.demo.kafka.elasticsearch;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;




public class ElasticSearchClient {
	    public static RestHighLevelClient createClient(){

	        //////////////////////////
	        /////////// IF YOU USE LOCAL ELASTICSEARCH
	        //////////////////////////

	        //  String hostname = "localhost";
	        //  RestClientBuilder builder = RestClient.builder(new HttpHost(hostname,9200,"http"));

	        //////////////////////////
	        /////////// IF YOU USE BONSAI / HOSTED ELASTICSEARCH
	        //////////////////////////
	    	
	        // replace with your own credentials
	        String hostname = "kafka-demo-poc-150789910.ap-southeast-2.bonsaisearch.net"; // localhost or bonsai url
	        String username = "sgajklnpm7"; // needed only for bonsai
	        String password = "dhefgdztst"; // needed only for bonsai

	        // credentials provider help supply username and password
	        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	        credentialsProvider.setCredentials(AuthScope.ANY,
	                new UsernamePasswordCredentials(username, password));

	        RestClientBuilder builder = RestClient.builder(
	                new HttpHost(hostname, 443, "https"))
	                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
	                    @Override
	                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
	                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
	                    }
	                });

	        RestHighLevelClient client = new RestHighLevelClient(builder);
	        return client;
	    }

}
