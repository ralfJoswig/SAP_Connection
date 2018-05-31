package com.github.ralfjoswig.SAP.SAP_Connection.logon;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.github.ralfjoswig.SAP.SAP_Connection.logon.messages"; //$NON-NLS-1$
	public static String SapLogon_Cancel;
	public static String SapLogon_DialogTitel;
	public static String SapLogon_Login;
	public static String SapLogon_Mandant;
	public static String SapLogon_Password;
	public static String SapLogon_System;
	public static String SapLogon_User;
	static {
		// initialize resource bundle 
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
