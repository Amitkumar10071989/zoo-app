package com.eurail.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.SmartNullPointerException;
import org.mockito.junit.jupiter.MockitoExtension;
import com.eurail.dao.RoomDao;
import com.eurail.exception.NotFoundException;
import com.eurail.model.Animal;
import com.eurail.model.Room;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

/**
 * @author Amit
 *
 */
@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {
	
	@InjectMocks
	RoomServiceImpl roomServiceImpl;
	
	@Mock
	RoomDao roomDao;


	@Test
	public void testCreateRoomSuccess() {
		
		Mockito.when(roomDao.checkRoomExists(Mockito.anyString())).thenReturn(false);
		
		Mockito.when(roomDao.createRoom(Mockito.any(Room.class))).thenReturn(getRoom());
		  
		Room response = roomServiceImpl.createRoom("Green");
		assertTrue(response.getTitle().equals("Green"));
		
	}
	
	@Test
	public void testDeleteRoomByTtileSuccess() {
	
		DeleteResult deleteResult = DeleteResult.acknowledged(1);
		Mockito.when(roomDao.deleteRoomByTitle("Green")).thenReturn(deleteResult);
		  
		boolean flag = roomServiceImpl.deleteRoomByTitle("Green");
		assertTrue(flag);
		
	}
	
	@Test
	public void testDeleteRoomByTtileFalure() {
	
		DeleteResult deleteResult = DeleteResult.acknowledged(0);
		Mockito.when(roomDao.deleteRoomByTitle("Green")).thenReturn(deleteResult);
		  
		boolean flag = roomServiceImpl.deleteRoomByTitle("Green");
		assertFalse(flag);
		
	}
	
	@Test
	public void testGetRoomByTitleSuccess() {
		String title ="Green";
		
		Mockito.when(roomDao.getRoomByTitle(title)).thenReturn(getRoom());
		  
		Room response = roomServiceImpl.getRoomByTitle(title);
		assertNotNull(response);
		assertEquals(response.getTitle(), title);
		
	}
	
	@Test
	public void testGetRoomByTitleFalure() {
		String title = "Green";
		
		Mockito.when(roomDao.getRoomByTitle(title)).thenThrow(SmartNullPointerException.class);
		  
		Exception exception = assertThrows(RuntimeException.class, () -> {
			roomServiceImpl.getRoomByTitle(title);
	    });

	    String expectedMessage = null;
	    String actualMessage = exception.getMessage();

	    assertEquals(expectedMessage, actualMessage);
		
	}
	
	@Test
	public void testUpdateRoomSuccess() {
		String title = "Green";
		String newTitle = "Big";
		UpdateResult updateResult = UpdateResult.acknowledged(1l, 1l, null);
		
		Mockito.when(roomDao.updateRoom(Mockito.anyString(),Mockito.anyString())).thenReturn(updateResult);
		  
		UpdateResult response = roomServiceImpl.updateRoom(title,newTitle);
		assertNotNull(response);
		assertEquals(response.getModifiedCount(), 1);
		
	}
	
	@Test
	public void testPlaceAnimalInRoomSuccess() {
		
		Room room = new Room();
		
		room.setTitle("Green");
		room.setCreated(Instant.now().toString());
		room.setUpdated(Instant.now().toString());
		Mockito.when(roomDao.checkRoomExists(Mockito.anyString())).thenReturn(true);
		Mockito.when(roomDao.findAndRemove(Mockito.anyString())).thenReturn(getAnimal());
		Mockito.when(roomDao.findAndModifyByTitle(Mockito.anyString(),Mockito.any(Animal.class))).thenReturn(room);
					
			Room response = roomServiceImpl.placeAnimalInRoom("634e1eebba33c202645d776a", "Green");
			assertNotNull(response);
			assertEquals(response.getTitle(), "Green");

		
	}
	
	@Test
	public void testPlaceAnimalInRoomException() {
		Exception exception = assertThrows(NotFoundException.class, () -> {
			roomServiceImpl.placeAnimalInRoom("634e1eebba33c202645d776a", "Green");
	    });

	    String expectedMessage = "Target Room not found";
	    String actualMessage = exception.getMessage();

	    assertEquals(expectedMessage, actualMessage);
		
	}
	
	
	@Test
	public void testMoveAnimalFrmOneRoomToOtherSuccess() {
		List<Animal> animalList = new ArrayList<Animal>();
		
		animalList.add(getAnimal());

		Animal[] animalArray = animalList.toArray(new Animal[0]);

		UpdateResult updateResult = UpdateResult.acknowledged(1l, 1l, null);

		Mockito.when(roomDao.checkRoomExists(Mockito.anyString())).thenReturn(true);

		Mockito.when(roomDao.findAnimal(Mockito.anyString())).thenReturn(animalArray);

		Mockito.when(roomDao.getAndUpdateAnimal(Mockito.anyString(), Mockito.anyString())).thenReturn(updateResult);

		Mockito.when(roomDao.findAndModify(Mockito.anyString(), Mockito.any(Animal.class))).thenReturn(getRoom());

		Room response = roomServiceImpl.moveAnimalFrmOneRoomToOther("Green", "Big", "634e1eebba33c202645d776a");

		assertNotNull(response);
		assertEquals(response.getTitle(), "Green");
	}
	
	
	private Room getRoom() {

		String title = "Green";
		Room room = new Room();

		room.setTitle(title);
		room.setCreated(Instant.now().toString());
		room.setUpdated(Instant.now().toString());

		return room;
	}
	
	private Animal getAnimal() {
		Animal animal = new Animal();

		animal.setTitle("Tiger");
		animal.setId("634e1eebba33c202645d776a");
		animal.setCreated(Instant.now().toString());
		animal.setUpdated(Instant.now().toString());
		
		return animal;
	}

}
