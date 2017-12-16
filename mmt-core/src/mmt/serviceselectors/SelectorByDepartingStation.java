package mmt.serviceselectors;

import mmt.Service;
import mmt.Station;


/** ServiceSelector which selects Services based on their departing Station. */

public class SelectorByDepartingStation implements ServiceSelector {

  /** @param service
    * @param station
    * @return true if service has station as its departing (first) station */
  @Override
  public boolean isValid(Service service, Station station) {
    return service.getDepartingStation().equals(station);
  }

}
