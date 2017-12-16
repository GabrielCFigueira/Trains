package mmt.app.itineraries;


import mmt.TicketOffice;
import pt.tecnico.po.ui.Command;


/**
 * §3.4.1. Show all itineraries (for all passengers).
 */
public class DoShowAllItineraries extends Command<TicketOffice> {

  /**
   * @param receiver
   */
  public DoShowAllItineraries(TicketOffice receiver) {
    super(Label.SHOW_ALL_ITINERARIES, receiver);
  }

  /** @see pt.tecnico.po.ui.Command#execute()
    * @see mmt.TicketOffice#getAllItineraries()
    */
  @Override
  public final void execute() {
    Iterable<String> itineraries;
    itineraries = _receiver.getAllItineraries();
    for(String s : itineraries)
      _display.addLine(s);
    _display.display();
  }

}
