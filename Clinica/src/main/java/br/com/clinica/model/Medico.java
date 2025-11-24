package br.com.clinica.model;

import jakarta.persistence.*;

/**
 * Entidade que representa um médico do sistema.
 * <p>
 * Mapeada para a tabela {@code medico}. Contém informações de identificação
 * profissional, como especialidade e CRM. 
 */
@Entity
@Table(name = "medico")
public class Medico {

    /**
     * Identificador único do médico (chave primária).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Nome completo do médico.
     */
    private String nome;

    /**
     * Especialidade médica (ex.: Psiquiatria, Dermatologia, Clínica Geral).
     */
    private String especialidade;

    /**
     * CRM do médico. Recomenda-se manter o formato padronizado (ex.: CRM12345).
     */
    private String crm;

    /**
     * Valida o CRM do médico.
     * <p>
     * Esta validação é propositalmente simples e apenas verifica:
     * <ul>
     * <li>se o CRM não é {@code null};</li>
     * <li>se começa com "CRM".</li>
     * </ul>
     * Não verifica dígitos ou estados — para validações avançadas, utilize uma
     * lógica mais completa.     
     *
     * @return {@code true} se o CRM começar com "CRM", {@code false} caso
     * contrário
     */
    public boolean validarCRM() {
        return crm != null && crm.startsWith("CRM");
    }

    /**
     * Construtor padrão (necessário para o JPA).
     */
    public Medico() {
    }

    /**
     * Construtor conveniência que inicializa os dados principais do médico.
     *
     * @param nome nome do médico
     * @param especialidade especialidade médica
     * @param crm CRM de registro profissional
     */
    public Medico(String nome, String especialidade, String crm) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.crm = crm;
    }

    /**
     * Retorna o identificador do médico.
     *
     * @return id do médico
     */
    public int getId() {
        return id;
    }

    /**
     * Define o identificador do médico.
     *
     * @param id novo id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retorna o nome do médico.
     *
     * @return nome do médico
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do médico.
     *
     * @param nome novo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna a especialidade médica.
     *
     * @return especialidade
     */
    public String getEspecialidade() {
        return especialidade;
    }

    /**
     * Define a especialidade do médico.
     *
     * @param especialidade nova especialidade
     */
    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    /**
     * Retorna o CRM do médico.
     *
     * @return CRM
     */
    public String getCrm() {
        return crm;
    }

    /**
     * Define o CRM do médico.
     *
     * @param crm novo CRM
     */
    public void setCrm(String crm) {
        this.crm = crm;
    }

    /**
     * Representação textual do médico.
     * <p>
     * Retorna apenas o nome, facilitando a exibição em componentes gráficos
     * como {@code JComboBox}.     
     *
     * @return nome do médico
     */
    @Override
    public String toString() {
        return nome;
    }

}
