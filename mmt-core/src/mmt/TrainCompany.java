package mmt;

import mmt.exceptions.BadDateSpecificationException;
import mmt.exceptions.BadEntryException;
import mmt.exceptions.BadTimeSpecificationException;
import mmt.exceptions.InvalidPassengerNameException;
import mmt.exceptions.NoSuchDepartureException;
import mmt.exceptions.NoSuchPassengerIdException;
import mmt.exceptions.NoSuchServiceIdException;
import mmt.exceptions.NoSuchStationNameException;
import mmt.exceptions.NoSuchItineraryChoiceException;
import mmt.exceptions.NonUniquePassengerNameException;

import mmt.serviceselectors.ServiceSelector;



import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;



/**
 * A train company has schedules (services) for its trains and passengers that
 * acquire itineraries based on those schedules.
 */
public class TrainCompany implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201708301010L;


	/** This TrainCompany's list of Services. */
	private Map<Integer, Service> _services;



	/** This TrainCompany's list of Passengers. */
	private Map<Integer, Passenger> _passengers = new TreeMap<Integer, Passenger>();
	/** Total number of Passengers. */
	private int _passengersNumber = 0;



	/** This TrainCompany's list of temporary Itineraries. */
	transient private List<Itinerary> _temporaryItineraries = new ArrayList<Itinerary>();







  /** Constructor for class TrainCompany.
    * @param services
    */
  public TrainCompany(Map<Integer, Service> services) {
    _services = services;
  }

  /** Default Constructor for class TrainCompany. */
  public TrainCompany() {
    _services = new TreeMap<Integer, Service>();
  }








  /** Reads information from a file and interprets it to create Services,
    * Passengers and Itineraries.
    * The information is in string format, which is suposed to be read
    * line by line, with each line representing an object (Service,
    * Passenger or Itinerary).
    * It calls registerFromFields() to read each line, after spliting
    * the line into an array of strings by the "|" character.
		*
    * @param filename the name of the file to be read
    * @throws IOException
    * @throws BadEntryException if there is an invalid entry in the file
    * @throws NonUniquePassengerNameException if there already is a passenger
    * with that name
    * @throws NoSuchPassengerIdException
    * @throws NoSuchServiceIdException
    */
  public void importFile(String filename) throws IOException, BadEntryException,
NonUniquePassengerNameException, NoSuchPassengerIdException, NoSuchServiceIdException {
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String s;
		while((s = in.readLine()) != null) {
			String[] fields = s.split("\\|");
			registerFromFields(fields);
		}
    in.close();
	}




  /** Determines which type is the object to be created, defined by the
    * first string in "fields", and calls the respective method to
    * construct it.
		*
    * @param fields
    * @throws BadEntryException if there is an invalid entry in the file
    * @throws NonUniquePassengerNameException if there already is a passenger
    * with that name
    * @throws NoSuchPassengerIdException
    * @throws NoSuchServiceIdException
    */
	public void registerFromFields(String[] fields) throws BadEntryException,
NonUniquePassengerNameException, NoSuchPassengerIdException, NoSuchServiceIdException {
		// Regular expression pattern to match a service.
		Pattern patService = Pattern.compile("^(SERVICE)");
    // Regular expression pattern to match a passenger.
    Pattern patPassenger = Pattern.compile("^(PASSENGER)");
    // Regular expression pattern to match a itinerary.
    Pattern parItinerary = Pattern.compile("^(ITINERARY)");


		if (patService.matcher(fields[0]).matches()) {
	    registerService(fields);
    } else if (patPassenger.matcher(fields[0]).matches()) {
      registerPassenger(fields[1]);
    } else if (parItinerary.matcher(fields[0]).matches()) {
      registerItinerary(fields);
	  } else
      throw new BadEntryException(fields[0]);
  }



  /** Creats a Service and adds to the Map of Services in this object.
    * The array of strings is suposed to have the following format:
		*
    * SERVICE|id|cost|Station1-time|Station1-name|...
		*
    * @param fields
    */
	public void registerService(String[] fields) {
		int id = Integer.parseInt(fields[1]);
		double cost = Double.parseDouble(fields[2]);
		Service service = new Service(id, cost);
		LocalTime lt;
		for(int i = 3; i < fields.length; i+=2) {
			lt = LocalTime.parse(fields[i]);
		  service.addStation(new Station(fields[i + 1], lt));
		}
		addService(service);
	}


  /** Creats a Itinerary and adds it to its respective Passenger.
    * The array of strings is suposed to have the following format:
    *
    * ITINERARY|passengerID|Service1/firstStation1/lastStation1|...
    *
    * @param fields
    * @throws NoSuchPassengerIdException
    * @throws NoSuchServiceIdException
    */
  public void registerItinerary(String[] fields) throws NoSuchPassengerIdException,
NoSuchServiceIdException {
    Passenger passenger = getPassenger(Integer.parseInt(fields[1]));
    Itinerary itinerary = new Itinerary(LocalDate.parse(fields[2]));
    Service service;
    Station first, last;
    String[] pseudoSegment;
    for(int i = 3; i < fields.length; i++) {
      pseudoSegment = fields[i].split("/");
      service = getService(Integer.parseInt(pseudoSegment[0]));
      first = new Station(pseudoSegment[1]);
      last = new Station(pseudoSegment[2]);
      for(Station station : service) {
        if(station.equals(first))
          first = station;
        else if(station.equals(last)) {
          last = station;
          break;
        }
      }
      itinerary.addServiceSegment(new ServiceSegment(service, first, last));
    }
    passenger.addItinerary(itinerary);
  }













	/** Adds a Service to the list of Services.
		* @param service
    */
	public void addService(Service service) {
		_services.put(service.getId(), service);
	}


	/** @return a Service chosen by its id.
		* @param id
    * @throws NoSuchServiceIdException
    */
	public Service getService(int id) throws NoSuchServiceIdException{
		if(_services.containsKey(id))
		  return _services.get(id);
    else
      throw new NoSuchServiceIdException(id);
	}


	/** @return this TrainCompany's Services. */
	public Map<Integer, Service> getAllServices() {
		return Collections.unmodifiableMap(_services);
	}


	/** @return a list of the Services chosen by the ServiceSelector
    *
    * @param ss the Service selector
		* @param sation
    * @throws NoSuchStationNameException if there is no station with
    * stationName as name
    */
	public List<Service> getSpecificServices(ServiceSelector ss, Station station)
throws NoSuchStationNameException {
		ArrayList<Service> res = new ArrayList<Service>();
		Collection<Service> collection = getAllServices().values();
		boolean noSuchStation = true;
		for(Service s : collection) {
			if(ss.isValid(s, station)) {
				res.add(s);
				noSuchStation = false;
			}
			else if(s.hasStation(station))
				noSuchStation = false;
		}
		if(noSuchStation)
			throw new NoSuchStationNameException(station.getName());
		return res;
	}











	/** Changes the Passenger's name specified by its id.
		*
		* @param id the Passenger's id
		* @param name the Passenger's new name
    * @throws NoSuchPassengerIdException
    * @throws NonUniquePassengerNameException if there already is a passenger
    * with that name
    */
	public void changePassengerName(int id, String name) throws
NoSuchPassengerIdException, NonUniquePassengerNameException {
		if(!_passengers.containsKey(id))
			throw new NoSuchPassengerIdException(id);
    Set<Integer> passengerIDs = _passengers.keySet();
		for(Integer i : passengerIDs)
			if(_passengers.get(i).getName().equals(name))
				throw new NonUniquePassengerNameException(name);
		_passengers.get(id).setName(name);
	}


	/** @return a Passenger chosen by its id
		* @param id
    * @throws NoSuchPassengerIdException
    */
	public Passenger getPassenger(int id) throws NoSuchPassengerIdException {
    if(!_passengers.containsKey(id))
			throw new NoSuchPassengerIdException(id);
		return _passengers.get(id);
	}

	/** @return this TrainCompany's Passengers */
	public Map<Integer, Passenger> getAllPassengers() {
		return Collections.unmodifiableMap(_passengers);
	}


	/** Registers a Passenger.
    * @param name
    * @throws NonUniquePassengerNameException if there already is a Passenger
    * with that name
    */
	public void registerPassenger(String name) throws NonUniquePassengerNameException {
    Set<Integer> passengerIDs = _passengers.keySet();
    for(Integer i : passengerIDs)
			if(_passengers.get(i).getName().equals(name))
				throw new NonUniquePassengerNameException(name);
		_passengers.put(_passengersNumber, new Passenger(name, _passengersNumber));
    _passengersNumber++;
	}















    /** Searches for itineraries from departure to arrival.
      * If there are more than 1 candidate for the same starting service, only
      * the 1. more direct 2. fastest, will be chosen.
      * The itineraries are then saved in a local attribute (_temporaryItineraries)
      * @param departure
      * @param arrival
      * @param localdate the Itineraries' date
      * @throws NoSuchStationNameException if either departure or arrival don't exist
      */
  public void search(Station departure, Station arrival, LocalDate localdate) throws NoSuchStationNameException {

    /* initializes _temporaryItineraries */
    _temporaryItineraries = new ArrayList<Itinerary>();

    Itinerary result;
    Set<Integer> serviceIDs = _services.keySet(); //to iterate over the Services of this TrainCompany
    Service service;

    /* to determime if departure or arrival are non existent */
    boolean hasArrival, hasDeparture;
    hasArrival = hasDeparture = false;

    /* to garantee that a itinerary will only go through the same service once */
    Map<Integer, Boolean> usedServices = new TreeMap<Integer, Boolean>();

    for(int i : serviceIDs)
      usedServices.put(i, false);

    for (int i : serviceIDs) {
      service = _services.get(i);
      for(Station station : service)
        if(departure.equals(station)) {
          hasDeparture = true;
          if(departure.getTimeInBetween(station).toMinutes() >= 0) {

            usedServices.put(i, true);
            result = search(new ArrayList<ServiceSegment>(), service, usedServices,
station, arrival, localdate, serviceIDs);
            usedServices.put(i, false);

            if(result != null)
              _temporaryItineraries.add(result);
          }
        }
    }

    if(_temporaryItineraries.size() == 0) {
      for(Service s : getAllServices().values())
        if(s.hasStation(arrival))
          hasArrival = true;
      if(hasArrival == false)
        throw new NoSuchStationNameException(arrival.getName());
    }
    else
      Collections.sort(_temporaryItineraries);
    if(hasDeparture == false)
      throw new NoSuchStationNameException(departure.getName());
  }









  /** Recursion based function; does most of the work for the other search()
    * @param ss the list of service segments built untill now
    * @param service starting Service
    * @param usedServices map representing the used Services
    * @param first the starting point in service
    * @param last the Station we want to get to
    * @param localdate the date of the Itinerary
    * @param serviceIDs
    * @return an Itinerary or null if none were found
    */
  private Itinerary search(List<ServiceSegment> ss, Service service,
Map<Integer, Boolean> usedServices, Station first, Station last,
LocalDate localdate, Set<Integer> serviceIDs) {

    List<Itinerary> obtained = new ArrayList<Itinerary>();

    Itinerary itinerary;
    Station departure;
    int minimum = Integer.MAX_VALUE;

    /* stop condition: if it's successful, a new Itinerary is created and returned */
    if(service.hasStation(last)) {
      for(Station station : service)
        if(station.equals(last) && first.getTimeInBetween(station).toMinutes() >= 0) {
          ss.add(new ServiceSegment(service, first, station));
          itinerary = new Itinerary(localdate);
          for(ServiceSegment s : ss)
            itinerary.addServiceSegment(s);
          ss.remove(ss.size() - 1);
          return itinerary;
        }
    }

    /* otherwise, it will search for the last Station in other Services (by calling this function again) */
    else {
      for(Station station : service)
        for(int i : serviceIDs)
          if(!usedServices.get(i)) {
            Service nextService = _services.get(i);
            if(nextService.hasStation(station) && !station.equals(service.getDepartingStation()))
              for(Station nextStation : nextService)
                if(nextStation.equals(station) && station.getTimeInBetween(nextStation).toMinutes() >= 0) {

                  ss.add(new ServiceSegment(service, first, station));
                  usedServices.put(i, true);
                  itinerary = search(ss, nextService, usedServices, nextStation,
  last, localdate, serviceIDs);
                  ss.remove(ss.size() - 1);
                  usedServices.put(i, false);

                  /* if there is no possible itinerary, null will be returned */
                  if(itinerary != null) {
                    /* the itineraries must have a minimal number of Segments
                    (they must be as direct as possible) */
                    if(minimum > itinerary.getSegmentNumber()) {
                      minimum = itinerary.getSegmentNumber();
                      obtained = new ArrayList<Itinerary>();
                    }
                    else if(minimum < itinerary.getSegmentNumber())
                      break;
                    obtained.add(itinerary);
                  }
                  break;
                }
          }
    }

    /** from the list of itineraries obtained, the smallest is chosen (smallest by the natural order)
      * @see mmt.Itinerary#compareTo(Itinerary)
      */
    if(obtained.size() > 0)
      return Collections.min(obtained);
    else
      return null;
  }




  /** @return the list of temporary Itineraries, built by search()
    * @see mmt.TrainCompany#search(Station,Station,LocalDate)
    */
  public List<Itinerary> getTemporaryItineraries() {
    return _temporaryItineraries;
  }




  /** Registers the specified Itinerary in a Passenger
    * @param passengerId
    * @param itineraryNumber
    * @throws NoSuchItineraryChoiceException if the itineraryNumber is invalid
    * @throws NoSuchPassengerIdException
    */
  public void commitItinerary(int passengerId, int itineraryNumber) throws
NoSuchItineraryChoiceException, NoSuchPassengerIdException {

    Passenger passenger = getPassenger(passengerId);

    if(itineraryNumber > 0 && itineraryNumber <= _temporaryItineraries.size())
      passenger.addItinerary(_temporaryItineraries.get(itineraryNumber - 1));
    else if(itineraryNumber != 0) //because 0 is not an invalid number, it just means no action
      throw new NoSuchItineraryChoiceException(passengerId, itineraryNumber);

  }

}
