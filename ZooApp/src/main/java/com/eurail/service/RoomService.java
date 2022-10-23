package com.eurail.service;

import java.util.Map;

import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.eurail.model.Room;
import com.mongodb.client.result.UpdateResult;

/**
 * @author Amit
 *
 */
public interface RoomService {

	

	/**
	 * Create room in DB
	 * 
	 * @param title
	 * @return
	 */
	Room createRoom(String title);
	
	/**
	 * Delete room by title in DB
	 * 
	 * @param roomTitle
	 * @return
	 */
	boolean deleteRoomByTitle(String roomTitle);
	
	/**
	 * Place Animal in room in DB
	 * 
	 * @param animalTitle
	 * @param roomTitle
	 * @return
	 */
	Room placeAnimalInRoom(String animalTitle,String roomTitle);

	/**
	 * Move Animal one room to other in DB
	 * 
	 * @param fromRoomName
	 * @param toRoomName
	 * @param animalName
	 * @return
	 */
	Room moveAnimalFrmOneRoomToOther(String fromRoomName, String toRoomName, String animalName);

	/**
	 * Remove Animal from room in DB
	 * 
	 * @param roomName
	 * @param animalName
	 * @return
	 */
	UpdateResult removeAnimalFrmRoom(String roomName, String animalName);

	/**
	 * Assign favourite room
	 * 
	 * @param roomName
	 * @param animalName
	 * @param isFavourite
	 * @return
	 */
	String assignFavouriteRoom(String roomName, String animalName,boolean isFavourite);

	/**
	 * Get Animal By room title
	 * 
	 * @param roomName
	 * @param located
	 * @param pageable
	 * @return
	 */
	Page<Document> getAnimalsByRoomName(String roomName,String sortBy,Pageable pageable);

	/**
	 * List of favourite room
	 * 
	 * @return
	 */
	Map<String,Integer> getListOfFavouriteRooms();

	/**
	 * Get room using title
	 * 
	 * @param roomTitle
	 * @return
	 */
	Room getRoomByTitle(String roomTitle);

	/**
	 * Update room in DB
	 * 
	 * @param title
	 * @param newTitle
	 * @return
	 */
	UpdateResult updateRoom(String title, String newTitle); 


	

}
