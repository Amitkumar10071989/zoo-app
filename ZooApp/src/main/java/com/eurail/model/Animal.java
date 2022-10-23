package com.eurail.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Amit
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "animals")
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class Animal {
	
	@Id
	private String id;
	/**
	 * Animal title
	 */
	private String title;
	/**
	 * Animal Created Date
	 */
	private String created;
	/**
	 * Animal Updated Date
	 */
	private String updated;
	/**
	 * Animal Located formated date
	 */
	private String located;
	

}
