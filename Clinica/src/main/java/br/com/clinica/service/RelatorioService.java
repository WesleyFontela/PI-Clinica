package br.com.clinica.service;

import br.com.clinica.model.Consulta;
import br.com.clinica.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

/**
 * Serviço responsável por consultas de relatório relacionadas à entidade
 * {@link Consulta}.
 * <p>
 * Este serviço oferece métodos de leitura (somente leitura) para recuperar
 * consultas filtradas por paciente, médico ou período. Internamente utiliza
 * {@link JPAUtil} para obter o {@link EntityManager} e fecha a unidade de
 * persistência após cada operação de leitura. 
 *
 * <p>
 * <b>Observação:</b> os métodos retornam listas possivelmente vazias se não
 * houver correspondências. Não abrem/fecham transações pois são operações
 * apenas de leitura.
 */
public class RelatorioService {

    /**
     * Busca consultas cujo nome do paciente contenha o texto informado
     * (case-insensitive).
     *
     * @param nome parte ou todo o nome do paciente a ser pesquisado
     * @return lista de {@link Consulta} que correspondem ao critério; lista
     * vazia se nenhuma correspondência
     */
    public List<Consulta> consultaPorPaciente(String nome) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Consulta> lista = em.createQuery(
                    "SELECT c FROM Consulta c WHERE LOWER(c.paciente.nome) LIKE :nome", Consulta.class)
                    .setParameter("nome", "%" + nome.toLowerCase() + "%")
                    .getResultList();
            return lista;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Busca consultas cujo nome do médico contenha o texto informado
     * (case-insensitive).
     *
     * @param nome parte ou todo o nome do médico a ser pesquisado
     * @return lista de {@link Consulta} que correspondem ao critério; lista
     * vazia se nenhuma correspondência
     */
    public List<Consulta> consultaPorMedico(String nome) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Consulta> lista = em.createQuery(
                    "SELECT c FROM Consulta c WHERE LOWER(c.medico.nome) LIKE :nome", Consulta.class)
                    .setParameter("nome", "%" + nome.toLowerCase() + "%")
                    .getResultList();
            return lista;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Busca consultas agendadas dentro do período informado (inclusivo).
     *
     * @param inicio data inicial do intervalo (inclusive)
     * @param fim data final do intervalo (inclusive)
     * @return lista de {@link Consulta} cujas {@code dataAgendada} estão entre
     * {@code inicio} e {@code fim}; lista vazia se nenhuma correspondência
     */
    public List<Consulta> consultaPorPeriodo(LocalDate inicio, LocalDate fim) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Consulta> lista = em.createQuery(
                    "SELECT c FROM Consulta c WHERE c.dataAgendada BETWEEN :inicio AND :fim", Consulta.class)
                    .setParameter("inicio", inicio)
                    .setParameter("fim", fim)
                    .getResultList();
            return lista;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}
