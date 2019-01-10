package playground.client;

import org.springframework.web.client.RestTemplate;

import playground.constants.Playground;
import playground.constants.User;
import playground.layout.UserTO;

public class ClientModel {

	private static String current_userPlayground;
	private static String current_email;
	private RestTemplate restTemplate;
	private String host;
	private int port;

	
	public ClientModel(String host,int port) {
		this.restTemplate = new RestTemplate();
		this.host = host;
		this.port = port;
	}
	
	
	/**
	public static void SignIn(String userPlayground, String email) {
		if(!(userPlayground.equals("") || email.equals("")))
		{

			UserTO user = this.restTemplate.getForObject(this.url + Playground.Function_3, UserTO.class, userPlayground, email);
		}
	}
	*/
	
	
	public String getURL()
	{
		return "http://" + host + ":" + port;
	}

	public static String getCurrent_userPlayground() {
		return current_userPlayground;
	}

	public static void setCurrent_userPlayground(String current_userPlayground) {
		ClientModel.current_userPlayground = current_userPlayground;
	}

	public static String getCurrent_email() {
		return current_email;
	}

	public static void setCurrent_email(String current_email) {
		ClientModel.current_email = current_email;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	

	
}
