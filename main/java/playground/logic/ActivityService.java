package playground.logic;

public interface ActivityService {
	public void setElementService(ElementService elementService);
	public void setUserService(UserService userService);
	public String toString();
	public boolean equalsEntity(ActivityEntity e1,ActivityEntity e2);
	//public boolean equalsTO(ActivityTO t1,ActivityTO t2);
	
}
