package com.skillbox.boxes.carrepo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;

public class Car extends Model {

	enum Field {
		CAR_YEAR("INTEGER NOT NULL"), CAR_MAKE("VARCHAR(100) NOT NULL"), CAR_MODEL(
				"VARCHAR(100) NOT NULL"), CAR_DESC("VARCHAR(1000)"), CAR_PRICE(
				"DECIMAL(8,2) NOT NULL");

		private String mSqlType;

		int fieldIndex() {
			return this.ordinal() + 1;
		}

		Field(String sqlType) {
			this.mSqlType = sqlType;
		}

		static String fieldNames() {
			return StringUtils.join(Field.values(), ", ");
		}

		static String DDL() {

			String[] fields = new String[Field.values().length];

			for (int i = 0; i < fields.length; i++) {
				fields[i] = Field.values()[i] + " "
						+ Field.values()[i].mSqlType;
			}

			return StringUtils.join(fields, ", ");
		}
	}

	public static final String TABLE_NAME = "cars";

	public static final String DROP_SCHEMA_SQL = "DROP TABLE " + TABLE_NAME;

	public static final String CREATE_SCHEMA_SQL = "CREATE TABLE "
			+ TABLE_NAME
			+ "("
			+ "ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ Field.DDL() + ")";

	private final static String LOAD_SQL = "SELECT (" + Field.fieldNames()
			+ ")  FROM " + TABLE_NAME + " WHERE ID=?";

	private final static String INSERT_SQL = "INSERT INTO " + TABLE_NAME + " ("
			+ Field.fieldNames() + ")  VALUES (?,?,?,?,?) ";

	private final static String UPDATE_SQL = "UPDATE "
			+ TABLE_NAME
			+ " SET CAR_YEAR=?, CAR_MAKE=?, CAR_MODEL=?, CAR_DESC=?, CAR_PRICE=? WHERE ID=?";

	Integer mYear;
	String mMake;
	String mModel;
	String mDescription;
	BigDecimal mPrice;

	@Override
	protected void reset() {
		mYear = null;
		mMake = null;
		mModel = null;
		mDescription = null;
		mPrice = null;
	}

	@Override
	protected boolean loadImpl(Connection connection, int id)
			throws SQLException {
		boolean bResult = false;

		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = connection.prepareStatement(LOAD_SQL);

			stmt.setInt(1, id);

			rs = stmt.executeQuery();
			if (rs != null) {
				if (rs.next()) {
					this.setYear(rs.getInt(Field.CAR_YEAR.fieldIndex()));
					this.setMake(rs.getString(Field.CAR_MAKE.fieldIndex()));
					this.setModel(rs.getString(Field.CAR_MODEL.fieldIndex()));
					this.setDescription(rs.getString(Field.CAR_DESC
							.fieldIndex()));
					this.setPrice(rs.getBigDecimal(Field.CAR_PRICE.fieldIndex()));
					bResult = true;
				}
			}

		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}

		return bResult;
	}

	@Override
	protected boolean update(Connection connection) throws SQLException {
		boolean bResult = false;

		PreparedStatement stmt = null;

		try {
			stmt = connection.prepareStatement(UPDATE_SQL);

			stmt.setInt(Field.CAR_YEAR.fieldIndex(), this.getYear());
			stmt.setString(Field.CAR_MAKE.fieldIndex(), this.getMake());
			stmt.setString(Field.CAR_MODEL.fieldIndex(), this.getModel());
			stmt.setString(Field.CAR_DESC.fieldIndex(), this.getDescription());
			stmt.setBigDecimal(Field.CAR_PRICE.fieldIndex(), this.getPrice());

			// And the Primary Key
			stmt.setInt(Field.values().length + 1, getId());

			int updated = stmt.executeUpdate();

			bResult = updated > 0;

			System.out.println("Updated : " + bResult + " : " + getId());

		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		return bResult;
	}

	@Override
	protected int insert(Connection connection) throws SQLException {
		int newId = 0;

		PreparedStatement stmt = null;

		try {
			stmt = connection.prepareStatement(INSERT_SQL,
					Statement.RETURN_GENERATED_KEYS);

			stmt.setInt(Field.CAR_YEAR.fieldIndex(), this.getYear());
			stmt.setString(Field.CAR_MAKE.fieldIndex(), this.getMake());
			stmt.setString(Field.CAR_MODEL.fieldIndex(), this.getModel());
			stmt.setString(Field.CAR_DESC.fieldIndex(), this.getDescription());
			stmt.setBigDecimal(Field.CAR_PRICE.fieldIndex(), this.getPrice());

			stmt.execute();
			ResultSet rs = null;
			try {

				rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					newId = rs.getInt(1);
					System.out.println("Inserted : " + newId);
				}

			} finally {
				if (rs != null) {
					rs.close();
				}
			}

		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		return newId;
	}

	public int getYear() {
		return mYear;
	}

	public void setYear(int year) {
		mYear = year;
	}

	public String getMake() {
		return mMake;
	}

	public void setMake(String make) {
		mMake = make;
	}

	public String getModel() {
		return mModel;
	}

	public void setModel(String model) {
		mModel = model;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
	}

	public BigDecimal getPrice() {
		return mPrice;
	}

	public void setPrice(BigDecimal price) {
		mPrice = price;
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

}
