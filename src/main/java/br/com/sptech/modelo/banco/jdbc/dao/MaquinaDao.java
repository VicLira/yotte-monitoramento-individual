package br.com.sptech.modelo.banco.jdbc.dao;

import br.com.sptech.modelo.banco.jdbc.conexao.Conexao;
import br.com.sptech.modelo.banco.jdbc.modelo.ModelMaquina;
import org.springframework.jdbc.core.JdbcTemplate;

public class MaquinaDao {
    private Integer idMaquinaMySQL;
    private Integer idMaquinaSQLServer;

    public MaquinaDao() {
    }

    public void salvarMaquina(ModelMaquina novaMaquina, Integer fkUsuario, Integer fkToken, JdbcTemplate conexaoMySQL, JdbcTemplate conexaoSQLServer) {
        try {
            if (conexaoSQLServer == null) {
                // Salvar no MySQL
                conexaoMySQL.update("INSERT INTO maquina (ip, so, modelo, fk_usuario, fk_token) VALUES (?, ?, ?, ?, ?)",
                        novaMaquina.getIp(), novaMaquina.getSo(), novaMaquina.getModelo(), fkUsuario, fkToken);
            }else {
                // Salvar no SQL Server
                conexaoSQLServer.update("INSERT INTO maquina (ip, so, modelo, fk_usuario, fk_token) VALUES (?, ?, ?, ?, ?)",
                        novaMaquina.getIp(), novaMaquina.getSo(), novaMaquina.getModelo(), fkUsuario, fkToken);
            }

            idMaquinaMySQL = getIdMaquinaMySQL(fkUsuario, conexaoMySQL); // Usando o MySQL para buscar o ID
            idMaquinaSQLServer = getIdMaquinaSQLServer(fkUsuario, conexaoSQLServer); // Usando o SQL Server para buscar o ID
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
        }
    }

    private Integer getIdMaquinaMySQL(Integer fkUsuario, JdbcTemplate conexaoMySQL) {
        String sql = "SELECT id_maquina FROM maquina WHERE fk_usuario = ?";
        return conexaoMySQL.queryForObject(sql, Integer.class, fkUsuario);
    }

    private Integer getIdMaquinaSQLServer(Integer fkUsuario, JdbcTemplate conexaoSQLServer) {
        String sql = "SELECT id_maquina FROM maquina WHERE fk_usuario = ?";
        return conexaoSQLServer.queryForObject(sql, Integer.class, fkUsuario);
    }

    public Integer buscarMaquinaPorUsuario(Integer fkUsuario, JdbcTemplate conexaoMySQL) {
        try {
            String sql = "SELECT id_maquina FROM maquina WHERE fk_usuario = ?";
            idMaquinaMySQL = conexaoMySQL.queryForObject(sql, Integer.class, fkUsuario);

            // Implementar lógica semelhante para buscar no SQL Server

            return idMaquinaMySQL;
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
            return null;
        }
    }

    public Integer getIdMaquinaMySQL() {
        return idMaquinaMySQL;
    }

    public Integer getIdMaquinaSQLServer() {
        return idMaquinaSQLServer;
    }
}
