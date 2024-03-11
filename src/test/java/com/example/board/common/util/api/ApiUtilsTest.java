package com.example.board.common.util.api;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiUtilsTest {
    private class TestObject {
        private int number;
        private String name;

        public TestObject(int number, String name) {
            this.number = number;
            this.name = name;
        }

        public int getNumber() {
            return number;
        }

        public String getName() {
            return name;
        }
    }

    @Test
    void success_객체_생성_성공() {
        TestObject testObject = new TestObject(10, "Kim");
        ResponseEntity<ApiUtils.ApiResult<TestObject>> success = ApiUtils.success(testObject, HttpStatus.OK);

        assertAll("testObject verification",
                () -> assertEquals(HttpStatus.OK, success.getStatusCode()),
                () -> assertEquals(testObject.getNumber(), Objects.requireNonNull(success.getBody()).getResponse().getNumber()),
                () -> assertEquals(testObject.getName(), Objects.requireNonNull(success.getBody()).getResponse().getName())
        );
    }

    @Test
    void error_객체_생성_성공() {
        ResponseEntity<ApiUtils.ApiResult<Void>> error = ApiUtils.error(new RuntimeException("테스트용 RuntimeException"), HttpStatus.BAD_REQUEST);

        assertAll("testObject verification",
                () -> assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode()),
                () -> assertEquals("테스트용 RuntimeException", Objects.requireNonNull(error.getBody()).getError().getMessage())
        );
    }
}