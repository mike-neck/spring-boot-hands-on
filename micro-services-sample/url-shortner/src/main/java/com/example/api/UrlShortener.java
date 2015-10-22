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

import com.google.common.hash.Hashing;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@Configuration
@RestController
@RefreshScope
public class UrlShortener {

    @Value("${urlshorten.url:http://localhost:${server.port:8080}}")
    String urlShortenerUrl;

    @Autowired
    StringRedisTemplate redis;

    final UrlValidator validator =
            new UrlValidator(new String[]{"http", "https"});

    @RequestMapping(value = "/", method = RequestMethod.POST)
    ResponseEntity<String> save(@RequestParam String url) {
        if (validator.isValid(url)) {
            String hash = getHash(url);
            redis.opsForValue().set(hash, url);
            String shorten = new StringBuilder(urlShortenerUrl)
                    .append('/')
                    .append(hash)
                    .toString();
            return new ResponseEntity<>(shorten, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    String getHash(String url) {
        int hashCode = Hashing.murmur3_32()
                .hashString(url, StandardCharsets.UTF_8)
                .asInt();
        return Integer.toHexString(hashCode);
    }

    @RequestMapping(value = "{hash}", method = RequestMethod.GET)
    ResponseEntity<?> get(@PathVariable String hash) {
        String url = redis.opsForValue().get(hash);
        if (url != null) {
            return new ResponseEntity<>(url, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
