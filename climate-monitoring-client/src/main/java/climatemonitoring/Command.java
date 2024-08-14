package climatemonitoring;



class Command {

	public void Command(String line){

		m_cmd = line;
	}

	public String getCmd(){

		return m_cmd;
	}

	public String getArgs(){
		
		return m_args;
	}
	
	public static final String SEARCH = "search";
	public static final String VIEW = "view";
	public static final String LOGIN = "login";
	public static final String REGISTER = "register";
	public static final String ADD = "add";
	public static final String EDIT = "edit";
	public static final String SETTINGS = "settings";
	private String m_cmd;
	private String m_args;
}
