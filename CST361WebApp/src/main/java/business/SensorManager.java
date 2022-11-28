package business;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import beans.Sensor;
import beans.UserModel;
import data.DAO;

/**
 * 
 * @author tanner ray
 * logic to handle sensor data
 */

@Stateless
@LocalBean
@Local(SensorManagerInterface.class)
@Alternative
public class SensorManager implements SensorManagerInterface {
	@Inject
	DAO dao;
	
	public SensorManager() {
		this.dao = new DAO();
	}

	@Override
	public void addSensor(Sensor sensor) {
		this.dao.create(sensor);
	}
	
	@Override
	public List<Sensor> getSensor(UserModel um) {
		return this.dao.getSensor(um);
	}

	@Override
	public Sensor findSensor(Sensor sensor) {
		Sensor found = this.dao.findSensor(sensor);
		return found;
	}
	@Override
	public void generateEvent() {
		this.dao.generateEvent();
	}
}
