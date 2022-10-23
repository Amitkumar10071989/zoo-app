package com.eurail.dao;

import com.eurail.model.Animal;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

/**
 * @author Amit
 *
 */
public interface AnimalDao {

	/**
	 * @param animal
	 * @return Animal
	 */
	Animal saveAnimal(Animal animal);

	/**
	 * @param animalId
	 * @return DeleteResult
	 */
	DeleteResult deleteAnimalById(String animalId);

	/**
	 * @param animalId
	 * @return Animal
	 */
	Animal getAnimalById(String animalId);

	/**
	 * @param title
	 * @param animalTitle
	 * @return UpdateResult
	 */
	UpdateResult updateAnimalById(String title, String animalTitle);

}
