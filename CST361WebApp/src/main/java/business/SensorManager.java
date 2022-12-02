package business;

import java.sql.SQLException;
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
	public void addSensor(Sensor sensor) throws RuntimeException, SQLException {
		this.dao.create(sensor);
	}
	
	@Override
	public List<Sensor> getSensor(UserModel um) throws RuntimeException, SQLException {
		return this.dao.getSensor(um);
	}

	@Override
	public Sensor findSensor(Sensor sensor) throws RuntimeException, SQLException {
		Sensor found = this.dao.findSensor(sensor);
		return found;
	}
	@Override
	public void generateEvent() throws RuntimeException, SQLException{
		this.dao.generateEvent();
	}
}
