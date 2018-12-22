package playground.logic;

public interface ActivityService {
	

	public String toString();
	
	public Object executeActivity(ActivityEntity activity);

	ActivityEntity addActivity(ActivityEntity e);
	
	ActivityEntity getActivity(String superkey);

	Object getMessage(ActivityEntity activity);

	Object deleteMessage(ActivityEntity activity);

	Object setMessage(ActivityEntity activity);

}
