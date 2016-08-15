/**
 * Copyright 2010-2016 Boxfuse GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flywaydb.core.internal.dbsupport.ingres;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.flywaydb.core.internal.dbsupport.DbSupport;
import org.flywaydb.core.internal.dbsupport.JdbcTemplate;
import org.flywaydb.core.internal.dbsupport.Schema;
import org.flywaydb.core.internal.dbsupport.SqlStatementBuilder;
import org.flywaydb.core.internal.util.StringUtils;

public class Ingres10DbSupport extends DbSupport {

	public Ingres10DbSupport(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
	}

	public Ingres10DbSupport(Connection connection) {
		super(new JdbcTemplate(connection, Types.NULL));
	}

    @Override
    public String getDbName() {
        return "ingres";
    }

    public String getCurrentUserFunction() {
        return "current_user";
    }

    @Override
    protected String doGetCurrentSchemaName() throws SQLException {
        // Schemas are not actually supported by Ingres. Returning User
        return StringUtils.trimTrailingWhitespace(jdbcTemplate.queryForString("SELECT current_user"));
    }


    public boolean supportsDdlTransactions() {
        return true;
    }

    public String getBooleanTrue() {
        return "1";
    }

    public String getBooleanFalse() {
        return "0";
    }

    public SqlStatementBuilder createSqlStatementBuilder() {
        return new IngresSqlStatementBuilder();
    }

    @Override
    public String doQuote(String identifier) {
        return "\"" + StringUtils.replaceAll(identifier, "\"", "\"\"") + "\"";
    }

    @Override
    public IngresSchema getSchema(String name) {
        return new IngresSchema(jdbcTemplate, this, name);
    }

    @Override
    public boolean catalogIsSchema() {
        return false;
    }

	@Override
	protected void doChangeCurrentSchemaTo(String schema) throws SQLException {
		//NO OP
	}
}
