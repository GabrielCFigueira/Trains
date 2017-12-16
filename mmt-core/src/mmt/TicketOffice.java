package mmt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.LinkedList;
import java.util.ArrayList;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;



import mmt.exceptions.BadDateSpecificationException;
import mmt.exceptions.BadTimeSpecificationException;
import mmt.exceptions.BadEntryException;
import mmt.exceptions.ImportFileException;
import mmt.exceptions.InvalidPassengerNameException;
import mmt.exceptions.MissingFileAssociationException;
import mmt.exceptions.NoSuchPassengerIdException;
import mmt.exceptions.NoSuchServiceIdException;
import mmt.exceptions.NoSuchStationNameException;
import mmt.exceptions.NoSuchItineraryChoiceException;
import mmt.exceptions.NonUniquePassengerNameException;

import mmt.serviceselectors.SelectorByArrivingStation;
import mmt.serviceselectors.SelectorByDepartingStation;


/**
 * Façade for handling persistence and other functions.
 * Has a TrainCompany, which does most of the work.
 */
public class TicketOffice {

  /** The object doing most of the actual work. */
  private TrainCompany _trains = new TrainCompany();

	/** The name of the file associated with this TicketOffice. */
  private String _associatedFilename = null;

	/** Determines whether changes were made since last save. */
	private boolean _changesMade = true;









	/** Destroys the information about passengers and itineraries and resets
		* the associated file name.
		*/
  public void reset() {
    _trains = new TrainCompany(_trains.getAllServices());
		_associatedFilename = null;
		_changesMade = true;
  }


  /** If changes were made, it serializes the TrainCompany to the file
    * associated with this application
    *
    *	@param filename the name of the file
    * @throws IOException
    * @throws MissingFileAssociationException if there is no file associated
    * this application
    */
  public void save() throws IOException, MissingFileAssociationException {
    if(_associatedFilename == null)
      throw new MissingFileAssociationException();
    if(_changesMade) {
      ObjectOutputStream oos =
  new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_associatedFilename)));
      oos.writeObject(_trains);
      oos.close();
      _changesMade = false;
    }
  }

	/** If changes were made, it serializes the TrainCompany to the file "filename".
		* The	file becomes associated with this application.
		*
		*	@param filename the name of the file
		* @throws IOException
		*/
  public void save(String filename) throws IOException {
		if(_changesMade) {
			ObjectOutputStream oos =
new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
			oos.writeObject(_trains);
			oos.close();
      _associatedFilename = filename;
			_changesMade = false;
		}
  }


	/** Creates a TrainCompany from the file "filename".
		* The	file becomes associated with this application.
		*
		*	@param filename the name of the file
		* @throws FileNotFoundException
		* @throws IOException
		* @throws ClassNotFoundException
		*/
  public void load(String filename) throws FileNotFoundException,
ClassNotFoundException, IOException {
    ObjectInputStream ois =
new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
		_trains = (TrainCompany) ois.readObject();
		ois.close();
    _associatedFilename = filename;
		_changesMade = false;
  }


	/** Reads characters from the file with name "datafile".
		* The work is all done by the TrainCompany.
    * @throws ImportFileException
		*/
  public void importFile(String datafile) throws ImportFileException {
    _changesMade = true;
		try {
			_trains.importFile(datafile);
		} catch (IOException | BadEntryException | NonUniquePassengerNameException |
NoSuchPassengerIdException | NoSuchServiceIdException e) {
			throw new ImportFileException(e);
		}
  }















	/** @return a list of strings, each representing the string format of the
		* Services in the TrainCompany.
		*/
	public List<String> getAllServices() {
		Collection<Service> services = _trains.getAllServices().values();
		List<String> res = new LinkedList<String>();
		for(Service s : services)
			res.add(s.toString());
		return res;
	}


	/** @return a string representation of a Service, chosen by its id.
		* @throws NoSuchServiceIdException
		*/
	public String getServiceById(int id) throws NoSuchServiceIdException {
		return _trains.getService(id).toString();
	}


	/** @return the list of strings, each representing a Service which arrives
		* at the Sation with stationName as name.
		*
		* @param stationName
		* @throws NoSuchStationNameException if there is no Station with stationName
		* as name
		*/
	public List<String> getServicesArrivingAt(String stationName) throws NoSuchStationNameException {
		List<Service> services = _trains.getSpecificServices(new SelectorByArrivingStation(),
new Station(stationName));
    services.sort(Service.getServiceComparatorByArrivingStation());
		List<String> res = new LinkedList<String>();
		for(Service s : services)
			res.add(s.toString());
		return res;
	}


	/** @return the list of strings, each representing a Service which departs
		* froms the Sation with stationName as name.
		*
		* @param stationName
		* @throws NoSuchStationNameException if there is no Station with stationName
		* as name
		*/
	public List<String> getServicesDepartingFrom(String stationName) throws NoSuchStationNameException{
		List<Service> services = _trains.getSpecificServices(new SelectorByDepartingStation(),
new Station(stationName));
    services.sort(Service.getServiceComparatorByDepartingStation());
		List<String> res = new LinkedList<String>();
		for(Service s : services)
			res.add(s.toString());
		return res;
	}













	/** @return the list of strings, each representing the string format of the
		* Passengers in the TrainCompany.
		*/
	public List<String> getAllPassengers() {
		Collection<Passenger> passengers = _trains.getAllPassengers().values();
		List<String> res = new LinkedList<String>();
		for(Passenger p : passengers)
			res.add(p.toString());
		return res;
	}


	/** @return a string representation of a Passenger, chosen by his id.
		* @param id
		* @throws NoSuchPassengerIdException
		*/
	public String getPassengerById(int id) throws NoSuchPassengerIdException {
		return _trains.getPassenger(id).toString();
	}


	/** Registers a Passenger in the TrainCompany.
		* @param name the Passenger's name
		* @throws NonUniquePassengerNameException if there already is a Passenger
		* with that name
		*/
	public void registerPassenger(String name) throws NonUniquePassengerNameException {
		_trains.registerPassenger(name);
		_changesMade = true;
	}


	/** Changes the Passenger's name specified by its id.
		*
		* @param id
		* @param name
		* @throws NoSuchPassengerIdException
		* @throws NonUniquePassengerNameException
		*
		*/
	public void changePassengerName(int id, String name) throws NoSuchPassengerIdException, NonUniquePassengerNameException {
		_trains.changePassengerName(id, name);
		_changesMade = true;
	}












  /** @param id the Passenger's id
    * @return a list of Strings representing the Itineraries of the Passenger
    */
  public List<String> getPassengerItineraries(int id) throws NoSuchPassengerIdException {
    return stringPassengerItineraries(_trains.getPassenger(id));
  }

  /** @param passaenger
    * @return the String representation of a Passenger's Itineraries
    */
  private List<String> stringPassengerItineraries(Passenger passenger) {
    List<String> res = new ArrayList<String>();
    res.add("== Passageiro " + passenger.getId() + ": " + passenger.getName() + " ==\n");
    int itineraryID = 1;
    Collections.sort(passenger.getItineraries(), Itinerary.getItineraryComparatorByDate());
    for(Itinerary i : passenger.getItineraries())
      res.add("Itinerário " + itineraryID++ + " para " + i.getDate() + " @ " +
String.format("%.2f", i.getCost()).replace(",",".") + "\n" + i.toString());
    return res;
  }



  /** @return a list of Strings representing all of the Itineraries in existance */
  public List<String> getAllItineraries() {
    List<String> res = new ArrayList<String>();
    List<String> itineraries;
    Collection<Passenger> passengers = _trains.getAllPassengers().values();
    for(Passenger passenger : passengers) {
      itineraries = stringPassengerItineraries(passenger);
      if(itineraries.size() > 1) {
        for(String s : itineraries)
          res.add(s);
      }
    }
    return res;
  }




  /** Asks the TrainCompany to search for Itineraries between departureStation and
    * arrivalStation, and return the results in a list of Strings.
    *
    * @param passengerId
    * @param departureStation
    * @param arrivalStation
    * @param departureDate
    * @param departureTime the minimum time of departure
    * @throws NoSuchPassengerIdException
    * @throws BadTimeSpecificationException
    * @throws BadDateSpecificationException
    * @throws NoSuchStationNameException
    * @return a list of itineraries in String format
    *
    */
  public List<String> search(int passengerId, String departureStation, String arrivalStation, String departureDate,
String departureTime) throws NoSuchPassengerIdException, BadTimeSpecificationException, BadDateSpecificationException, NoSuchStationNameException {

    _trains.getPassenger(passengerId); /*just checking if he exists */
    Station arrival = new Station(arrivalStation);
    Station departure;
    LocalDate localdate;

    try {
      departure = new Station(departureStation, LocalTime.parse(departureTime));
    } catch (DateTimeParseException e) {
      throw new BadTimeSpecificationException(departureTime);
    }
    try {
      localdate = LocalDate.parse(departureDate);
    } catch (DateTimeParseException e) {
      throw new BadDateSpecificationException(departureDate);
    }

    _trains.search(departure, arrival, localdate);

    List<Itinerary> itineraries = _trains.getTemporaryItineraries();
    List<String> res = new ArrayList<String>();

    int itineraryID = 1;
    for(Itinerary i : itineraries)
      res.add("Itinerário " + itineraryID++ + " para " + i.getDate() + " @ " +
String.format("%.2f", i.getCost()).replace(",",".") + "\n" + i.toString());

    if(itineraryID > 1)
      res.set(0, "\n" + res.get(0));

    return res;

  }


  /** @see mmt.TrainCompany#commitItinerary(int,int) */
  public void commitItinerary(int passengerId, int itineraryNumber) throws NoSuchPassengerIdException,
NoSuchItineraryChoiceException {

    _trains.commitItinerary(passengerId, itineraryNumber);
    _changesMade = true;
  }




}
