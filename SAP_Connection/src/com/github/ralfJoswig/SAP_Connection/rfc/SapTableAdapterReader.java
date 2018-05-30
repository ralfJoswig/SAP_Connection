/**
 * 
 */
package com.github.ralfJoswig.SAP_Connection.rfc;

import com.github.ralfJoswig.SAP_Connection.constance.Constance;
import com.sap.conn.jco.JCoTable;

/**
 * @author Ralf Joswig
 *
 */
public class SapTableAdapterReader {
	protected JCoTable table;

	public SapTableAdapterReader(JCoTable table) {
		this.table = table;
	}

	public String get(String s) {
		return table.getValue(s).toString();
	}

	public Boolean getBoolean(String s) {
		String value = table.getValue(s).toString();
		return value.equals(Constance.CHAR_X);
	}

	public String getMessage() {
		return table.getString(Constance.MESSAGE);
	}

	public int size() {
		return table.getNumRows();
	}

	public void next() {
		table.nextRow();
	}

}
