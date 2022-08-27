package com.sp.fc.web;

import com.sp.fc.web.student.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MultiChainProxyTest {
    @LocalServerPort
    private int port;
    TestRestTemplate testRestTemplate = new TestRestTemplate("choi", "1");

    @DisplayName("1. 학생 리스트를 조회")
    @Test
    void test_1() {
        // choi로 로그인해서 학생 리스트를 내려 받음
        ResponseEntity<List<Student>> response =
                testRestTemplate.exchange("http://localhost:" + port + "/api/teacher/students",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {});
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
    }

}
