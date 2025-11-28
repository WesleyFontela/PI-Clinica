package br.com.clinica.model;

import jakarta.persistence.*;

/**
 * Entidade que representa um usuário do sistema.
 * <p>
 * Mapeada para a tabela {@code usuario}. Contém campos mínimos para
 * autenticação/autorizações no sistema (login, senha e perfil).
 *
 * <p>
 * <b>Atenção de segurança:</b> esta classe expõe a senha como texto simples. Em
 * ambientes reais recomenda-se armazenar apenas hashes seguros da senha (por
 * exemplo, bcrypt, Argon2) e nunca guardar/expor a senha em texto puro.
 */
@Entity
@Table(name = "usuario")
public class Usuario {

    /**
     * Identificador único do usuário (chave primária).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Nome de login (identificador) do usuário.
     */
    private String login;

    /**
     * Senha do usuário. (Evitar armazenar em texto puro em produção.)
     */
    private String senha;

    /**
     * Perfil/role do usuário (ex.: "ADMIN", "MEDICO", "RECEP").
     */
    private String perfil;

    /**
     * Construtor padrão exigido pelo JPA.
     */
    public Usuario() {
    }

    /**
     * Construtor conveniência para criar instâncias de {@code Usuario}.
     *
     * @param login nome de usuário/login
     * @param senha senha do usuário (recomenda-se hash em produção)
     * @param perfil perfil/role (por exemplo: "ADMIN", "MEDICO", "RECEP")
     */
    public Usuario(String login, String senha, String perfil) {
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    /**
     * Retorna o identificador do usuário.
     *
     * @return id do usuário
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna o login (nome de usuário).
     *
     * @return login do usuário
     */
    public String getLogin() {
        return login;
    }

    /**
     * Retorna a senha do usuário.
     * <p>
     * Em produção, prefira métodos que comparem hashes em vez de expor o valor.
     *
     * @return senha do usuário
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Retorna o perfil/role do usuário.
     *
     * @return perfil do usuário
     */
    public String getPerfil() {
        return perfil;
    }    
}
