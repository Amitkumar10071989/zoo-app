package com.eurail.dao;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.eurail.constants.Constants;
import com.eurail.model.Animal;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

/**
 * In this class write queries to interact with the database
 * 
 * @author Amit
 *
 */
@Repository
public class AnimalDaoImpl implements AnimalDao {

	Logger logger = LoggerFactory.getLogger(AnimalDaoImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Animal saveAnimal(Animal animal) {

		return mongoTemplate.save(animal);
	}

	@Override
	public DeleteResult deleteAnimalById(String animalId) {

		Query query = new Query().addCriteria(Criteria.where(Constants.ID).is(animalId));

		return mongoTemplate.remove(query, Animal.class);

	}

	@Override
	public Animal getAnimalById(String animalId) {

		Query query = new Query(Criteria.where(Constants.ID).is(animalId));

		return mongoTemplate.findOne(query, Animal.class);

	}

	@Override
	public UpdateResult updateAnimalById(String animalId, String animalTitle) {

		return mongoTemplate.updateFirst(Query.query(Criteria.where(Constants.ID).is(animalId)),
				new Update().set("updated", Instant.now().toString()).set(Constants.TITLE, animalTitle),
				Constants.COLLECTION_ANIMALS);
	}

}
