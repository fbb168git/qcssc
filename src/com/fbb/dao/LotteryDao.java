package com.fbb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.fbb.bean.Lottery;
import com.fbb.util.LogUtil;

public class LotteryDao {
	public static final String TABLE_NAME = "lottery";
	public static final String sql_add = "replace into " + TABLE_NAME
			+ " values (?,?,?,now())";

	public ArrayList<Lottery> getByStartDate(String start_date, String end_date) {
		if (start_date == null || start_date.equalsIgnoreCase("")
				|| end_date == null || end_date.equalsIgnoreCase(""))
			return null;
		ArrayList<Lottery> result = new ArrayList<Lottery>();
		Connection conn = DB.getConnection();
		Statement stmt = DB.createStmt(conn);
		ResultSet resultSet = DB.executeQuery(stmt, "select * from "
				+ TABLE_NAME + " where lottery_date >= '" + start_date
				+ "' and lottery_date <= '" + end_date + "'");
		try {
			while (resultSet.next()) {
				Lottery lottery = fromResultset(resultSet);
				if (lottery != null) {
					result.add(lottery);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LogUtil.e("select表" + TABLE_NAME + "异常");
		} finally {
			DB.close(conn);
			DB.close(stmt);
			DB.close(resultSet);
		}
		return result;
	}

	public ArrayList<Lottery> getAll() {
		ArrayList<Lottery> result = new ArrayList<Lottery>();
		Connection conn = DB.getConnection();
		Statement stmt = DB.createStmt(conn);
		ResultSet resultSet = DB.executeQuery(stmt, "select * from "
				+ TABLE_NAME);
		try {
			while (resultSet.next()) {
				Lottery bean = fromResultset(resultSet);
				result.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LogUtil.e("获取bean列表异常");
		} finally {
			DB.close(conn);
			DB.close(stmt);
			DB.close(resultSet);
		}
		return result;
	}

	public Lottery fromResultset(ResultSet set) {
		Lottery bean = null;
		try {
			bean = new Lottery(set.getString("code"),set.getString("numbers"),set.getString("lottery_date"));
//			bean.setLottery_code(set.getString("code"));
//			bean.setLottery_nums(set.getString("numbers"));
//			bean.setLottery_date(set.getString("lottery_date"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	// public boolean addOrUpdateShares(Shares bean) {
	// boolean result = false;
	// if (bean != null) {
	// Connection conn = DB.getConnection();
	// PreparedStatement pstmt = DB.prepareStmt(conn, sql_add);
	// try {
	// pstmt.setString(1, bean.getCode());
	// pstmt.setString(2, bean.getName());
	// int executeUpdate = pstmt.executeUpdate();
	// result = executeUpdate > 0;
	// } catch (SQLException e) {
	// e.printStackTrace();
	// LogUtil.e("replace表"+TABLE_NAME+"异常");
	// } finally {
	// DB.close(pstmt);
	// DB.close(conn);
	// }
	// }
	// return result;
	// }

	private boolean addOrUpdateBean(Connection conn, Lottery bean) {
		boolean result = false;
		if (bean != null) {
			PreparedStatement pstmt = DB.prepareStmt(conn, sql_add);
			try {
				pstmt.setString(1, bean.getLottery_code());
				pstmt.setString(2, bean.getLottery_nums());
				pstmt.setString(3, bean.getLottery_date());
				int executeUpdate = pstmt.executeUpdate();
				result = executeUpdate > 0;
			} catch (SQLException e) {
				e.printStackTrace();
				LogUtil.e("replace表" + TABLE_NAME + "异常");
			} finally {
				DB.close(pstmt);
			}
		}
		return result;
	}

	public int addOrUpdateBeanList(ArrayList<Lottery> beans) {
		int successCount = 0;
		if (beans != null && beans.size() > 0) {
			Connection conn = DB.getConnection();
			for (Lottery bean : beans) {
				boolean success = addOrUpdateBean(conn, bean);
				if (success)
					successCount++;
			}
			DB.close(conn);
		}
		return successCount;
	}
}
