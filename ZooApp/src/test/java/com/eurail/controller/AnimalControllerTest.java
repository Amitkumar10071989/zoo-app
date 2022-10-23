package com.eurail.controller;

import static org.mockito.Mockito.when;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.eurail.model.Animal;
import com.eurail.service.AnimalService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AnimalController.class)
public class AnimalControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	AnimalService animalService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testGetAnimalByIdSuccess() throws Exception {
		String uri = "/v1/animal/fetch?animalId=634e1eebba33c202645d776a";
		Animal animal = new Animal();
		animal.setId("634e1eebba33c202645d776a");
		animal.setCreated(Instant.now().toString());
		animal.setUpdated(Instant.now().toString());
		animal.setLocated("2022-10-20");

		when(animalService.getAnimal(Mockito.anyString())).thenReturn(animal);

		RequestBuilder request = MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(animal));

		ResultActions response = mockMvc.perform(request);
		response.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());

	}

	@Test
	public void testGetAnimalByIdFalure() throws Exception {
		String uri = "/v1/animal/getAnimal?animalId=634e1eebba33c202645d776a";
		Animal animal = null;

		when(animalService.getAnimal(Mockito.anyString())).thenReturn(animal);

		RequestBuilder request = MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(animal));

		ResultActions response = mockMvc.perform(request);
		response.andExpect(MockMvcResultMatchers.status().is4xxClientError()).andDo(MockMvcResultHandlers.print());

	}

	@Test
	public void testCreateAnimalSuccess() throws Exception {
		String uri = "/v1/animal/create?title=Tiger";
		Animal animal = new Animal();
		animal.setTitle("Tiger");
		animal.setCreated(Instant.now().toString());
		animal.setUpdated(Instant.now().toString());

		when(animalService.createAnimal(Mockito.anyString())).thenReturn(animal);

		RequestBuilder request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(animal));

		ResultActions response = mockMvc.perform(request);
		response.andExpect(MockMvcResultMatchers.status().isCreated());

	}

	@Test
	public void testCreateAnimalFalure() throws Exception {
		String uri = "/v1/animal/createAnimal?title=Tiger";
		Animal animal = null;

		when(animalService.createAnimal(Mockito.anyString())).thenReturn(animal);

		RequestBuilder request = MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(animal));

		ResultActions response = mockMvc.perform(request);
		response.andExpect(MockMvcResultMatchers.status().is4xxClientError());

	}

}
