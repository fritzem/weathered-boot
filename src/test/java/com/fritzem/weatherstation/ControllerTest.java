package com.fritzem.weatherstation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fritzem.weatherstation.controller.SensorController;
import com.fritzem.weatherstation.model.Report;
import com.fritzem.weatherstation.model.Sensor;
import com.fritzem.weatherstation.repository.ReportRepository;
import com.fritzem.weatherstation.repository.SensorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    SensorRepository sensorRepository;

    private Sensor testSensor;

    @BeforeEach
    public void setup() {
        testSensor = new Sensor("testland", "testtown");
        sensorRepository.save(testSensor);
    }

    @AfterEach
    public void teardown() {
        reportRepository.deleteAll();
        sensorRepository.deleteAll();
    }

    @Test
    public void queryNoContent() throws Exception {
        mockMvc.perform(post("/reports").content("{ }").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void generateAndRetrieveReport() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode request = mapper.createObjectNode()
                .put("temperature", 70.3)
                        .put("humidity", 23.0);
        mockMvc.perform(post("/" + testSensor.getId() + "/report").content(request.toString())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(post("/reports").content("{ }").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
