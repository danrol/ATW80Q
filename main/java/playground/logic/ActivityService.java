package playground.logic;


import java.util.ArrayList;
import org.springframework.data.domain.Pageable;
public interface ActivityService {
	

	public String toString();
	
	ActivityEntity getActivity(String superkey);

	Object getMessage(ActivityEntity activity);

	Object addMessage(ActivityEntity activity);

	ArrayList<ActivityEntity> getAllByElementAttributeSuperkey(String Superkey,Pageable pageable);

	ActivityEntity addActivity(String userPlayground, String email, ActivityEntity e);

	Object executeActivity(String userPlayground, String email, ActivityEntity activity);

	Object getQuestion(ActivityEntity activity);

	Object setQuestion(ActivityEntity activity);

	Object answerQuestion(ActivityEntity activity);

}
