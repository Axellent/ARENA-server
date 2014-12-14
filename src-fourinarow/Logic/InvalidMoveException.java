package Logic;

/*
 * @ Author Andreas Lönnermark
 */

@SuppressWarnings("serial")
public class InvalidMoveException extends Exception {
	String error;
	
    public InvalidMoveException () {
        super();
        error = "unknown error";
    }

    public InvalidMoveException (String message) {
        super (message);
        error = message;
    }

    public InvalidMoveException (Throwable cause) {
        super (cause);
    }

    public String getError(){
    	return error;
    }
}