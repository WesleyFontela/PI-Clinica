package br.com.clinica.dao;

import br.com.clinica.model.Medico;
import jakarta.persistence.*;
import java.util.List;

/**
 * DAO (Data Access Object) responsável pelas operações de persistência da
 * entidade {@link br.com.clinica.model.Medico}.
 * <p>
 * Implementa as operações CRUD definidas na interface {@link DAO} e também
 * fornece um método de busca específica (nome, especialidade ou CRM). 
 */
public class MedicoDAO implements DAO<Medico> {

    /**
     * Factory compartilhada para criação de {@link EntityManager}.
     */
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("clinicaPU");

    /**
     * Insere um novo médico no banco.
     *
     * @param m médico a ser persistido
     */
    @Override
    public void inserir(Medico m) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(m);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Atualiza as informações de um médico existente.
     *
     * @param m médico com dados atualizados
     */
    @Override
    public void atualizar(Medico m) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(m);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Remove um médico com base no seu id.
     * <p>
     * Caso o médico não exista, o método apenas encerra sem exceção.     
     *
     * @param id identificador do médico
     */
    @Override
    public void deletar(int id) {
        EntityManager em = emf.createEntityManager();
        Medico m = em.find(Medico.class, id);
        if (m != null) {
            em.getTransaction().begin();
            em.remove(m);
            em.getTransaction().commit();
        }
        em.close();
    }

    /**
     * Busca um médico pelo identificador.
     *
     * @param id identificador do médico
     * @return instância de {@link Medico} encontrada ou {@code null} caso
     * inexistente
     */
    @Override
    public Medico buscarPorId(int id) {
        EntityManager em = emf.createEntityManager();
        Medico m = em.find(Medico.class, id);
        em.close();
        return m;
    }

    /**
     * Retorna todos os médicos cadastrados no banco.
     *
     * @return lista de médicos
     */
    @Override
    public List<Medico> listarTodos() {
        EntityManager em = emf.createEntityManager();
        List<Medico> lista = em.createQuery("SELECT m FROM Medico m", Medico.class).getResultList();
        em.close();
        return lista;
    }

    /**
     * Busca médicos cujo nome, especialidade ou CRM coincidam parcial ou
     * totalmente com o termo informado.
     * <p>
     * A consulta usa {@code LIKE} e aplica o termo entre curingas (%).     
     *
     * @param termo texto de busca (nome, especialidade ou CRM)
     * @return lista de médicos correspondentes ao termo
     */
    public List<Medico> buscarPorNomeOuCrm(String termo) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT m FROM Medico m "
                    + "WHERE m.nome LIKE :termo "
                    + "   OR m.especialidade LIKE :termo "
                    + "   OR m.crm LIKE :termo ",
                    Medico.class)
                    .setParameter("termo", "%" + termo + "%")
                    .getResultList();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

}
