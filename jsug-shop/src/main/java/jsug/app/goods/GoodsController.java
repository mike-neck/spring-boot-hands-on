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
package jsug.app.goods;

import jsug.domain.model.Cart;
import jsug.domain.model.Category;
import jsug.domain.model.Goods;
import jsug.domain.model.OrderLine;
import jsug.domain.service.category.CategoryService;
import jsug.domain.service.goods.GoodsNotFoundException;
import jsug.domain.service.goods.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    Cart cart;

    @ModelAttribute("categories")
    List<Category> getCategories() {
        return categoryService.findAll();
    }

    @ModelAttribute
    AddToCartForm addToCartForm() {
        return new AddToCartForm();
    }

    @RequestMapping(value = "/")
    String showGoods(
            @RequestParam(defaultValue = "1") Integer categoryId,
            @PageableDefault Pageable pageable, Model model) {
        Page<Goods> page = goodsService.findByCategoryId(categoryId, pageable);
        model.addAttribute("page", page);
        model.addAttribute("categoryId", categoryId);
        return "goods/showGoods";
    }

    @RequestMapping(value = "/addToCart", method = RequestMethod.POST)
    String addToCart(
            @Validated AddToCartForm form, BindingResult result,
            @PageableDefault Pageable pageable, Model model) {
        if (result.hasErrors()) {
            return showGoods(form.getCategoryId(), pageable, model);
        }
        Goods goods = goodsService.findOne(form.getGoodsId());
        OrderLine line = OrderLine.builder()
                .goods(goods)
                .quantity(form.getQuantity())
                .build();
        cart.add(line);
        return "redirect:/cart";
    }

    @ExceptionHandler(GoodsNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String handleGoodsNotFoundException() {
        return "goods/notFound";
    }
}
