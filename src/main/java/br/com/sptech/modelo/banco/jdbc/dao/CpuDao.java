package br.com.sptech.modelo.banco.jdbc.dao;

import br.com.sptech.modelo.banco.jdbc.conexao.Conexao;
import br.com.sptech.modelo.banco.jdbc.modelo.ModelCpu;
import org.springframework.jdbc.core.JdbcTemplate;

public class CpuDao {

    private Integer idInfoMySQL;
    private Integer idInfoSQLServer;

    public void salvarCapturaFixa(ModelCpu novaCapturaCpu, Integer fkMaquina) {
        if (fkMaquina != null) {
            Conexao conexao = new Conexao();
            JdbcTemplate conMySQL = conexao.getConexaoDoBancoMySQL();
            JdbcTemplate conSQLServer = conexao.getConexaoDoBancoSQLServer();
            if (conSQLServer == null) {
                // Inserir dados na tabela info_componente
                conMySQL.update("INSERT INTO info_componente (qtd_cpu_fisica, qtd_cpu_logica) VALUES (?, ?)",
                        novaCapturaCpu.getNumCPUsFisicas(),
                        novaCapturaCpu.getNumCPUsLogicas());

                // Obter o ID inserido na tabela info_componente
                idInfoMySQL = conMySQL.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

                // Inserir dados na tabela componente
                conMySQL.update("INSERT INTO componente (nome, parametro, fk_info, fk_maquina) VALUES (?, ?, ?, ?)",
                        "cpu", "%", idInfoMySQL, fkMaquina);

                conMySQL.update("INSERT INTO parametro_componente (valor_minimo, valor_maximo, fk_componente) VALUES (?, ?, ?)",
                        30, 80, idInfoMySQL);
            }else {

                // Inserir dados na tabela info_componente
                conSQLServer.update("INSERT INTO info_componente (qtd_cpu_fisica, qtd_cpu_logica) VALUES (?, ?)",
                        novaCapturaCpu.getNumCPUsFisicas(),
                        novaCapturaCpu.getNumCPUsLogicas());

                // Obter o ID inserido na tabela info_componente
                idInfoSQLServer = conSQLServer.queryForObject("SELECT SCOPE_IDENTITY()", Integer.class);

                // Inserir dados na tabela componente
                conSQLServer.update("INSERT INTO componente (nome, parametro, fk_info, fk_maquina) VALUES (?, ?, ?, ?)",
                        "cpu", "%", idInfoSQLServer, fkMaquina);

                conSQLServer.update("INSERT INTO parametro_componente (valor_minimo, valor_maximo, fk_componente) VALUES (?, ?, ?)",
                        30, 80, idInfoSQLServer);
            }
        } else {
            throw new RuntimeException("Precisa existir um processador no banco primeiro.");
        }
    }

    public void salvarCapturaDinamica(ModelCpu novaCapturaCpu) {
        if (idInfoMySQL != null) {
            Conexao conexao = new Conexao();
            JdbcTemplate conMySQL = conexao.getConexaoDoBancoMySQL();
            JdbcTemplate conSQLServer = conexao.getConexaoDoBancoSQLServer();
            if (conSQLServer == null) {

                // Inserir dados na tabela dados_captura
                conMySQL.update("INSERT INTO dados_captura (uso, frequencia, data_captura, fk_componente, desligada) VALUES (?, ?, ?, ?, ?)",
                        novaCapturaCpu.getUsoCpu(),
                        novaCapturaCpu.getFreq(),
                        novaCapturaCpu.getDataCaptura(),
                        idInfoMySQL,
                        novaCapturaCpu.getDesligada()
                );
            }else {


                // Inserir dados na tabela dados_captura
                conSQLServer.update("INSERT INTO dados_captura (uso, frequencia, data_captura, fk_componente, desligada) VALUES (?, ?, ?, ?, ?)",
                        novaCapturaCpu.getUsoCpu(),
                        novaCapturaCpu.getFreq(),
                        novaCapturaCpu.getDataCaptura(),
                        idInfoSQLServer,
                        novaCapturaCpu.getDesligada()
                );
            }
        } else {
            throw new RuntimeException("ID não foi capturado. Execute salvarCapturaFixa() primeiro.");
        }
    }

    public void buscarDadosFixo(Integer idMaquina) {
        Conexao conexao = new Conexao();
        JdbcTemplate conMySQL = conexao.getConexaoDoBancoMySQL();
        JdbcTemplate conSQLServer = conexao.getConexaoDoBancoSQLServer();

        if (conSQLServer == null) {
            String sqlMySQL = "SELECT c.id_componente\n" +
                    "FROM componente c\n" +
                    "JOIN info_componente i ON c.fk_info = i.id_info\n" +
                    "JOIN maquina m ON c.fk_maquina = m.id_maquina\n" +
                    "WHERE c.nome = ? AND m.id_maquina = ?";

            idInfoMySQL = conMySQL.queryForObject(sqlMySQL, Integer.class, "cpu", idMaquina);
        }else {


            String sqlSQLServer = "SELECT c.id_componente\n" +
                    "FROM componente c\n" +
                    "JOIN info_componente i ON c.fk_info = i.id_info\n" +
                    "JOIN maquina m ON c.fk_maquina = m.id_maquina\n" +
                    "WHERE c.nome = ? AND m.id_maquina = ?";

            idInfoSQLServer = conSQLServer.queryForObject(sqlSQLServer, Integer.class, "cpu", idMaquina);
        }
    }

}
