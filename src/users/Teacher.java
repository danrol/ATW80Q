package users;

import java.util.ArrayList;
import java.util.HashMap;

import org.assertj.core.util.Arrays;

import com.sun.javafx.collections.MappingChange.Map;

import activities.Session;
import database.Database;
import elements.Lesson;
import elements.MessageBoard;

@Component
public class Teacher extends User{

	
	public Teacher(String name, String email, String avatar) {
		super(name, email,avatar);
	}
	


	
}
