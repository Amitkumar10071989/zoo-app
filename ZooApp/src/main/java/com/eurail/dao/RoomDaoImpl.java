package com.eurail.dao;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.eurail.constants.Constants;
import com.eurail.model.Animal;
import com.eurail.model.Room;
import com.eurail.utility.Utility;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

/**
 * In this class write queries to interact with the database
 * 
 * @author Amit
 *
 */
@Repository
public class RoomDaoImpl implements RoomDao {

	Logger logger = LoggerFactory.getLogger(RoomDaoImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Room createRoom(Room room) {

		return mongoTemplate.save(room);

	}

	@Override
	public boolean checkRoomExists(String title) {

		return mongoTemplate.exists(
				new Query().addCriteria(Criteria.where(Constants.TITLE).is(Utility.capitalize(title))), Room.class);

	}

	@Override
	public DeleteResult deleteRoomByTitle(String roomTitle) {

		Query query = new Query().addCriteria(Criteria.where(Constants.TITLE).is(Utility.capitalize(roomTitle)));

		return mongoTemplate.remove(query, Room.class);

	}

	@Override
	public Room getRoomByTitle(String title) {
		Query query = new Query(Criteria.where(Constants.TITLE).is(Utility.capitalize(title)));

		return mongoTemplate.findOne(query, Room.class, Constants.COLLECTION_ROOMS);
	}

	@Override
	public UpdateResult updateRoom(String title, String newTitle) {

		return mongoTemplate.updateFirst(
				Query.query(Criteria.where(Constants.TITLE).is(Utility.capitalize(title))), new Update()
						.set("updated", Instant.now().toString()).set(Constants.TITLE, Utility.capitalize(newTitle)),
				Constants.COLLECTION_ROOMS);
	}

	@Override
	public Animal findAndRemove(String animalId) {

		Query query = new Query().addCriteria(Criteria.where(Constants.ID).is(animalId));
		return mongoTemplate.findAndRemove(query, Animal.class);

	}

	@Override
	public Room findAndModifyByTitle(String roomTitle, Animal animal) {

		Query queryForAnimal = new Query()
				.addCriteria(Criteria.where(Constants.TITLE).is(Utility.capitalize(roomTitle)));
		Update update = new Update().push(Constants.ANIMALS, animal);
		update.set("updated", Instant.now().toString());
		return mongoTemplate.findAndModify(queryForAnimal, update, Room.class);

	}

	@Override
	public Animal[] findAnimal(String fromRoomName) {

		Query query = new Query(Criteria.where(Constants.TITLE).is(Utility.capitalize(fromRoomName)));

		return mongoTemplate.findOne(query, Room.class).getAnimals();

	}

	@Override
	public UpdateResult getAndUpdateAnimal(String fromRoomName, String animalId) {

		return mongoTemplate.updateFirst(
				Query.query(Criteria.where(Constants.TITLE).is(Utility.capitalize(fromRoomName))),
				new Update().pull(Constants.ANIMALS, Query.query(Criteria.where(Constants.ID).is(animalId)))
						.set("updated", Instant.now().toString()),
				Constants.COLLECTION_ROOMS);
	}

	@Override
	public Room findAndModify(String toRoomName, Animal animal) {

		Query query1 = new Query().addCriteria(Criteria.where(Constants.TITLE).is(Utility.capitalize(toRoomName)));
		Update update = new Update().push(Constants.ANIMALS, animal);
		update.set("updated", Instant.now().toString());
		return mongoTemplate.findAndModify(query1, update, Room.class);

	}

	@Override
	public Room assingFavouriteRoom(String roomName, String animalId) {

		Query query = new Query().addCriteria(Criteria.where(Constants.TITLE).is(Utility.capitalize(roomName)));
		Update update = new Update().push("favourites", animalId);

		return mongoTemplate.findAndModify(query, update, Room.class);

	}

	@Override
	public UpdateResult removedFavouriteRoom(String roomName, String animalId) {

		return mongoTemplate.updateFirst(Query.query(Criteria.where(Constants.TITLE).is(Utility.capitalize(roomName))),
				new Update().pull("favourites", animalId), Constants.COLLECTION_ROOMS);

	}

	@Override
	public Page<Document> getAnimalByRoomTitle(Query query, Aggregation aggregation, Pageable pageable) {

		List<Document> listOdDocument = mongoTemplate.aggregate(aggregation, Room.class, Document.class)
				.getMappedResults();

		return PageableExecutionUtils.getPage(listOdDocument, pageable,
				() -> mongoTemplate.count(query.skip(0).limit(0), Room.class));
	}

	@Override
	public Map<String, Integer> getListOfFavouriteRooms() {

		Map<String, Integer> favouriteRooms = new HashMap<String, Integer>();
		mongoTemplate.findAll(Room.class).stream()
				.filter(room -> room.getFavourites() != null && room.getFavourites().length > 0).forEach(a -> {
					favouriteRooms.put(a.getTitle(), a.getFavourites().length);
				});
		return favouriteRooms;
	}

	@Override
	public void saveAnimal(Animal animal) {

		mongoTemplate.save(animal);

	}

	@Override
	public boolean checkAnimalExists(String animalId) {

		return mongoTemplate.exists(new Query().addCriteria(Criteria.where(Constants.ID).is(animalId)), Animal.class);

	}

	@Override
	public String[] findFavourites(String roomTitle) {

		Query query = new Query(Criteria.where(Constants.TITLE).is(Utility.capitalize(roomTitle)));
		String[] result = mongoTemplate.findOne(query, Room.class).getFavourites();
		if (result == null)
			return new String[0];

		return result;

	}

}
