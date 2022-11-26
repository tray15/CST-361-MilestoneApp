package business;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import beans.Sensor;
import util.SensorNotFoundException;
import beans.ResponseDataModel;

/**
 * 
 * @author Tanner Ray
 *
 */

@Path("/sensors")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class SensorRestService {
	@Inject
	SensorManager sm;
	
	@GET
	@Path("/getsensors/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseDataModel getSensorsAsJson(@PathParam("user_id") int userId) {
		try {
			Sensor sensor = new Sensor();
			sensor.setSensorId(userId);
			
			Sensor found = sm.getSensor(sensor);
			
			return new ResponseDataModel(200, "Sensors found", found);
		} catch (SensorNotFoundException e) {
			return new ResponseDataModel(404, "Sensor not found", null);
		} catch(Exception e) {
			return new ResponseDataModel(500, "Something went wrong.", null);
		}
	}
	
	@GET
	@Path("/getusersensors/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Sensor[] getUserSensors(@PathParam("id") String id) {
		try {
			List<Sensor> sensors = sm.getAllSensors(id);
			return sensors.toArray(new Sensor[sensors.size()]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}	