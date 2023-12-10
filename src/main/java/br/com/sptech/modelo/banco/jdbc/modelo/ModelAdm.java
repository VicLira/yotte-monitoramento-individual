package br.com.sptech.modelo.banco.jdbc.modelo;

public class ModelAdm extends ModelUsuario {

    public ModelAdm() {
    }

    public ModelAdm(String nome, String email, String senha, String area, String cargo, Integer fkEmpresa, Integer fkTipoUsuario) {
        super(nome, email, senha, area, cargo, fkEmpresa, fkTipoUsuario);
    }

    @Override
    public String toString() {
        return "AdmModel{}";
    }
}

