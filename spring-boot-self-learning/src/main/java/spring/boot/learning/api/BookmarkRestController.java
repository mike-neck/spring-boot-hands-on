package spring.boot.learning.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.boot.learning.domain.Bookmark;
import spring.boot.learning.service.BookmarkService;

import java.util.List;

/**
 * Copyright 2014Shinya Mochida
 * <p>
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@RestController
@RequestMapping("api/bookmarks")
public class BookmarkRestController {

    @Autowired
    BookmarkService service;

    @RequestMapping(method = RequestMethod.GET)
    public List<Bookmark> getBookmarks() {
        return service.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Bookmark post(@RequestBody @Validated Bookmark bookmark) {
        return service.save(bookmark);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    @ResponseStatus(HttpStatus.OK)
    public Bookmark findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }
}
