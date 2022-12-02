package data;

import java.sql.SQLException;

import java.util.List;

import beans.Sensor;
import beans.UserModel;

/**
 * 
 * @author Tanner Ray
 *
 */

public interface DataAccessInterface {
	void register(UserModel user) throws RuntimeException, SQLException;
	public UserModel getByUsername(UserModel u) throws RuntimeException, SQLException;
	
	void create(Sensor sensor) throws RuntimeException, SQLException;
	List<Sensor> getSensor(UserModel um) throws RuntimeException, SQLException;
	Sensor findSensor(Sensor sensor) throws RuntimeException, SQLException;
	
	void generateEvent() throws RuntimeException, SQLException;
	
}
