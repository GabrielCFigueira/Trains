package mmt.app.passenger;

import mmt.TicketOffice;
import mmt.app.exceptions.BadPassengerNameException;
import mmt.app.exceptions.DuplicatePassengerNameException;
import mmt.app.exceptions.NoSuchPassengerException;
import mmt.exceptions.InvalidPassengerNameException;
import mmt.exceptions.NoSuchPassengerIdException;
import mmt.exceptions.NonUniquePassengerNameException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;


/**
 * §3.3.4. Change passenger name.
 */
public class DoChangerPassengerName extends Command<TicketOffice> {

  private Input<Integer> _id;
	private Input<String> _name;

  /**
   * @param receiver
   */
  public DoChangerPassengerName(TicketOffice receiver) {
    super(Label.CHANGE_PASSENGER_NAME, receiver);
    _id = _form.addIntegerInput(Message.requestPassengerId());
		_name = _form.addStringInput(Message.requestPassengerName());
  }

  /** @see pt.tecnico.po.ui.Command#execute()
    * @see mmt.TicketOffice#changePassengerName(int,String)
    */
  @Override
  public final void execute() throws DialogException {
		_form.parse();
		try {
    _receiver.changePassengerName(_id.value(), _name.value());
		} catch (NoSuchPassengerIdException e) {
			throw new NoSuchPassengerException(e.getId());
		} catch (NonUniquePassengerNameException e) {
			throw new DuplicatePassengerNameException(e.getName());
		}
	}
}
