package br.com.sptech.modelo.banco.jdbc.dao;

import br.com.sptech.modelo.banco.jdbc.conexao.Conexao;
import br.com.sptech.modelo.banco.jdbc.modelo.ModelMemoria;
import org.springframework.jdbc.core.JdbcTemplate;

public class MemoriaDao {
    private Integer idInfoMySQL;
    private Integer idInfoSQLServer;

    public void salvarCapturaFixa(ModelMemoria novaCapturaRam, Integer idMaquina, JdbcTemplate conexaoMySQL, JdbcTemplate conexaoSQLServer) {
        if (idMaquina != null) {
            try {
                if (conexaoSQLServer == null) {
                    // Salvar no MySQL
                    conexaoMySQL.update("INSERT INTO info_componente (total) VALUES (?)", novaCapturaRam.getRamTotal());

                    idInfoMySQL = getIdInfoMySQL(conexaoMySQL); // Usando o MySQL para buscar o ID
                }else {
                    // Salvar no SQL Server
                    conexaoSQLServer.update("INSERT INTO info_componente (total) VALUES (?)", novaCapturaRam.getRamTotal());

                    // Usando o SQL Server para salvar o componente
                    conexaoSQLServer.update("INSERT INTO componente (nome, parametro, fk_info, fk_maquina) VALUES (?, ?, ?, ?)", "memoria", "bytes", idInfoMySQL, idMaquina);

                    // Usando o SQL Server para salvar o parametro_componente
                    conexaoSQLServer.update("INSERT INTO parametro_componente (valor_minimo, valor_maximo, fk_componente) VALUES (?, ?, ?)", 30, 80, idInfoMySQL);
                }

            } catch (Exception e) {
                // Tratar exceções
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("Precisa existir uma máquina no banco primeiro.");
        }
    }

    private Integer getIdInfoMySQL(JdbcTemplate conexaoMySQL) {
        String sql = "SELECT LAST_INSERT_ID()";
        return conexaoMySQL.queryForObject(sql, Integer.class);
    }

    public void salvarCapturaDinamica(ModelMemoria novaCapturaRam, JdbcTemplate conexaoMySQL, JdbcTemplate conexaoSQLServer) {
        if (idInfoMySQL != null) {
            try {
                if (conexaoSQLServer == null) {
                    // Salvar no MySQL
                    conexaoMySQL.update("INSERT INTO dados_captura (uso, data_captura, fk_componente, desligada) VALUES (?, ?, ?, ?)",
                            novaCapturaRam.getMemoriaUso(),
                            novaCapturaRam.getDataCaptura(),
                            idInfoMySQL,
                            novaCapturaRam.getDesligada()
                    );
                }else {

                    // Salvar no SQL Server
                    conexaoSQLServer.update("INSERT INTO dados_captura (uso, data_captura, fk_componente, desligada) VALUES (?, ?, ?, ?)",
                            novaCapturaRam.getMemoriaUso(),
                            novaCapturaRam.getDataCaptura(),
                            idInfoMySQL,
                            novaCapturaRam.getDesligada()
                    );
                }
            } catch (Exception e) {
                // Tratar exceções
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("ID não foi capturado. Execute salvarCapturaFixa() primeiro.");
        }
    }

<<<<<<< HEAD
    public void buscarDadosFixo(Integer idMaquina) {

        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();
=======
    public void buscarDadosFixo(Integer idMaquina, JdbcTemplate conexaoMySQL, JdbcTemplate conexaoSQLServer) {
        try {
            if (conexaoSQLServer == null) {
                // Buscar no MySQL
                String sqlMySQL = "SELECT c.id_componente\n" +
                        "FROM componente c\n" +
                        "JOIN info_componente i ON c.fk_info = i.id_info\n" +
                        "JOIN maquina m ON c.fk_maquina = m.id_maquina\n" +
                        "WHERE c.nome = ? AND m.id_maquina = ?";
                idInfoMySQL = conexaoMySQL.queryForObject(sqlMySQL, Integer.class, "memoria", idMaquina);
            }else {
>>>>>>> 3262890df42045ca61c54e506f28f9432a5c6f96

                // Buscar no SQL Server
                String sqlSQLServer = "SELECT c.id_componente\n" +
                        "FROM componente c\n" +
                        "JOIN info_componente i ON c.fk_info = i.id_info\n" +
                        "JOIN maquina m ON c.fk_maquina = m.id_maquina\n" +
                        "WHERE c.nome = ? AND m.id_maquina = ?";
                idInfoSQLServer = conexaoSQLServer.queryForObject(sqlSQLServer, Integer.class, "memoria", idMaquina);
            }
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
        }
    }
}
