package com.mjc.school.controller.implementation;

import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthorControllerTest {
    private static final RequestSpecification SPEC = given().baseUri("http://localhost:8080/api/v1/authors")
            .contentType(ContentType.JSON)
            .queryParam("version", 1);


    @Test
    public void readAllAuthorsTest() {
        SPEC.param("size", 40)
                .when().get()
                .then().statusCode(200);
    }

    @Test
    public void readAuthorByIdTest() {
        AuthorDtoResponse response = SPEC.basePath("15")
                .when().get()
                .then().statusCode(200)
                .extract().as(AuthorDtoResponse.class);
        assertThat(response.getId()).isEqualTo(15L);
    }

    @Test
    public void createAuthorTest() {
        AuthorDtoResponse response = given()
                .baseUri("http://localhost:8080/api/v1")
                .basePath("authors")
                .contentType(ContentType.JSON)
                .queryParam("version", 1)
                .body(new AuthorDtoRequest(null, "cr_name"))
                .when().post()
                .then().statusCode(201)
                .extract().as(AuthorDtoResponse.class);

        assertThat(response.getName()).isEqualTo("cr_name");
    }

    @Test
    public void updateAuthorTest() {
        AuthorDtoResponse response = SPEC.basePath("/21")
                .body(new AuthorDtoRequest(null, "up_name"))
                .when().put()
                .then().statusCode(200)
                .extract().as(AuthorDtoResponse.class);

        assertThat(response.getId()).isEqualTo(21L);
        assertThat(response.getName()).isEqualTo("up_name");
    }

    @Test
    public void patchAuthorTest() {
        AuthorDtoResponse response = SPEC.basePath("/3")
                .body(new AuthorDtoRequest(null, "pat_name"))
                .when().patch()
                .then().statusCode(200)
                .extract().as(AuthorDtoResponse.class);

        assertThat(response.getId()).isEqualTo(3L);
        assertThat(response.getName()).isEqualTo("pat_name");
    }

    @Test
    public void deleteAuthorTest() {
        SPEC.basePath("/22")
                .when().delete()
                .then().statusCode(204);
    }
}
