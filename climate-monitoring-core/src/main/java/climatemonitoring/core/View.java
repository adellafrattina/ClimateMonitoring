package climatemonitoring.core;

public class View{

	
	public void setState(ViewState state){
		
		m_State = state;
	}
	
	public void onHeadlessRender(String args){
		
		m_State.onHeadlessRender(args);
	}
	
	public void onGUIRender(){
		
		m_State.onGUIRender();
	}

	private ViewState m_State; 
}
