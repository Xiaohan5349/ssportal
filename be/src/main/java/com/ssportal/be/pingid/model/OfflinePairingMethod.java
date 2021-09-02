package com.ssportal.be.pingid.model;

public enum OfflinePairingMethod {
	
	VOICE("VOICE"),
	SMS("SMS"),
	EMAIL("EMAIL");
	
	private final String method;
	
	private OfflinePairingMethod(String method) {
		this.method = method;
	}
	
	public String getValue() {
		return this.method;
	}

}
