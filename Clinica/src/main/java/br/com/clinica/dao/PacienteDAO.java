package br.com.clinica.dao;

import br.com.clinica.model.Paciente;
import jakarta.persistence.*;
import java.util.List;

/**
 * DAO (Data Access Object) para a entidade
 * {@link br.com.clinica.model.Paciente}.
 * <p>
 * Fornece operações básicas de persistência (CRUD) e consultas específicas
 * relacionadas a pacientes. Usa JPA {@link EntityManagerFactory} configurado
 * com a unidade de persistência "clinicaPU". 
 */
public class PacienteDAO implements DAO<Paciente> {

    /**
     * Factory compartilhada para criar {@link EntityManager}.
     */
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("clinicaPU");

    /**
     * Persiste um novo paciente no banco.
     *
     * @param p paciente a ser inserido
     */
    @Override
    public void inserir(Paciente p) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Atualiza um paciente existente.
     * <p>
     * Normalmente corresponde a um {@code merge} no contexto JPA.     
     *
     * @param p paciente com os dados atualizados
     */
    @Override
    public void atualizar(Paciente p) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(p);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Remove o paciente identificado por {@code id}, caso exista.
     * <p>
     * Se o registro não existir, o método retorna silenciosamente.     
     *
     * @param id identificador do paciente a ser removido
     */
    @Override
    public void deletar(int id) {
        EntityManager em = emf.createEntityManager();
        Paciente p = em.find(Paciente.class, id);
        if (p != null) {
            em.getTransaction().begin();
            em.remove(p);
            em.getTransaction().commit();
        }
        em.close();
    }

    /**
     * Busca um paciente pelo identificador.
     *
     * @param id identificador do paciente
     * @return instância de {@link Paciente} correspondente ao id, ou
     * {@code null} se não encontrado
     */
    @Override
    public Paciente buscarPorId(int id) {
        EntityManager em = emf.createEntityManager();
        Paciente p = em.find(Paciente.class, id);
        em.close();
        return p;
    }

    /**
     * Lista todos os pacientes existentes no repositório.
     *
     * @return lista (possivelmente vazia) de pacientes
     */
    @Override
    public List<Paciente> listarTodos() {
        EntityManager em = emf.createEntityManager();
        List<Paciente> lista = em.createQuery("SELECT p FROM Paciente p", Paciente.class).getResultList();
        em.close();
        return lista;
    }

    /**
     * Busca pacientes cujo nome, CPF ou telefone correspondam ao termo
     * informado.
     * <p>
     * A pesquisa usa {@code LIKE} com curingas antes e depois do termo.     
     *
     * @param termo texto a ser pesquisado (nome, cpf ou telefone)
     * @return lista de pacientes correspondentes ao termo
     */
    public List<Paciente> buscarPorNomeOuCpf(String termo) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM Paciente p "
                    + "WHERE p.nome LIKE :termo "
                    + "OR p.cpf LIKE :termo "
                    + "OR p.telefone LIKE :termo ",
                    Paciente.class)
                    .setParameter("termo", "%" + termo + "%")
                    .getResultList();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

}
