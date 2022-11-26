package data;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Sensor;
import beans.UserModel;

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
				Sensor found = new Sensor();
				found.setSensorId(rs.getInt("sensor_id"));
				found.setUserId(rs.getInt("user_id"));
				found.setLocation(rs.getString("location"));
				
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
			URL url = new URL("http://localhost:8080/MilestoneUI/rest/sensors/getusersensors/" + id);
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
