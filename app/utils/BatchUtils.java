package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import play.Logger;
import play.Play;

/**
 * Created by daikai on 2017/5/8.
 */
public class BatchUtils {

	private static final String username = Play.configuration.getProperty("db.user");
	private static final String password = Play.configuration.getProperty("db.pass");
	private static final String driver = Play.configuration.getProperty("db.driver");
	private static String connectStr = Play.configuration.getProperty("db.url");

	public static synchronized void batchInsert(String insertSql, List<List<String>> params)
			throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		connectStr += "&useServerPrepStmts=false&rewriteBatchedStatements=true";// 此处是测试高效批次插入，去掉之后执行时普通批次插入
		Connection conn = DriverManager.getConnection(connectStr, username, password);
		conn.setAutoCommit(false); // 设置手动提交
		PreparedStatement psts = conn.prepareStatement(insertSql);
		params.stream().forEach(param -> {
			try {
				for (int i = 1; i <= param.size(); i++) {
					psts.setObject(i, param.get(i - 1));
				}
				psts.addBatch(); // 加入批量处理
			} catch (SQLException e) {
				Logger.error(e.getMessage());
			}
		});
		psts.executeBatch(); // 执行批量处理
		conn.commit(); // 提交
		conn.close();
	}

	public static synchronized void batchInsert(String insertSql, String[][] params)
			throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		connectStr += "&useServerPrepStmts=false&rewriteBatchedStatements=true";// 此处是测试高效批次插入，去掉之后执行时普通批次插入
		Connection conn = DriverManager.getConnection(connectStr, username, password);
		conn.setAutoCommit(false); // 设置手动提交
		PreparedStatement psts = conn.prepareStatement(insertSql);
		Arrays.stream(params).forEach(param -> {
			try {
				for (int i = 1; i <= param.length; i++) {
					psts.setObject(i, param[i - 1]);
				}
				psts.addBatch(); // 加入批量处理
			} catch (SQLException e) {
				Logger.error(e.getMessage());
			}
		});
		psts.executeBatch(); // 执行批量处理
		conn.commit(); // 提交
		conn.close();
	}

}
