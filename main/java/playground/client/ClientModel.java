package playground.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import org.springframework.web.client.RestTemplate;

import playground.constants.Activity;
import playground.constants.Client;
import playground.constants.Element;
import playground.constants.Playground;
import playground.constants.User;
import playground.layout.ActivityTO;
import playground.layout.ElementTO;
import playground.layout.UserTO;
import playground.logic.ActivityEntity;
import playground.logic.ElementEntity;
import playground.logic.NewUserForm;
import playground.logic.UserEntity;

public class ClientModel {

	private String current_userPlayground;
	private String current_email;
	private UserEntity current_user;
	private RestTemplate restTemplate;
	private String host;
	private int port;

	public ClientModel(String host, int port) {
		this.restTemplate = new RestTemplate();
		this.host = host;
		this.port = port;
	}

	public boolean SignIn(String email) {
		if (!email.equals("")) {
			try {
				UserTO user = this.restTemplate.getForObject(this.getURL() + Playground.Function_3, UserTO.class,
						Client.PLAYGROUND_NAME, email);
				setCurrentUser(user.toEntity());
				 
			} catch (Exception e) {
				
				return false;
			}
			return true;
		}
		return false;
	}

	private void refreshUser()
	{
		try {
			UserTO user = this.restTemplate.getForObject(this.getURL() + Playground.Function_3, UserTO.class,
					this.current_userPlayground, this.current_email);
			setCurrentUser(user.toEntity());
			 
		} catch (Exception e) {
			
			System.err.println(e.getMessage());
		}
	}
	

	public boolean signUp(String username, String email, String avatar, String playground, String role) {
		try {
			NewUserForm form = new NewUserForm(email, username, avatar, role);
			UserTO user = this.restTemplate.postForObject(this.getURL() + Playground.Function_1, form, UserTO.class);
			setCurrentUser(user.toEntity());
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	
	public void setCurrentUser(UserEntity entity) {
		this.current_user = entity;
		this.current_email = entity.getEmail();
		this.current_userPlayground = entity.getPlayground();

	}

	public boolean verifyUser(String code, String mail) {
		try {
			UserTO user = this.restTemplate.getForObject(this.getURL() + Playground.Function_2, UserTO.class, Client.PLAYGROUND_NAME, mail, code);
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}

	public boolean updateAccount(String username, String avatar, String role) {
		UserEntity user = this.getCurrentUser();
		user.setUsername(username);
		user.setAvatar(avatar);
		user.setRole(role);
		try {
			this.restTemplate.put(this.getURL() + Playground.Function_4, new UserTO(user), Client.PLAYGROUND_NAME, this.current_email);
			this.current_user = user;
			return true;
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			return false;
		}
	}

	public String getGameRules() {
	
		try {

			ActivityEntity ent = new ActivityEntity();
			ent.setType(Activity.GET_GAME_RULES_ACTIVITY);
			ActivityTO act = new ActivityTO(ent);
			String rules = this.restTemplate.postForObject(this.getURL() + Playground.Function_11, act, String.class,
					this.current_userPlayground, this.current_email);
			return rules;
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			JOptionPane.showMessageDialog(null, Client.CONNECTION_PROBLEM);
		}
		return null;
	}

	public boolean addQuestion(String question_title, String question_body, String _question_answer, String points) {
		try {
			int num = Integer.parseInt(points);
			ElementEntity question = createQuestionElement(question_title,question_body,_question_answer,num, 0 ,0);
			
			ElementTO q = new ElementTO(question);
			
			q = this.restTemplate.postForObject(this.getURL() + Playground.Function_5, q, ElementTO.class,
					this.current_userPlayground, this.current_email);
			question = q.toEntity();
			JOptionPane.showMessageDialog(null, Client.SUCCESSFULLY_ADDED_QUESTION + "\n" + question.getName());
			return true;
		}
		catch(NumberFormatException e)
		{
			JOptionPane.showMessageDialog(null, Client.INVALID_POINT_INPUT_ERROR);
		}
		return false;
	}
	public ElementEntity createQuestionElement(String questionTitle, String questionBody, String answer, int points,
			double x, double y) {
		ElementEntity question = new ElementEntity(questionTitle, x, y);
		question.setType(Element.ELEMENT_QUESTION_TYPE);
		question.getAttributes().put(Element.ELEMENT_QUESTION_KEY, questionBody);
		question.getAttributes().put(Element.ELEMENT_ANSWER_KEY, answer);
		question.getAttributes().put(Element.ELEMENT_POINT_KEY, points);
		return question;
	}

	public ElementEntity[] getQuestions(int page, int size) {
		Map<String, String> questions = new HashMap<String, String>();
		ArrayList<ElementEntity> list = new ArrayList();
		ElementTO[] questionsTO=null;
		try {

			questionsTO = restTemplate.getForObject(
					this.getURL() + Playground.Function_10
							+ createPaginationStringAppendixForUrl(page, size),
					ElementTO[].class, this.current_userPlayground, this.current_email, Element.TYPE_FIELD,
					Element.ELEMENT_QUESTION_TYPE);
			System.err.println("Found " + questionsTO.length + " questions in database");
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
		
		for(ElementTO e : questionsTO)
		{
			ElementEntity q = e.toEntity();
			list.add(q);
			
		}
		
		return list.toArray(new ElementEntity[0]);
	}
	
	public String createPaginationStringAppendixForUrl(int pageNum, int sizeNum) {
		if(sizeNum == 0)
			return "";
		return "?page=" + String.valueOf(pageNum) + "&size=" + String.valueOf(sizeNum);
	}

	public boolean answerQuestion(String superkey, String answer) {
		ActivityEntity activity = new ActivityEntity();
		activity.setType(Activity.QUESTION_ANSWER_ACTIVITY);
		activity.getAttribute().put(Activity.ACTIVITY_USER_ANSWER_KEY,
				answer);

		activity.setElementId(superkey);
		ActivityTO act = new ActivityTO(activity);
		try {

			boolean SystemResponse = this.restTemplate.postForObject(this.getURL() + Playground.Function_11, act,
					boolean.class, this.current_userPlayground, this.current_email);
			
			refreshUser();
			return SystemResponse;
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
		return false;
		
	}
	public void viewHighScores() {
		try {
			ActivityEntity actEnt = new ActivityEntity();
			actEnt.setType(Activity.GET_SCORES_ACTIVITY);
			

			UserTO[] result = this.restTemplate.postForObject(this.getURL()+Playground.Function_11 + 
					createPaginationStringAppendixForUrl(0, 20), actEnt, UserTO[].class, 
					this.current_userPlayground, this.current_email);
			StringBuilder s = new StringBuilder("");
			for(UserTO u : result)
			{
				s.append(u.getUsername() + " " + u.getPoints()+" points\n");
			}
			JOptionPane.showMessageDialog(null, s.toString());
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, Client.CONNECTION_PROBLEM);
		}

	}

	public String getURL() {
		return "http://" + host + ":" + port;
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

	public String getUserPlayground() {
		return current_userPlayground;
	}

	public void setUserPlayground(String current_userPlayground) {
		this.current_userPlayground = current_userPlayground;
	}

	public String getEmail() {
		return current_email;
	}

	public void setEmail(String current_email) {
		this.current_email = current_email;
	}

	public UserEntity getCurrentUser() {
		refreshUser();
		return current_user;
	}


}
