package beans;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class Sensor {
	private int sensorId;
	private int userId;
	private String location;
	private Date eventDate;
	
	public Sensor() {
		sensorId = 0;
		userId = 0;
		location = "";
	}
	public Sensor(int sensorId, int userId, String location, Date eventDate) {
		this.sensorId = sensorId;
		this.userId = userId;
		this.location = location;
		this.eventDate = eventDate;
	}
	public int getSensorId() {
		return sensorId;
	}
	public void setSensorId(int sensorId) {
		this.sensorId = sensorId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Date getEventDate() {
		return eventDate;
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	
}
