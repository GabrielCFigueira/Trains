package mmt.app.passenger;

import mmt.TicketOffice;
import pt.tecnico.po.ui.Command;


/**
 * §3.3.1. Show all passengers.
 */
public class DoShowAllPassengers extends Command<TicketOffice> {

  /**
   * @param receiver
   */
  public DoShowAllPassengers(TicketOffice receiver) {
    super(Label.SHOW_ALL_PASSENGERS, receiver);
  }

  /** @see pt.tecnico.po.ui.Command#execute()
    * @see mmt.TicketOffice#getAllPassengers()
    */
  @Override
  public final void execute() {
		Iterable<String> passengers = _receiver.getAllPassengers();
		for(String p : passengers)
			_display.addLine(p);
		_display.display();
  }

}
