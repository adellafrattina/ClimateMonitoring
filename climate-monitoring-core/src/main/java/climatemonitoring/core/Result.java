package climatemonitoring.core;

public abstract class Result<T> {

	public Result() {


	}

	public abstract void exec();

	public boolean ready() {

		// Still need to understand wtf is going on here
		return true;
	}

	public T get() {

		return m_returnData;
	}

	private Thread m_thread;
	private T m_returnData;
}
