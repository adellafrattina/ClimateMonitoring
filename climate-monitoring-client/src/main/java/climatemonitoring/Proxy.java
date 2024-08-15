package climatemonitoring;

import climatemonitoring.core.Database;

interface Proxy extends Database{

	public boolean connect(String address, short port);
	public void close();
	
}
