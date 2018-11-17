package playground.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import playground.database.Database;


@RestController
public class EdenDupontController {
	
	@Autowired
	Database db;
	

	
}
