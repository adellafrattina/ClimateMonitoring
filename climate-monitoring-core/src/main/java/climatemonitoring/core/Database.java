package climatemonitoring.core;

public interface Database {

	public Area[] searchAreasByName(String str);
	public Area[] searchAreasByCountry(String str);
	public Area[] searchAreasByCoords(double latitude, double longitude);

	public Parameter[] getParameters(int geoname_id, String center_id);
	public Category[] getCategories();

	public void addArea(Area area);
	public void addCenter(Center center);
	public void addOperator(Operator operator);
	public void addParameter(Parameter parameter);

	public void editOperator(String user_id, Operator operator);
	public Operator validateCredentials(String user_id, String password);
}
