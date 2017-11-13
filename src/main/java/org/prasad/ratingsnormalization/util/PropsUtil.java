package org.prasad.ratingsnormalization.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class PropsUtil {

	public static PropertiesConfiguration getDBPropsConfiguration() throws ConfigurationException {
		PropertiesConfiguration props = new PropertiesConfiguration("dbProps.properties");
		return props;
	}
	
	public static PropertiesConfiguration getInputConfiguration() throws ConfigurationException {
		PropertiesConfiguration props = new PropertiesConfiguration("input.properties");
		return props;
	}
}
