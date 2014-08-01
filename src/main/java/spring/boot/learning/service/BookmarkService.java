package spring.boot.learning.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.learning.domain.Bookmark;
import spring.boot.learning.repository.BookmarkRepository;

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
@Service
@Transactional
public class BookmarkService {

    @Autowired
    BookmarkRepository repository;

    public List<Bookmark> findAll () {
        return repository.findAll(new Sort(Sort.Direction.ASC, "id"));
    }

    public Bookmark save(Bookmark bookmark) {
        return repository.save(bookmark);
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
