/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

/**
 * The result class is used to keep track of the data to be
 * returned by a function that is being executed on a different
 * thread. ready() is used to check if the function has completed
 * its task and get() is used to retrieve the result
 * 
 * @author ccapiferri
 * @version 1.0-SNAPSHOT
 */
public abstract class Result<T> {

	/**
	 * Initializes a new working thread and starts its execution.
	 * After finishing, collects the data returned
	 */
	public Result() {

		m_thread = new Thread() {

			@Override
			public void run() {

				try {

					m_returnData = exec();
				}

				catch (Exception e) {

					m_exception = e;
				}
			}
		};

		m_thread.start();
	}

	/**
	 * Needs to be overwritten with the code that is going to be
	 * executed on a different thread
	 * 
	 * @throws Exception If anything goes wrong, at the moment of calling the get() function,
	 * an exception gets thrown
	 * @returns The result of the function executed from the other thread
	 */
	public abstract T exec() throws Exception;

	/**
	 * Checks whether the working thread has finished its execution.
	 * If so, the function get() can be called to retrieve the returned data
	 * 
	 * @return True if the thread has finished its execution, false otherwise
	 */
	public boolean ready() {

		return !m_thread.isAlive();
	}

	/**
	 * Waits for the thread to die. This makes the operation single-threaded
	 */
	public void join() {

		try {

			m_thread.join();
		}

		catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	/**
	 * To interrupt the thread
	 */
	public void interrupt() {

		m_thread.interrupt();
	}

	/**
	 * 
	 * @return True if the thread was interrupted
	 */
	public boolean interrupted() {

		return m_thread.isInterrupted();
	}

	/**
	 * Keeps track of the returned data after the working thread has finished
	 * its execution
	 * 
	 * @return The data returned by the working thread
	 * @throws Exception If anything went wrong with the exec() function
	 */
	public T get() throws Exception {

		if (m_exception != null) throw m_exception;

		return m_returnData;
	}

	private Thread m_thread;
	private T m_returnData;
	private Exception m_exception;
}
