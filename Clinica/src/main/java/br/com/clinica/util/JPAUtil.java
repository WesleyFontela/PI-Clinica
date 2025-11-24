package br.com.clinica.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Classe utilitária para gerenciamento do JPA e fornecimento de instâncias de
 * {@link EntityManager}.
 * <p>
 * Centraliza a criação do {@link EntityManagerFactory} e o disponibiliza para
 * toda a aplicação, garantindo melhor aproveitamento de recursos e evitando
 * múltiplas inicializações desnecessárias. 
 *
 * <p>
 * O factory é criado uma única vez com base na unidade de persistência
 * "clinicaPU".
 */
public class JPAUtil {

    /**
     * Fábrica de EntityManagers compartilhada por toda a aplicação. Instanciada
     * apenas uma vez ao carregar a classe.
     */
    private static final EntityManagerFactory emf
            = Persistence.createEntityManagerFactory("clinicaPU");

    /**
     * Obtém uma nova instância de {@link EntityManager}.
     * <p>
     * Cada chamada retorna um EntityManager independente, que deve ser fechado
     * após o uso para evitar vazamento de recursos.     
     *
     * @return um novo EntityManager
     */
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Fecha o {@link EntityManagerFactory}, liberando seus recursos.
     * <p>
     * Deve ser chamado apenas no encerramento da aplicação, pois após o
     * fechamento nenhuma nova instância de EntityManager poderá ser criada.     
     */
    public static void fechar() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}
