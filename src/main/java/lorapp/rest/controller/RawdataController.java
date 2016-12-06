package lorapp.rest.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lorapp.db.entity.UploadMessage;
import lorapp.db.entity.component.AppDevMap;
import lorapp.db.repo.UploadMessageRepo;

@RestController
@RequestMapping(value = "/rawdata/")
@Api(value = "Rawdata Interface")
public class RawdataController {

	@Autowired
	UploadMessageRepo umRepo;

	@ApiOperation(value = "List all data for a AppEUI and DevEUI Map", notes = "")
	@ApiImplicitParams({
		 @ApiImplicitParam(name = "appeui", value = "AppEUI", 
				 required = true, dataType = "String", paramType = "path"),
		 @ApiImplicitParam(name = "deveui", value = "DevEUI", 
				 required = true, dataType = "String", paramType = "path")
	})
	@ApiResponse(code = 200, message = "Success", response = List.class)
	
	@RequestMapping(value = "{appeui}/{deveui}/all", method = RequestMethod.GET)
	public List<UploadMessage> getAllData(@PathVariable("appeui") String appEUI,
			@PathVariable("deveui") String devEUI) {
		return umRepo.findAllByDevAppMap(new AppDevMap(appEUI, devEUI));
	}

	@RequestMapping(value = "{appeui}/{deveui}/day", method = RequestMethod.GET)
	public List<UploadMessage> getAllDataForDay(@PathVariable("appeui") String appEUI,
			@PathVariable("deveui") String devEUI, @RequestParam("date") String day) {
		LocalDate localDate = LocalDate.parse(day);
		LocalDate nextday = localDate.plusDays(1);
		return umRepo.findAllByDevAppMapAndReceiveTimeBetween(new AppDevMap(appEUI, devEUI),
				Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
				Date.from(nextday.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	}

	@RequestMapping(value = "{appeui}/{deveui}", method = RequestMethod.GET)
	public List<UploadMessage> getAllDataBetween(@PathVariable("appeui") String appEUI,
			@PathVariable("deveui") String devEUI,
			@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
		return umRepo.findAllByDevAppMapAndReceiveTimeBetween(new AppDevMap(appEUI, devEUI), from, to);
	}
}
