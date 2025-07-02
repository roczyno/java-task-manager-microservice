package com.roczyno.notificationservice.model;

public enum EmailTemplate {
	TASK_ASSIGNED("task_assgined"),
	TASK_SUBMITTED("task_submitted"),
	SUBMISSION_ACCEPTED("submission_accepted"),
	SUBMISSION_DECLINED("submission_declined");

	private final String name;
	EmailTemplate(String name) {
		this.name = name;
	}
}
