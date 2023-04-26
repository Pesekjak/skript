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

import java.io.File;

import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import ch.njol.skript.config.SectionNode;

@Deprecated
@ScheduledForRemoval
public class SQLiteStorage extends SQLStorage {

	SQLiteStorage(String name) {
		super(name, "CREATE TABLE IF NOT EXISTS %s (" +
				"name         VARCHAR(" + MAX_VARIABLE_NAME_LENGTH + ")  NOT NULL  PRIMARY KEY," +
				"type         VARCHAR(" + MAX_CLASS_CODENAME_LENGTH + ")," +
				"value        BLOB(" + MAX_VALUE_SIZE + ")," +
				"update_guid  CHAR(36)  NOT NULL" +
				");");
	}

	@Override
	@Nullable
	public HikariDataSource initialize(SectionNode config) {
		File file = this.file;
		if (file == null)
			return null;
		setTableName(config.get("table", "variables21"));
		String name = file.getName();
		assert name.endsWith(".db");

		HikariConfig configuration = new HikariConfig();
		configuration.setJdbcUrl("jdbc:sqlite:" + (file == null ? ":memory:" : file.getAbsolutePath()));
		return new HikariDataSource(configuration);
	}

	@Override
	protected boolean requiresFile() {
		return true;
	}

}