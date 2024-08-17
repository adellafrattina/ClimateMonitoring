package climatemonitoring;

import climatemonitoring.core.Area;
import climatemonitoring.core.Category;
import climatemonitoring.core.Center;
import climatemonitoring.core.Operator;
import climatemonitoring.core.Parameter;
import climatemonitoring.core.Result;

class ProxyMTImpl implements ProxyMT {

	public ProxyMTImpl(Proxy proxy) {

		m_proxy = proxy;
	}

	@Override
	public Result<Area[]> searchAreasByName(String str) {

		throw new UnsupportedOperationException("Unimplemented method 'searchAreasByName'");
	}

	@Override
	public Result<Area[]> searchAreasByCountry(String str) {

		throw new UnsupportedOperationException("Unimplemented method 'searchAreasByCountry'");
	}

	@Override
	public Result<Area[]> searchAreasByCoords(double latitude, double longitude) {

		throw new UnsupportedOperationException("Unimplemented method 'searchAreasByCoords'");
	}

	@Override
	public Result<Parameter[]> getParameters(int geoname_id, String center_id) {

		throw new UnsupportedOperationException("Unimplemented method 'getParameters'");
	}

	@Override
	public Result<Category[]> getCategories() {

		throw new UnsupportedOperationException("Unimplemented method 'getCategories'");
	}

	@Override
	public Result<Boolean> addArea(Area area) {

		throw new UnsupportedOperationException("Unimplemented method 'addArea'");
	}

	@Override
	public Result<Boolean> addCenter(Center center) {

		throw new UnsupportedOperationException("Unimplemented method 'addCenter'");
	}

	@Override
	public Result<Boolean> addOperator(Operator operator) {

		throw new UnsupportedOperationException("Unimplemented method 'addOperator'");
	}

	@Override
	public Result<Boolean> addParameter(Parameter parameter) {

		throw new UnsupportedOperationException("Unimplemented method 'addParameter'");
	}

	@Override
	public Result<Boolean> editOperator(String user_id, Operator operator) {

		throw new UnsupportedOperationException("Unimplemented method 'editOperator'");
	}

	@Override
	public Result<Operator> validateCredentials(String user_id, String password) {

		throw new UnsupportedOperationException("Unimplemented method 'validateCredentials'");
	}

	@Override
	public Result<Boolean> connect(String address, short port) {

		throw new UnsupportedOperationException("Unimplemented method 'connect'");
	}

	private Proxy m_proxy;
}
