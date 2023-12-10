package br.com.sptech.modelo.banco.jdbc.dao;

import br.com.sptech.modelo.banco.jdbc.conexao.Conexao;
import br.com.sptech.modelo.banco.jdbc.modelo.ModelProcesso;
import org.springframework.jdbc.core.JdbcTemplate;

public class ProcessoDao {

    public void atualizarProcesso(ModelProcesso novaCapturaProcesso, Integer fkMaquina, JdbcTemplate conexaoMySQL, JdbcTemplate conexaoSQLServer) {
        try {
            if (conexaoSQLServer == null) {
                // Atualizar no MySQL
                conexaoMySQL.update("INSERT INTO processo (pid, uso_cpu, uso_memoria, bytes_utilizados, fk_maquina) VALUES (?, ?, ?, ?, ?)",
                        novaCapturaProcesso.getPid(),
                        novaCapturaProcesso.getUsoCpu(),
                        novaCapturaProcesso.getUsoMemoria(),
                        novaCapturaProcesso.getBytesUtilizados(),
                        fkMaquina
                );
            }else {

                // Atualizar no SQL Server
                conexaoSQLServer.update("INSERT INTO processo (pid, uso_cpu, uso_memoria, bytes_utilizados, fk_maquina) VALUES (?, ?, ?, ?, ?)",
                        novaCapturaProcesso.getPid(),
                        novaCapturaProcesso.getUsoCpu(),
                        novaCapturaProcesso.getUsoMemoria(),
                        novaCapturaProcesso.getBytesUtilizados(),
                        fkMaquina
                );
            }
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
        }
    }

    public void excluirProcesso(Integer idProcesso, JdbcTemplate conexaoMySQL, JdbcTemplate conexaoSQLServer) {
        try {
            if (conexaoSQLServer == null) {
                // Excluir do MySQL
                conexaoMySQL.update("DELETE FROM processo WHERE idProcesso = ?", idProcesso);
            }else {

                // Excluir do SQL Server
                conexaoSQLServer.update("DELETE FROM processo WHERE idProcesso = ?", idProcesso);
            }

            System.out.println("Processo excluído com sucesso!");
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
        }
    }
}
