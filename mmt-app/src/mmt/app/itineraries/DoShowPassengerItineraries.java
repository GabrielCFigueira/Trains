package mmt.app.itineraries;

import mmt.TicketOffice;
import mmt.exceptions.NoSuchPassengerIdException;
import mmt.app.exceptions.NoSuchPassengerException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;

import java.util.List;

/**
 * §3.4.2. Show all itineraries (for a specific passenger).
 */
public class DoShowPassengerItineraries extends Command<TicketOffice> {

  private Input<Integer> _id;

  /**
   * @param receiver
   */
  public DoShowPassengerItineraries(TicketOffice receiver) {
    super(Label.SHOW_PASSENGER_ITINERARIES, receiver);
    _id = _form.addIntegerInput(Message.requestPassengerId());
  }

  /** @see pt.tecnico.po.ui.Command#execute()
    * @see mmt.TicketOffice#getPassengerItineraries(int)
    */
  @Override
  public final void execute() throws DialogException {
    _form.parse();
    List<String> itineraries;
    try {
      itineraries = _receiver.getPassengerItineraries(_id.value());
		} catch (NoSuchPassengerIdException e) {
			throw new NoSuchPassengerException(e.getId());
		}
    if(itineraries.size() > 1)
      for(String s : itineraries)
        _display.addLine(s);
    else
      _display.addLine(Message.noItineraries(_id.value()));
    _display.display();
  }

}
