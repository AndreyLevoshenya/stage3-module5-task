package com.mjc.school.controller.implementation;

import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class TagControllerTest {
    private static final RequestSpecification SPEC = given().baseUri("http://localhost:8080/api/v1/tags")
            .contentType(ContentType.JSON)
            .queryParam("version", 1);

    @Test
    public void readAllTagsTest() {
        SPEC.basePath("").param("size", 40)
                .when().get()
                .then().statusCode(200);
    }

    @Test
    public void readTagByIdTest() {
        TagDtoResponse response = SPEC.basePath("/1")
                .when().get()
                .then().statusCode(200)
                .extract().as(TagDtoResponse.class);
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    public void createTagTest() {
        TagDtoResponse response = given()
                .baseUri("http://localhost:8080/api/v1")
                .basePath("tags")
                .contentType(ContentType.JSON)
                .queryParam("version", 1)
                .body(new TagDtoRequest(null, "cr_name"))
                .when().post()
                .then().statusCode(201)
                .extract().as(TagDtoResponse.class);

        assertThat(response.getName()).isEqualTo("cr_name");
    }

    @Test
    public void updateTagTest() {
        TagDtoResponse response = SPEC.basePath("/4")
                .body(new TagDtoRequest(null, "up_name"))
                .when().put()
                .then().statusCode(200)
                .extract().as(TagDtoResponse.class);

        assertThat(response.getId()).isEqualTo(4L);
        assertThat(response.getName()).isEqualTo("up_name");
    }

    @Test
    public void patchTagTest() {
        TagDtoResponse response = SPEC.basePath("/3")
                .body(new TagDtoRequest(null, "pat_name"))
                .when().patch()
                .then().statusCode(200)
                .extract().as(TagDtoResponse.class);

        assertThat(response.getId()).isEqualTo(3L);
        assertThat(response.getName()).isEqualTo("pat_name");
    }

    @Test
    public void deleteTagTest() {
        SPEC.basePath("/2")
                .when().delete()
                .then().statusCode(204);
    }
}
