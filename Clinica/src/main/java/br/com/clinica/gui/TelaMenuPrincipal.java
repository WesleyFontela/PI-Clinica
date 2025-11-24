package br.com.clinica.gui;

import br.com.clinica.model.Usuario;
import br.com.clinica.util.UIStyle;

/**
 * Tela principal do sistema, exibida após a autenticação do usuário.
 * <p>
 * Esta tela apresenta as opções principais do sistema, como cadastro de
 * pacientes, cadastro de médicos, agendamento de consultas e relatórios. O
 * conjunto de funcionalidades é habilitado ou desabilitado conforme o perfil do
 * usuário logado. 
 *
 * <p>
 * Perfis suportados:
 * <ul>
 * <li>ADMIN – acesso completo</li>
 * <li>MEDICO – acesso limitado (sem cadastros ou agendamentos)</li>
 * <li>RECEP – acesso intermediário</li>
 * </ul> 
 *
 * @author Wesley
 */
public class TelaMenuPrincipal extends javax.swing.JFrame {

    /**
     * Usuário atualmente autenticado no sistema.
     *
     * <p>
     * É utilizado para definir permissões de acesso e determinar quais
     * componentes podem ser visualizadas ou manipuladas na interface.     
     */
    private Usuario usuarioLogado;

    /**
     * Construtor da tela principal.
     *
     * @param usuarioLogado usuário autenticado, cujo perfil define as
     * permissões exibidas
     */
    public TelaMenuPrincipal(Usuario usuarioLogado) {
        initComponents();
        this.usuarioLogado = usuarioLogado;

        UIStyle.aplicarAzul(lblTitulo);

        UIStyle.primaryButton(
                btnAgendarConsulta,
                btnCadastroMedicos,
                btnCadastroPacientes,
                btnRelatorios
        );
        UIStyle.ghostButton(btnSair);

        aplicarPermissoes();
    }

    /**
     * Aplica as permissões de acesso conforme o perfil do usuário logado.
     * <p>
     * Perfis:
     * <ul>
     * <li><b>ADMIN:</b> acesso total</li>
     * <li><b>MEDICO:</b> sem cadastros ou agendamentos</li>
     * <li><b>RECEP:</b> acesso completo exceto funções administrativas</li>
     * <li><b>OUTROS:</b> todas as ações ocultas</li>
     * </ul>     
     */
    private void aplicarPermissoes() {
        switch (usuarioLogado.getPerfil().toUpperCase()) {
            case "ADMIN":
                break;

            case "MEDICO":
                btnCadastroPacientes.setEnabled(false);
                btnCadastroMedicos.setEnabled(false);
                btnAgendarConsulta.setEnabled(false);
                break;

            case "RECEP":
                break;

            default:
                btnCadastroPacientes.setVisible(false);
                btnCadastroMedicos.setVisible(false);
                btnAgendarConsulta.setVisible(false);
                btnRelatorios.setVisible(false);
                break;
        }
    }

    /**
     * Inicializa os componentes gráficos da tela.
     * <p>
     * Método gerado automaticamente pelo editor visual (NetBeans). Recomenda-se
     * não modificar manualmente.     
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        btnCadastroPacientes = new javax.swing.JButton();
        btnCadastroMedicos = new javax.swing.JButton();
        btnAgendarConsulta = new javax.swing.JButton();
        btnRelatorios = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("SISTEMA DE CONSULTAS MÉDICAS");

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("O QUE DESEJA FAZER?");

        btnCadastroPacientes.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnCadastroPacientes.setText("Cadastro de Pacientes");
        btnCadastroPacientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastroPacientesActionPerformed(evt);
            }
        });

        btnCadastroMedicos.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnCadastroMedicos.setText("Cadastro de Médicos");
        btnCadastroMedicos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastroMedicosActionPerformed(evt);
            }
        });

        btnAgendarConsulta.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnAgendarConsulta.setText("Agendar Consulta");
        btnAgendarConsulta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgendarConsultaActionPerformed(evt);
            }
        });

        btnRelatorios.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnRelatorios.setText("Relatórios");
        btnRelatorios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelatoriosActionPerformed(evt);
            }
        });

        btnSair.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnSair.setText("Sair");
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(lblTitulo))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(btnCadastroPacientes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnAgendarConsulta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnRelatorios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnCadastroMedicos, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTitulo)
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCadastroPacientes, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCadastroMedicos, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btnAgendarConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnRelatorios, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Abre a tela de cadastro de pacientes.
     *
     * @param evt evento do botão
     */
    private void btnCadastroPacientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastroPacientesActionPerformed
        TelaCadastroPaciente tela = new TelaCadastroPaciente(usuarioLogado);
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnCadastroPacientesActionPerformed

    /**
     * Abre a tela de cadastro de médicos.
     *
     * @param evt evento do botão
     */
    private void btnCadastroMedicosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastroMedicosActionPerformed
        TelaCadastroMedico tela = new TelaCadastroMedico(usuarioLogado);
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnCadastroMedicosActionPerformed

    /**
     * Abre a tela de agendamento de consultas.
     *
     * @param evt evento do botão
     */
    private void btnAgendarConsultaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgendarConsultaActionPerformed
        TelaAgendamentoConsulta tela = new TelaAgendamentoConsulta(usuarioLogado);
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnAgendarConsultaActionPerformed

    /**
     * Abre a tela de relatórios.
     *
     * @param evt evento do botão
     */
    private void btnRelatoriosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelatoriosActionPerformed
        TelaRelatorios tela = new TelaRelatorios(usuarioLogado);
        tela.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRelatoriosActionPerformed

    /**
     * Retorna à tela de login.
     *
     * @param evt evento do botão
     */
    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        this.dispose();
        new TelaLogin().setVisible(true);
    }//GEN-LAST:event_btnSairActionPerformed

    /**
     * Método principal para teste da tela.
     * <p>
     * Inicializa o sistema com um usuário ADMIN fictício.     
     *
     * @param args argumentos de linha de comando
     */
    public static void main(String args[]) {

        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaMenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaMenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaMenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaMenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                br.com.clinica.model.Usuario teste = new br.com.clinica.model.Usuario("admin", "123", "ADMIN");
                new TelaMenuPrincipal(teste).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgendarConsulta;
    private javax.swing.JButton btnCadastroMedicos;
    private javax.swing.JButton btnCadastroPacientes;
    private javax.swing.JButton btnRelatorios;
    private javax.swing.JButton btnSair;
    private javax.swing.JLabel lblTitulo;
    // End of variables declaration//GEN-END:variables
}
