package br.com.sptech.modelo.banco.jdbc.modelo;

import br.com.sptech.modelo.banco.jdbc.dao.UsuarioDao;

public class ModelFuncionario extends ModelUsuario {

    public ModelFuncionario() {
    }

    public ModelFuncionario(String nome, String email, String senha, String area, String cargo, Integer fkEmpresa, Integer fkTipoUsuario) {
        super(nome, email, senha, area, cargo, fkEmpresa, fkTipoUsuario);
    }

    @Override
    public String toString() {
        return "FuncionarioModel{}";
    }
}

