package mmt.serviceselectors;


import mmt.Service;
import mmt.Station;


/** Interface which has a method to select Services. */

public interface ServiceSelector {

  public boolean isValid(Service service, Station station);

}
