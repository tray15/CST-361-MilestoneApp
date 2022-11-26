package controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import beans.UserModel;
import business.UserManager;

@ManagedBean
@SessionScoped
public class RegisterController {
	@Inject
	UserManager um;
	
	public String register() {
		FacesContext context = FacesContext.getCurrentInstance();
		UserModel user = context.getApplication().evaluateExpressionGet(context, "#{userModel}", UserModel.class);
		
		if (user != null) {
			try {
				this.um.register(user);
			} catch (Exception e) {
				e.printStackTrace();
				
				return "";
			}
		}
		
		return "login.xhtml";
	}
}
