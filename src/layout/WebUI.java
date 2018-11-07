package layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import elements.GameRules;

@RestController
public class WebUI {
	
	@Autowired
	private GameRules gameRules;
	
}
