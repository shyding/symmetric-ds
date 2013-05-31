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
package org.jumpmind.db.platform.hsqldb2;

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

import org.jumpmind.db.platform.AbstractJdbcDatabasePlatform;
import org.jumpmind.db.platform.DatabaseNamesConstants;
import org.jumpmind.db.sql.SqlTemplateSettings;

/*
 * The platform implementation for the HsqlDb database.
 */
public class HsqlDb2DatabasePlatform extends AbstractJdbcDatabasePlatform {

    /* The standard Hsqldb jdbc driver. */
    public static final String JDBC_DRIVER = "org.hsqldb.jdbcDriver";

    /* The subprotocol used by the standard Hsqldb driver. */
    public static final String JDBC_SUBPROTOCOL = "hsqldb";

    /*
     * Creates a new instance of the Hsqldb platform.
     */
    public HsqlDb2DatabasePlatform(DataSource dataSource, SqlTemplateSettings settings) {
        super(dataSource, settings);
    }
    
    @Override
    protected HsqlDb2DdlBuilder createDdlBuilder() {
        return new HsqlDb2DdlBuilder();
    }

    @Override
    protected HsqlDb2DdlReader createDdlReader() {
        return new HsqlDb2DdlReader(this);
    }    
    
    @Override
    protected HsqlDb2JdbcSqlTemplate createSqlTemplate() {
        return new HsqlDb2JdbcSqlTemplate(dataSource, settings, null, getDatabaseInfo());
    }

    public String getName() {
        return DatabaseNamesConstants.HSQLDB2;
    }

    public String getDefaultSchema() {
        return getDefaultCatalog();
    }

    public String getDefaultCatalog() {
        if (defaultCatalog == null) {
            defaultCatalog = (String) getSqlTemplate()
                    .queryForObject(
                            "select value from INFORMATION_SCHEMA.SYSTEM_SESSIONINFO where key='CURRENT SCHEMA'",
                            String.class);
        }
        return defaultCatalog;
    }

}
