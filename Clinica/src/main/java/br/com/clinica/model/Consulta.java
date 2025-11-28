package br.com.clinica.model;

import br.com.clinica.enums.StatusConsulta;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.*;

/**
 * Entidade que representa uma consulta médica agendada no sistema.
 * <p>
 * Armazena informações de data, hora, status e relacionamentos com
 * {@link Paciente} e {@link Medico}. Mapeada para a tabela {@code consulta}. 
 */
@Entity
@Table(name = "consulta")
public class Consulta {

    /**
     * Identificador único da consulta (chave primária).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Data em que a consulta está agendada.
     */
    private LocalDate dataAgendada;

    /**
     * Horário em que a consulta está agendada.
     */
    private LocalTime horaAgendada;

    /**
     * Status atual da consulta.
     * <p>
     * Mapeado como texto (ex.: AGENDADA, REALIZADA, CANCELADA).     
     */
    @Enumerated(EnumType.STRING)
    private StatusConsulta status = StatusConsulta.AGENDADA;

    /**
     * Paciente associado a esta consulta.
     */
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    /**
     * Médico responsável pela consulta.
     */
    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico;

    /**
     * Marca a consulta como realizada.
     * <p>
     * Altera o status para {@link StatusConsulta#REALIZADA}.     
     */
    public void concluir() {
        this.status = StatusConsulta.REALIZADA;
    }

    /**
     * Cancela a consulta.
     * <p>
     * Altera o status para {@link StatusConsulta#CANCELADA}.     
     */
    public void cancelar() {
        this.status = StatusConsulta.CANCELADA;
    }

    /**
     * Construtor padrão (necessário para o JPA).
     */
    public Consulta() {
    }

    /**
     * Construtor conveniência usado para criar uma nova consulta com seus dados
     * essenciais.
     *
     * @param dataAgendada data agendada da consulta
     * @param horaAgendada horário agendado da consulta
     * @param paciente paciente associado
     * @param medico médico responsável
     */
    public Consulta(LocalDate dataAgendada, LocalTime horaAgendada, Paciente paciente, Medico medico) {
        this.dataAgendada = dataAgendada;
        this.horaAgendada = horaAgendada;
        this.paciente = paciente;
        this.medico = medico;
        this.status = StatusConsulta.AGENDADA;
    }

    /**
     * Retorna o identificador da consulta.
     *
     * @return id da consulta
     */
    public int getId() {
        return id;
    }

    /**
     * Define o identificador da consulta.
     *
     * @param id novo id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retorna a data agendada da consulta.
     *
     * @return data agendada
     */
    public LocalDate getDataAgendada() {
        return dataAgendada;
    }

    /**
     * Define a data agendada da consulta.
     *
     * @param dataAgendada nova data
     */
    public void setDataAgendada(LocalDate dataAgendada) {
        this.dataAgendada = dataAgendada;
    }

    /**
     * Retorna o horário agendado da consulta.
     *
     * @return horário agendado
     */
    public LocalTime getHoraAgendada() {
        return horaAgendada;
    }

    /**
     * Define o horário agendado da consulta.
     *
     * @param horaAgendada novo horário
     */
    public void setHoraAgendada(LocalTime horaAgendada) {
        this.horaAgendada = horaAgendada;
    }

    /**
     * Retorna o status atual da consulta.
     *
     * @return status
     */
    public StatusConsulta getStatus() {
        return status;
    }

    /**
     * Define o status da consulta.
     *
     * @param status novo status
     */
    public void setStatus(StatusConsulta status) {
        this.status = status;
    }

    /**
     * Retorna o paciente da consulta.
     *
     * @return paciente
     */
    public Paciente getPaciente() {
        return paciente;
    }

    /**
     * Define o paciente da consulta.
     *
     * @param paciente novo paciente
     */
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    /**
     * Retorna o médico da consulta.
     *
     * @return médico
     */
    public Medico getMedico() {
        return medico;
    }

    /**
     * Define o médico responsável pela consulta.
     *
     * @param medico novo médico
     */
    public void setMedico(Medico medico) {
        this.medico = medico;
    }

}
