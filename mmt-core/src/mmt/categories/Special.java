package mmt.categories;


import mmt.Passenger;



/** This class represents the sate of a Passenger, "Special" */

public class Special extends Category {


	/** Constructor for class Special.
		* @see mmt.categories.Category#Category(passenger, double)
		*/
	public Special (Passenger passenger, double lastTenCost) {
		super(passenger, lastTenCost);
	}



	/** tests whether the cost of the last ten Itineraries is below 2500,
		* which is a trigger to change the Passenger's state to Frequent.
		*/
	@Override
	public void event() {
		if(getLastTenCost() <= 2500) {
			getPassenger().setCategory(new Frequent(getPassenger(), getLastTenCost()));
			getPassenger().getCategory().event();
		}
	}



	/** @return 50% of cost, for this is the category Special
		* @see mmt.categories.Category#applyDiscount(double)
		*/
	@Override
	public double applyDiscount(double cost) {
		return cost * 0.5;
	}



	/** @return the string representation of this class */
	@Override
	public String toString() {return "ESPECIAL";}

}
