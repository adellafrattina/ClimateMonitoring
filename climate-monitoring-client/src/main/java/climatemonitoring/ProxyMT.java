package climatemonitoring;

import climatemonitoring.core.DatabaseMT;
import climatemonitoring.core.Result;

interface ProxyMT extends DatabaseMT{

	public Result<Boolean> connect (String address, short port);
}
