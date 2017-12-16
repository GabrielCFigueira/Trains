package mmt.categories;


import mmt.Passenger;



/** This class represents the sate of a Passenger, "Frequent" */

public class Frequent extends Category {


	/** Constructor for class Frequent.
		* @see mmt.categories.Category#Category(passenger, double)
		*/
	public Frequent(Passenger passenger, double lastTenCost) {
		super(passenger, lastTenCost);
	}



	/** tests whether the cost of the last ten Itineraries is over 2500 or
		* below 250, which is a trigger to change the Passenger's state to Special
		* or Normal, respectively.
		*/
	@Override
	public void event() {
		if(getLastTenCost() > 2500)
			getPassenger().setCategory(new Special(getPassenger(), getLastTenCost()));
		else if (getLastTenCost() <= 250)
			getPassenger().setCategory(new Normal(getPassenger(), getLastTenCost()));
	}



	/** @return 85% of cost, for this is the category Frequent
		* @see mmt.categories.Category#applyDiscount(double)
		*/
	@Override
	public double applyDiscount(double cost) {
		return cost * 0.85;
	}



	/** @return the string representation of this class */
	@Override
	public String toString() {return "FREQUENTE";}
}
