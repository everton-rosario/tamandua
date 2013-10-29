package br.com.tamandua.service.vo.validator;

/**
 * @author sma_erosario
 *
 */
public class LevelMessageVO {
    private Level level;
    private String message;
    private Throwable throwable;
    /**
     * @return the level
     */
    public Level getLevel() {
        return level;
    }
    /**
     * @param level the level to set
     */
    public void setLevel(Level level) {
        this.level = level;
    }
    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    /**
     * @return the throwable
     */
    public Throwable getThrowable() {
        return throwable;
    }
    /**
     * @param throwable the throwable to set
     */
    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    /**
     * @param level
     * @param message
     * @param throwable
     */
    public LevelMessageVO(Level level, String message) {
        this(level, message, null);
    }

    /**
     * @param level
     * @param message
     * @param throwable
     */
    public LevelMessageVO(Level level, String message, Throwable throwable) {
        super();
        this.level = level;
        this.message = message;
        this.throwable = throwable;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LevelMessageVO [");
        if (level != null) {
            builder.append("level=");
            builder.append(level);
            builder.append(", ");
        }
        if (message != null) {
            builder.append("message=");
            builder.append(message);
            builder.append(", ");
        }
        if (throwable != null) {
            builder.append("throwable=");
            builder.append(throwable);
        }
        builder.append("]");
        return builder.toString();
    }
    
    

}
