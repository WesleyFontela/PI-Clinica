package br.com.clinica.model;

/**
 * Representa os possíveis status de uma {@link br.com.clinica.model.Consulta}.
 * <p>
 * Usado para controlar o fluxo da consulta (agendada, realizada ou cancelada).
 * O mapeamento na entidade {@code Consulta} está configurado como
 * {@code @Enumerated(EnumType.STRING)}, portanto os valores são armazenados
 * como texto no banco de dados (por exemplo, "AGENDADA"). 
 */
public enum StatusConsulta {

    /**
     * Consulta agendada, ainda não realizada nem cancelada.
     */
    AGENDADA,
    /**
     * Consulta que foi realizada.
     */
    REALIZADA,
    /**
     * Consulta que foi cancelada.
     */
    CANCELADA
}
