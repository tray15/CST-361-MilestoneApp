package business;

import java.sql.SQLException;
import java.util.List;

import beans.Sensor;
import beans.UserModel;

/**
 * 
 * @author tanner ray
 * sensor manager interface
 */

public interface SensorManagerInterface {
	void addSensor(Sensor sensor) throws RuntimeException, SQLException;
	List<Sensor> getSensor(UserModel um) throws RuntimeException, SQLException;
	Sensor findSensor(Sensor sensor) throws RuntimeException, SQLException;
	
	void generateEvent() throws RuntimeException, SQLException;
}
