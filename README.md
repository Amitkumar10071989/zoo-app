
# Zoo Back End API

This project basically interacts with the database to get the details of the Animals & Rooms, Modify the details,
Insert new details, Delete the existing ones (CRUD operations) and some special operations.

## Requirement

The project need to expose below endpoints, that can talk to database layer and provide appropreate response.

## Animals

| OPERATION | ENDPOINT                | DESCRIPTION                                                                                                  |
| --------- |---------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| GET | /fetch?title=animalID      | Endpoint fetches the required data based on the input variables (animalId)                |
| POST | /create?title=animalTitle      | Endpoint inserts the new records.
| PUT | /modify?animalId=animalId&animalTitle=animailTitle | Endpoint updates the title based on the input id |
| DELETE | /delete?animalId=animalId | Endpoint deletes the existing Animal based on the unique identifier passed from the external provider          | 

## Rooms 

| OPERATION | ENDPOINT                | DESCRIPTION                                                                                                  |
| --------- |---------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| GET | /{title}      | Endpoint fetches the required data based on the input variables (roomTitle)                |
| POST | /create?title={title}      | Endpoint inserts the new records.
| PUT | /modify?title=roomTitle&newTitle={newRoomTitle} | Endpoint updates the title based on the input title |
| DELETE | /delete/{roomTitle} | Endpoint deletes the existing Room based on the unique identifier   | 
| POST | /place-animal?animalId={animalId}&roomTitle={roomTitle} | Endpoint place an animal into the room |
| POST |/move-animal?fromRoomTitle=?{fromRoomTile}&toRoomTitle={toRoomTitle}&animalId={animalId} | Enpoint move an animal from one room to another room based on their id |
| DELETE |/remove-animal?roomTitle={roomTitle&animalId={animalId}} | Enpoint remove an animal from the specific room based on animal id | 
| PUT |/favourite?roomTitle={roomTitle&animalId={animalId}&isFavourite={isFavourite}} | Enpoint assign a favourite room to the animal based on their animal id |
| GET |/fetch-all-animals?roomTitle={roomTitle} | Endpoint fetch all the animals from a specific room based on room title| 
| GET |/fetch-favourite| Enpoint fetchs all the favourite rooms which are assigned to animals|


## Skill set used

- Java 11
- Spring Boot
- MongoDB
- Docker
- Swagger


## How to start the project?
### There are two ways:-

Since this project uses the MongoDB  to support our operations.
1. Run on local machine 
	-Need to start MongoDb instance on local
	-import the application into eclipse as an existing maven project
	-right click on ZooAppApplication.java file and Run As > Java Application 

2. Run on using Docker Compose file 
	-go to project root directory, where pom.xml file is present
	-build the jar by usnig: > mvn clean package, verify that a jar is build insite target folder
	Go to the directory where the Dockerfile exists and run the following command in Command Prompt/Terminal to create the docker image
	>docker build -t zoo-app:1.0 .

	Go to the project directory, where docker-compose.yml is present and execute cmd 
	>docker-compose up

## WE ARE READY?

lets start the microservice project, so by default it will start on the port localhost:8080 now!

## Swagger API

> Access Swagger API page on the URL **http://localhost:8080/swagger-ui/**

## Project Structure

-  **controller**: contains the REST end points for application.
- **service**: service layer of application, fulfilling business logic
- **repository**: Its an interface which allows various operations on Animals & Rooms objects. It gets these operations by extending MongoTemplate.

Spring boot uses some default properties for interacting with MongoDB, e.g:
>spring.data.mongodb.port=27017 # Mongo server port. 
spring.data.mongodb.repositories.enabled=true # Enable Mongo repositories. 
spring.data.mongodb.uri=mongodb://localhost/test

## Testing the Application

For testing this application, I have used JUnit and Mockito. All these dependencies are maintained by Spring Boot. Check pom.xml for "spring-boot-starter-test" dependency , it is taking care of all underlying dependencies and latest versions of these open source projects.

   - **Testing controller**: AnimalControllerTest & RoomControllerTest contains various tests for AnimalController and RoomController. There are tests for both sucess and failure scenarios. --MockMvc is used to mock various POST, GET and DELETE calls. --Mockito is used to mock the AnimalService & RoomService and return the reponse from mocked service instead of hitting actual service and DB.
   - **Testing Service**: AnimalServiceTest & RoomServiceTest contains various tests for AnimalService & RoomService. --Mockito is used here to mock the repository calls, instead of hitting the actual repository, the service requests will be returned by our mocked repository.
