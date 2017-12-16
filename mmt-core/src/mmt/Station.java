package mmt;


import java.io.Serializable;
import java.time.LocalTime;
import java.time.Duration;




/** Class which represents a departure in a station (definied by its name)
	* at a given time (a point in space and time).
	*/



public class Station implements Serializable {

	/** This Station's name. */
	private String _name;
	/** This Station's time (of departure). */
	private LocalTime _time;



	/** Constructor for class Station (used to compare Stations by their name).
		* @param name Station's name
		*/
	public Station(String name) {
		_name = name;
		_time = LocalTime.of(0,0); /* to avoid nullPointerExceptions */
	}

	/** Constructor for class Station.
		* @param name Station's name
		* @param time Station's time
		*/
	public Station(String name, LocalTime time) {
		_name = name;
		_time = time;
	}



	/** @return this Station's name */
	public String getName() {return _name;}
	/** @return this Station's time */
	public LocalTime getTime() {return _time;}




	/** @param Station the Station we are comparing this Station with
		* @return the duration between the Stations
		*/
	public Duration getTimeInBetween(Station station) {
		return Duration.between(this.getTime(), station.getTime());
	}





	/** Tests whether this Station and an object are equal.
	  * This is done by comparing their names.
		* @param o the Object we are comparing this Station with
		* @return true if they are equal
		*/
	@Override
	public boolean equals(Object o) {
		if(o instanceof Station) {
			Station station = (Station) o;
			return _name.equals(station.getName());
		}
		return false;
	}



	/** @return the string representation of a Station:
		* hh:mm Name
		*/
	@Override
	public String toString() {
		return _time + " " + _name;
	}
}
