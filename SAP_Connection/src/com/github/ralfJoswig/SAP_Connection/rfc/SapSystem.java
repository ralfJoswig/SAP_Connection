/**
 * 
 */
package com.github.ralfJoswig.SAP_Connection.rfc;

/**
 * @author Ralf Joswig
 * 
 */
public class SapSystem implements Cloneable {

	private String name = null;
	private String host = null;
	private String client = null;
	private String systemNumber = null;
	private String user = null;
	private String password = null;
	private String language = null;

	public SapSystem(String _name, String _host, String _client,
			String _systemNumber, String _user, String _password,
			String _language) {
		name = _name;
		host = _host;
		client = _client;
		systemNumber = _systemNumber;
		user = _user;
		password = _password;
		language = _language;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the client
	 */
	public String getClient() {
		return client;
	}

	/**
	 * @return the systemNumber
	 */
	public String getSystemNumber() {
		return systemNumber;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result	+ ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result	+ ((password == null) ? 0 : password.hashCode());
		result = prime * result	+ ((systemNumber == null) ? 0 : systemNumber.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SapSystem other = (SapSystem) obj;
		if (client == null) {
			if (other.client != null) {
				return false;
			}
		} else if (!client.equals(other.client)) {
			return false;
		}
		if (host == null) {
			if (other.host != null) {
				return false;
			}
		} else if (!host.equals(other.host)) {
			return false;
		}
		if (language == null) {
			if (other.language != null) {
				return false;
			}
		} else if (!language.equals(other.language)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (password == null) {
			if (other.password != null) {
				return false;
			}
		} else if (!password.equals(other.password)) {
			return false;
		}
		if (systemNumber == null) {
			if (other.systemNumber != null) {
				return false;
			}
		} else if (!systemNumber.equals(other.systemNumber)) {
			return false;
		}
		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!user.equals(other.user)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SapSystem [client=" + client + ", host=" + host + ", language=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ language + ", name=" + name + ", systemNumber=" //$NON-NLS-1$ //$NON-NLS-2$
				+ systemNumber + ", user=" + user + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}

}