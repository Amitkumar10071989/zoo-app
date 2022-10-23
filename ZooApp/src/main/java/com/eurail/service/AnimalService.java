package com.eurail.service;

import com.eurail.model.Animal;
import com.mongodb.client.result.UpdateResult;

/**
 * @author Amit
 *
 */
public interface AnimalService {

	/**
	 * Create entity in DB
	 * 
	 * @param title
	 * @return Animal
	 */
	Animal createAnimal(String title);

	/**
	 * Delete entity in DB
	 * 
	 * @param animalId
	 * @return boolean
	 */
	boolean deleteAnimal(String animalId);

	/**
	 * Fetch entity from DB
	 * 
	 * @param animalId
	 * @return Animal
	 */
	Animal getAnimal(String animalId);

	/**
	 * Update entity in DB
	 * 
	 * @param animalId
	 * @param animalTitle
	 * @return UpdateResult
	 */
	UpdateResult updateAnimal(String animalId, String animalTitle);

}
