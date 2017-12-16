package mmt.app.service;

import mmt.TicketOffice;
import mmt.exceptions.NoSuchStationNameException;
import mmt.app.exceptions.NoSuchStationException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;




/**
 * 3.2.3 Show services departing from station.
 */
public class DoShowServicesDepartingFromStation extends Command<TicketOffice> {

  private Input<String> _name;

  /**
   * @param receiver
   */
  public DoShowServicesDepartingFromStation(TicketOffice receiver) {
    super(Label.SHOW_SERVICES_DEPARTING_FROM_STATION, receiver);
    _name = _form.addStringInput(Message.requestStationName());
  }

  /** @see pt.tecnico.po.ui.Command#execute()
    * @see mmt.TicketOffice#getServicesDepartingFrom(String)
    */
  @Override
  public final void execute() throws DialogException {
    Iterable<String> services;
		_form.parse();
		try {
	    services = _receiver.getServicesDepartingFrom(_name.value());
    } catch (NoSuchStationNameException e) {
  		throw new NoSuchStationException(e.getName());
  	}
		for(String s : services)
			_display.addLine(s);
		_display.display();
  }

}
