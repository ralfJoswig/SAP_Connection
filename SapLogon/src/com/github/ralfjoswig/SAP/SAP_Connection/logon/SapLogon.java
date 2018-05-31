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
package com.github.ralfjoswig.SAP.SAP_Connection.logon;

/**
 * @author Ralf Joswig 
 *
 */

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.github.ralfJoswig.SAP_Connection.SAPini.SAPini;

import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class SapLogon extends Dialog {

	protected Object result;
	protected Shell shlSapLogon;
	private Text txtUser;
	private Text txtPassword;
	private Text txtMandant;
	private Combo cbSystem;
	private Button btnLogin;
	private Button btnCancel;
	private SAPini sapIni;
	protected LoginData logindata = new LoginData();
	
	private static Logger log = null;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public SapLogon(Shell parent, int style, LoginData logindata) {
		super(parent, style);
		
		//Daten aus der saplogon.ini lesen
		readSapIni();
		
		//Dialog aufbauen
		createContents();
		
		//wenn Login-Daten übergeben wurden, diese setzen
		setLoginDataInDialog(logindata);

		//vorbelegen das nicht Login gedrückt wurde
		logindata.doLogin = false;
	}

	/**
	 * Setzt die Standardwerte im Dialog
	 * 
	 * @param logindata
	 */
	private void setLoginDataInDialog(LoginData logindata) {
		if (logindata != null) {
			txtMandant.setText(logindata.mandant);
			txtUser.setText(logindata.user);
			cbSystem.setText(logindata.system);
		}
	}

	/**
	 * Liest die saplogon.ini
	 */
	private void readSapIni() {
		//Instanz zum lesen erzeugen
		sapIni = new SAPini();
		
		//Daten einlesen
		try {
			sapIni.read();
		} catch (FileNotFoundException e) {
			//einen Fehler ins Log schreiben
			writeToLog(e.getMessage(), Constance.MESSAGE_TYPE_ERROR);
		}
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		if (!txtUser.getText().isEmpty()) {
			txtPassword.setFocus();
		}
		shlSapLogon.open();
		shlSapLogon.layout();
		Display display = getParent().getDisplay();
		setButtonState();
		while (!shlSapLogon.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlSapLogon = new Shell(getParent(), SWT.BORDER | SWT.TITLE);
		shlSapLogon.setSize(395, 239);
		shlSapLogon.setText(Messages.SapLogon_DialogTitel);
		shlSapLogon.setLayout(null);

		Label lblUser = new Label(shlSapLogon, SWT.NONE);
		lblUser.setBounds(29, 22, 55, 15);
		lblUser.setText(Messages.SapLogon_User);

		txtUser = new Text(shlSapLogon, SWT.BORDER);
		txtUser.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				setButtonState();
			}
		});
		txtUser.setBounds(106, 19, 94, 21);
		txtUser.setText(Constance.EMPTY_STRING);

		Label lblPassword = new Label(shlSapLogon, SWT.NONE);
		lblPassword.setBounds(29, 58, 55, 15);
		lblPassword.setText(Messages.SapLogon_Password);

		txtPassword = new Text(shlSapLogon, SWT.BORDER | SWT.PASSWORD);
		txtPassword.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				setButtonState();
			}
		});
		txtPassword.setBounds(106, 55, 94, 21);

		Label lblMandant = new Label(shlSapLogon, SWT.NONE);
		lblMandant.setBounds(29, 91, 55, 15);
		lblMandant.setText(Messages.SapLogon_Mandant);

		txtMandant = new Text(shlSapLogon, SWT.BORDER);
		txtMandant.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				setButtonState();
			}
		});
		txtMandant.setBounds(106, 88, 30, 21);
		txtMandant.setText(Constance.EMPTY_STRING);

		Label lblSystem = new Label(shlSapLogon, SWT.NONE);
		lblSystem.setBounds(29, 125, 55, 15);
		lblSystem.setText(Messages.SapLogon_System);

		cbSystem = new Combo(shlSapLogon, SWT.DROP_DOWN | SWT.READ_ONLY);
		cbSystem.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				setButtonState();
			}
		});
		cbSystem.setBounds(106, 122, 242, 23);
		addSystemsToComboBox();

		btnLogin = new Button(shlSapLogon, SWT.NONE);
		btnLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveLgonData();
				logindata.doLogin = true;
				shlSapLogon.dispose();
			}
		});
		btnLogin.setEnabled(false);
		btnLogin.setBounds(125, 167, 75, 25);
		btnLogin.setText(Messages.SapLogon_Login);

		btnCancel = new Button(shlSapLogon, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveLgonData();
				logindata.doLogin = false;
				shlSapLogon.dispose();
			}
		});
		btnCancel.setBounds(238, 167, 75, 25);
		btnCancel.setText(Messages.SapLogon_Cancel);
	}

	protected void saveLgonData() {
		if (logindata == null) {
			logindata = new LoginData();
		}
		logindata.user = txtUser.getText();
		logindata.system = cbSystem.getText();
		logindata.password = txtPassword.getText();
	}

	/**
	 * Fügt die SAP-Systeme zur Auswahlbox hinzu
	 */
	private void addSystemsToComboBox() {
		//SAP-Systeme aus der eingelesenen saplogon.ini holen
		ArrayList<String> listOfSystems = sapIni.getListOfSystems();

		//Systeme sortieren
		listOfSystems.sort(null);

		//und Systeme in die Auswahlbox aufnehmen
		for (String system : listOfSystems) {
			cbSystem.add(system);
		}
	}

	/**
	 * Gibt die erfassten Login-Daten zurück
	 * @return
	 */
	public LoginData getLoginData() {
		return logindata;
	}

	/**
	 * Setzt ob die Drucktaste Login aktiv ist
	 */
	private void setButtonState() {
		//wenn noch nicht alle Objekte instanziiert sind
		if (txtMandant == null || txtPassword == null || txtUser == null || cbSystem == null || btnLogin == null) {
			//Verarbeitung abbrechen
			return;
		}

		//ist in allen Felder was erfasst
		if (txtMandant.getText().isEmpty() ||
			txtPassword.getText().isEmpty() || 
			txtUser.getText().isEmpty()         ||
			cbSystem.getSelectionIndex() != 0) {
			//dann Drucktaste aktivieren
			btnLogin.setEnabled(false);
		} else {
			//nein, dann Drucktaste deaktivieren
			btnLogin.setEnabled(true);
		}
	}

	@SuppressWarnings("rawtypes")
	public void setLogger(Class classForLogger) {
		log = Logger.getLogger(classForLogger);
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
			case Constance.MESSAGE_TYPE_ERROR:
				log.error(logMessage);
				break;
			case Constance.MESSAGE_TYPE_FATAL:
				log.fatal(logMessage);
				break;
			case Constance.MESSAGE_TYPE_INFO:
				log.info(logMessage);
				break;
			case Constance.MESSAGE_TYPE_TRACE:
				log.trace(logMessage);
				break;
			case Constance.MESSAGE_TYPE_WARN:
				log.warn(logMessage);
				break;
			case Constance.MESSAGE_TYPE_DEBUG:
				log.debug(logMessage);
				break;
			default:
				log.info(logMessage);
				break;
			}
		}
	}
}
