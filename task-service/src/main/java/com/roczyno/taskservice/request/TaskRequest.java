package com.roczyno.taskservice.request;

import com.roczyno.taskservice.util.AppConstants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record TaskRequest(
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		@Size(min = 5,max = 20,message = AppConstants.SIZE_MESSAGE)
		String title,
		@Size(min = 5,max = 20,message = AppConstants.SIZE_MESSAGE)
		String description,
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		String image,
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		List<String> tags,
		@NotNull(message = AppConstants.CANNOT_BE_NULL)
		LocalDateTime deadline
) {
}
