package mmt;


import mmt.categories.Category;
import mmt.categories.Normal;


import java.util.List;
import java.util.ArrayList;
import java.time.LocalTime;
import java.time.Duration;
import java.io.Serializable;



	/** Class Passenger, a representation of a passenger, a user of trains. */

public class Passenger implements Serializable{

	/** This Passenger's identification number. */
	private int _id;

	/** This Passenger's name. */
	private String _name;

	/** This Passenger's category. */
	private Category _category = new Normal(this, 0);

	/** This Passenger's itineraries. */
	private List<Itinerary> _itineraries = new ArrayList<Itinerary>();

	/** This Passenger's total money spent */
	private double _moneySpent = 0;

	/** This Passenger's acumulated time */
	private Duration _timeSpent = Duration.ofMinutes(0);





	/** Constructor for class Passenger.
		* @param name Passenger's name
		* @param id Passenger's id
		*/
	public Passenger(String name, int id) {
		_name = name;
		_id = id;
	}



	/** @return this Passenger's id */
	public int getId() {return _id;}
	/** @return this Passenger's name */
	public String getName() {return _name;}
	/** @return this Passenger's Category */
	public Category getCategory() {return _category;}
	/** @return this Passenger's money spent */
	public double getMoneySpent() {return _moneySpent;}
	/** @return this Passenger's time spent */
	public Duration getTimeSpent() {return _timeSpent;}
	/** @return this Passenger's Itineraries */
	public List<Itinerary> getItineraries() {return _itineraries;}





	/** Sets this Passenger's name.
		* @param name this Passenger's new name
		*/
	public void setName(String name) {_name = name;}

	/** Sets this Passenger's category.
		* @param category this Passenger's new Category
		*/
	public void setCategory(Category category) {_category = category;}




	/** Adds an Itinerary to the list of existent Itineraries of this Passenger.
		* It updates the time and money spent by this Passenger, as well as
		* updating his category.
		* @param itinerary
	 	*/
	public void addItinerary(Itinerary itinerary) {

		_itineraries.add(itinerary);
		_moneySpent += _category.applyDiscount(itinerary.getCost());
		_timeSpent = _timeSpent.plus(itinerary.getTime());

		double cost = 0;
		if(_itineraries.size() > 10)
		/* The cost of the last ten Itineraries is calculated by adding the
difference between the newly added Itinerary and the 11th Itinerary (if it exists). */
			cost = _itineraries.get(_itineraries.size() - 11).getCost();

		_category.updateCost(itinerary.getCost() - cost);
	}









	/** @return the string representation of Passenger.
		* It follows this format:
		*
		*	id | name | category | number of itineraries | money spent | time spent
		*
		*/
	public String toString() {

		String time = String.format("%02d", _timeSpent.toHours()) + ":" +
String.format("%02d", _timeSpent.toMinutes() % 60);

		return _id + "|" + _name + "|" + _category + "|" + _itineraries.size()
+ "|" + String.format("%.2f", _moneySpent).replace(",",".") + "|" + time;

	}

}
