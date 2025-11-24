package br.com.clinica.util;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

/**
 * Classe utilitária responsável por centralizar estilos visuais aplicados aos
 * componentes da interface Swing.
 * <p>
 * O objetivo é padronizar cores e estilos de botões, rótulos e radio buttons,
 * facilitando manutenção e garantindo consistência visual em toda a aplicação. 
 */
public final class UIStyle {

    /**
     * Cor azul padrão utilizada para elementos de destaque.
     */
    public static final Color AZUL = Color.decode("#1E3A8A");

    /**
     * Cor vermelha utilizada principalmente em botões de remoção ou alerta.
     */
    public static final Color VERMELHO = Color.decode("#D32F2F");

    /**
     * Tom de cinza utilizado para elementos neutros.
     */
    public static final Color CINZA = Color.decode("#6B7280");

    /**
     * Cor branca padrão.
     */
    public static final Color BRANCO = Color.WHITE;

    /**
     * Aplica a cor azul aos textos dos labels informados.
     *
     * @param labels um ou mais {@link JLabel} a serem estilizados
     */
    public static void aplicarAzul(JLabel... labels) {
        for (JLabel lbl : labels) {
            lbl.setForeground(AZUL);
        }
    }

    /**
     * Aplica estilo de botão primário (fundo azul e texto branco).
     *
     * @param buttons um ou mais {@link JButton} a serem estilizados
     */
    public static void primaryButton(JButton... buttons) {
        for (JButton btn : buttons) {
            btn.setBackground(AZUL);
            btn.setForeground(BRANCO);
        }
    }

    /**
     * Aplica estilo "ghost" (fundo branco e texto azul). Ideal para botões
     * secundários.
     *
     * @param buttons um ou mais {@link JButton} a serem estilizados
     */
    public static void ghostButton(JButton... buttons) {
        for (JButton btn : buttons) {
            btn.setBackground(BRANCO);
            btn.setForeground(AZUL);
        }
    }

    /**
     * Estilo específico para botões de remoção (texto vermelho). Mantém fundo
     * branco para reforçar o alerta.
     *
     * @param button botão a ser estilizado
     */
    public static void removeButton(JButton button) {
        button.setBackground(BRANCO);
        button.setForeground(VERMELHO);
    }

    /**
     * Aplica cor azul aos textos dos radio buttons informados.
     *
     * @param radios um ou mais {@link JRadioButton} a serem estilizados
     */
    public static void radioAzul(JRadioButton... radios) {
        for (JRadioButton r : radios) {
            r.setForeground(AZUL);
        }
    }

}
