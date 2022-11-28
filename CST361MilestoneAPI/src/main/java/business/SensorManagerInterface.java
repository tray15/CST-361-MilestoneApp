package business;

import java.util.List;

import beans.Sensor;

/**
 * 
 * @author Tanner Ray
 *
 */

public interface SensorManagerInterface {
	void addSensor(Sensor sensor);
	Sensor getSensor(Sensor sensor);
	List<Sensor> getAllSensors(String name);
}
