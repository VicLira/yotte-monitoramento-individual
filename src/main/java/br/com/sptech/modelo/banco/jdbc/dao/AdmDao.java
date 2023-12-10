package br.com.sptech.modelo.banco.jdbc.dao;

import br.com.sptech.modelo.banco.jdbc.conexao.Conexao;
import br.com.sptech.modelo.banco.jdbc.modelo.ModelUsuario;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class AdmDao extends UsuarioDao {

    public AdmDao() {
    }

    public List buscarFuncEmail(String email, Integer tempo) {
        Conexao conexao = new Conexao();
        JdbcTemplate conSQLServer = conexao.getConexaoDoBancoSQLServer();
        JdbcTemplate conMySQL = conexao.getConexaoDoBancoMySQL();

        String sql = "SELECT COUNT(*) FROM usuario WHERE email LIKE ?;";
        Integer emailFunc = conSQLServer.queryForObject(sql, Integer.class, email);
        System.out.println(emailFunc);

        if (emailFunc.equals(1)) {
            List dadosFunc = conSQLServer.queryForList("SELECT componente.nome, uso, leituras FROM dados_captura " +
                    "JOIN componente ON id_componente = fk_componente " +
                    "JOIN maquina ON fk_maquina = id_maquina " +
                    "JOIN usuario ON id_usuario = fk_usuario " +
                    "WHERE email LIKE ? AND data_captura >= NOW() - INTERVAL ? hour;", email, tempo);

            for (int i = 0; i < dadosFunc.size(); i++) {
                System.out.println(dadosFunc.get(i));
            }
        } else {
            // Se a consulta no SQL Server retornar null, execute a consulta no MySQL
            List dadosFunc = conMySQL.queryForList("SELECT componente.nome, uso, leituras FROM dados_captura " +
                    "JOIN componente ON id_componente = fk_componente " +
                    "JOIN maquina ON fk_maquina = id_maquina " +
                    "JOIN usuario ON id_usuario = fk_usuario " +
                    "WHERE email LIKE ? AND data_captura >= NOW() - INTERVAL ? hour;", email, tempo);

            for (int i = 0; i < dadosFunc.size(); i++) {
                System.out.println(dadosFunc.get(i));
            }
        }
        return null;
    }


    public List buscarListFunc(ModelUsuario modelUsuario) {
        Conexao conexao = new Conexao();
        JdbcTemplate conSQLServer = conexao.getConexaoDoBancoSQLServer();
        JdbcTemplate conMySQL = conexao.getConexaoDoBancoMySQL();
        UsuarioDao modelUsuario1 = new UsuarioDao();
        List listFunc = new ArrayList<>();
        if (conSQLServer == null) {
             listFunc = conSQLServer.queryForList("SELECT admin.nome FROM usuario AS admin\n" +
                    "JOIN token ON admin.id_usuario = token.fk_usuario\n" +
                    "JOIN maquina ON token.idtoken = maquina.fk_token\n" +
                    "JOIN usuario AS funcionario ON funcionario.id_usuario = maquina.fk_usuario " +
                    "WHERE funcionario.id_usuario = ?;", modelUsuario1.buscarIdUsuario(modelUsuario));
        }else {
            if (listFunc.isEmpty()) {
                // Se a consulta no SQL Server retornar null, execute a consulta no MySQL
                listFunc = conMySQL.queryForList("SELECT admin.nome FROM usuario AS admin\n" +
                        "JOIN token ON admin.id_usuario = token.fk_usuario\n" +
                        "JOIN maquina ON token.idtoken = maquina.fk_token\n" +
                        "JOIN usuario AS funcionario ON funcionario.id_usuario = maquina.fk_usuario " +
                        "WHERE funcionario.id_usuario = ?;", modelUsuario1.buscarIdUsuario(modelUsuario));
            }
        }

        System.out.println();
        for (var i = 0; i < listFunc.size(); i++) {
            System.out.println(i);
        }
        return listFunc;
    }
}
