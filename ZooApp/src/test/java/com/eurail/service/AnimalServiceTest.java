package com.eurail.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.eurail.dao.AnimalDao;
import com.eurail.model.Animal;
import com.eurail.utility.Utility;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {
	
	@InjectMocks
	AnimalServiceImpl animalServiceImpl;
	
	@Mock
	AnimalDao animalDao;
	
	@Test
	public void testCreateRoomSuccess() {
		Animal animal = new Animal();
		
		animal.setTitle(Utility.capitalize("Tiger"));
		animal.setCreated(Instant.now().toString());
		animal.setUpdated(Instant.now().toString());
		animal.setLocated(Utility.formatDate(Instant.now()));
		
		
		Mockito.when(animalDao.saveAnimal(Mockito.any(Animal.class))).thenReturn(animal);
		  
		Animal response = animalServiceImpl.createAnimal("Green");
		assertNotNull(response);
		assertEquals(response.getTitle(), "Tiger");
		
	}
	
	@Test
	public void testDeleteAnimalSuccess() {
		DeleteResult deleteResult = DeleteResult.acknowledged(1l);
		Mockito.when(animalDao.deleteAnimalById(Mockito.anyString())).thenReturn(deleteResult);
		boolean flag = animalServiceImpl.deleteAnimal("6351556a93f16204f282da2b");
		assertTrue(flag);
		
	}
	
	@Test
	public void testDeleteAnimalFalure() {
		DeleteResult deleteResult = DeleteResult.acknowledged(0);
		Mockito.when(animalDao.deleteAnimalById(Mockito.anyString())).thenReturn(deleteResult);
		boolean flag = animalServiceImpl.deleteAnimal("6351556a93f16204f282da2b");
		assertFalse(flag);
		
	}
	
	@Test
	public void testGetAnimalByIdSuccess() {
		Animal animal = new Animal();
		
		animal.setTitle(Utility.capitalize("Tiger"));
		animal.setCreated(Instant.now().toString());
		animal.setUpdated(Instant.now().toString());
		animal.setLocated(Utility.formatDate(Instant.now()));
		Mockito.when(animalDao.getAnimalById(Mockito.anyString())).thenReturn(animal);
		  
		Animal response = animalServiceImpl.getAnimal("6351556a93f16204f282da2b");
		assertNotNull(response);
		assertEquals(response.getTitle(), "Tiger");
	}
	
	@Test
	public void testUpdateAnimalSuccess() {
		UpdateResult updateResult = UpdateResult.acknowledged(1l, 1l, null);
		Mockito.when(animalDao.updateAnimalById(Mockito.anyString(),Mockito.anyString())).thenReturn(updateResult);
		  
		UpdateResult response = animalServiceImpl.updateAnimal("6351556a93f16204f282da2b","Tiger1");
		assertNotNull(response);
		assertEquals(response.getModifiedCount(), 1l);
	}
}
