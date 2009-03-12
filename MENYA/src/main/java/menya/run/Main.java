package menya.run;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class Main {
	private static Logger logger = Logger.getLogger(Main.class);
	static {
		DOMConfigurator.configure("log4j-configuration.xml");
	}
	public static void main(String[] argv) {
		logger.debug("working?");
	}
}
