package lorapp.nslistener.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lorapp.nslistener.db.entity.UploadMessage;
import lorapp.nslistener.db.entity.component.AppDevMap;
import lorapp.nslistener.db.repo.UploadMessageRepo;

@RestController
@RequestMapping("/rawdata/")
public class RawdataController {
	
	@Autowired
	UploadMessageRepo umRepo;
	
	@RequestMapping(value = "{appeui}/{deveui}", method = RequestMethod.GET )
	public List<UploadMessage> getAllData(@PathVariable("appeui") String appEUI,
			@PathVariable("deveui") String devEUI) {
		return umRepo.findAllByDevAppMap(new AppDevMap(appEUI, devEUI));
	}
}
