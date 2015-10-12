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
package jsug.app.account;

import jsug.domain.model.Account;
import jsug.domain.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("account")
public class AccountController {

    private static final String TO_CREATE_FORM = "account/createForm";

    @Autowired
    AccountService accountService;

    @ModelAttribute
    AccountForm setupForm() {
        return new AccountForm();
    }

    @RequestMapping(value = "create", params = "form", method = RequestMethod.GET)
    String createForm() {
        return TO_CREATE_FORM;
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    String create(@Validated AccountForm form,
                  BindingResult result,
                  RedirectAttributes attr) {
        if (result.hasErrors()) {
            return TO_CREATE_FORM;
        }
        Account account = Account.builder()
                .name(form.getName())
                .email(form.getEmail())
                .birthDay(form.getBirthDay())
                .zip(form.getZip())
                .address(form.getAddress())
                .build();
        accountService.register(account, form.getPassword());
        attr.addFlashAttribute(account);
        return "redirect:/account/create?finish";
    }

    @RequestMapping(value = "create", params = "finish", method = RequestMethod.GET)
    String createFinish() {
        return "account/createFinish";
    }
}
