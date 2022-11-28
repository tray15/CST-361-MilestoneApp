package data;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;

import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Sensor;
import beans.UserModel;

/**
 * 
 * @author Tanner Ray
 *
 */

@Stateless
@Local(DataAccessInterface.class)
@LocalBean
@Alternative
public class DAO implements DataAccessInterface {
	
	private static final String DB_URL = "jdbc:mysql://localhost:3307/cst-361?autoReconnect=true&useSSL=true";
	private static final String DB_USER = "root";
	private static final String PASSWORD = "root";
	
	private static final String INSERT_USER = "INSERT INTO user (username, password) VALUES (?, ?)";
	private static final String FIND_USER = "SELECT * FROM user WHERE username=?";
	
	private static final String INSERT_SENSOR = "INSERT INTO sensors (location) VALUES (?)";
	private static final String FIND_SENSOR = "SELECT * FROM sensors WHERE user_id=?";
	
	private static final String GENERATE_RANDOM_EVENT = "INSERT INTO eventhistory (sensor_id, user_id, event_date) VALUES (?, ?, ?)";
	@Override
	public void register(UserModel user) throws SQLException {
		
		Connection c = null;
		PreparedStatement stmt = null;
		try {

			c = DriverManager.getConnection(DB_URL, DB_USER, PASSWORD);

			stmt = c.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, user.getUsername());	
			stmt.setString(2, user.getPassword());
			
			stmt.executeUpdate();
			
			
			close(stmt);
			close(c);
		} catch (SQLException e) {
			System.out.println("Failed to connect to database. User: " + DB_USER);
			e.printStackTrace();
		}
	}
	
	@Override
	public UserModel getByUsername(UserModel user) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(FIND_USER, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setString(1, user.getUsername());
			
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				UserModel found = new UserModel();
				found.setUserId(rs.getInt("user_id"));
				found.setUsername(rs.getString("username"));
				found.setPassword(rs.getString("password"));
				
				return found;
			}
			close(stmt);
			close(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
}
	
	@Override
	public void create(Sensor sensor) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(INSERT_SENSOR, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, sensor.getLocation());
			
			stmt.executeUpdate();
			
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
				Sensor found = new Sensor();
				found.setSensorId(rs.getInt("sensor_id"));
				found.setUserId(rs.getInt("user_id"));
				found.setLocation(rs.getString("location"));
				found.setEventDate(rs.getString("event"));
				
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
	public List<Sensor> getSensor(UserModel um) {
		HttpURLConnection conn = null;
		try {
			int id = um.getUserId();
			//URL with parameters
			URL url = new URL("http://localhost:8080/CST361MilestoneAPI/rest/sensors/getusersensors/" + id);
			conn = (HttpURLConnection)url.openConnection();
			
			try {
				//Let's get the data from our request
				Scanner scanner = new Scanner(url.openStream());
				String result = "";
				//get every line from json formatted text
				while (scanner.hasNext()) {
					result+= scanner.nextLine();
				}
				//all finished reading, let's avoid a resource leak.
				scanner.close();
				
				ObjectMapper mapper = new ObjectMapper();
				try {
					List<Sensor> sensorList = Arrays.asList(mapper.readValue(result, Sensor[].class));
					
					return sensorList;
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			conn.disconnect();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void generateEvent() {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			int sensor = randomSensor();
			String date = randomDate();
			
			conn = getConnection();
			stmt = conn.prepareStatement(GENERATE_RANDOM_EVENT, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, sensor);
			stmt.setInt(2, 1);
			stmt.setString(3, date);
			
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
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
	public int randomSensor() {
		int[] sensors = { 1, 2, 3, 4, 5, 6 };
		int randomSensor = new Random().nextInt(sensors.length);
		
		return sensors[randomSensor];
	}
	
	public String randomDate() {
		Random rand = new Random();
		int month = rand.nextInt(12-1) + 1;
		int day = rand.nextInt(30-1) +1;
		int year = 2022;
		
		
		
		String date = month + "-" + day + "-" + year;
		
		return date;
	}
}
