package br.com.maxxmobi.teste.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "tb_canditados")
public class CandidadosModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NOME", length = 100, nullable = false)
    private String nome;

    @Column(name = "NASCIMENTO", nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date nascimento;

    @Column(name = "DATA_CRIACAO", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dataCriacao;

    @Column(name = "SEXO", length = 1, nullable = false)
    private String sexo = "M"; // Valor padrão definido aqui

    @Column(name = "NOTA", nullable = false)
    private Integer nota;

    @Column(name = "LOGRADOURO", length = 200, nullable = true)
    private String logradouro;

    @Column(name = "BAIRRO", length = 50, nullable = true)
    private String bairro;

    @Column(name = "CIDADE", length = 50, nullable = true)
    private String cidade;

    @Column(name = "UF", length = 2, nullable = true)
    private String uf;

    public CandidadosModel(Long id, String nome, Date nascimento, Date dataCriacao, String sexo, Integer nota, String logradouro, String bairro, String cidade, String uf) {
        this.id = id;
        this.nome = nome;
        this.nascimento = nascimento;
        this.dataCriacao = dataCriacao;
        this.sexo = sexo;
        this.nota = nota;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
    }

    public CandidadosModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
}
