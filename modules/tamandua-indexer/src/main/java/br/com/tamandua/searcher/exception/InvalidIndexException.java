package br.com.tamandua.searcher.exception;

/**
 * @author Everton Rosario
 *
 */
public class InvalidIndexException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidIndexException() {
        super();
    }

    public InvalidIndexException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidIndexException(String message) {
        super(message);
    }

    public InvalidIndexException(Throwable cause) {
        super(cause);
    }

}
