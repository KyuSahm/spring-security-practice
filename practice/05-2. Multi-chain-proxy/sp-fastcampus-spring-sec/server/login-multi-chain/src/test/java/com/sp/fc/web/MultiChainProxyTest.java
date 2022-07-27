package com.sp.fc.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.fc.web.student.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MultiChainProxyTest {
    @LocalServerPort
    int port;
    RestTemplate restTemplate = new RestTemplate();

    @DisplayName("1. 학생조사")
    @Test
    void test_1() throws JsonProcessingException {
        String url = format("http://localhost:%d/api/teacher/students", port);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION,
                "Basic " + Base64.getEncoder().encodeToString("choi:1".getBytes()));
        HttpEntity entity = new HttpEntity(null, headers);
        ResponseEntity<List<Student>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Student>>(){});
        List<Student> studentList = response.getBody();
        assertEquals(3, studentList.size());
    }
}
