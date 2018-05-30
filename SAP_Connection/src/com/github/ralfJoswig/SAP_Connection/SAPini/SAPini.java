/**
 * Copyright (C) 2018 Ralf Joswig
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, see <http://www.gnu.org/licenses/>
 */
package com.github.ralfJoswig.SAP_Connection.SAPini;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.github.ralfJoswig.SAP_Connection.constance.Constance;

/**
 * @author Ralf Joswig
 *
 */
public class SAPini {

	private Logger log = null;
	private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
	
	private static final String ERROR_READ_FILE = "can not read saplogon.ini"; //$NON-NLS-1$
	private static final String SEARCHING_IN = "searching saplogon.ini in: "; //$NON-NLS-1$
	private static final String FOUND_IN = "saplogon.ini found: "; //$NON-NLS-1$

	/**
	 * Konstruktur
	 */
	public SAPini() {
	}
	
	/**
	 * List die saplogon.ini ein
	 * @throws FileNotFoundException
	 */
	public void read() throws FileNotFoundException {
		//ermitteln wo die saplogon.ini gespeichert ist
		Path pathToSAPini = findPathToSAPini();
		
		//Datei einlesen
		readSapIni(pathToSAPini);
	}
	
	/**
	 * Gibt die eingelesene saplogon.ini zurück
	 * @return
	 */
	public Map<String, Map<String, String>> getMap() {
		return data;
	}
	
	/**
	 * Gibt eine Liste der SAP-Systeme aus der saplogon.ini zurück
	 * @return
	 */
	public ArrayList<String> getListOfSystems(){
		//neue Liste anlegen
		ArrayList<String> listOfSystems = new ArrayList<>();
		
		//Beschreibung der Systeme holen
		Map<String, String> desciption = data.get("Description"); //$NON-NLS-1$
		
		//alle Beschreibungen in die Liste aufnehmen
		desciption.forEach((key, value)->{
			listOfSystems.add(value);
		});
		
		//Liste zurückgeben
		return listOfSystems;
	}

	/**
	 * Liest die saplohon.ini ein
	 * @param pathToSAPini
	 * @throws FileNotFoundException
	 */
	private void readSapIni(Path pathToSAPini) throws FileNotFoundException {
		//eine neue Untergruppe anlegen
		Map<String, String> secData = new HashMap<String, String>();

		//prüfen ob die Datei lesbar ist
		File saplogonini = new File(pathToSAPini.toString());
		if (!saplogonini.canRead() || !saplogonini.isFile()) {
			//nein, dann Fehler ausgeben
			writeToLog(ERROR_READ_FILE, "e"); //$NON-NLS-1$
			throw new FileNotFoundException(ERROR_READ_FILE);
		}

		//Datei öffnen
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(pathToSAPini.toString()));
			String line = null;
			String section = null;
			
			//Datei Zeile für Zeile einlesen
			while ((line = in.readLine()) != null) {
				// Beginnt die Zeile mit dem Zeichen für einen neuen Bereich
				if (line.startsWith("[")) { //$NON-NLS-1$
					// ja, ist es der erste Bereich
					if (section != null && !section.equals(Constance.EMPTY_STRNG)) {
						// nein, dann bisher gelesenen Bereich speichern
						data.put(section, secData);
					}

					// Namen des Bereiches auslesen und neuen Datencontainer anlegen
					section = line.substring(1, line.length() - 1);
					secData = new HashMap<String, String>();
				} else {
					// kein neuer Bereich, dann sollte es sich um einen Schlüssel und den Wert
					// handeln.
					// deshalb die Zeile in beide Teile auftrennen
					String[] keyValue = line.split("="); //$NON-NLS-1$

					// wurde die Zeile in genau zwei Teile aufgeteilt
					if (keyValue.length == 2) {
						// ja, dann speichern
						secData.put(keyValue[0], keyValue[1]);
					}
				}
			}
		} catch (IOException e) {
			//Fehler ins Log schreiben
			writeToLog(e.toString(), "e"); //$NON-NLS-1$
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}
	}

	@SuppressWarnings("rawtypes")
	public void setLogger(Class classForLogger) {
		log = Logger.getLogger(classForLogger);
	}

	/**
	 * Prüft ob am übergebenen Pfad eine saplogon.ini gibt
	 * @param pathToSapIni
	 * @return
	 */
	private boolean existsSapIniAtPath(Path pathToSapIni) {
		//prüfen ob die Datei vorhanden ist
		String fullPathToSapIni = pathToSapIni.toString();
		writeToLog(SEARCHING_IN + fullPathToSapIni,"i"); //$NON-NLS-1$
		File sapIni = new File(fullPathToSapIni);
		if (sapIni.exists()) {
			writeToLog(FOUND_IN + fullPathToSapIni,"i"); //$NON-NLS-1$
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Prüft verschiedene Verzeichnisse ob dort die saplogon.ini vorhande ist
	 * @return
	 * @throws FileNotFoundException
	 */
	private Path findPathToSAPini() throws FileNotFoundException {
		// Eigene Datei für Benutzer
		Path pathToSapIni = Paths.get(System.getenv("APPDATA"), "SAP", "Common", "saplogon.ini"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		if (existsSapIniAtPath(pathToSapIni)) {
			return pathToSapIni;
		}

		// allg. im Windowsverzeichnis
		pathToSapIni = Paths.get(System.getenv("SystemRoot"), "saplogon.ini"); //$NON-NLS-1$ //$NON-NLS-2$
		if (existsSapIniAtPath(pathToSapIni)) {
			return pathToSapIni;
		}

		// allg. im Verzeichnis ProgramData
		pathToSapIni = Paths.get(System.getenv("ALLUSERSPROFILE"), "saplogon", "saplogon.ini"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		if (existsSapIniAtPath(pathToSapIni)) {
			return pathToSapIni;
		}

		// Datei wurde nicht gefunden
		throw new FileNotFoundException("saplogon.ini"); //$NON-NLS-1$
	}
	
	/**
	 * Schreibt eine Nachricht ins Log
	 * @param logMessage Nachricht die ins Log soll
	 * @param type Typ der Nachricht
	 */
	private void writeToLog(String logMessage, String type) {
		//ist das Log gesetzt
		if (log != null) {
			//ja, dann Nachricht ins Log
			switch (type.toLowerCase()) {
			case "e": //$NON-NLS-1$
				log.error(logMessage);
				break;
			case "f": //$NON-NLS-1$
				log.fatal(logMessage);
				break;
			case "i": //$NON-NLS-1$
				log.info(logMessage);
				break;
			case "t": //$NON-NLS-1$
				log.trace(logMessage);
				break;
			case "w": //$NON-NLS-1$
				log.warn(logMessage);
				break;
			case "d": //$NON-NLS-1$
				log.debug(logMessage);
				break;
			default:
				log.info(logMessage);
				break;
			}
		}
	}
}
