package br.web.prova.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {
	public static Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/prova_web", "postgres", "");
		} catch (SQLException e) {
			System.out.println("Erro de conexão! " + e);
		} catch (ClassNotFoundException e) {
			System.out.println("Erro de driver! " + e);
		}
		return con;
	}

	public static ResultSet executeQuery(String queryString) {
		Statement stmt;
		try {
			stmt = getConnection().createStatement();
		} catch (SQLException e) {
			return null;
		}
		try {
			return stmt.executeQuery(queryString);
		} catch (SQLException e) {
			return null;
		}
	}
}
