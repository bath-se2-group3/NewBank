// Package
package newbank.server;

/** 
 * Represents a Customer ID
 *
 * @author University of Bath | Group 3
 */
public class CustomerID {

	/**
	 * The key of the Customer ID
	 */
	private String key;
	
	/**
	 * Constructor for a CustomerID Object
	 *
	 * @param key the customer ID key
	 */
	public CustomerID(String key) {
		this.key = key;
	}
	
	/**
	 * Get and return the key.
	 * 
	 * @return the customer ID key
	 */
	public String getKey() {
		return key;
	}
}
