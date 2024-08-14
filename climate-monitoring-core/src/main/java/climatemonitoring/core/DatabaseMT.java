package climatemonitoring.core;

public interface DatabaseMT {

	public Result<Area[]> searchAreasByName(String str);
	public Result<Area[]> searchAreasByCountry(String str);
	public Result<Area[]> searchAreasByCoords(double latitude, double longitude);

	public Result<Parameter[]> getParameters(int geoname_id, String center_id);
	public Result<Category[]> getCategories();

	public void addArea(Area area);
	public void addCenter(Center center);
	public void addOperator(Operator operator);
	public void addParameter(Parameter parameter);

	public void editOperator(String user_id, Operator operator);
	public Result<Operator> validateCredentials(String user_id, String password);
}
