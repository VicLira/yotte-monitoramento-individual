package br.com.sptech.modelo.banco.jdbc.conexao;

import br.com.sptech.modelo.banco.jdbc.App;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class Conexao {

    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/yotte";
    private static final String MYSQL_USERNAME = "yotte";
    private static final String MYSQL_PASSWORD = "yotte2023";

    private static final String SQL_SERVER_URL = "jdbc:sqlserver://54.205.98.102;database=yotte;user=sa;password=Projetoyotte2023;";
    private static final String SQL_SERVER_USERNAME = "sa";
    private static final String SQL_SERVER_PASSWORD = "Projetoyotte2023";

    private JdbcTemplate conexaoDoBancoMySQL;
    private JdbcTemplate conexaoDoBancoSQLServer;
    private Boolean conexaoEstabelecida = false;

    public Conexao() {
        try {
            if (!conexaoEstabelecida) {
                DataSource dataSourceMySQL = createDataSource(MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD);
                this.conexaoDoBancoMySQL = new JdbcTemplate(dataSourceMySQL);

                DataSource dataSourceSQLServer = createDataSource(SQL_SERVER_URL, SQL_SERVER_USERNAME, SQL_SERVER_PASSWORD);
                this.conexaoDoBancoSQLServer = new JdbcTemplate(dataSourceSQLServer);

                App.log("Conexões com o banco de dados estabelecidas com sucesso.");
                conexaoEstabelecida = true;  // Marcamos as conexões como estabelecidas
            }
        } catch (Exception e) {
            App.logError("Erro ao estabelecer as conexões com o banco de dados.", e);
        }
    }

    private DataSource createDataSource(String url, String username, String password) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver"); // Driver MySQL

        // Configurações específicas do SQL Server
        dataSource.addConnectionProperty("trustServerCertificate", "true");
        dataSource.setDriverClassLoader(getClass().getClassLoader());
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // Driver SQL Server

        return dataSource;
    }

    public JdbcTemplate getConexaoDoBancoMySQL() {
        return conexaoDoBancoMySQL;
    }

    public JdbcTemplate getConexaoDoBancoSQLServer() {
        return conexaoDoBancoSQLServer;
    }

    public void log(String message) {
        App.log(message);
    }

    public void logError(String errorMessage, Exception exception) {
        App.logError(errorMessage, exception);
    }
}
