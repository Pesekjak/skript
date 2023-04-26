/**
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright Peter Güttinger, SkriptLang team and contributors
 */
package ch.njol.skript.variables;

import org.eclipse.jdt.annotation.Nullable;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import ch.njol.skript.config.SectionNode;

public class MySQLStorage extends SQLStorage {

	MySQLStorage(String name) {
		super(name, "CREATE TABLE IF NOT EXISTS %s (" +
				"rowid        BIGINT  NOT NULL  AUTO_INCREMENT  PRIMARY KEY," +
				"name         VARCHAR(" + MAX_VARIABLE_NAME_LENGTH + ")  NOT NULL  UNIQUE," +
				"type         VARCHAR(" + MAX_CLASS_CODENAME_LENGTH + ")," +
				"value        BLOB(" + MAX_VALUE_SIZE + ")," +
				"update_guid  CHAR(36)  NOT NULL" +
				") CHARACTER SET ucs2 COLLATE ucs2_bin");
	}

	@Override
	@Nullable
	public HikariDataSource initialize(SectionNode config) {
		String host = getValue(config, "host");
		Integer port = getValue(config, "port", Integer.class);
		String database = getValue(config, "database");
		if (host == null || port == null || database == null)
			return null;

		HikariConfig configuration = new HikariConfig();
		configuration.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
		configuration.setUsername(getValue(config, "user"));
		configuration.setPassword(getValue(config, "password"));

		setTableName(config.get("table", "variables21"));
		return new HikariDataSource(configuration);
	}

	@Override
	protected boolean requiresFile() {
		return false;
	}

}