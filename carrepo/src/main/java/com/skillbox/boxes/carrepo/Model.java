package com.skillbox.boxes.carrepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public abstract class Model {

	private int mId;

	private static Logger log = Logger.getLogger(Model.class);

	public final boolean load(Connection connection, int id) {
		reset();
		try {
			if (loadImpl(connection, id)) {
				mId = id;
				return true;
			}
		} catch (SQLException e) {
			log.error("load[" + id + "]", e);
		} finally {

		}
		return false;
	}

	protected abstract void reset();

	protected abstract boolean loadImpl(Connection connection, int id)
			throws SQLException;

	final public boolean save(Connection connection) {
		boolean bResult = false;

		try {
			if (isNew()) {
				mId = insert(connection);
				bResult = mId != 0;
			} else {
				bResult = update(connection);
			}
		} catch (SQLException e) {
			log.error("save[" + getId() + "]", e);
		}

		return bResult;
	}

	protected abstract boolean update(Connection connection)
			throws SQLException;

	protected abstract int insert(Connection connection) throws SQLException;

	final public boolean delete(Connection connection) {
		boolean bDeleted = false;

		try {

			if (!isNew()) {
				String sql = "DELETE FROM " + getTableName() + " WHERE ID=?";

				PreparedStatement stmt = null;

				try {
					stmt = connection.prepareStatement(sql);

					// Add the Primary Key
					stmt.setInt(1, getId());

					int updated = stmt.executeUpdate();

					bDeleted = updated > 0;

					System.out.println("Deleted : " + bDeleted + " : "
							+ getId());

					mId = 0;

				} finally {
					if (stmt != null) {
						stmt.close();
					}
				}

			}
		} catch (SQLException e) {
			log.error("load[" + getId() + "]", e);
		} finally {
		}

		return bDeleted;
	}

	protected abstract String getTableName();

	final public boolean isNew() {
		return mId == 0;
	}

	final public int getId() {
		return mId;
	}
}
