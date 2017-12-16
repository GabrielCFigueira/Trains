package mmt;

import java.time.Duration;
import java.util.List;
import java.util.Iterator;
import java.io.Serializable;



/** Class for representing a fraction or a segment of a Service. This is done
  * by having a reference to an actual Service and the two Stations which
  * limit this segment
  */



public class ServiceSegment implements Serializable {

  /** This ServiceSegment's Service */
  private Service _service;
  /** The Station in which this ServiceSegment begins */
  private Station _firstStation;
  /** The Station in which this ServiceSegment ends */
  private Station _lastStation;



  /** @return the first Station */
  public Station getFirstStation() {return _firstStation;}
  /** @return the last Station */
  public Station getLastStation() {return _lastStation;}
  /** @return the Service's id */
  public int getId() {return _service.getId();}


  /** Constructor for class ServiceSegment
    * @param service
    * @param first
    * @param last
    */
  public ServiceSegment(Service service, Station first, Station last) {
    _service = service;
    _firstStation = first;
    _lastStation = last;
  }





  /** @return the duration between the first and last Station */
  public Duration getTime() {
    return _firstStation.getTimeInBetween(_lastStation);
  }


  /** @return the cost between the first and last Station */
  public double getCost() {
    return getTime().toMinutes() * _service.getCost() / _service.getTime().toMinutes();
  }


  /** @param station
		* @return true if this ServiceSegment has the Station given as argument
		*/
	public boolean hasStation(Station station) {
    Station first = null;
		for(Station s : _service) {
      if(s.equals(_firstStation))
        first = s;
      if(s.equals(station))
        if(first != null)
          return true;
      if(s.equals(_lastStation))
        break;
    }
    return false;

	}





  /** @see mmt.Service#toString()
    * @return the String representation of the Service but only between the
    * two Stations
    */
  @Override
  public String toString() {

    String res = "Serviço #" + _service.getId() + " @ " + String.format("%.2f", getCost());

    Iterator<Station> iterator = _service.iterator();
    Station station;

    if(!iterator.hasNext())
      return res; //shouldn't happen
    do {
      station = iterator.next();
    } while(iterator.hasNext() && !station.equals(_firstStation));

    res += "\n" + station;
    while(iterator.hasNext() && !station.equals(_lastStation)) {
      station = iterator.next();
      res += "\n" + station;
    }
    return res;
  }
}
