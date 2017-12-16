package mmt.app.service;

import mmt.TicketOffice;
import mmt.exceptions.NoSuchStationNameException;
import mmt.app.exceptions.NoSuchStationException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;



/**
 * 3.2.4 Show services arriving at station.
 */
public class DoShowServicesArrivingAtStation extends Command<TicketOffice> {

  private Input<String> _name;

  /**
   * @param receiver
   */
  public DoShowServicesArrivingAtStation(TicketOffice receiver) {
    super(Label.SHOW_SERVICES_ARRIVING_AT_STATION, receiver);
    _name = _form.addStringInput(Message.requestStationName());
  }

  /** @see pt.tecnico.po.ui.Command#execute()
    * @see mmt.TicketOffice#getServicesArrivingAt(String)
    */
  @Override
  public final void execute() throws DialogException {
    Iterable<String> services;
		_form.parse();
		try {
	    services = _receiver.getServicesArrivingAt(_name.value());
    } catch (NoSuchStationNameException e) {
  		throw new NoSuchStationException(e.getName());
  	}
		for(String s : services)
			_display.addLine(s);
		_display.display();
  }

}
