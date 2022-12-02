package controllers;

import java.sql.SQLException;

import javax.faces.application.FacesMessage;
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
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			UserModel um = this.manager.findUser(user);
			//If we have a result from our database
			if (um != null) {
				//add the user model we found in context, go to index page.
				context.getExternalContext().getSessionMap().put("userModel", um);
				return "index.xhtml";
			}
			else {
				//we didn't find a user, add a message, clear model so nav bar doesn't update
				context.addMessage(null, new FacesMessage("Invalid username or password."));
				context.getExternalContext().getSessionMap().clear();
				return "";
			}
		} catch ( RuntimeException | SQLException e) {
			context.addMessage(null, new FacesMessage("Unable to connect to the database. Try again later."));
			context.getExternalContext().getSessionMap().clear();
			return "";
		}
	}
	
	public String logout() {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		context.getSessionMap().clear();
		return "login.xhtml";
	}
}
