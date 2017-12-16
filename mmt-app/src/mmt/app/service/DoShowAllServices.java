package mmt.app.service;

import mmt.TicketOffice;
import pt.tecnico.po.ui.Command;


/**
 * 3.2.1 Show all services.
 */
public class DoShowAllServices extends Command<TicketOffice> {

  /**
   * @param receiver
   */
  public DoShowAllServices(TicketOffice receiver) {
    super(Label.SHOW_ALL_SERVICES, receiver);
  }

  /** @see pt.tecnico.po.ui.Command#execute()
    * @see mmt.TicketOffice#getAllServices()
    */
  @Override
  public final void execute() {
		Iterable<String> services = _receiver.getAllServices();
		for(String s : services)
			_display.addLine(s);
		_display.display();
  }

}
