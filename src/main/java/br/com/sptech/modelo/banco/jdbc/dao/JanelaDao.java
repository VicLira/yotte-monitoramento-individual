package br.com.sptech.modelo.banco.jdbc.dao;

import br.com.sptech.modelo.banco.jdbc.modelo.ModelJanela;
import org.springframework.jdbc.core.JdbcTemplate;

public class JanelaDao {

    public void atualizarJanela(ModelJanela novaCapturaJanela, Integer fkMaquina, JdbcTemplate conexaoMySQL, JdbcTemplate conexaoSQLServer) {
        try {
            if (conexaoSQLServer == null) {
                // Atualizar no MySQL
                conexaoMySQL.update("INSERT INTO janela (pid, titulo, comando, visivel, data_captura, fk_maquina) VALUES (?, ?, ?, ?, ?, ?)",
                        novaCapturaJanela.getPid(),
                        novaCapturaJanela.getTitulo(),
                        novaCapturaJanela.getComando(),
                        novaCapturaJanela.getVisivel(),
                        novaCapturaJanela.getDataCaptura(),
                        fkMaquina
                );
            }else {

                // Atualizar no SQL Server
                conexaoSQLServer.update("INSERT INTO janela (pid, titulo, comando, visivel, data_captura, fk_maquina) VALUES (?, ?, ?, ?, ?, ?)",
                        novaCapturaJanela.getPid(),
                        novaCapturaJanela.getTitulo(),
                        novaCapturaJanela.getComando(),
                        novaCapturaJanela.getVisivel(),
                        novaCapturaJanela.getDataCaptura(),
                        fkMaquina
                );
            }
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
        }
    }

    public void excluirJanela(Integer idJanela, JdbcTemplate conexaoMySQL, JdbcTemplate conexaoSQLServer) {
        try {
            if (conexaoSQLServer == null) {
                // Excluir do MySQL
                conexaoMySQL.update("DELETE FROM janela WHERE idJanela = ?", idJanela);
            }else {
                // Excluir do SQL Server
                conexaoSQLServer.update("DELETE FROM janela WHERE idJanela = ?", idJanela);
            }

            System.out.println("Janela excluída com sucesso!");
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
        }
    }
}
