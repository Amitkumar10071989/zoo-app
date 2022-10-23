package com.eurail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eurail.constants.Constants;
import com.eurail.model.Animal;
import com.eurail.service.AnimalService;
import com.mongodb.client.result.UpdateResult;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Amit
 *
 */
@Tag(name = "Animal Controller", description = "(Version 1) Controller that basically do the CRUD operations for Animals!")
@RestController
@RequestMapping("/v1/animal")
@Slf4j
public class AnimalController {

	/**
	 * Animal service instance
	 */
	@Autowired
	private AnimalService animalService;

	/**
	 * Creates an Animal
	 * 
	 * @param title
	 * @return
	 */
	@PostMapping("/create")
	public ResponseEntity<Animal> createAnimal(@RequestParam("title") String title) {
		log.info("createAnimal title: {} ", title);

		Animal animal = animalService.createAnimal(title);
		if (animal != null)
			return new ResponseEntity<Animal>(animal, HttpStatus.CREATED);

		return new ResponseEntity<Animal>(animal, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Fetch Animal by id
	 * 
	 * @param animalId
	 * @return
	 */
	@GetMapping("/fetch")
	public ResponseEntity<Animal> getAnimalById(@RequestParam("animalId") String animalId) {
		log.info("getAnimalById Animal id:  {} ", animalId);

		Animal animal = animalService.getAnimal(animalId);
		if (animal != null)
			return new ResponseEntity<Animal>(animal, HttpStatus.OK);

		return new ResponseEntity<Animal>(animal, HttpStatus.NOT_FOUND);
	}

	/**
	 * Delete Animal by using id
	 * 
	 * @param animalId
	 * @return
	 */
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteAnimal(@RequestParam("animalId") String animalId) {
		log.info("deleteAnimal Animal id:  {} ", animalId);

		if (animalService.deleteAnimal(animalId))
			return new ResponseEntity<String>(Constants.ANIMAL_DELETED, HttpStatus.OK);

		return new ResponseEntity<String>(Constants.NOT_FOUND, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Update animal title using Animal id
	 * 
	 * @param animalId
	 * @param animalTitle
	 * @return
	 */
	@PutMapping("/modify")
	public ResponseEntity<String> updateAnimal(@RequestParam("animalId") String animalId,
			@RequestParam("animalTitle") String animalTitle) {
		log.info("Animal id: {} ", animalId);

		UpdateResult updateResult = animalService.updateAnimal(animalId, animalTitle);
		if (updateResult.getModifiedCount() > 0)
			return new ResponseEntity<String>(Constants.ANIMAL_UPDATED, HttpStatus.OK);

		return new ResponseEntity<String>(Constants.MALFORMED_INPUT, HttpStatus.BAD_REQUEST);
	}

}
