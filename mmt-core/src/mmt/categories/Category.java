package mmt.categories;


import java.io.Serializable;

import mmt.Passenger;



/** This abstract class represents the category of a Passenger, which will
	* then determine how much the Passenger pays for each Itinerary.
	*/

public abstract class Category implements Serializable {

	/** The Passenger. */
	private Passenger _passenger;

	/** Cost of the last ten Itineraries. */
	private double _lastTenCost;





	/** Constructor for class Category.
		* @param passenger the Passenger this Category belongs to
		* @param lastTenCost
		*/
	public Category(Passenger passenger, double lastTenCost) {
		_passenger = passenger;
		_lastTenCost = lastTenCost;
	}





	/** @return the cost of the last ten Itineraries */
	public double getLastTenCost() {
		return _lastTenCost;
	}
	/** @return the Passenger this Category belongs to */
	public Passenger getPassenger() {
		return _passenger;
	}







	/** Updates the cost of the last ten Itineraries.
		* @param cost
	 	*/
	public void updateCost(double cost) {
		_lastTenCost += cost;
		event();
	}







	/** Decides whether to change the type of Category. */
	public abstract void event();



	/** @param cost
		* @return a discounted cost, depending on the type of Category
		*/
	public abstract double applyDiscount(double cost);


}
