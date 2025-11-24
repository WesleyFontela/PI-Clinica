package br.com.clinica.model;

import jakarta.persistence.*;

/**
 * Entidade que representa um paciente do sistema.
 * <p>
 * Mapeada para a tabela {@code paciente}. Contém informações básicas de
 * identificação e contato, além de utilitários simples como validação de CPF e
 * atualização de telefone.
 */
@Entity
@Table(name = "paciente")
public class Paciente {

    /**
     * Identificador único do paciente (chave primária).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Nome completo do paciente.
     */
    private String nome;

    /**
     * CPF formatado do paciente (ex.: {@code 000.000.000-00}).
     */
    private String cpf;

    /**
     * Telefone de contato do paciente.
     */
    private String telefone;

    /**
     * Verifica se o CPF possui um formato básico esperado.
     * <p>
     * Esta validação é propositalmente simples: apenas verifica se o campo não
     * é {@code null} e se tem 14 caracteres (formato com pontos e traço). Para
     * validação completa do CPF (dígitos verificadores) implemente a lógica
     * apropriada ou use uma biblioteca específica.     
     *
     * @return {@code true} se o CPF não for nulo e possuir 14 caracteres,
     * {@code false} caso contrário
     */
    public boolean validarCPF() {
        return cpf != null && cpf.length() == 14;
    }

    /**
     * Atualiza o telefone de contato do paciente.
     *
     * @param telefone novo telefone a ser definido
     */
    public void atualizarContato(String telefone) {
        this.telefone = telefone;
    }

    /**
     * Construtor padrão (requerido pelo JPA).
     */
    public Paciente() {
    }

    /**
     * Construtor conveniência para criar um {@code Paciente} com seus dados
     * principais.
     *
     * @param nome nome completo do paciente
     * @param cpf CPF do paciente (formatado)
     * @param telefone telefone de contato
     */
    public Paciente(String nome, String cpf, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    /**
     * Retorna o identificador do paciente.
     *
     * @return id do paciente
     */
    public int getId() {
        return id;
    }

    /**
     * Define o identificador do paciente.
     *
     * @param id novo id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retorna o nome do paciente.
     *
     * @return nome do paciente
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do paciente.
     *
     * @param nome novo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o CPF do paciente.
     *
     * @return CPF (formatado) do paciente
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Define o CPF do paciente.
     *
     * @param cpf novo CPF (recomenda-se passar formatado)
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * Retorna o telefone de contato do paciente.
     *
     * @return telefone do paciente
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * Define o telefone de contato do paciente.
     *
     * @param telefone novo telefone
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * Retorna uma representação em {@code String} do paciente.
     * <p>
     * Implementado para facilitar a exibição do nome em componentes Swing, como
     * {@code JComboBox}.     
     *
     * @return nome do paciente
     */
    @Override
    public String toString() {
        return nome;
    }

}
