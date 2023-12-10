package br.com.sptech.modelo.banco.jdbc.dao;

import br.com.sptech.modelo.banco.jdbc.conexao.Conexao;
import br.com.sptech.modelo.banco.jdbc.modelo.ModelDisco;
import org.springframework.jdbc.core.JdbcTemplate;

public class DiscoDao {
    private Integer idInfoMySQL;
    private Integer idInfoSQLServer;

    public void salvarCapturaFixa(ModelDisco novaCapturaDisco, Integer fkMaquina, JdbcTemplate conexaoMySQL, JdbcTemplate conexaoSQLServer) {
        if (fkMaquina != null) {
            try {
                if (conexaoSQLServer == null) {
                    // Salvar no MySQL
                    conexaoMySQL.update("INSERT INTO info_componente (total) VALUES (?)", novaCapturaDisco.getTotalDisco());
                    idInfoMySQL = getIdInfoMySQL(conexaoMySQL); // Usando o MySQL para buscar o ID
                }else {

                    // Salvar no SQL Server
                    conexaoSQLServer.update("INSERT INTO info_componente (total) VALUES (?)", novaCapturaDisco.getTotalDisco());
                    idInfoSQLServer = getIdInfoSQLServer(conexaoSQLServer); // Usando o SQL Server para buscar o ID

                    // Usando o SQL Server para salvar o componente
                    conexaoSQLServer.update("INSERT INTO componente (nome, parametro, fk_info, fk_maquina) VALUES (?, ?, ?, ?)",
                            "disco", "bytes", idInfoSQLServer, fkMaquina);

                    // Usando o SQL Server para salvar o parametro_componente
                    conexaoSQLServer.update("INSERT INTO parametro_componente (valor_minimo, valor_maximo, fk_componente) VALUES (?, ?, ?)", 30, 80, idInfoSQLServer);
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

    private Integer getIdInfoSQLServer(JdbcTemplate conexaoSQLServer) {
        String sql = "SELECT SCOPE_IDENTITY()";
        return conexaoSQLServer.queryForObject(sql, Integer.class);
    }

    public void salvarCapturaDinamica(ModelDisco novaCapturaDisco, JdbcTemplate conexaoMySQL, JdbcTemplate conexaoSQLServer) {
        if (idInfoMySQL != null && idInfoSQLServer != null) {
            try {
                if (conexaoSQLServer == null) {
                    // Salvar no MySQL
                    conexaoMySQL.update("INSERT INTO dados_captura (byte_leitura, leituras, byte_escrita, escritas, data_captura, fk_componente, desligada) VALUES (?, ?, ?, ?, ?, ?, ?)",
                            novaCapturaDisco.getBytesLeitura(),
                            novaCapturaDisco.getLeituras(),
                            novaCapturaDisco.getBytesEscrita(),
                            novaCapturaDisco.getEscritas(),
                            novaCapturaDisco.getDataCaptura(),
                            idInfoMySQL,
                            novaCapturaDisco.getDesligada()
                    );
                }else {

                    // Salvar no SQL Server
                    conexaoSQLServer.update("INSERT INTO dados_captura (byte_leitura, leituras, byte_escrita, escritas, data_captura, fk_componente, desligada) VALUES (?, ?, ?, ?, ?, ?, ?)",
                            novaCapturaDisco.getBytesLeitura(),
                            novaCapturaDisco.getLeituras(),
                            novaCapturaDisco.getBytesEscrita(),
                            novaCapturaDisco.getEscritas(),
                            novaCapturaDisco.getDataCaptura(),
                            idInfoSQLServer,
                            novaCapturaDisco.getDesligada()
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

    public void buscarDadosFixo(Integer idMaquina, JdbcTemplate conexaoMySQL, JdbcTemplate conexaoSQLServer) {
        try {
            if(conexaoSQLServer == null) {
                // Buscar no MySQL
                String sqlMySQL = "SELECT c.id_componente\n" +
                        "FROM componente c\n" +
                        "JOIN info_componente i ON c.fk_info = i.id_info\n" +
                        "JOIN maquina m ON c.fk_maquina = m.id_maquina\n" +
                        "WHERE c.nome = ? AND m.id_maquina = ?";
                idInfoMySQL = conexaoMySQL.queryForObject(sqlMySQL, Integer.class, "disco", idMaquina);
            }else {
                // Buscar no SQL Server
                String sqlSQLServer = "SELECT c.id_componente\n" +
                        "FROM componente c\n" +
                        "JOIN info_componente i ON c.fk_info = i.id_info\n" +
                        "JOIN maquina m ON c.fk_maquina = m.id_maquina\n" +
                        "WHERE c.nome = ? AND m.id_maquina = ?";
                idInfoSQLServer = conexaoSQLServer.queryForObject(sqlSQLServer, Integer.class, "disco", idMaquina);
            }
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
        }
    }
}
