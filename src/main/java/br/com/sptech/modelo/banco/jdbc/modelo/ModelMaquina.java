package br.com.sptech.modelo.banco.jdbc.modelo;

public class ModelMaquina {
    private Integer idMaquina;
    private String ip;
    private String so;
    private String modelo;

    private Integer fkUsuario;
    private Integer fkToken;

    public ModelMaquina() {
    }

    public ModelMaquina(String ip, String so, String modelo, Integer fkUsuario, Integer fkToken) {
        this.ip = ip;
        this.so = so;
        this.modelo = modelo;
        this.fkUsuario = fkUsuario;
        this.fkToken = fkToken;
    }

    public Integer getIdMaquina() {
        return idMaquina;
    }

    public void setIdMaquina(Integer idMaquina) {
        this.idMaquina = idMaquina;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSo() {
        return so;
    }

    public void setSo(String so) {
        this.so = so;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }


    public Integer getFkUsuario() {
        return fkUsuario;
    }

    public void setFkUsuario(Integer fkUsuario) {
        this.fkUsuario = fkUsuario;
    }

    public Integer getFkToken() {
        return fkToken;
    }

    public void setFkToken(Integer fkToken) {
        this.fkToken = fkToken;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d%n" +
                        "IP: %s%n" +
                        "Sistema Operacional: %s%n" +
                        "Modelo: %s%n" +
                        "FK Usuário: %d%n" +
                        "FK Token: %d%n",
                idMaquina, ip, so, modelo, fkUsuario, fkToken
        );
    }
}
