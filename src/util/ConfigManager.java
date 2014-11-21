package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
	private final String clzz = "ConfigManager.class";
	private static ConfigManager configManager;
	// Name of the properties file, if none was set, a default will be searched
	private String propsFileName = "";

	private static final String DEFAULT_ICM_PROPERTIES = "/conf/config.properties";

	private Properties cpProps = null;
	
	public static ConfigManager getInstance()
    {
    	if (configManager == null)
    	{
    		// init the configManager
    		configManager = new ConfigManager(null);
    	}
    	
    	return configManager;
    }
	public ConfigManager(String propsResourceName, InputStream is) throws Exception {
		super();
		if (propsResourceName != null) {
			this.propsFileName = propsResourceName;
		} else {
			this.propsFileName = DEFAULT_ICM_PROPERTIES;
		}

		loadIt(is);
	}

	public ConfigManager(String propsResourceName){
		super();
		if (propsResourceName != null) {
			this.propsFileName = propsResourceName;
		} else {
			this.propsFileName = DEFAULT_ICM_PROPERTIES;
		}

		try {
			loadIt();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadIt() throws Exception {
		String propsFile = getPropsFileName();
		printMsg(" >> Loading properties: " + propsFile);
		InputStream is = getClass().getResourceAsStream(propsFile);
		loadIt(is);
	}

	private void loadIt(InputStream is) throws Exception {
		String propsFile = getPropsFileName();

		try {
			printMsg(" >>>> Loading properties: " + propsFile);
			if (is == null) {
				// Resource not found!
				printMsg("ERROR: Unable to find resource: " + propsFile);
				throw new Exception("Property no found");
			}
			cpProps = new Properties(); // Initialize!
			cpProps.load(is);
		} catch (IOException ioe) {
			printMsg("ERROR: Could not load properties from resource [" + propsFile + "]");
			ioe.printStackTrace();
			throw new Exception(ioe.getMessage());
		} catch (Exception e) {
			printMsg("ERROR: Could not load properties from resource [" + propsFile + "]");
			throw new Exception(e.getCause().getMessage());
		}
	}

	public String getPropsFileName() {
		return propsFileName;
	}

	public void setPropsFileName(String propertyFileName) {
		propsFileName = propertyFileName;
	}

	public String getProperty(String propertyName) {
		return (getCpProps().getProperty(propertyName, ""));
	}

	public String getProperty(String propertyName, String defaultValue) {
		return (getCpProps().getProperty(propertyName, defaultValue));
	}

	private void printMsg(String msg) {
		if (msg != null) {
			System.out.println(clzz + ": " + msg);
		}
	}

	public Properties getCpProps() {
		return (cpProps == null ? null : (Properties) cpProps.clone());
	}

	public void setCpProps(Properties properties) {
		cpProps = properties;
	}

	public boolean isEmpty() {
		return (getCpProps() == null ? true : getCpProps().isEmpty());
	}

	public void printProps() {
		if (!this.isEmpty()) {
			printMsg("*** Dumping Properties ***");
			printMsg("Properties loaded are: " + asText());
		}
	}

	public String asText() {
		Properties p = this.getCpProps();
		return (p.toString());
	}
} // end, class CpSrvProperties
