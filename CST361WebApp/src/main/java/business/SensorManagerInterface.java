package business;

import java.util.List;

import beans.Sensor;
import beans.UserModel;

/**
 * 
 * @author tanner ray
 * sensor manager interface
 */

public interface SensorManagerInterface {
	void addSensor(Sensor sensor);
	List<Sensor> getSensor(UserModel um);
	Sensor findSensor(Sensor sensor);
	
	void generateEvent();
}
