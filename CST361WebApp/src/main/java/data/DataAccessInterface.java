package data;

import java.sql.SQLException;
import java.util.List;

import beans.Sensor;
import beans.UserModel;

public interface DataAccessInterface {
	void register(UserModel user) throws SQLException;
	public UserModel getByUsername(UserModel u);
	
	Sensor create(Sensor sensor);
	List<Sensor> getSensor(UserModel um);
	Sensor findSensor(Sensor sensor);
	
}
