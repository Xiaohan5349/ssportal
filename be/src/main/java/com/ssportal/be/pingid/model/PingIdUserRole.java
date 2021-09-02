package com.ssportal.be.pingid.model;


public enum PingIdUserRole {
	REGULAR("REGULAR"),
	ADMIN("ADMIN");

	private final String role;
	
	private PingIdUserRole(String role) {
		this.role = role;
	}
	
	public String getValue() {
		return this.role;
	}

}
