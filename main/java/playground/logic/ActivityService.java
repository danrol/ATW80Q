package playground.logic;


import java.util.ArrayList;
import org.springframework.data.domain.Pageable;
public interface ActivityService {
	

	public String toString();
	
	ActivityEntity getActivity(String superkey);

	Object getMessage(ActivityEntity activity);

	Object addMessage(ActivityEntity activity);

	ArrayList<ActivityEntity> getAllMessagesActivitiesInMessageBoard(String Superkey,Pageable pageable);

	ActivityEntity addActivity(String userPlayground, String email, ActivityEntity e);

	Object executeActivity(String userPlayground, String email, ActivityEntity activity, Pageable pageable);

	Object getQuestion(ActivityEntity activity);

	Object setQuestion(ActivityEntity activity);

	Object answerQuestion(ActivityEntity activity);

	void cleanActivityService();

	Object addMessageBoard(ActivityEntity activity);

	Object addQuestion(ActivityEntity activity);

}
