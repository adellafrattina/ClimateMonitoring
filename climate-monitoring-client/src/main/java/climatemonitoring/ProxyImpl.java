package climatemonitoring;

import java.io.IOException;
import java.net.Socket;

import climatemonitoring.core.Area;
import climatemonitoring.core.Category;
import climatemonitoring.core.Center;
import climatemonitoring.core.Operator;
import climatemonitoring.core.Parameter;

class ProxyImpl implements Proxy{

	
	/**
	 * Allows the connection to the desired address.
	 * Return true if the connection was succesfull, false if not
	 */
	public boolean connect(String address, short port) {
		
		try {

			s = new Socket(address, port);
			
			return true;
		} catch (IOException e) {
			System.err.println("Error during the connection");
		}

		return false;
	}
	
	/**
	 * Close the connection between the socket and the server
	 */
	public void close() {

		if(s != null && !s.isClosed()){

			try {

				s.close();
				System.out.println("Connection closed");
			} catch (IOException e) {

				System.out.println("Error during the closure of the connection");
			}
		}
	}
	
	
	@Override
	public Area[] searchAreasByName(String str) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'searchAreasByName'");
	}
	
	@Override
	public Area[] searchAreasByCountry(String str) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'searchAreasByCountry'");
	}
	
	@Override
	public Area[] searchAreasByCoords(double latitude, double longitude) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'searchAreasByCoords'");
	}
	
	@Override
	public Parameter[] getParameters(int geoname_id, String center_id) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getParameters'");
	}
	
	@Override
	public Category[] getCategories() throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getCategories'");
	}
	
	@Override
	public boolean addArea(Area area) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'addArea'");
	}
	
	@Override
	public boolean addCenter(Center center) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'addCenter'");
	}
	
	@Override
	public boolean addOperator(Operator operator) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'addOperator'");
	}
	
	@Override
	public boolean addParameter(Parameter parameter) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'addParameter'");
	}
	
	@Override
	public boolean editOperator(String user_id, Operator operator) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'editOperator'");
	}
	
	@Override
	public Operator validateCredentials(String user_id, String password) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'validateCredentials'");
	}
	
	private Socket s;
}
