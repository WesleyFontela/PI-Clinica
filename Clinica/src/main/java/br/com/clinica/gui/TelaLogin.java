package br.com.clinica.gui;

import br.com.clinica.util.UIStyle;

/**
 * Tela de login do sistema de clínicas.
 * <p>
 * Esta interface gráfica permite que o usuário informe seu login e senha para
 * autenticação no sistema. Aplica estilos visuais definidos em {@link UIStyle}
 * e direciona o usuário para o menu principal conforme o perfil autenticado. 
 *
 * <p>
 * Perfis suportados: ADMIN, MEDICO, RECEP. 
 *
 * @author Wesley
 */
public class TelaLogin extends javax.swing.JFrame {

    /**
     * Construtor padrão da tela de login.
     * <p>
     * Inicializa os componentes, aplica o estilo visual padrão e configura os
     * botões com o tema definido na classe {@link UIStyle}.     
     */
    public TelaLogin() {

        initComponents();

        UIStyle.aplicarAzul(
                lblTitulo,
                lblUsuario,
                lblSenha
        );

        UIStyle.primaryButton(btnEntrar);
        UIStyle.ghostButton(btnLimpar);
    }

    /**
     * Inicializa todos os componentes gráficos da tela.
     * <p>
     * Método gerado automaticamente pelo editor visual (NetBeans). Recomenda-se
     * não modificar manualmente.     
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        lblSenha = new javax.swing.JLabel();
        btnEntrar = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        CampoSenha = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SISTEMA DE CONSULTAS MÉDICAS");

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Acesse Sua Conta");

        lblUsuario.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblUsuario.setText("Usuário");

        txtUsuario.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        lblSenha.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblSenha.setText("Senha");

        btnEntrar.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnEntrar.setText("Entrar");
        btnEntrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntrarActionPerformed(evt);
            }
        });

        btnLimpar.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnLimpar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        CampoSenha.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(150, 150, 150)
                        .addComponent(lblTitulo))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(97, 97, 97)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnEntrar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                                .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblUsuario)
                            .addComponent(lblSenha)
                            .addComponent(CampoSenha, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                            .addComponent(txtUsuario))))
                .addContainerGap(103, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(lblTitulo)
                .addGap(26, 26, 26)
                .addComponent(lblUsuario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblSenha)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CampoSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEntrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(98, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Ação executada ao clicar no botão "Entrar".
     * <p>
     * Valida os campos, autentica o usuário por meio de
     * {@link br.com.clinica.service.LoginService}, e redireciona para a tela
     * principal conforme o perfil de acesso.     
     *
     * @param evt evento disparado pelo clique do botão
     */
    private void btnEntrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntrarActionPerformed
        String login = txtUsuario.getText().trim();
        String senha = new String(CampoSenha.getPassword());

        if (login.isEmpty() || senha.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Preencha usuário e senha.",
                    "Aviso",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        br.com.clinica.service.LoginService loginService = new br.com.clinica.service.LoginService();
        br.com.clinica.model.Usuario usuario = loginService.autenticar(login, senha);

        if (usuario != null) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Login realizado com sucesso!",
                    "Bem-vindo",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

            switch (usuario.getPerfil().toUpperCase()) {
                case "ADMIN":
                    new TelaMenuPrincipal(usuario).setVisible(true);
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "Você entrou como ADMIN.");
                    break;

                case "MEDICO":
                    new TelaMenuPrincipal(usuario).setVisible(true);
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "Você entrou como MÉDICO.");
                    break;

                case "RECEP":
                    new TelaMenuPrincipal(usuario).setVisible(true);
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "Você entrou como RECEPCIONISTA.");
                    break;

                default:
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "Perfil desconhecido: " + usuario.getPerfil(),
                            "Erro",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                    break;
            }

            this.dispose();
        }
    }//GEN-LAST:event_btnEntrarActionPerformed

    /**
     * Ação executada ao clicar no botão "Limpar".
     * <p>
     * Limpa os campos de usuário e senha, retornando o foco ao campo de
     * usuário.     
     *
     * @param evt evento do botão
     */
    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        txtUsuario.setText("");
        CampoSenha.setText("");

        txtUsuario.requestFocus();
    }//GEN-LAST:event_btnLimparActionPerformed

    /**
     * Método principal de execução da aplicação.
     * <p>
     * Configura o Look and Feel Nimbus (se disponível) e abre a tela de login.     
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
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaLogin().setVisible(true);
            }
        });

    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField CampoSenha;
    private javax.swing.JButton btnEntrar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JLabel lblSenha;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
