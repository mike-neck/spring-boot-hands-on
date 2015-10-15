/*
 * Copyright 2015 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.api;

import com.example.UrlShortenerApplication;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class UrlShortenerTest {

    public static class UnitTest {

        private UrlShortener shortener;

        @Before
        public void setup() {
            shortener = new UrlShortener();
        }

        @Test
        public void shortenTwitter() {
            String actual = shortener.getHash("https://twitter.com");
            assertThat(actual, is("b78187bf"));
        }
    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = {UrlShortenerApplication.class, UrlShortener.class})
    @WebAppConfiguration
    @IntegrationTest({"server.port:0"})
    public static class IntegralTest {

        @Value("${local.server.port}")
        int port;

        @Before
        public void setup() {
            RestAssured.port = port;
        }

        @Test
        public void testSaveAndGet() {
            given().log().all()
                    .when()
                    .body("url=https://twitter.com")
                    .contentType("application/x-www-form-urlencoded")
                    .post()
                    .then()
                    .log().all()
                    .statusCode(200)
                    .body(is("http://localhost:0/b78187bf"));

            given().log().all()
                    .when()
                    .get("/b78187bf")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .body(is("https://twitter.com"));
        }

        @Test
        public void invalidUrl() {
            given().log().all()
                    .when()
                    .body("url=foobar")
                    .contentType("application/x-www-form-urlencoded")
                    .post()
                    .then()
                    .log().all()
                    .statusCode(400);
        }

        @Test
        public void notExist() {
            given().log().all()
                    .when()
                    .get("/20150220")
                    .then()
                    .log().all()
                    .statusCode(404);
        }
    }
}
