package com.florianingerl.nachhilfe.webshop.conn;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtils {

	public static Connection getConnection() 
			  throws ClassNotFoundException, SQLException {

		// Ici, je me connecte à l'Oracle Database.
		// (Vous pouvez également utiliser d'autre base de données).
		//return OracleConnUtils.getOracleConnection();
		
		// return OracleConnUtils.getOracleConnection();
		return MySQLConnUtils.getMySQLConnection();
		// return SQLServerConnUtils_JTDS.getSQLServerConnection_JTDS();
		// return SQLServerConnUtils_SQLJDBC.getSQLServerConnection_SQLJDBC();
		// return PostGresConnUtils.getPostGresConnection();
	}
	
	public static void closeQuietly(Connection conn) {
		try {
			conn.close();
		} catch (Exception e) {
		}
	}

	public static void rollbackQuietly(Connection conn) {
		try {
			conn.rollback();
		} catch (Exception e) {
		}
	}
}