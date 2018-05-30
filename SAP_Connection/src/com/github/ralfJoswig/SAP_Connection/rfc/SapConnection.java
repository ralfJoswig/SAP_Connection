/**
 * 
 */
package com.github.ralfJoswig.SAP_Connection.rfc;

import org.apache.log4j.Logger;

import com.github.ralfJoswig.SAP_Connection.constance.Constance;
import com.github.ralfJoswig.SAP_Connection.messages.Messages;
import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;

/**
 * @author Ralf Joswig
 *
 */
public class SapConnection {
	
	private Logger log;

	private JCoRepository repository;
	private JCoDestination destination;
	private SapDestinationDataProvider myProvider;

	/**
	 * Verbindung zu einem SAP aufbauen
	 * 
	 * @param system Daten für SAP- Verbindung
	 */
	public SapConnection(SapSystem system) {
		myProvider = new SapDestinationDataProvider(system);
		try {
			com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(myProvider);
			destination = JCoDestinationManager.getDestination(Constance.SAP_SERVER);
			repository = destination.getRepository();
		} catch (JCoException e) {
			throw new RuntimeException(e);
		}

	}
	
	public void setLogger(Class classForLogger) {
		log = Logger.getLogger( classForLogger );
	}

	/**
	 * Daten zu einem Funktionsbaustein / BAPI aus dem SAP holen
	 * 
	 * @param functionStr Name des Bausteins / BAPI
	 * @return Verweis auf die Funktion
	 */
	public JCoFunction getFunction(String functionStr) {
		JCoFunction function = null;
		try {
			function = repository.getFunction(functionStr);
		} catch (Exception e) {
			if (log != null) {
				log.error(Constance.ERROR_MSG_SAPCONNECTION, e);
			}
			throw new RuntimeException(Messages.getString("ERROR_RETRIEVING_JCO")); //$NON-NLS-1$
		}
		if (function == null) {
			throw new RuntimeException(Messages.getString("ERROR_RECEIVE_FUNCTION")); //$NON-NLS-1$
		}

		return function;
	}
	
	/**
	 * Funktionsbaustein / BAPI im SAP ausführen
	 * 
	 * @param function Die auszuführende Funktion
	 */
	public void execute(JCoFunction function) {
		try {
			JCoContext.begin(destination);
			function.execute(destination);
			JCoContext.end(destination);
		} catch (JCoException e) {
			if (log != null) {
				log.error(Constance.ERROR_MSG_SAPCONNECTION, e);
			}
		}
	}

	/**
	 * Schliesst eine Verbindung 
	 */
	public void close() {
		if (repository != null) {
			repository.clear();
			repository = null;
		}
		if (destination != null) {
			destination = null;
		}
		if (myProvider != null) {
			com.sap.conn.jco.ext.Environment.unregisterDestinationDataProvider(myProvider);
		}
	}

}
