package business;

import java.util.List;

import beans.Sensor;

/**
 * 
 * @author Tanner Ray
 *
 */

public interface SensorManagerInterface {
	Sensor addSensor(Sensor sensor);
	Sensor getSensor(Sensor sensor);
	List<Sensor> getAllSensors(String name);
}
