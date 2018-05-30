/**
 * 
 */
package com.github.ralfJoswig.SAP_Connection.rfc;

import java.util.Properties;

import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

/**
 * @author Ralf Joswig
 *
 */
public class SapDestinationDataProvider implements DestinationDataProvider {

	private final Properties ABAP_AS_properties;

	public SapDestinationDataProvider(SapSystem system) {
		Properties properties = new Properties();
		properties.setProperty(DestinationDataProvider.JCO_ASHOST, system.getHost());
		properties.setProperty(DestinationDataProvider.JCO_SYSNR, system.getSystemNumber());
		properties.setProperty(DestinationDataProvider.JCO_CLIENT, system.getClient());
		
		properties.setProperty(DestinationDataProvider.JCO_USER, system.getUser());
		properties.setProperty(DestinationDataProvider.JCO_PASSWD, system.getPassword());
	
		ABAP_AS_properties = properties;

	}
	/* (non-Javadoc)
	 * @see com.sap.conn.jco.ext.DestinationDataProvider#getDestinationProperties(java.lang.String)
	 */
	@Override
	public Properties getDestinationProperties(String system) {
		return ABAP_AS_properties;
	}

	/* (non-Javadoc)
	 * @see com.sap.conn.jco.ext.DestinationDataProvider#setDestinationDataEventListener(com.sap.conn.jco.ext.DestinationDataEventListener)
	 */
	@Override
	public void setDestinationDataEventListener(DestinationDataEventListener arg0) {
	}

	/* (non-Javadoc)
	 * @see com.sap.conn.jco.ext.DestinationDataProvider#supportsEvents()
	 */
	@Override
	public boolean supportsEvents() {
		return false;
	}

}
