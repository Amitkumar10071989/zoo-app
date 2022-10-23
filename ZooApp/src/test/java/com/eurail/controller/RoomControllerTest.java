package com.eurail.controller;

import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.Assertions;
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

import com.eurail.model.Room;
import com.eurail.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(RoomController.class)
public class RoomControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RoomService roomService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testGetRoomByTitleSuccess() throws Exception {

		when(roomService.getRoomByTitle(Mockito.anyString())).thenReturn(getRoom());

		// when - action or the behaviour that we are going test
		RequestBuilder request = MockMvcRequestBuilders.get("/v1/room/Green")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(getRoom()));

		ResultActions response = mockMvc.perform(request);
		// then - verify the output
		response.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());

	}

	@Test
	public void testGetRoomByTitleFailure() throws Exception {
		Room room = null;
		when(roomService.getRoomByTitle(Mockito.anyString())).thenReturn(room);

		RequestBuilder request = MockMvcRequestBuilders.get("/v1/room/getRoom/title=Green")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(room));

		ResultActions response = mockMvc.perform(request);
		response.andExpect(MockMvcResultMatchers.status().isNotFound()).andDo(MockMvcResultHandlers.print());

	}

	@Test
	public void testCreateRoomSuccess() throws Exception {

		when(roomService.createRoom(Mockito.anyString())).thenReturn(getRoom());

		RequestBuilder request = MockMvcRequestBuilders.post("/v1/room/create?title=Green")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(getRoom()));

		ResultActions response = mockMvc.perform(request);
		response.andExpect(MockMvcResultMatchers.status().isCreated());
		
				

	}

	@Test
	public void testCreateRoomFalure() throws Exception {

		Room room = null;

		when(roomService.createRoom(Mockito.anyString())).thenReturn(room);

		RequestBuilder request = MockMvcRequestBuilders.post("/v1/room/create?title=Green")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(room));

		ResultActions response = mockMvc.perform(request);
		response.andExpect(MockMvcResultMatchers.status().is3xxRedirection());			

	}
	
	@Test
	public void testPlaceAnAnimalInRoomSuccess() {
		String url = "/v1/room/place-animal?animalId=634e1eebba33c202645d776a&roomTitle=Green";
		Room room = new Room();
		room.setTitle("Green");
		room.setCreated(Instant.now().toString());
		room.setUpdated(Instant.now().toString());

		when(roomService.placeAnimalInRoom(Mockito.anyString(), Mockito.anyString())).thenReturn(room);

		RequestBuilder request;
		try {
			request = MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(room));

			ResultActions response = mockMvc.perform(request);
			response.andExpect(MockMvcResultMatchers.status().isOk());
		} catch (Exception e) {
			Assertions.fail(e);
		}

	}
	
	@Test
	public void testMoveAnimalFrmOneRoomToOtherSuccess() throws Exception {
		
		when(roomService.moveAnimalFrmOneRoomToOther(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(getRoom());
		
		RequestBuilder request = MockMvcRequestBuilders.post("/v1/room/move-animal?fromRoomTitle=Green&toRoomTitle=Big&animalId=634e1eebba33c202645d776a")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(getRoom()));

		ResultActions response = mockMvc.perform(request);
		response.andExpect(MockMvcResultMatchers.status().isOk());		
	}
	
	private Room getRoom() {
		Room room = new Room();

		room.setId("634e1eebba33c202645d776a");
		room.setTitle("Green");
		room.setCreated(Instant.now().toString());
		room.setUpdated(Instant.now().toString());
		
		return room;
	}
	
	
}
