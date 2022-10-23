package com.eurail.controller;

import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eurail.constants.Constants;
import com.eurail.model.Room;
import com.eurail.service.RoomService;
import com.mongodb.client.result.UpdateResult;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Amit
 *
 */
@Tag(name = "Room Controller", description = "(Version 1) Controller that basically do the CRUD operations that is related to the rooms and some mix operations!")
@RestController
@RequestMapping("/v1/room")
@Slf4j
public class RoomController {

	@Autowired
	private RoomService roomService;

	/**
	 * using this method, you can create a room for Animals
	 * 
	 * @param title
	 * 
	 */
	@PostMapping("/create")
	public ResponseEntity<Room> createRoom(@RequestParam("title") String title) {
		log.info("createRoom title {} ", title);

		Room room = roomService.createRoom(title);
		if (room != null)
			return new ResponseEntity<Room>(room, HttpStatus.CREATED);

		return new ResponseEntity<>(room, HttpStatus.FOUND);
	}

	/**
	 * Delete room using title
	 * 
	 * @param roomTitle
	 * @return
	 */
	@DeleteMapping("/delete/{roomTitle}")
	public ResponseEntity<String> deleteRoomByTitle(@PathVariable("roomTitle") String roomTitle) {
		if (roomService.deleteRoomByTitle(roomTitle))
			return new ResponseEntity<String>(Constants.ROOM_DELETED, HttpStatus.OK);

		return new ResponseEntity<String>(Constants.ROOM_NOT_PRESENT, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Get room using title
	 * 
	 * @param title
	 * @return
	 */
	@GetMapping("/{title}")
	public ResponseEntity<Room> getRoomByTitle(@PathVariable("title") String title) {
		log.info("getRoomByTitle title {} ", title);
		Room room = roomService.getRoomByTitle(title);
		if (room != null)
			return new ResponseEntity<Room>(room, HttpStatus.OK);

		return new ResponseEntity<Room>(room, HttpStatus.NOT_FOUND);
	}

	/**
	 * Update room using title
	 * 
	 * @param title
	 * @param newTitle
	 * @return
	 */
	@PutMapping("/modify")
	public ResponseEntity<String> updateRoom(@RequestParam("title") String title,
			@RequestParam("newTitle") String newTitle) {
		log.info("update Room title: {} {} ", title, newTitle);

		UpdateResult updateResult = roomService.updateRoom(title, newTitle);
		if (updateResult.getModifiedCount() > 0)
			return new ResponseEntity<String>(Constants.ROOM_UPDATED, HttpStatus.OK);

		return new ResponseEntity<String>(Constants.MALFORMED_INPUT, HttpStatus.BAD_REQUEST);
	}

	/**
	 * using this function, you can place an animal in room
	 * 
	 * @param animalTitle
	 * @param roomTitle
	 * @return
	 * @throws BusinessException
	 */
	@PostMapping("/place-animal")
	public ResponseEntity<String> placeAnimalInRoom(@RequestParam("animalId") String animalId,
			@RequestParam(required = true, value = "roomTitle") String roomTitle) {
		log.info("placeAnimalInRoom animalId:{} roomTitle:{} ", animalId, roomTitle);
		Room room = roomService.placeAnimalInRoom(animalId, roomTitle);
		if (room != null)
			return new ResponseEntity<String>(Constants.ANIMAL_PLACED, HttpStatus.OK);

		return new ResponseEntity<String>(Constants.MALFORMED_INPUT, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Using this function, you can move animal from one room to other
	 * 
	 * @param fromRoomName
	 * @param toRoomName
	 * @param animalId
	 * @return
	 */
	@PostMapping("/move-animal")
	public ResponseEntity<String> moveAnimalFrmOneRoomToOther(@RequestParam("fromRoomTitle") String fromRoomTitle,
			@RequestParam("toRoomTitle") String toRoomTitle, @RequestParam("animalId") String animalId) {
		log.info("moveAnimalFrmOneRoomToOther fromRoomName: {} toRoomName: {} ", fromRoomTitle, toRoomTitle);
		try {
			Room room = roomService.moveAnimalFrmOneRoomToOther(fromRoomTitle, toRoomTitle, animalId);
			if (room != null) {
				return new ResponseEntity<String>(Constants.ANIMAL_MOVED, HttpStatus.OK);
			}
		} catch (Exception e) {
			log.error("Error in moveAnimalFrmOneRoomToOther ", e);
		}
		return new ResponseEntity<String>(Constants.MALFORMED_INPUT, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Remove Animal from the room
	 * 
	 * @param roomName
	 * @param animalId
	 * @return
	 */
	@DeleteMapping("/remove-animal")
	public ResponseEntity<String> removeAnimalFrmRoom(@RequestParam("roomTitle") String roomTitle,
			@RequestParam("animalId") String animalId) {

		log.info("removeAnimalFrmRoom roomName: {} animalId: {} ", roomTitle, animalId);

		UpdateResult updateResult = roomService.removeAnimalFrmRoom(roomTitle, animalId);
		if (updateResult.getModifiedCount() > 0) {
			return new ResponseEntity<String>(Constants.ANIMAL_REMOVED, HttpStatus.OK);
		}

		return new ResponseEntity<String>(Constants.MALFORMED_INPUT, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Assign Favourite room to the Animals and remove animals from assigned room
	 * using flag
	 * 
	 * @param roomName
	 * @param animalId
	 * @param isFavourite
	 * @return
	 */
	@PutMapping("/favourite")
	public ResponseEntity<String> assignFavouriteRoom(@RequestParam("roomTitle") String roomTitle,
			@RequestParam("animalId") String animalId, @RequestParam("isFavourite") boolean isFavourite) {

		log.info("assignFavouriteRoom roomName: {} animalId: {} ", roomTitle, animalId);

		String status = roomService.assignFavouriteRoom(roomTitle, animalId, isFavourite);
		if (status.equalsIgnoreCase(Constants.FAVOURITE_ROOM_ADDED)) {
			return new ResponseEntity<String>(Constants.FAVOURITE_ROOM_ADDED, HttpStatus.OK);
		} else if (status.equalsIgnoreCase(Constants.FAVOURITE_ROOM_REMOVED)) {
			return new ResponseEntity<String>(Constants.FAVOURITE_ROOM_REMOVED, HttpStatus.OK);
		}

		return new ResponseEntity<String>(Constants.MALFORMED_INPUT, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Get Animals those are in room, using room title along with pagination
	 * 
	 * @param roomName
	 * @param sortBy
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/fetch-all-animals")
	public Page<Document> getAnimalsByRoomName(@RequestParam(value = "roomTitle") String roomTitle,
			@RequestParam(required = false) String sortBy, @RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer size) {

		Pageable pageable = PageRequest.of(page, size,Sort.by("animals.title").ascending());

		return roomService.getAnimalsByRoomName(roomTitle, sortBy, pageable);
	}

	/**
	 * Get all favourite rooms, which are choosen by Animals
	 * 
	 * @return
	 */
	@GetMapping("/fetch-favourite")
	public Map<String, Integer> getListOfFavouriteRooms() {

		return roomService.getListOfFavouriteRooms();
	}

}
