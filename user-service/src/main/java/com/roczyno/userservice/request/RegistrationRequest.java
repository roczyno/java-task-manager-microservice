package com.roczyno.userservice.request;

import com.roczyno.userservice.util.AppConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequest {
	@NotNull(message = AppConstants.CANNOT_BE_NULL)
	@Size(min = 3, max = 20, message = AppConstants.NAME_SIZE_MESSAGE)
	@Pattern(regexp = AppConstants.USERNAME_PATTERN, message = AppConstants.USERNAME_PATTERN_MESSAGE)
	private String username;
	@Pattern(regexp = AppConstants.PASSWORD_PATTERN,message = AppConstants.PASSWORD_PATTERN_MESSAGE)
	@NotNull(message = AppConstants.CANNOT_BE_NULL)
	private String password;
	@Email(message = AppConstants.EMAIL)
	@NotNull(message = AppConstants.CANNOT_BE_NULL)
	private String email;
	@NotNull(message = AppConstants.CANNOT_BE_NULL)
	private String specialization;

}
