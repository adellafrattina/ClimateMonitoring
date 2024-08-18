/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring;

import java.sql.Connection;
import java.sql.ResultSet;

import climatemonitoring.core.Area;
import climatemonitoring.core.Category;
import climatemonitoring.core.Center;
import climatemonitoring.core.ConnectionLostException;
import climatemonitoring.core.DatabaseRequestException;
import climatemonitoring.core.Operator;
import climatemonitoring.core.Parameter;

class ServerDatabaseImpl implements ServerDatabase {

	public ServerDatabaseImpl(String url, String username, String password) {


	}

	public void shutdown() {


	}

	public ResultSet execute(String statement) {

		throw new UnsupportedOperationException("Unimplemented method 'execute'");
	}

	@Override
	public Area[] searchAreasByName(String str) throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'searchAreasByName'");
	}

	@Override
	public Area[] searchAreasByCountry(String str) throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'searchAreasByCountry'");
	}

	@Override
	public Area[] searchAreasByCoords(double latitude, double longitude)
			throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'searchAreasByCoords'");
	}

	@Override
	public Parameter[] getParameters(int geoname_id, String center_id)
			throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'getParameters'");
	}

	@Override
	public Category[] getCategories() throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'getCategories'");
	}

	@Override
	public boolean addArea(Area area) throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'addArea'");
	}

	@Override
	public boolean addCenter(Center center) throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'addCenter'");
	}

	@Override
	public boolean addOperator(Operator operator) throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'addOperator'");
	}

	@Override
	public boolean addParameter(Parameter parameter) throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'addParameter'");
	}

	@Override
	public boolean editOperator(String user_id, Operator operator)
			throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'editOperator'");
	}

	@Override
	public Operator validateCredentials(String user_id, String password)
			throws ConnectionLostException, DatabaseRequestException {

		throw new UnsupportedOperationException("Unimplemented method 'validateCredentials'");
	}

	private Connection m_connection;
}
