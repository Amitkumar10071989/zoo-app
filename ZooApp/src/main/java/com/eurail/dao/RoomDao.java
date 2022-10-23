package com.eurail.dao;

import java.util.Map;

import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;

import com.eurail.model.Animal;
import com.eurail.model.Room;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

/**
 * @author Amit
 *
 */
public interface RoomDao {

	/**
	 * @param room
	 * @return
	 */
	Room createRoom(Room room);

	/**
	 * @param title
	 * @return
	 */
	boolean checkRoomExists(String title);

	/**
	 * @param roomTitle
	 * @return
	 */
	DeleteResult deleteRoomByTitle(String roomTitle);

	/**
	 * @param title
	 * @return
	 */
	Room getRoomByTitle(String title);

	/**
	 * @param title
	 * @param newTitle
	 * @return
	 */
	UpdateResult updateRoom(String title, String newTitle);

	/**
	 * @param animalId
	 * @return
	 */
	Animal findAndRemove(String animalId);

	/**
	 * @param roomTitle
	 * @param animal
	 * @return
	 */
	Room findAndModifyByTitle(String roomTitle, Animal animal);

	/**
	 * @param fromRoomName
	 * @return
	 */
	Animal[] findAnimal(String fromRoomName);

	/**
	 * @param fromRoomName
	 * @param animalId
	 * @return
	 */
	UpdateResult getAndUpdateAnimal(String fromRoomName, String animalId);

	/**
	 * @param toRoomName
	 * @param animal
	 * @return
	 */
	Room findAndModify(String toRoomName, Animal animal);

	/**
	 * @param roomName
	 * @param animalId
	 * @return
	 */
	Room assingFavouriteRoom(String roomName, String animalId);

	/**
	 * @param roomName
	 * @param animalId
	 * @return
	 */
	UpdateResult removedFavouriteRoom(String roomName, String animalId);

	/**
	 * @param query
	 * @param pageable
	 * @return
	 */
	Page<Document> getAnimalByRoomTitle(Query query, Aggregation aggregation, Pageable pageable);

	/**
	 * @return
	 */
	Map<String, Integer> getListOfFavouriteRooms();

	/**
	 * @param animal
	 */
	void saveAnimal(Animal animal);

	/**
	 * @param animalId
	 * @return
	 */
	boolean checkAnimalExists(String animalId);

	/**
	 * @param fromRoomName
	 * @return
	 */
	String[] findFavourites(String fromRoomName);

}
