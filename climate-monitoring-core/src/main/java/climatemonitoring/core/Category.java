/*

Alessandro della Frattina 753073 VA
Cristian Capiferri 752918 VA
Francesco Lops 753175 VA
Dariia Sniezhko 753057 VA

*/

package climatemonitoring.core;

import java.io.Serializable;

/**
 * Represents a category type with its explanation
 * 
 * @author dariiasniezhkoinsubria
 * @version 1.0-SNAPSHOT
 */
public class Category implements Serializable {

	/**
	 * Initializes category fields
	 * @param The category ID
	 * @param The category explanation
	 */
	public Category(String category, String explanation) {

		m_categoryID = category;
		m_explanation = explanation;
	}

	/**
	 * 
	 * @return The category ID
	 */
	public String getCategory() {

		return m_categoryID;
	}

	/**
	 * 
	 * @return The category explanation
	 */
	public String getExplanation() {

		return m_explanation;
	}

	private String m_categoryID;
	private String m_explanation;
}
