package controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import beans.UserModel;
import business.UserManager;

/**
 * 
 * @author tanner ray
 * login controller
 */

@ManagedBean
@SessionScoped
public class LoginController {
	@Inject
	UserManager manager;
	
	boolean loggedIn = false;
	public boolean getLoggedIn() {
		return this.loggedIn;
	}
	
	public String login(UserModel user) {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		UserModel um = this.manager.findUser(user);
		if (um != null) {
			context.getSessionMap().put("userModel", um);
			return "index.xhtml";
		} else {
			context.getRequestMap().put("message", "Invalid username or password");
			return "";
		}
	}
	
	public String logout() {
		
		return "login.xhtml";
	}
}
