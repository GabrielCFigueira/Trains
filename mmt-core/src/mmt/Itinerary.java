package mmt;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;




/** Class representive of a train voyage at a given date, using one or
  * more services.
	*/


public class Itinerary implements Comparable<Itinerary>, Serializable{

	/** This Itinerary's date (day/month/year). */
	private LocalDate _date;
	/** An Itinerary is made of ServiceSegments */
	private List<ServiceSegment> _serviceSegments = new ArrayList<ServiceSegment>();



	/** Constructor for class Itinerary.
		* @param date this Itinerary's date
		*/
	public Itinerary(LocalDate date) {
		_date = date;
	}



	/** @return this Itinerary's date */
	public LocalDate getDate() {return _date;}

	/** @return the number of Segments in this Itinerary */
	public int getSegmentNumber() {return _serviceSegments.size();}






	/** Appends a ServiceSegment to list of existent ServiceSegments
		* @param ServiceSegment
		*/
	public void addServiceSegment(ServiceSegment serviceSegment) {
		_serviceSegments.add(serviceSegment);
	}



	/** @return this Itinerary's cost */
	public double getCost() {
		double cost = 0;
		for (ServiceSegment ss : _serviceSegments)
			cost += ss.getCost();
		return cost;
	}



	/** @return the Duration of this Itinerary, which is gotten between the
		* the departure Station and the arrival Station (therefore, the time the
		* Passenger is waiting at any Station for a train is also counted)
		*/
	public Duration getTime() {
		return _serviceSegments.get(0).getFirstStation().getTimeInBetween(
_serviceSegments.get(_serviceSegments.size() - 1).getLastStation());
	}






	/** Defines the natural order of Itineraries. By ascending order and hierarchically
		* by the following criteria: Departure time; Arrival time; Cost.
		* @param itinerary
		* @return 1 if this Itinerary is greater than itinerary, 0 if they are
		* "equal", and -1 otherwise
		*/
	@Override
	public int compareTo(Itinerary itinerary) {
		long difference = this._serviceSegments.get(0).getFirstStation().getTimeInBetween(
itinerary._serviceSegments.get(0).getFirstStation()).toMinutes();
		if(difference == 0)
			difference = this._serviceSegments.get(this._serviceSegments.size() - 1
).getLastStation().getTimeInBetween(itinerary._serviceSegments.get(
itinerary._serviceSegments.size() - 1).getLastStation()).toMinutes();
		if(difference == 0) {
			double costDifference = this.getCost() - itinerary.getCost();
			if(costDifference > 0)
				difference = 1;
			else if (costDifference < 0)
				difference = -1;
		}
		else if(difference < 0)
			return 1;
		else if (difference > 0)
			return -1;
		return 0;
	}


/** @return a Comparator for class Itinerary, which comparates by its date
	* (ascending order)
	*/
public final static Comparator<Itinerary> getItineraryComparatorByDate() {
	return new Comparator<Itinerary>() {
		@Override
		public int compare(Itinerary itinerary1, Itinerary itinerary2) {
			return itinerary1.getDate().compareTo(itinerary2.getDate());
		}
	};
}




	/** @return a string with all the ServiceSegments concatenated
	 	* @see mmt.ServiceSegment#toString()
		*/
	@Override
	public String toString() {
		String res = "";
		for (ServiceSegment ss : _serviceSegments)
			res += ss + "\n";
		return res;
	}
}
