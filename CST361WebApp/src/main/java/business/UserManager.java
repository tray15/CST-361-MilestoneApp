package business;

import java.sql.SQLException;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
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
	
	public void register(UserModel user) throws RuntimeException, SQLException {
		try {
			dao.register(user);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public UserModel findUser(UserModel user) throws RuntimeException, SQLException {
		try {
			return dao.getByUsername(user);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Boolean userExists(UserModel user) throws RuntimeException, SQLException {
		try {
			if (dao.getByUsername(user) != null) {
				return true;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return false;
	}

}
