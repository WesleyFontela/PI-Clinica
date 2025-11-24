package br.com.clinica.dao;

import java.util.List;

/**
 * Interface genérica para operações básicas de persistência (DAO).
 * <p>
 * Define o contrato mínimo que implementações de acesso a dados devem seguir:
 * operações CRUD básicas — inserir, atualizar, deletar, buscar por id e listar
 * todos. 
 *
 * @param <T> tipo da entidade manipulado pelo DAO
 */
public interface DAO<T> {

    /**
     * Persiste a entidade fornecida.
     * <p>
     * Implementações devem abrir/gerenciar transação conforme necessário.     
     *
     * @param obj instância da entidade a ser inserida
     */
    void inserir(T obj);

    /**
     * Atualiza a entidade fornecida no repositório.
     * <p>
     * Normalmente corresponde a um {@code merge} em JPA/Hibernate.     
     *
     * @param obj instância da entidade com os dados atualizados
     */
    void atualizar(T obj);

    /**
     * Remove a entidade cujo identificador é {@code id}.
     * <p>
     * Implementações devem tratar o caso em que o registro não existe e
     * garantir consistência da transação.     
     *
     * @param id identificador da entidade a ser removida
     */
    void deletar(int id);

    /**
     * Busca e retorna a entidade pelo seu identificador.
     *
     * @param id identificador da entidade
     * @return instância da entidade correspondente ao id, ou {@code null} se
     * não encontrada
     */
    T buscarPorId(int id);

    /**
     * Lista todas as instâncias da entidade gerenciadas por este DAO.
     *
     * @return lista de todas as entidades
     */
    List<T> listarTodos();
}
