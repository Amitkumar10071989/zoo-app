package com.eurail.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eurail.dao.AnimalDao;
import com.eurail.model.Animal;
import com.eurail.utility.Utility;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import lombok.extern.slf4j.Slf4j;

/**
 * In this class prepared data for database and extract/validate results for
 * customer
 * 
 * @author Amit
 *
 */
@Service
@Slf4j
public class AnimalServiceImpl implements AnimalService {

	@Autowired
	AnimalDao animalDao;

	@Override
	public Animal createAnimal(String title) {

		Animal animal = new Animal();

		animal.setTitle(Utility.capitalize(title));
		animal.setCreated(Instant.now().toString());
		animal.setUpdated(Instant.now().toString());
		animal.setLocated(Utility.formatDate(Instant.now()));
		log.info("Animal {} ", animal.toString());
		return animalDao.saveAnimal(animal);

	}

	@Override
	public boolean deleteAnimal(String animalId) {

		DeleteResult deleteResult = animalDao.deleteAnimalById(animalId);

		if (deleteResult.getDeletedCount() > 0) {
			return true;
		}

		return false;

	}

	@Override
	public Animal getAnimal(String animalId) {

		return animalDao.getAnimalById(animalId);
	}

	@Override
	public UpdateResult updateAnimal(String animalId, String animalTitle) {

		return animalDao.updateAnimalById(animalId, animalTitle);
	}

}
