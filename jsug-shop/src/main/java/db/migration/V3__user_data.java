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
package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class V3__user_data implements JdbcMigration {

    private static final String INSERT_INTO_ACCOUNT = new StringBuilder()
            .append("INSERT INTO ")
            .append("account(")
            .append(  "email").append(',')
            .append(  "password").append(',')
            .append(  "name").append(',')
            .append(  "birth_day").append(',')
            .append(  "zip").append(',')
            .append(  "address").append(") ")
            .append("VALUES").append('(')
            .append(  '?').append(',')
            .append(  '?').append(',')
            .append(  '?').append(',')
            .append(  '?').append(',')
            .append(  '?').append(',')
            .append(  '?').append(')')
            .toString();

    @Override
    public void migrate(Connection connection) throws Exception {
        try(PreparedStatement ps = connection.prepareStatement(INSERT_INTO_ACCOUNT)) {
            ps.setString(1, "hoge@test.com");
            ps.setString(2, "$2a$10$4jFxLgOX6nBgKdzsPhc14.L3.k36Twnb9/hBSQoZUysJOpcG3epCm");
            ps.setString(3, "ほげた ほげお");
            ps.setDate(4, Date.valueOf(LocalDate.of(2000, 2, 29)));
            ps.setString(5, "1234567");
            ps.setString(6, "文京区湯島1-2-3");
            ps.execute();
        }
    }
}
