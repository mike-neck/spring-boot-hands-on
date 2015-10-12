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

import jsug.domain.validation.Confirm;
import jsug.domain.validation.UnusedEmail;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Confirm(field = "password")
public class AccountForm {

    @Email
    @Size(min = 1, max = 100)
    @NotNull
    @UnusedEmail
    private String email;

    @Size(min = 6)
    @NotNull
    private String password;

    @NotNull
    private String confirmPassword;

    @Size(min = 1, max = 40)
    @NotNull
    private String name;

    @DateTimeFormat(iso = ISO.DATE)
    @NotNull
    private LocalDate birthDay;

    @NotNull
    @Pattern(regexp = "[0-9]{7}")
    private String zip;

    @Size(min = 1, max = 100)
    @NotNull
    private String address;
}
