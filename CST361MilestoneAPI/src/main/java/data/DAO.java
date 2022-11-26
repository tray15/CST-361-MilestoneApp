package data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;
import javax.interceptor.Interceptors;

import beans.Sensor;
import beans.UserModel;
import util.LoggingInterceptor;

/**
 * 
 * @author Tanner Ray
 *
 */

@Interceptors(LoggingInterceptor.class)
@Stateless
@Local(DataAccessInterface.class)
@LocalBean
@Alternative
public class DAO implements DataAccessInterface {
	
	private static final String DB_URL = "jdbc:mysql://localhost:3307/cst-361?autoReconnect=true&useSSL=true";
	private static final String DB_USER = "root";
	private static final String PASSWORD = "root";
		
	private static final String INSERT_SENSOR = "INSERT INTO sensors (location) VALUES (?)";
	private static final String FIND_SENSOR = "SELECT * FROM sensors WHERE user_id=?";
	private static final String FIND_USER_SENSORS = "SELECT * FROM sensors INNER JOIN eventhistory ON sensors.sensor_id=eventhistory.sensor_id WHERE sensors.user_id=?";

	
	@Override
	public Sensor create(Sensor sensor) {
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(INSERT_SENSOR, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, sensor.getSensorId());
			stmt.setString(2, sensor.getLocation());
			
			ResultSet rs = stmt.getGeneratedKeys();
			
			if (rs.next()) {
				sensor.setSensorId(rs.getInt(1));
			}
			
			return this.findSensor(sensor);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(stmt);
			close(conn);
		}
		
	}

	@Override
	public Sensor findSensor(Sensor sensor) {
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(FIND_SENSOR);
			stmt.setInt(1, sensor.getSensorId());
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				int sensorId = rs.getInt("sensor_id");
				int userId = rs.getInt("user_id");
				String location = rs.getString("location");
				String date = rs.getString("event");
				
				Sensor found = new Sensor(sensorId, userId, location, date);
				
				return found;
			}
		} catch (SQLException e) {
			 e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			close(stmt);
			close(conn);
		}
		return null;
	}

	@Override
	public List<Sensor> getAllSensors(String id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(FIND_USER_SENSORS, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, id);
			
			ResultSet rs = stmt.executeQuery();
			List<Sensor> sensors = new ArrayList<Sensor>();
			
			while (rs.next()) {
				Sensor found = new Sensor();
				found.setSensorId(rs.getInt("sensor_id"));
				found.setUserId(rs.getInt("user_id"));
				found.setLocation(rs.getString("location"));
				found.setEventDate(rs.getString("event_date"));
				
				sensors.add(found);
			}
			
			rs.close();
			
			return sensors;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			close(stmt);
			close(conn);
		}
	}
	
	private Connection getConnection() {
		try {
			return DriverManager.getConnection(DB_URL, DB_USER, PASSWORD);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	private static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
