package com.eurail.exception;


import java.time.Instant;

/**
 * @author Amit
 *
 */
public class ErrorResponse {
	
    private String code;
    private String description;
    private String timestamp = Instant.now().toString();

    public ErrorResponse(String code, String description) {
        this.code = code;
        this.description = description;
    }

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public String getTimestamp() {
		return timestamp;
	}
    
}
