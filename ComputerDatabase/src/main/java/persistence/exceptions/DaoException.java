package persistence.exceptions;

public class DaoException extends Error {

    private static final long serialVersionUID = -2067133414903576948L;

    /**
     * @param e the reason that failed DB access, will init message/cause/stacktrace
     */
    public DaoException(Exception e) {
        super(e);
    }

    /**
     * @param message the reason that failed DB access
     */
    public DaoException(String message) {
        super(message);
    }
}
