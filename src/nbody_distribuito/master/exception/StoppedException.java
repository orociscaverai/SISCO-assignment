package nbody_distribuito.master.exception;

@SuppressWarnings("serial")
public class StoppedException extends RuntimeException {

    public StoppedException(String err) {
	super(err);
    }

}
