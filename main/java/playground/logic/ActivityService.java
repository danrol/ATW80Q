package playground.logic;


import java.util.ArrayList;
import org.springframework.data.domain.Pageable;
public interface ActivityService {
	

	public String toString();
	
	public Object executeActivity(ActivityEntity activity);

	ActivityEntity addActivity(ActivityEntity e);
	
	ActivityEntity getActivity(String superkey);

	Object getMessage(ActivityEntity activity);

	Object deleteMessage(ActivityEntity activity);

	Object addMessage(ActivityEntity activity);

	ArrayList<ActivityEntity> getAllByElementAttributeSuperkey(String Superkey,Pageable pageable);

}
