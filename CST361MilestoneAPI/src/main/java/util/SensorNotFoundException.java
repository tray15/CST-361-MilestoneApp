package util;

public class SensorNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	 public SensorNotFoundException(String error) {
	        super(error);
	 }
}