/**
 * Licensed to JumpMind Inc under one or more contributor
 * license agreements.  See the NOTICE file distributed
 * with this work for additional information regarding
 * copyright ownership.  JumpMind Inc licenses this file
 * to you under the GNU General Public License, version 3.0 (GPLv3)
 * (the "License"); you may not use this file except in compliance
 * with the License.
 *
 * You should have received a copy of the GNU General Public License,
 * version 3.0 (GPLv3) along with this library; if not, see
 * <http://www.gnu.org/licenses/>.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jumpmind.db.platform.mssql;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.jumpmind.db.platform.AbstractJdbcDatabasePlatform;
import org.jumpmind.db.platform.DatabaseNamesConstants;
import org.jumpmind.db.sql.SqlTemplateSettings;

/*
 * The platform implementation for the Microsoft SQL Server database.
 */
public class MsSqlDatabasePlatform extends AbstractJdbcDatabasePlatform {

    /* The standard SQLServer jdbc driver. */
    public static final String JDBC_DRIVER = "net.sourceforge.jtds.jdbc.Driver";

    /* The sub protocol used by the standard SQL Server driver. */
    public static final String JDBC_SUBPROTOCOL = "jtds";

    /*
     * Creates a new platform instance.
     */
    public MsSqlDatabasePlatform(DataSource dataSource, SqlTemplateSettings settings) {
        super(dataSource, settings);
        // override the ddl builder based on the version
        if (this.sqlTemplate.getDatabaseMajorVersion() >= 10) {
            this.ddlBuilder = new MsSql10DdlBuilder();
        } else {
            this.ddlBuilder = new MsSqlDdlBuilder();    
        }

    }

    @Override
    protected MsSqlDdlBuilder createDdlBuilder() {    	
       return new MsSqlDdlBuilder();    
    }

    @Override
    protected MsSqlDdlReader createDdlReader() {
        return new MsSqlDdlReader(this);
    }    
    
    @Override
    protected MsSqlJdbcSqlTemplate createSqlTemplate() {
        return new MsSqlJdbcSqlTemplate(dataSource, settings, getDatabaseInfo());
    }

    public String getName() {
        return DatabaseNamesConstants.MSSQL;
    }

    public String getDefaultCatalog() {
        if (StringUtils.isBlank(defaultCatalog)) {
            defaultCatalog = (String) getSqlTemplate().queryForObject("select DB_NAME()",
                    String.class);
        }
        return defaultCatalog;
    }

    public String getDefaultSchema() {
        if (StringUtils.isBlank(defaultSchema)) {
            defaultSchema = (String) getSqlTemplate().queryForObject("select SCHEMA_NAME()",
                    String.class);
        }
        return defaultSchema;
    }

    @Override
    public boolean isClob(int type) {
        return super.isClob(type) ||
        // SQL-Server ntext binary type
                type == -10;
    }

}
