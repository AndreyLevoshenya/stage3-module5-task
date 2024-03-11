package com.mjc.school.controller.implementation;

import com.mjc.school.service.dto.*;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

class NewsControllerTest {
    private static final RequestSpecification SPEC = given().baseUri("http://localhost:8080/api/v1/news")
            .contentType(ContentType.JSON)
            .queryParam("version", 1);

    @Test
    public void readAllNewsTest() {
        SPEC.param("size", 40)
                .when().get()
                .then().statusCode(200);
    }

    @Test
    public void readNewsByIdTest() {
        NewsDtoResponse response = SPEC.basePath("/15")
                .when().get()
                .then().statusCode(200)
                .extract().as(NewsDtoResponse.class);
        assertThat(response.getId()).isEqualTo(15L);
    }

    @Test
    public void createNewsTest() {
        NewsDtoResponse response = SPEC.basePath("")
                .body(new NewsDtoRequest(null, "cr_title", "content", 1L, new ArrayList<>()))
                .when().post()
                .then().statusCode(201)
                .extract().as(NewsDtoResponse.class);

        assertThat(response.getTitle()).isEqualTo("cr_title");
        assertThat(response.getContent()).isEqualTo("content");
        assertThat(response.getAuthorDtoResponse().getId()).isEqualTo(1L);
    }

    @Test
    public void updateNewsTest() {
        NewsDtoResponse response = SPEC.basePath("/10")
                .body(new NewsDtoRequest(null, "up_title", "content", 1L, new ArrayList<>()))
                .when().put()
                .then().statusCode(200)
                .extract().as(NewsDtoResponse.class);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getTitle()).isEqualTo("up_title");
        assertThat(response.getContent()).isEqualTo("content");
        assertThat(response.getAuthorDtoResponse().getId()).isEqualTo(1L);
    }

    @Test
    public void patchNewsTest() {
        NewsDtoResponse response = SPEC.basePath("/3")
                .body(new NewsDtoRequest(null, "pat_title", null, null, null))
                .when().patch()
                .then().statusCode(200)
                .extract().as(NewsDtoResponse.class);

        assertThat(response.getId()).isEqualTo(3L);
        assertThat(response.getTitle()).isEqualTo("pat_title");
        assertThat(response.getContent()).isNotNull();
        assertThat(response.getAuthorDtoResponse()).isNotNull();
    }

    @Test
    public void deleteNewsTest() {
        SPEC.basePath("/22")
                .when().delete()
                .then().statusCode(204);
    }

    @Test
    public void readNewsByParamsTest() {
        List<NewsDtoResponse> response = SPEC.basePath("/get")
                .body(new ParametersDtoRequest("title", "content", "William Shakespeare", new ArrayList<>(), new ArrayList<>()))
                .when().get()
                .then().statusCode(200)
                .extract().jsonPath().getList("", NewsDtoResponse.class);
        assertThat(response).isNotNull();
    }

    @Test
    public void getAuthorByNewsId() {
        AuthorDtoResponse response = SPEC.basePath("/1/authors")
                .when().get()
                .then().statusCode(200)
                .extract().as(AuthorDtoResponse.class);
        assertThat(response).isNotNull();
    }

    @Test
    public void getTagsByNewsId() {
        List<TagDtoResponse> response = SPEC.basePath("/1/tags")
                .when().get()
                .then().statusCode(200)
                .extract().jsonPath().getList("", TagDtoResponse.class);
        assertThat(response).isNotNull();
    }

    @Test
    public void getCommentsByNewsId() {
        List<CommentDtoResponse> response = SPEC.basePath("1/comments")
                .when().get()
                .then().statusCode(200)
                .extract().jsonPath().getList("", CommentDtoResponse.class);
        assertThat(response).isNotNull();
    }
}