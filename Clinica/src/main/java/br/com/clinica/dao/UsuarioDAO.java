package br.com.clinica.dao;

import br.com.clinica.model.Usuario;
import br.com.clinica.util.JPAUtil;
import jakarta.persistence.*;
import java.util.List;

/**
 * DAO (Data Access Object) para a entidade
 * {@link br.com.clinica.model.Usuario}.
 * <p>
 * Implementa operações CRUD básicas e uma consulta auxiliar para recuperar um
 * usuário pelo login. A implementação usa JPA {@link EntityManagerFactory}
 * configurada pela unidade de persistência {@code clinicaPU}. 
 *
 * <p>
 * Observação: a classe utiliza {@link JPAUtil#getEntityManager()} apenas no
 * método {@code buscarPorLogin} para demonstrar interoperabilidade com
 * utilitários de criação de {@link EntityManager}. Os demais métodos usam
 * diretamente a factory local {@code emf} — mantenha consistência no projeto se
 * preferir apenas uma forma de obter EntityManager. 
 */
public class UsuarioDAO implements DAO<Usuario> {

    /**
     * Factory compartilhada para criação de {@link EntityManager}.
     * <p>
     * Inicializada a partir do arquivo de configuração JPA (persistence.xml)
     * com a unidade "clinicaPU".     
     */
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("clinicaPU");

    /**
     * Persiste um novo {@link Usuario} no banco.
     *
     * @param u usuário a ser inserido
     */
    @Override
    public void inserir(Usuario u) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(u);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Atualiza os dados de um {@link Usuario} existente.
     * <p>
     * Normalmente corresponde a um {@code merge} no contexto JPA.     
     *
     * @param u usuário com os dados atualizados
     */
    @Override
    public void atualizar(Usuario u) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(u);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Remove o usuário identificado por {@code id}, caso exista.
     *
     * @param id identificador do usuário a ser removido
     */
    @Override
    public void deletar(int id) {
        EntityManager em = emf.createEntityManager();
        Usuario u = em.find(Usuario.class, id);
        if (u != null) {
            em.getTransaction().begin();
            em.remove(u);
            em.getTransaction().commit();
        }
        em.close();
    }

    /**
     * Busca um {@link Usuario} pelo seu identificador.
     *
     * @param id identificador do usuário
     * @return instância de {@code Usuario} correspondente ao id, ou
     * {@code null} caso não exista
     */
    @Override
    public Usuario buscarPorId(int id) {
        EntityManager em = emf.createEntityManager();
        Usuario u = em.find(Usuario.class, id);
        em.close();
        return u;
    }

    /**
     * Lista todos os usuários existentes.
     *
     * @return lista (possivelmente vazia) com todos os usuários
     */
    @Override
    public List<Usuario> listarTodos() {
        EntityManager em = emf.createEntityManager();
        List<Usuario> lista = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        em.close();
        return lista;
    }

    /**
     * Busca um usuário pelo seu login.
     * <p>
     * Retorna {@code null} se nenhum usuário for encontrado com o login
     * informado. Usa {@link JPAUtil#getEntityManager()} para obter o
     * {@link EntityManager} — trate essa dependência conforme padrão do
     * projeto.     
     *
     * @param login nome de login a ser pesquisado
     * @return usuário correspondente ao login, ou {@code null} se inexistente
     */
    public Usuario buscarPorLogin(String login) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.login = :login", Usuario.class)
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

}
