package com.roczyno.notificationservice.model;

public enum EmailTemplate {
	TASK_ASSIGNED("task_assgined");

	private final String name;
	EmailTemplate(String name) {
		this.name = name;
	}
}
