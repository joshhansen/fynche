package colors.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class SimplerFormatter extends Formatter {

//	    Date dat = new Date();
//	    private final static String format = "{0,date} {0,time}";
//	    private MessageFormat formatter;
//
//	    private Object args[] = new Object[1];

	    // Line separator string.  This is the value of the line.separator
	    // property at the moment that the SimpleFormatter was created.
	    private String lineSeparator = java.security.AccessController.doPrivileged(
	               new sun.security.action.GetPropertyAction("line.separator"));

	    /**
	     * Format the given LogRecord.
	     * <p>
	     * This method can be overridden in a subclass.
	     * It is recommended to use the {@link Formatter#formatMessage}
	     * convenience method to localize and format the message field.
	     *
	     * @param record the log record to be formatted.
	     * @return a formatted log record
	     */
	    public synchronized String format(LogRecord record) {
	        StringBuffer sb = new StringBuffer();
	        final Level level = record.getLevel();
			sb.append(tabs(level));
//	        sb.append(level.getLocalizedName());
//	        sb.append(" ");
	        String message = formatMessage(record);
	        sb.append(message);
	        sb.append("\t\t\t(");
	        if (record.getSourceClassName() != null) {
	            sb.append(record.getSourceClassName());
	        } else {
	            sb.append(record.getLoggerName());
	        }
	        if (record.getSourceMethodName() != null) {
	            sb.append(" ");
	            sb.append(record.getSourceMethodName());
	        }
	        sb.append(")");
	        sb.append(lineSeparator);
	        return sb.toString();
	    }
	    
	    private static String tabs(final Level level) {
	    	return repeat("\t", depth(level));
	    }
	    
	    private static String repeat(final String s, final int times) {
	    	StringBuilder sb = new StringBuilder();
	    	for(int i = 0; i < times; i++) {
	    		sb.append(s);
	    	}
	    	return sb.toString();
	    }
	    
	    private static int depth(Level level) {
	    	if(level.equals(Level.INFO))
	    		return 0;
	    	else if(level.equals(Level.FINE))
	    		return 1;
	    	else if(level.equals(Level.FINER))
	    		return 2;
	    	else if(level.equals(Level.FINEST))
	    		return 3;
	    	else if(level.equals(Level.WARNING))
	    		return 0;
	    	throw new IllegalArgumentException("Don't recognize level " + level);
	    }
	}