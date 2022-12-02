package controllers;

import java.sql.SQLException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import business.SensorManagerInterface;

/**
 * 
 * @author tanner ray
 * sensor controller current use is just business layer injection
 */

@ManagedBean
@ViewScoped
public class SensorController {
	@Inject
	SensorManagerInterface service;
	
	public SensorManagerInterface getService() {
		return service;
	}
	
	public String emulateEvent() throws RuntimeException, SQLException {
		service.generateEvent();
		
		return "";
	}
	
}
