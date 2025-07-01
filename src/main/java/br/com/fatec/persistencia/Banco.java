package br.com.fatec.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;       
import java.sql.SQLException;
import java.sql.Statement;     

public class Banco {
    public static String bancoDados, usuario, senha, servidor;
    public static int porta;

    static {
        // MySQL e MariaDB
        bancoDados = "db_oficina_mecanica";
        usuario = "root";
        senha = "";
        servidor = "localhost";
        porta = 3306;
    }

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mariadb://" + servidor + ":" + porta + "/" + bancoDados;
        
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC não encontrado: " + e.getMessage());
            throw new SQLException("Driver JDBC não encontrado. Certifique-se de que o JAR está no classpath.", e);
        }

        return DriverManager.getConnection(url, usuario, senha);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar statement: " + e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar result set: " + e.getMessage());
            }
        }
    }
}