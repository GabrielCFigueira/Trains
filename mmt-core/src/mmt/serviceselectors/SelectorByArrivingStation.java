package mmt.serviceselectors;

import mmt.Service;
import mmt.Station;


/** ServiceSelector which selects Services based on their arriving Station. */

public class SelectorByArrivingStation implements ServiceSelector {

  /** @param service
    * @param station
    * @return true if service has station as its arriving (last) station */
  @Override
  public boolean isValid(Service service, Station station) {
    return service.getArrivingStation().equals(station);
  }
}
