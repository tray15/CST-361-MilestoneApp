package business;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import beans.Sensor;
import beans.UserModel;
import data.DAO;
import util.LoggingInterceptor;

/**
 * 
 * @author Tanner Ray
 *
 */

@Stateless
@LocalBean
@Local(SensorManagerInterface.class)
@Alternative
@Interceptors(LoggingInterceptor.class)
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
	public Sensor getSensor(Sensor sensor) {
		Sensor findSensor = this.dao.findSensor(sensor);
		return findSensor;
	}

	@Override
	public List<Sensor> getAllSensors(String name) {
		return this.dao.getAllSensors(name);
	}
}
