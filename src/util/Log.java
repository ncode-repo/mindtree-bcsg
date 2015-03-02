package util;

import org.apache.log4j.Logger;

public class Log {
    public static Logger getLogger()
    {
        return Logger.getLogger(Log.class.getName());
    }


    public static void debug(String message)
    {
        getLogger().debug(prefix(message));
    }

 
    public static void debug(String message, Throwable t)
    {
        getLogger().debug(prefix(message), t);
    }

    /**
     * This is a log method with logLevel == INFO, printing is done by
     * the default logger
     *
     */
    public static void info(String message)
    {
        getLogger().info(prefix(message));
    }

    /**
     * This is a log method with logLevel == INFO, printing is done by
     * the default logger
     *
     */
    public static void info(String message, Throwable t)
    {
        getLogger().info(prefix(message), t);
    }

    /**
     * This is a log method with logLevel == WARN, printing is done by
     * the default logger
     *
     */
    public static void warn(String message)
    {
        getLogger().warn(prefix(message));
    }

    /**
     * This is a log method with logLevel == WARN, printing is done by
     * the default logger
     *
     */
    public static void warn(String message, Throwable t)
    {
        getLogger().warn(prefix(message), t);
    }

    /**
     * This is a log method with logLevel == ERROR, printing is done by
     * the default logger
     *
     */
    public static void error(String message)
    {
        getLogger().error(prefix(message));
    }

    /**
     * This is a log method with logLevel == ERROR, printing is done by
     * the default logger
     *
     */
    public static void error(String message, Throwable t)
    {
        getLogger().error(prefix(message), t);
    }

    /**
     * This is a log method with logLevel == ERROR, printing is done by
     * the default logger
     *
     */
    public static void error(Throwable t)
    {
        error("", t);
    }
    
    public static void log(String message)
    {
   		
    }
    
  
    private static String prefix(String txt)
    {
    	if (txt != null && !txt.equals(""))
    	{
    		return ("[" + getCurrentMethodName() + "]:" + txt);
    	}
    	else
    	{
    		return ("");
    	}
    }
    
	private static String getCurrentMethodName()
	{
		return getCurrentMethodName(4);
	}
	
	private static String getCurrentMethodName(int level) 
	{
		StringBuffer callingMethod = new StringBuffer("");
	
		Throwable throwable = new Throwable();
	
		throwable.fillInStackTrace();
		StackTraceElement ste[] = throwable.getStackTrace();
		if( ste != null && ste.length > level ) 
		{			
			String s = ste[level].getClassName();
			callingMethod.append(s.substring(ste[level].getClassName().lastIndexOf(".")+1));
			callingMethod.append(".").append(ste[level].getMethodName()).append("()");
		}
		
		
		return callingMethod.toString();
	}


}
