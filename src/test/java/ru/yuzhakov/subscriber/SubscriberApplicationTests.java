package ru.yuzhakov.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yuzhakov.subscriber.domain.Subscription;
import ru.yuzhakov.subscriber.domain.User;
import ru.yuzhakov.subscriber.dto.TopSubscribesResponse;
import ru.yuzhakov.subscriber.services.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class SubscriberApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private UserService userService;

	@Test
	void getAllUsers_ReturnsListOfUsers() throws Exception {
		User user1 = new User(1L, "Ivan", "Ivanov", "Ivanovich", null);
		User user2 = new User(2L, "Petr", "Petrov", "Petrovich", null);
		List<User> users = List.of(user1, user2);

		when(userService.getAllUsers()).thenReturn(users);

		mockMvc.perform(get("/api/v1/users"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(2))
				.andExpect(jsonPath("$[0].firstName").value("Ivan"))
				.andExpect(jsonPath("$[1].firstName").value("Petr"));
	}

	@Test
	void getUser_ReturnsUserById() throws Exception {
		User user = new User(1L, "Ivan", "Ivanov", "Ivanovich", null);

		when(userService.getUser(1L)).thenReturn(user);

		mockMvc.perform(get("/api/v1/users/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.firstName").value("Ivan"));
	}

	@Test
	void addUser_ReturnsCreatedUser() throws Exception {
		User newUser = new User(null, "Ivan", "Ivanov", "Ivanovich", null);
		User savedUser = new User(1L, "Ivan", "Ivanov", "Ivanovich", null);

		when(userService.addUser(any(User.class))).thenReturn(savedUser);

		mockMvc.perform(post("/api/v1/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newUser)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.firstName").value("Ivan"));
	}

	@Test
	void updateUser_ReturnsUpdatedUser() throws Exception {
		User updatedUser = new User(1L, "Ivan", "Ivanov", "Ivanovich", null);

		when(userService.updateUser(anyLong(), any(User.class))).thenReturn(updatedUser);

		mockMvc.perform(put("/api/v1/users/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedUser)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.firstName").value("Ivan"));
	}

	@Test
	void deleteUser_ReturnsSuccessMessage() throws Exception {
		mockMvc.perform(delete("/api/v1/users/1"))
				.andExpect(status().isOk())
				.andExpect(content().string("User 1 was deleted"));
	}

	@Test
	void addUserSubscription_ReturnsUserWithSubscription() throws Exception {
		User user = new User(1L, "Ivan", "Ivanov", "Ivanovich", null);
		Subscription subscription = new Subscription(1L, "Netflix", 10.0f, true, new Date(), new Date());

		when(userService.addSubscriptionToUser(anyLong(), any(Subscription.class))).thenReturn(user);

		mockMvc.perform(post("/api/v1/users/1/subscriptions")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(subscription)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1));
	}

	@Test
	void getUserSubscriptions_ReturnsListOfSubscriptions() throws Exception {
		Subscription sub1 = new Subscription(1L, "Netflix", 10.0f, true, new Date(), new Date());
		Subscription sub2 = new Subscription(2L, "Spotify", 5.0f, true, new Date(), new Date());
		List<Subscription> subscriptions = List.of(sub1, sub2);

		when(userService.getUserSubscription(1L)).thenReturn(subscriptions);

		mockMvc.perform(get("/api/v1/users/1/subscriptions"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(2))
				.andExpect(jsonPath("$[0].serviceName").value("Netflix"))
				.andExpect(jsonPath("$[1].serviceName").value("Spotify"));
	}

	@Test
	void deleteUserSubscription_ReturnsUpdatedUser() throws Exception {
		User user = new User(1L, "Ivan", "Ivanov", "Ivanovich", null);

		when(userService.deleteUserSubscription(1L, 1L)).thenReturn(user);

		mockMvc.perform(delete("/api/v1/users/1/subscriptions/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1));
	}

	@Test
	void getTopThreeSubscriptions_ReturnsTopSubscriptions() throws Exception {
		TopSubscribesResponse top1 = new TopSubscribesResponse(1L, "Netflix", 100);
		TopSubscribesResponse top2 = new TopSubscribesResponse(2L, "Spotify", 80);
		TopSubscribesResponse top3 = new TopSubscribesResponse(3L, "YouTube", 50);
		List<TopSubscribesResponse> topSubscriptions = List.of(top1, top2, top3);

		when(userService.getTopThreeSubscriptions()).thenReturn(topSubscriptions);

		mockMvc.perform(get("/api/v1/subscriptions/top"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(3))
				.andExpect(jsonPath("$[0].serviceName").value("Netflix"));
	}
}
