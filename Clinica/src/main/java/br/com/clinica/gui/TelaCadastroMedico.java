package br.com.clinica.gui;

import br.com.clinica.dao.MedicoDAO;
import br.com.clinica.model.Medico;
import br.com.clinica.model.Usuario;
import br.com.clinica.util.UIStyle;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Tela de cadastro de médicos.
 * <p>
 * Interface gráfica responsável por permitir o cadastro, edição, remoção e
 * busca dinâmica de médicos no sistema. Também gerencia permissões de acordo
 * com o perfil do usuário logado e aplica estilos visuais definidos em
 * {@link UIStyle}. 
 *
 * <p>
 * Perfis suportados: ADMIN, MEDICO, RECEP. Usuários médicos possuem acesso
 * limitado apenas à visualização. 
 *
 * @author Wesley
 */
public class TelaCadastroMedico extends javax.swing.JFrame {

    /**
     * Usuário atualmente autenticado no sistema.
     *
     * <p>
     * É utilizado para definir permissões de acesso e determinar quais
     * componentes podem ser visualizadas ou manipuladas na interface.     
     */
    private Usuario usuarioLogado;

    /**
     * Construtor da tela de cadastro de médicos.
     *
     * @param usuarioLogado Usuário autenticado no sistema, usado para aplicar
     * permissões.
     */
    public TelaCadastroMedico(Usuario usuarioLogado) {
        initComponents();
        this.usuarioLogado = usuarioLogado;

        UIStyle.aplicarAzul(
                lblTitulo,
                lblNome,
                lblEspecialidade,
                lblCRM,
                lblBuscarMedico
        );

        UIStyle.primaryButton(btnSalvar, btnEditar);
        UIStyle.ghostButton(btnLimpar, btnVoltar);
        UIStyle.removeButton(btnRemover);

        TitledBorder borda = (TitledBorder) pnlMedicos.getBorder();
        borda.setTitleColor(UIStyle.AZUL);

        aplicarPermissoes();
        atualizarTabela();

        /**
         * Adiciona um {@link javax.swing.event.DocumentListener} ao campo de
         * busca, permitindo que a pesquisa seja realizada de forma dinâmica
         * conforme o usuário digita.
         *
         * <p>
         * Cada evento do DocumentListener — inserção, remoção ou alteração de
         * estilo — aciona o método {@code buscarMedicos()}, garantindo
         * atualização imediata dos resultados exibidos.         
         */
        txtBuscarMedico.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            /**
             * Evento disparado quando caracteres são inseridos no campo de
             * busca.
             *
             * @param e Evento de alteração no documento.
             */
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                buscarMedicos(txtBuscarMedico.getText().trim());
            }

            /**
             * Evento disparado quando caracteres são removidos do campo de
             * busca.
             *
             * @param e Evento de alteração no documento.
             */
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                buscarMedicos(txtBuscarMedico.getText().trim());
            }

            /**
             * Evento disparado quando há alteração no estilo do documento.
             *
             * @param e Evento de alteração no documento.
             */
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                buscarMedicos(txtBuscarMedico.getText().trim());
            }
        });
    }

    /**
     * Aplica permissões na interface com base no perfil do usuário logado.
     * Oculta ou desabilita funcionalidades sensíveis para determinados perfis.
     */
    private void aplicarPermissoes() {
        switch (usuarioLogado.getPerfil().toUpperCase()) {
            case "ADMIN":
                break;
            case "RECEP":
                btnRemover.setVisible(false);
                break;
            case "MEDICO":
                btnRemover.setVisible(false);
                btnEditar.setVisible(false);
                btnSalvar.setVisible(false);
                btnLimpar.setVisible(false);
                btnVoltar.setVisible(true);
                break;
            default:
                btnRemover.setVisible(false);
                btnEditar.setVisible(false);
                btnSalvar.setVisible(false);
                btnLimpar.setVisible(false);
                btnVoltar.setVisible(true);
                break;
        }
    }

    /**
     * Atualiza a tabela exibindo todos os médicos cadastrados.
     */
    private void atualizarTabela() {
        MedicoDAO dao = new MedicoDAO();
        List<Medico> lista = dao.listarTodos();

        DefaultTableModel modelo = (DefaultTableModel) tblPacientes.getModel();
        modelo.setRowCount(0);

        for (Medico m : lista) {
            modelo.addRow(new Object[]{
                m.getId(),
                m.getNome(),
                m.getEspecialidade(),
                m.getCrm()
            });
        }
    }

    /**
     * Realiza busca dinâmica de médicos pelo nome ou CRM.
     *
     * @param termo Texto digitado pelo usuário no campo de busca.
     */
    private void buscarMedicos(String termo) {
        MedicoDAO dao = new MedicoDAO();
        List<Medico> lista;

        if (termo.isEmpty()) {
            lista = dao.listarTodos();
        } else {
            lista = dao.buscarPorNomeOuCrm(termo);
        }

        DefaultTableModel modelo = (DefaultTableModel) tblPacientes.getModel();
        modelo.setRowCount(0);

        for (Medico m : lista) {
            modelo.addRow(new Object[]{
                m.getId(),
                m.getNome(),
                m.getEspecialidade(),
                m.getCrm()
            });
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

        lblNome = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        btnVoltar = new javax.swing.JButton();
        lblEspecialidade = new javax.swing.JLabel();
        txtEspecialidade = new javax.swing.JTextField();
        lblCRM = new javax.swing.JLabel();
        txtCRM = new javax.swing.JTextField();
        btnSalvar = new javax.swing.JButton();
        pnlMedicos = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPacientes = new javax.swing.JTable();
        lblBuscarMedico = new javax.swing.JLabel();
        txtBuscarMedico = new javax.swing.JTextField();
        btnRemover = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CADASTRO DE MÉDICO");
        setBounds(new java.awt.Rectangle(0, 0, 800, 600));
        setResizable(false);

        lblNome.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblNome.setText("Nome:");

        txtNome.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        btnVoltar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnVoltar.setText("Voltar ao Menu");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        lblEspecialidade.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblEspecialidade.setText("Especialidade:");

        txtEspecialidade.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        lblCRM.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblCRM.setText("CRM:");

        txtCRM.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        btnSalvar.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        pnlMedicos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Médicos Cadastrados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        tblPacientes.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        tblPacientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Especialidade", "CRM"
            }
        ));
        jScrollPane2.setViewportView(tblPacientes);

        lblBuscarMedico.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblBuscarMedico.setText("Buscar Médico:");

        btnRemover.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnRemover.setText("Remover");
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });

        btnEditar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMedicosLayout = new javax.swing.GroupLayout(pnlMedicos);
        pnlMedicos.setLayout(pnlMedicosLayout);
        pnlMedicosLayout.setHorizontalGroup(
            pnlMedicosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(pnlMedicosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBuscarMedico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBuscarMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(239, Short.MAX_VALUE))
        );
        pnlMedicosLayout.setVerticalGroup(
            pnlMedicosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMedicosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMedicosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarMedico, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBuscarMedico)
                    .addComponent(btnRemover)
                    .addComponent(btnEditar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btnLimpar.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitulo.setText("Cadastro de Médico");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlMedicos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnVoltar)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(178, 178, 178)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCRM)
                    .addComponent(lblNome)
                    .addComponent(lblEspecialidade))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtCRM, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtEspecialidade, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTitulo)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblTitulo)
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNome)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEspecialidade)
                    .addComponent(txtEspecialidade, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCRM)
                    .addComponent(txtCRM, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlMedicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnVoltar)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Evento do botão "Remover". Remove o médico selecionado na tabela, após
     * confirmação do usuário.
     *
     * @param evt Evento de clique.
     */
    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        int linha = tblPacientes.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um médico na tabela para remover.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (Integer) tblPacientes.getValueAt(linha, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente remover este médico?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            MedicoDAO dao = new MedicoDAO();
            dao.deletar(id);
            JOptionPane.showMessageDialog(this, "Médico removido com sucesso!");
            atualizarTabela();
        }
    }//GEN-LAST:event_btnRemoverActionPerformed

    /**
     * Evento do botão "Salvar". Registra um novo médico no sistema após
     * validação dos campos.
     *
     * @param evt Evento de clique.
     */
    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        String nome = txtNome.getText().trim();
        String especialidade = txtEspecialidade.getText().trim();
        String crm = txtCRM.getText().trim();

        if (nome.isEmpty() || especialidade.isEmpty() || crm.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos!",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        MedicoDAO dao = new MedicoDAO();
        Medico medico = new Medico(nome, especialidade, crm);

        dao.inserir(medico);
        JOptionPane.showMessageDialog(this, "Médico cadastrado com sucesso!");

        atualizarTabela();
        btnLimparActionPerformed(evt);
    }//GEN-LAST:event_btnSalvarActionPerformed

    /**
     * Evento do botão "Limpar". Limpa os campos de entrada para novo cadastro.
     *
     * @param evt Evento de clique.
     */
    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        txtNome.setText("");
        txtEspecialidade.setText("");
        txtCRM.setText("");

        txtNome.requestFocus();
    }//GEN-LAST:event_btnLimparActionPerformed

    /**
     * Evento do botão "Editar". Atualiza os dados do médico selecionado.
     *
     * @param evt Evento de clique.
     */
    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        int linha = tblPacientes.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um médico na tabela para editar.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (Integer) tblPacientes.getValueAt(linha, 0);
        String nome = txtNome.getText().trim();
        String especialidade = txtEspecialidade.getText().trim();
        String crm = txtCRM.getText().trim();

        if (nome.isEmpty() || especialidade.isEmpty() || crm.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos antes de atualizar a consulta.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        MedicoDAO dao = new MedicoDAO();
        Medico medico = new Medico(nome, especialidade, crm);
        medico.setId(id);

        dao.atualizar(medico);
        JOptionPane.showMessageDialog(this, "Médico atualizado com sucesso!");

        atualizarTabela();
        btnLimparActionPerformed(evt);
    }//GEN-LAST:event_btnEditarActionPerformed

    /**
     * Evento do botão "Voltar". Retorna ao menu principal da aplicação.
     *
     * @param evt Evento de clique.
     */
    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        new TelaMenuPrincipal(usuarioLogado).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnVoltarActionPerformed

    /**
     * Método principal usado para testes diretos da tela.
     *
     * @param args Argumentos de linha de comando.
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
            java.util.logging.Logger.getLogger(TelaCadastroMedico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaCadastroMedico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaCadastroMedico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaCadastroMedico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                br.com.clinica.model.Usuario teste = new br.com.clinica.model.Usuario("admin", "123", "ADMIN");
                new TelaCadastroMedico(teste).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnRemover;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBuscarMedico;
    private javax.swing.JLabel lblCRM;
    private javax.swing.JLabel lblEspecialidade;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlMedicos;
    private javax.swing.JTable tblPacientes;
    private javax.swing.JTextField txtBuscarMedico;
    private javax.swing.JTextField txtCRM;
    private javax.swing.JTextField txtEspecialidade;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables
}
