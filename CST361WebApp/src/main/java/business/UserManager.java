package business;

import java.sql.SQLException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import beans.UserModel;
import data.DAO;

/**
 * 
 * @author tanner ray
 * handles user data logic
 */

@Stateless
@LocalBean
public class UserManager {
	@Inject
	DAO dao;
	
	public void register(UserModel user) {
		try {
			dao.register(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public UserModel findUser(UserModel user) {
		try {
			return dao.getByUsername(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
