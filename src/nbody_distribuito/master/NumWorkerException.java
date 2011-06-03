package nbody_distribuito.master;

@SuppressWarnings("serial")
public class NumWorkerException extends RuntimeException {
    public NumWorkerException(String msgError) {
	super(msgError);
    }
}
