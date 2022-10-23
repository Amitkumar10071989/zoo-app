package com.eurail.service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import com.eurail.constants.Constants;
import com.eurail.dao.RoomDao;
import com.eurail.exception.NotFoundException;
import com.eurail.model.Animal;
import com.eurail.model.Room;
import com.eurail.utility.Utility;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Amit
 *
 */
@Service
@Slf4j
public class RoomServiceImpl implements RoomService {

	@Autowired
	RoomDao roomDao;

	@Override
	public Room createRoom(String title) {
		Room room = null;
		// check title is already exists
		if (!isExist(title)) {
			room = new Room();
			room.setTitle(Utility.capitalize(title));
			room.setCreated(Instant.now().toString());
			room.setUpdated(Instant.now().toString());

			return roomDao.createRoom(room);

		}

		return room;

	}

	private boolean isExist(String title) {

		return roomDao.checkRoomExists(title);
	}

	@Override
	public boolean deleteRoomByTitle(String roomTitle) {

		DeleteResult deleteResult = roomDao.deleteRoomByTitle(Utility.capitalize(roomTitle));

		if (deleteResult.getDeletedCount() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public Room getRoomByTitle(String title) {

		return roomDao.getRoomByTitle(title);

	}

	@Override
	public UpdateResult updateRoom(String title, String newTitle) {
		return roomDao.updateRoom(title, newTitle);
	}

	@Override
	public Room placeAnimalInRoom(String animalId, String roomTitle) {
		Room room = null;

		if (isExist(roomTitle)) {
			Animal animal = roomDao.findAndRemove(animalId);
			if (animal != null) {
				animal.setLocated(Utility.formatDate(Instant.now()));
				room = roomDao.findAndModifyByTitle(roomTitle, animal);
			} else {
				log.error("Customer error: Animal not found ");
				throw new NotFoundException(Constants.ANIMAL_NOT_FOUND);
			}
		} else {
			log.error("Custom error: Room not found ");
			throw new NotFoundException(Constants.ROOM_NOT_FOUND);
		}

		return room;

	}

	@Override
	public Room moveAnimalFrmOneRoomToOther(String fromRoomName, String toRoomName, String animalId) {
		// check room/animal exist or not
		// move animal one room to other
		Room room = null;
		UpdateResult updateResult;
		if (isExist(toRoomName) && isExist(fromRoomName)) {
			Animal[] animals = roomDao.findAnimal(Utility.capitalize(fromRoomName));
			for (Animal animal : animals) {
				if (animal.getId() != null && animal.getId().equals(animalId)) {
					updateResult = roomDao.getAndUpdateAnimal(Utility.capitalize(fromRoomName), animalId);
					if (updateResult.getModifiedCount() > 0)
						room = roomDao.findAndModify(Utility.capitalize(toRoomName), animal);
					break;
				}
			}
		}
		return room;
	}

	@Override
	public UpdateResult removeAnimalFrmRoom(String roomTitle, String animalId) {

		// get animal details from room and save in animal collection
		// after that delete animal from room
		if (isExist(roomTitle)) {
			Animal[] animals = roomDao.findAnimal(Utility.capitalize(roomTitle));
			for (Animal animal : animals) {
				if (animal.getId() != null && animal.getId().equals(animalId)) {
					roomDao.saveAnimal(animal);
					break;
				}
			}

			return roomDao.getAndUpdateAnimal(roomTitle, animalId);
		} else {
			throw new NotFoundException(Constants.ROOM_NOT_FOUND);
		}

	}

	@Override
	public String assignFavouriteRoom(String roomName, String animalId, boolean isFavourite) {

		if (isFavourite) {
			// check animalId exists or not
			// check animal is already assigned to this room
			long count = Arrays.asList(roomDao.findFavourites(roomName)).stream().filter(str -> str.equals(animalId))
					.count();

			if (roomDao.checkAnimalExists(animalId) && count <= 0) {
				Room room = roomDao.assingFavouriteRoom(roomName, animalId);
				if (room != null) {
					return Constants.FAVOURITE_ROOM_ADDED;
				}
			}

		} else {
			UpdateResult updatedResult = roomDao.removedFavouriteRoom(roomName, animalId);
			if (updatedResult.getModifiedCount() > 0) {
				return Constants.FAVOURITE_ROOM_REMOVED;
			}
		}
		return Constants.NOT_FOUND;

	}

	@Override
	public Page<Document> getAnimalsByRoomName(String roomName, String sortBy, Pageable pageable) {

		if (!isExist(roomName)) {
			log.info("Custome error : {} ", Constants.ROOM_NOT_FOUND);
			throw new NotFoundException(Constants.ROOM_NOT_FOUND);
		}
		Query query = new Query().with(pageable);
		UnwindOperation unwindOperation = Aggregation.unwind("animals");

		MatchOperation matchCriteria = Aggregation
				.match(Criteria.where(Constants.TITLE).is(Utility.capitalize(roomName)));

		GroupOperation groupOperation = Aggregation.group("animals");
		Aggregation aggregation = Aggregation.newAggregation(unwindOperation, matchCriteria, groupOperation);

		return roomDao.getAnimalByRoomTitle(query, aggregation, pageable);

	}

	@Override
	public Map<String, Integer> getListOfFavouriteRooms() {

		return roomDao.getListOfFavouriteRooms();
	}

}
