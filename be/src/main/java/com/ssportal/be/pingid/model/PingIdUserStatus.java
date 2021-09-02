package com.ssportal.be.pingid.model;


public enum PingIdUserStatus {
	ACTIVE("ACTIVE"),
	PENDING("PENDING"),
	NOT_ACTIVE("NOT_ACTIVE"),
	PENDING_ACTIVATION("PENDING_ACTIVATION"),
	SUSPENDED("SUSPENDED"),
	PENDING_CHANGE_DEVICE("PENDING_CHANGE_DEVICE");

	private final String status;
	
	private PingIdUserStatus(String status) {
		this.status = status;
	}
	
	public String getValue() {
		return this.status;
	}
}
