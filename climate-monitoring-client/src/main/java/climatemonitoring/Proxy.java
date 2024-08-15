package climatemonitoring;

interface Proxy {

	public boolean connect(String address, int port);
	public void close();
	
}
