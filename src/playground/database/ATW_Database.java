package playground.database;

import playground.activities.AnswerQuestionTO;
import playground.elements.ElementTO;
import playground.logic.UserTO;

public interface ATW_Database {
	public AnswerQuestionTO editQuestion();
	public void deleteQuestion();
	public UserTO getUser(String email);
	public ElementTO[] getAllElementsTOInRadius(ElementTO element,double distance);
}
