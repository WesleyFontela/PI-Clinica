package br.com.clinica.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Classe utilitária para conversão de textos em objetos de data e hora.
 *
 * Fornece métodos estáticos que tentam interpretar strings em diferentes
 * formatos comuns de {@link LocalDate} e {@link LocalTime}.
 *
 * Essa classe centraliza a lógica de parsing para reutilização em diferentes
 * camadas do sistema (DAO, Service, GUI).
 */
public class DateTimeUtils {

    /**
     * Tenta converter o texto para {@link LocalDate} usando múltiplos formatos.
     *
     * Formatos aceitos:
     * <ul>
     * <li>ISO_LOCAL_DATE (yyyy-MM-dd)</li>
     * <li>d/M/yyyy</li>
     * <li>d-M-yyyy</li>
     * <li>dd/MM/yyyy</li>
     * <li>dd-MM-yyyy</li>
     * </ul>
     *
     * @param dateText valor digitado pelo usuário
     * @return instância de {@link LocalDate} válida ou {@code null} se não for
     * possível converter
     */
    public static LocalDate tryParseDate(String dateText) {
        if (dateText == null) {
            return null;
        }
        String trimmed = dateText.trim();

        DateTimeFormatter[] fmts = new DateTimeFormatter[]{
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("d-M-yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
        };

        for (DateTimeFormatter f : fmts) {
            try {
                return LocalDate.parse(trimmed, f);
            } catch (DateTimeParseException ignored) {
            }
        }
        return null;
    }

    /**
     * Tenta converter o texto para {@link LocalTime} usando formatos comuns.
     *
     * Formatos aceitos:
     * <ul>
     * <li>H:mm</li>
     * <li>HH:mm</li>
     * <li>H:mm:ss</li>
     * <li>HH:mm:ss</li>
     * </ul>
     *
     * @param timeText valor digitado pelo usuário
     * @return instância de {@link LocalTime} válida ou {@code null} se não for
     * possível converter
     */
    public static LocalTime tryParseTime(String timeText) {
        if (timeText == null) {
            return null;
        }
        String trimmed = timeText.trim();

        DateTimeFormatter[] fmts = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern("H:mm"),
            DateTimeFormatter.ofPattern("HH:mm"),
            DateTimeFormatter.ofPattern("H:mm:ss"),
            DateTimeFormatter.ofPattern("HH:mm:ss")
        };

        for (DateTimeFormatter f : fmts) {
            try {
                return LocalTime.parse(trimmed, f);
            } catch (DateTimeParseException ignored) {
            }
        }
        return null;
    }
}
