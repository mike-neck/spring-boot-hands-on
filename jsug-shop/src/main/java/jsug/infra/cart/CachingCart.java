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
package jsug.infra.cart;

import jsug.domain.model.Cart;
import jsug.domain.model.OrderLine;
import jsug.domain.model.OrderLines;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class CachingCart extends Cart {

    public CachingCart() {
        super();
        loadCache();
    }

    void loadCache() {
        withSyncCache(() -> {}, false);
    }

    void withSyncCache(Runnable action, boolean save) {
        Cache cache = getCache();
        String userName = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        OrderLines orderLines = cache.get(userName, OrderLines.class);

        if (orderLines != null) {
            log.debug("load {} -> {}", userName, orderLines);
            List<OrderLine> list = new ArrayList<>(orderLines.getList());
            super.getOrderLines().getList().clear();
            super.getOrderLines().getList().addAll(list);
        }

        action.run();
        if (save) {
            if (log.isDebugEnabled()) {
                log.debug("save {} -> {}", userName, super.getOrderLines());
            }
            cache.put(userName, super.getOrderLines());
        }
    }

    Cache getCache() {
        ServletRequestAttributes attr = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        WebApplicationContext context = RequestContextUtils
                .getWebApplicationContext(attr.getRequest());
        CacheManager cacheManager = context.getBean(CacheManager.class);
        return cacheManager.getCache("orderLines");
    }

    @Override
    public OrderLines getOrderLines() {
        loadCache();
        return super.getOrderLines();
    }

    @Override
    public Cart add(OrderLine orderLine) {
        withSyncCache(() -> super.add(orderLine), true);
        return this;
    }

    @Override
    public Cart remove(Set<Integer> lineNo) {
        withSyncCache(() -> super.remove(lineNo), true);
        return this;
    }

    @Override
    public Cart clear() {
        withSyncCache(super::clear, true);
        return this;
    }
}
