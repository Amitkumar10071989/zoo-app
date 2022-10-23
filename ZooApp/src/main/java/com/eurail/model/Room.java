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
@Document(collection = "rooms")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Room {
	
	@Id
	private String id;
	/**
	 * Room Title
	 */
	private String title;
	/**
	 * Room Created Date
	 */
	private String created;
	/**
	 * Room Updated date
	 */
	private String updated;
	/**
	 * Array of favourite rooms
	 */
	private String[] favourites;
	/**
	 * Array of Animals in room
	 */
	private Animal[] animals;
	
	
	
}
