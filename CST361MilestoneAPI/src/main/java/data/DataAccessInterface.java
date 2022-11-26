package data;

import java.util.List;

import beans.Sensor;
import beans.UserModel;

/**
 * 
 * @author Tanner Ray
 *
 */

public interface DataAccessInterface {
	Sensor create(Sensor sensor);
	Sensor findSensor(Sensor sensor);
	List<Sensor> getAllSensors(String id);
}
