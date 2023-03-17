package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.DemoApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = DemoApplication.class)
@AutoConfigureMockMvc
@DirtiesContext
@Testcontainers
public class WarehouseControllerTest {
  @Autowired private MockMvc mvc;

  @Container
  static MongoDBContainer mongoDBContainer =
      new MongoDBContainer(DockerImageName.parse("mongo:6.0.4"));

  @DynamicPropertySource
  static void registerPgProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri", () -> mongoDBContainer.getConnectionString());
    registry.add("spring.data.mongodb.database", () -> "testDB");
  }

  @BeforeAll
  public static void initContainers() {
    Assertions.assertTrue(mongoDBContainer.isRunning());
  }

  @Test
  void testCreate() throws Exception {
    mvc.perform(get("/api/warehouses").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
}
