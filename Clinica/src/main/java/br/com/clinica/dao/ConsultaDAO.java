package br.com.clinica.dao;

import br.com.clinica.model.Consulta;
import br.com.clinica.model.StatusConsulta;
import static br.com.clinica.util.DateTimeUtils.tryParseDate;
import static br.com.clinica.util.DateTimeUtils.tryParseTime;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * DAO responsável pela persistência e consultas avançadas da entidade
 * {@link br.com.clinica.model.Consulta}.
 * <p>
 * Além das operações CRUD, esta classe implementa múltiplos filtros e uma busca
 * dinâmica capaz de interpretar termos como textos, datas, horários e status. 
 */
public class ConsultaDAO implements DAO<Consulta> {

    /**
     * Factory compartilhada para criação de EntityManager.
     */
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("clinicaPU");

    /**
     * Insere uma nova consulta no banco.
     *
     * @param c consulta a ser persistida
     */
    @Override
    public void inserir(Consulta c) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(c);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Atualiza os dados de uma consulta existente.
     *
     * @param c consulta com informações atualizadas
     */
    @Override
    public void atualizar(Consulta c) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(c);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Remove uma consulta pelo ID, caso exista.
     *
     * @param id identificador da consulta
     */
    @Override
    public void deletar(int id) {
        EntityManager em = emf.createEntityManager();
        Consulta c = em.find(Consulta.class, id);
        if (c != null) {
            em.getTransaction().begin();
            em.remove(c);
            em.getTransaction().commit();
        }
        em.close();
    }

    /**
     * Busca uma consulta pelo identificador.
     *
     * @param id identificador da consulta
     * @return instância de {@link Consulta} ou null caso não exista
     */
    @Override
    public Consulta buscarPorId(int id) {
        EntityManager em = emf.createEntityManager();
        Consulta c = em.find(Consulta.class, id);
        em.close();
        return c;
    }

    /**
     * Retorna todas as consultas cadastradas.
     *
     * @return lista completa de consultas
     */
    @Override
    public List<Consulta> listarTodos() {
        EntityManager em = emf.createEntityManager();
        List<Consulta> lista = em.createQuery(
                "SELECT c FROM Consulta c",
                Consulta.class).getResultList();
        em.close();
        return lista;
    }

    /**
     * Realiza uma busca dinâmica por consultas.
     * <p>
     * O termo pode ser:
     * <ul>
     * <li>parte do nome do paciente</li>
     * <li>parte do nome do médico</li>
     * <li>status (AGENDADA, REALIZADA, CANCELADA)</li>
     * <li>data válida (em vários formatos comuns)</li>
     * <li>horário válido (HH:mm ou HH:mm:ss)</li>
     * </ul>
     * A consulta se ajusta automaticamente dependendo do tipo do termo.     
     *
     * @param termo entrada da busca digitada pelo usuário
     * @return lista de consultas correspondentes
     */
    public List<Consulta> buscarConsulta(String termo) {
        EntityManager em = emf.createEntityManager();
        try {
            String likeTerm = "%" + termo + "%";

            LocalDate parsedDate = tryParseDate(termo);
            LocalTime parsedTime = tryParseTime(termo);

            StringBuilder jpql = new StringBuilder(
                    "SELECT c FROM Consulta c "
                    + "WHERE LOWER(c.paciente.nome) LIKE LOWER(:termo) "
                    + "   OR LOWER(c.medico.nome) LIKE LOWER(:termo) "
                    + "   OR LOWER(CAST(c.status AS string)) LIKE LOWER(:termo)"
            );

            if (parsedDate != null) {
                jpql.append(" OR c.dataAgendada = :dataAgendada");
            }
            if (parsedTime != null) {
                jpql.append(" OR c.horaAgendada = :horaAgendada");
            }

            TypedQuery<Consulta> query
                    = em.createQuery(jpql.toString(), Consulta.class);

            query.setParameter("termo", likeTerm);

            if (parsedDate != null) {
                query.setParameter("dataAgendada", parsedDate);
            }
            if (parsedTime != null) {
                query.setParameter("horaAgendada", parsedTime);
            }

            return query.getResultList();

        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }      

    /**
     * Lista consultas associadas a um médico específico.
     */
    public List<Consulta> listarPorMedico(int medicoId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Consulta c WHERE c.medico.id = :medicoId",
                    Consulta.class)
                    .setParameter("medicoId", medicoId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Lista consultas de um determinado paciente.
     */
    public List<Consulta> listarPorPaciente(int pacienteId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Consulta c WHERE c.paciente.id = :pacienteId",
                    Consulta.class)
                    .setParameter("pacienteId", pacienteId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Lista consultas filtrando pelo status.
     */
    public List<Consulta> listarPorStatus(StatusConsulta status) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Consulta c WHERE c.status = :status",
                    Consulta.class)
                    .setParameter("status", status)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Lista consultas dentro de um intervalo de datas.
     */
    public List<Consulta> listarPorPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Consulta c WHERE c.dataAgendada BETWEEN :dataInicial AND :dataFinal",
                    Consulta.class)
                    .setParameter("dataInicial", dataInicial)
                    .setParameter("dataFinal", dataFinal)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Lista consultas filtrando paciente e médico ao mesmo tempo.
     */
    public List<Consulta> listarPorPacienteEMedico(int pacienteId, int medicoId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Consulta c WHERE c.paciente.id = :pacienteId AND c.medico.id = :medicoId",
                    Consulta.class)
                    .setParameter("pacienteId", pacienteId)
                    .setParameter("medicoId", medicoId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Lista consultas por status e médico específico.
     */
    public List<Consulta> listarPorStatusEMedico(StatusConsulta status, int medicoId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Consulta c WHERE c.status = :status AND c.medico.id = :medicoId",
                    Consulta.class)
                    .setParameter("status", status)
                    .setParameter("medicoId", medicoId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Lista consultas dentro de um período para um médico específico.
     */
    public List<Consulta> listarPorPeriodoEMedico(LocalDate dataInicial, LocalDate dataFinal, int medicoId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Consulta c WHERE c.dataAgendada BETWEEN :dataInicial AND :dataFinal AND c.medico.id = :medicoId",
                    Consulta.class)
                    .setParameter("dataInicial", dataInicial)
                    .setParameter("dataFinal", dataFinal)
                    .setParameter("medicoId", medicoId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

}
