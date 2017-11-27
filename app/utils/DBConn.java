package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DBConn {
	private static String user = "ywl"; // 用户名
	private static String password = "123456"; // 密码
	private static String dbHost = "120.26.103.175:3306";

	private static String url = "jdbc:mysql://" + dbHost + "/ywldb";

	public static Connection getConn() {
		Connection conn = null;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, user, password); // 获取连接
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;
	}

	public static PreparedStatement getPS(Connection conn, String sql, String... params) {
		PreparedStatement pstmt = null;
		try {
			if (conn != null) {
				pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				for (int i = 0; i < params.length; i++) {
					pstmt.setString(i + 1, params[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pstmt;
	}

	public static PreparedStatement getPS(Connection conn, String sql, List<String> params) {
		PreparedStatement pstmt = null;
		try {
			if (conn != null) {
				pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				for (int i = 0; i < params.size(); i++) {
					pstmt.setString(i + 1, params.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pstmt;
	}

	public static ResultSet getRS(PreparedStatement ps) {
		ResultSet rs = null;
		try {
			if (ps != null) {
				rs = ps.executeQuery();
				return rs;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
		if (rs != null) { // 关闭记录集
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (ps != null) { // 关闭声明
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) { // 关闭连接对象
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Connection conn = getConn();
		PreparedStatement ps = getPS(conn, "select * from lc_user");
		ResultSet rs = getRS(ps);
		try {
			String[][] data = new String[2694][];
			int i = 0;
			String[] title = { "id", "mobile", "password", "nickname", "realname", "sex", "headimg", "birth",
					"createtime", "status", "familyno", "familyid", "qrcode", "rank", "shipid" };
			data[i++] = title;
			while (rs.next()) {
				String[] line = { rs.getString("id"), rs.getString("mobile"), rs.getString("password"),
						rs.getString("nickname"), rs.getString("realname"), rs.getString("sex"),
						rs.getString("headimg"), rs.getString("birth"), rs.getString("createtime"),
						rs.getString("status"), rs.getString("familyno"), rs.getString("familyid"),
						rs.getString("qrcode"), rs.getString("rank"), rs.getString("shipid") };
				data[i++] = line;
			}
			// ExcelUtils.export(data, new
			// File("/Users/suzui/Desktop/lcuser.xls"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, ps, rs);
		}
		conn = getConn();
		ps = getPS(conn, "select * from lc_family");
		rs = getRS(ps);
		try {
			String[][] data = new String[2854][];
			int i = 0;
			String[] title = { "id", "idno", "nickname", "area", "addr", "longitude", "latitude", "createtime", "prov",
					"city", "district", "street", "region", "tag", "sqid" };
			data[i++] = title;
			while (rs.next()) {
				String[] line = { rs.getString("id"), rs.getString("idno"), rs.getString("nickname"),
						rs.getString("area"), rs.getString("addr"), rs.getString("longitude"), rs.getString("latitude"),
						rs.getString("createtime"), rs.getString("prov"), rs.getString("city"),
						rs.getString("district"), rs.getString("street"), rs.getString("region"), rs.getString("tag"),
						rs.getString("sqid") };
				data[i++] = line;
			}
			// ExcelUtils.export(data, new
			// File("/Users/suzui/Desktop/lcfamily.xls"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, ps, rs);
		}

		conn = getConn();
		ps = getPS(conn, "select * from lc_family_member");
		rs = getRS(ps);
		try {
			String[][] data = new String[3266][];
			int i = 0;
			String[] title = { "id", "realname", "nickname", "sex", "birth", "rank", "status", "createtime", "type",
					"familyid" };
			data[i++] = title;
			while (rs.next()) {
				String[] line = { rs.getString("id"), rs.getString("realname"), rs.getString("nickname"),
						rs.getString("sex"), rs.getString("birth"), rs.getString("rank"), rs.getString("status"),
						rs.getString("createtime"), rs.getString("type"), rs.getString("familyid") };
				data[i++] = line;
			}
			// ExcelUtils.export(data, new
			// File("/Users/suzui/Desktop/lcfamilymember.xls"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(conn, ps, rs);
		}

	}

}
