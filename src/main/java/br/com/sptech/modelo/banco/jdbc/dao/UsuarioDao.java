package br.com.sptech.modelo.banco.jdbc.dao;

import br.com.sptech.modelo.banco.jdbc.conexao.Conexao;
import br.com.sptech.modelo.banco.jdbc.modelo.ModelUsuario;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class UsuarioDao {
    private Integer idUsuario;
    private JdbcTemplate conexaoMySQL;
    private JdbcTemplate conexaoSQLServer;

    public UsuarioDao() {
        Conexao conexao = new Conexao();
        conexaoMySQL = conexao.getConexaoDoBancoMySQL();
        conexaoSQLServer = conexao.getConexaoDoBancoSQLServer();
    }

    public Integer salvarUsuario(ModelUsuario novoUsuario) {
        try {
            conexaoMySQL.update("INSERT INTO usuario (nome, email, senha, area, cargo, fk_empresa, fk_tipo_usuario) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    novoUsuario.getNome(), novoUsuario.getEmail(), novoUsuario.getSenha(), novoUsuario.getArea(), novoUsuario.getCargo(),
                    novoUsuario.getFkEmpresa(), novoUsuario.getFkTipoUsuario());

            idUsuario = conexaoMySQL.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            System.out.println(idUsuario);
            return idUsuario;
        } catch (Exception e) {
            // Tratar exceções
            e.printStackTrace();
            return null;
        }
    }

    public Boolean isUsuarioExistente(ModelUsuario usuario) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE email = ? AND senha = ?";
        Integer count = conexaoMySQL.queryForObject(sql, Integer.class, usuario.getEmail(), usuario.getSenha());
        return count > 0;
    }

    public Integer buscarIdUsuario(ModelUsuario usuario) {
        String sql = "SELECT id_usuario FROM usuario WHERE email = ? AND senha = ?";
        Integer idUsuario = conexaoMySQL.queryForObject(sql, Integer.class, usuario.getEmail(), usuario.getSenha());
        return idUsuario;
    }

    public Boolean isTokenValido(String token) {
        String dbToken = conexaoMySQL.queryForObject("SELECT token FROM token WHERE token = ?", String.class, token);
        return token.equals(dbToken); // O token é válido
    }

    public Integer buscarIdToken(String token) {
        Integer idToken = conexaoMySQL.queryForObject("SELECT idToken FROM token WHERE token = ?", Integer.class, token);
        return idToken; // O token é válido
    }

    public Integer buscarEmpresaPorNome(String empresa) {
        Integer fkEmpresa = conexaoMySQL.queryForObject("SELECT id_empresa FROM empresa WHERE nome LIKE ?", Integer.class, empresa.toLowerCase());
        return fkEmpresa;
    }

    public Integer buscarFkTipoUsuario(ModelUsuario usuario) {
        String sql = "SELECT fk_tipo_usuario FROM usuario WHERE email = ? AND senha = ?";
        Integer fkTipoUsuario = conexaoMySQL.queryForObject(sql, Integer.class, usuario.getEmail(), usuario.getSenha());
        return fkTipoUsuario;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public List<ModelUsuario> listarUsuarios() {
        String sql = "SELECT * FROM usuario";
        List<ModelUsuario> usuarios = conexaoMySQL.query(sql, new BeanPropertyRowMapper<>(ModelUsuario.class));
        return usuarios;
    }

}
