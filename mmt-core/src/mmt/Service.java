package mmt;


import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Comparator;
import java.time.Duration;



/** Class Service, consisting of an aggregation of Stations (schedules)
	* and its respective total cost
	*/



public class Service implements Serializable, Iterable<Station> {

	/** This Service's identification number. */
	private int _id;
	/** This Service's total cost. */
	private double _cost;
	/** This Service's aggregation of Stations. */
	private LinkedList<Station> _stations = new LinkedList<Station>();



	/** Constructor for class Service.
		* @param id Service's id
		* @param cost the Service's cost
		*/
	public Service(int id, double cost) {
		_id = id;
		_cost = cost;
}



	/** @return this Service's identification number */
	public int getId() {return _id;}
	/** @return this Service's cost */
	public double getCost() {return _cost;}




	/** Adds a Station to this Service (adds to end of the aggregation).
		* @param station the new Station
		*/
	public void addStation(Station station) {
		_stations.add(station);
	}



	/** @return the last Station of this Service */
	public Station getArrivingStation() {return _stations.getLast();}


	/** @return the first Station of this Service */
	public Station getDepartingStation() {return _stations.getFirst();}


	/** @return the total time (minutes) between the first and last Station */
	public Duration getTime() {
		return getDepartingStation().getTimeInBetween(getArrivingStation());
	}


	/** @param station
		* @return true if this Service has the Station given as argument
		*/
	public boolean hasStation(Station station) {
		return _stations.contains(station);
	}







	/** Makes this Service iterable (in ascending order), allowing the
		* use of the foreach loop.
		* @return default iterator for this Service
		*/
	@Override
	public Iterator<Station> iterator() {
		return _stations.listIterator();
	}




	/** @return the string representation of a Service:
		*
		* Service #_id @ _cost + \n
		* Station - 1 + \n
		*   ...
		* Station - N + \n
		*
		*/
	@Override
	public String toString() {
		String res = "Serviço #" + _id + " @ " + String.format("%.2f", _cost);
		for (Station station : _stations)
			res += "\n" + station;
		return res;
	}






	/** The overriden method compare() returns 1 if service1's arrival time is
		* before service2's, 0 if they are the same, and -1 otherwise.
		*
		* @return a comparator for this class (which compares by arriving
		* Station)
		*/
	public final static Comparator<Service> getServiceComparatorByArrivingStation() {
		return new Comparator<Service>() {
			@Override
			public int compare(Service service1, Service service2) {
				long difference = service2.getArrivingStation().getTimeInBetween(service1.getArrivingStation()).toMinutes();
				if(difference > 0)
					return 1;
				else if(difference < 0)
					return -1;
				else
					return 0;
			}
		};
	}

	/** The overriden method compare() returns 1 if service1's departure time is
		* before service2's, 0 if they are the same, and -1 otherwise.
		*
		* @return a comparator for this class (which compares by departing
	 	* Station)
		*/
	public final static Comparator<Service> getServiceComparatorByDepartingStation() {
		return new Comparator<Service>() {
			@Override
			public int compare(Service service1, Service service2) {
				long difference = service2.getDepartingStation().getTimeInBetween(service1.getDepartingStation()).toMinutes();
				if(difference > 0)
					return 1;
				else if(difference < 0)
					return -1;
				else
					return 0;
			}
		};
	}

}
