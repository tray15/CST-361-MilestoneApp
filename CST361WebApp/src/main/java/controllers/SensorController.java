package controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import beans.UserModel;
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
}
