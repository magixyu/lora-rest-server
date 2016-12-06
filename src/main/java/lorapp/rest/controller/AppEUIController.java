package lorapp.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lorapp.db.entity.AppEUI;
import lorapp.db.repo.AppEUIRepo;

@RestController
@RequestMapping(value = "/appeui")
public class AppEUIController {

	@Autowired
	AppEUIRepo appEUIRepo;
	@RequestMapping(value = "{appeui}", method = RequestMethod.PUT)
	public void add(@PathVariable("appeui") String appEUI) {
		if(appEUIRepo.findAllByAppEUI(appEUI).isEmpty()){
			appEUIRepo.save(new AppEUI(appEUI));
		}
		//TODO: send message to rabbitmq to ask listener to send JOIN to 
	}
	
	@RequestMapping(value = "{appeui}", method = RequestMethod.DELETE)
	public void remove(@PathVariable("appeui") String appEUI) {
		if(appEUIRepo.findAllByAppEUI(appEUI).isEmpty()){
			appEUIRepo.save(new AppEUI(appEUI));
		}
		//TODO: send message to rabbitmq to ask listener to send QUIT to 
	}
	
	@RequestMapping(value = "{appeui}/join", method = RequestMethod.PUT)
	public void join(@PathVariable("appeui") String appEUI) {
		//TODO: send message to rabbitmq to ask listener to send JOIN to 
	}
	
	@RequestMapping(value = "{appeui}/quit", method = RequestMethod.PUT)
	public void quit(@PathVariable("appeui") String appEUI) {
		//TODO: send message to rabbitmq to ask listener to send JOIN to 
	}
	
}
