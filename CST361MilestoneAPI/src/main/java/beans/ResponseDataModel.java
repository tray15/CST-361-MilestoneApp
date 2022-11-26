package beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseDataModel extends ResponseModel {
	private Sensor data;
	
	public ResponseDataModel() {
		super();
	}
	public ResponseDataModel(int status, String message, Sensor data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}
	public Sensor getData() {
		return data;
	}
	public void setData(Sensor data) {
		this.data = data;
	}
}
