package mmt.categories;


import mmt.Passenger;



/** This class represents the category of a Passenger, "Normal" */

public class Normal extends Category {


	/** Constructor for class Normal.
		* @see mmt.categories.Category#Category(passenger, double)
		*/
	public Normal(Passenger passenger, double lastTenCost) {
		super(passenger, lastTenCost);
	}



	/** tests whether the cost of the last ten Itineraries is over 250,
		* which is a trigger to change the Passenger's state to Frequent.
		*/
	@Override
	public void event() {
		if(getLastTenCost() > 250) {
			getPassenger().setCategory(new Frequent(getPassenger(), getLastTenCost()));
			getPassenger().getCategory().event();
		}
	}



	/** @return cost, for there is no discount for category Normal
		* @see mmt.categories.Category#applyDiscount(double)
		*/
	@Override
	public double applyDiscount(double cost) {
		return cost;
	}



	/** @return the string representation of this class */
	@Override
	public String toString() {return "NORMAL";}

}
