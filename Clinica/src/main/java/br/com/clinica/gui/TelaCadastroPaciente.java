package br.com.clinica.gui;

import br.com.clinica.dao.PacienteDAO;
import br.com.clinica.model.Paciente;
import br.com.clinica.model.Usuario;
import br.com.clinica.util.UIStyle;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Tela de cadastro e gerenciamento de pacientes do sistema de clínicas.
 * <p>
 * Esta interface gráfica permite realizar operações de CRUD sobre pacientes,
 * além de oferecer busca dinâmica por nome ou CPF e aplicar regras de permissão
 * com base no perfil do usuário autenticado. 
 *
 * <p>
 * <b>Funcionalidades principais:</b>
 * <ul>
 * <li>Cadastro de novos pacientes</li>
 * <li>Edição de registros existentes</li>
 * <li>Remoção com confirmação</li>
 * <li>Busca dinâmica (nome ou CPF)</li>
 * <li>Controle de acesso por perfil: ADMIN, MEDICO, RECEP</li>
 * </ul>
 *
 * <p>
 * A tela utiliza estilos visuais fornecidos por {@link UIStyle} e integra-se
 * com a camada de acesso a dados por meio de {@link PacienteDAO}. 
 *
 * @author Wesley
 */
public class TelaCadastroPaciente extends javax.swing.JFrame {

    /**
     * Usuário atualmente autenticado no sistema.
     *
     * <p>
     * É utilizado para definir permissões de acesso e determinar quais
     * componentes podem ser visualizadas ou manipuladas na interface.     
     */
    private Usuario usuarioLogado;

    /**
     * Construtor da tela de cadastro de pacientes.
     *
     * @param usuarioLogado usuário autenticado no sistema, utilizado para
     * definir permissões.
     */
    public TelaCadastroPaciente(Usuario usuarioLogado) {
        initComponents();
        this.usuarioLogado = usuarioLogado;

        UIStyle.aplicarAzul(
                lblTitulo,
                lblNome,
                lblCPF,
                lblTelefone,
                lblBuscarPaciente
        );

        UIStyle.primaryButton(btnSalvar, btnEditar);
        UIStyle.ghostButton(btnLimpar, btnVoltar);
        UIStyle.removeButton(btnRemover);

        TitledBorder borda = (TitledBorder) pnlPacientes.getBorder();
        borda.setTitleColor(UIStyle.AZUL);

        /**
         * Adiciona um {@link javax.swing.event.DocumentListener} ao campo de
         * busca, permitindo que a pesquisa seja realizada de forma dinâmica
         * conforme o usuário digita.
         *
         * <p>
         * Cada evento do DocumentListener — inserção, remoção ou alteração de
         * estilo — aciona o método {@code buscarPacientes()}, garantindo
         * atualização imediata dos resultados exibidos.         
         */
        txtBuscarPaciente.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            /**
             * Evento disparado quando caracteres são inseridos no campo de
             * busca.
             *
             * @param e Evento de alteração no documento.
             */
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                buscarPacientes(txtBuscarPaciente.getText().trim());
            }

            /**
             * Evento disparado quando caracteres são removidos do campo de
             * busca.
             *
             * @param e Evento de alteração no documento.
             */
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                buscarPacientes(txtBuscarPaciente.getText().trim());
            }

            /**
             * Evento disparado quando há alteração no estilo do documento.
             *
             * @param e Evento de alteração no documento.
             */
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                buscarPacientes(txtBuscarPaciente.getText().trim());
            }
        });

        aplicarPermissoes();
        atualizarTabela();
    }

    /**
     * Realiza a busca dinâmica de pacientes por nome ou CPF. Caso o termo
     * esteja vazio, exibe todos os pacientes cadastrados.
     *
     * @param termo texto usado para busca.
     */
    private void buscarPacientes(String termo) {
        PacienteDAO dao = new PacienteDAO();
        List<Paciente> lista;

        if (termo.isEmpty()) {
            lista = dao.listarTodos();
        } else {
            lista = dao.buscarPorNomeOuCpf(termo);
        }

        DefaultTableModel modelo = (DefaultTableModel) tblPacientes.getModel();
        modelo.setRowCount(0);

        for (Paciente p : lista) {
            modelo.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                p.getCpf(),
                p.getTelefone()
            });
        }
    }

    /**
     * Aplica permissões na interface com base no perfil do usuário logado.
     * Perfis: - ADMIN: acesso total - RECEP: sem permissão para remover -
     * MEDICO: apenas visualização - Outros: apenas visualização
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
     * Atualiza a tabela exibindo todos os pacientes cadastrados.
     */
    private void atualizarTabela() {
        PacienteDAO dao = new PacienteDAO();
        List<Paciente> lista = dao.listarTodos();

        DefaultTableModel modelo = (DefaultTableModel) tblPacientes.getModel();
        modelo.setRowCount(0);

        for (Paciente p : lista) {
            modelo.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                p.getCpf(),
                p.getTelefone()
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
        lblCPF = new javax.swing.JLabel();
        txtCPF = new javax.swing.JTextField();
        lblTelefone = new javax.swing.JLabel();
        txtTelefone = new javax.swing.JTextField();
        btnSalvar = new javax.swing.JButton();
        pnlPacientes = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPacientes = new javax.swing.JTable();
        lblBuscarPaciente = new javax.swing.JLabel();
        txtBuscarPaciente = new javax.swing.JTextField();
        btnRemover = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CADASTRO DE PACIENTE");
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

        lblCPF.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblCPF.setText("CPF:");

        txtCPF.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        lblTelefone.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblTelefone.setText("Telefone:");

        txtTelefone.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        btnSalvar.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        pnlPacientes.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pacientes Cadastrados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        tblPacientes.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        tblPacientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "CPF", "Telefone"
            }
        ));
        jScrollPane2.setViewportView(tblPacientes);

        lblBuscarPaciente.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblBuscarPaciente.setText("Buscar Paciente:");

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

        javax.swing.GroupLayout pnlPacientesLayout = new javax.swing.GroupLayout(pnlPacientes);
        pnlPacientes.setLayout(pnlPacientesLayout);
        pnlPacientesLayout.setHorizontalGroup(
            pnlPacientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(pnlPacientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBuscarPaciente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBuscarPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(230, Short.MAX_VALUE))
        );
        pnlPacientesLayout.setVerticalGroup(
            pnlPacientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPacientesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPacientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBuscarPaciente)
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
        lblTitulo.setText("Cadastro de Paciente");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlPacientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(210, 210, 210)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTelefone)
                            .addComponent(lblNome)
                            .addComponent(lblCPF))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtCPF, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblTitulo)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnVoltar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo)
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNome)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCPF)
                    .addComponent(txtCPF, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTelefone)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(txtTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(pnlPacientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnVoltar)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Evento do botão Remover. Remove o paciente selecionado após confirmação.
     *
     * @param evt evento de clique.
     */
    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        int linha = tblPacientes.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um paciente na tabela para remover.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (Integer) tblPacientes.getValueAt(linha, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente remover este paciente?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            PacienteDAO dao = new PacienteDAO();
            dao.deletar(id);
            JOptionPane.showMessageDialog(this, "Paciente removido com sucesso!");
            atualizarTabela();
        }
    }//GEN-LAST:event_btnRemoverActionPerformed

    /**
     * Evento do botão Salvar. Valida os campos e registra um novo paciente no
     * banco.
     *
     * @param evt evento de clique.
     */
    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        String nome = txtNome.getText().trim();
        String cpf = txtCPF.getText().trim();
        String telefone = txtTelefone.getText().trim();

        if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos!",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        PacienteDAO dao = new PacienteDAO();
        Paciente paciente = new Paciente(nome, cpf, telefone);

        dao.inserir(paciente);

        JOptionPane.showMessageDialog(this, "Paciente cadastrado com sucesso!");

        atualizarTabela();
        btnLimparActionPerformed(evt);
    }//GEN-LAST:event_btnSalvarActionPerformed

    /**
     * Evento do botão Limpar. Limpa os campos de entrada.
     *
     * @param evt evento de clique.
     */
    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        txtNome.setText("");
        txtCPF.setText("");
        txtTelefone.setText("");

        txtNome.requestFocus();
    }//GEN-LAST:event_btnLimparActionPerformed

    /**
     * Evento do botão Editar. Atualiza o paciente selecionado na tabela.
     *
     * @param evt evento de clique.
     */
    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        int linha = tblPacientes.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um paciente na tabela para editar.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (Integer) tblPacientes.getValueAt(linha, 0);
        String nome = txtNome.getText().trim();
        String cpf = txtCPF.getText().trim();
        String telefone = txtTelefone.getText().trim();

        if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos antes de atualizar a consulta.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        PacienteDAO dao = new PacienteDAO();
        Paciente paciente = new Paciente(nome, cpf, telefone);

        paciente.setId(id);
        dao.atualizar(paciente);

        JOptionPane.showMessageDialog(this, "Paciente atualizado com sucesso!");

        atualizarTabela();
        btnLimparActionPerformed(evt);
    }//GEN-LAST:event_btnEditarActionPerformed

    /**
     * Evento do botão Voltar. Retorna ao menu principal.
     *
     * @param evt evento de clique.
     */
    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        new TelaMenuPrincipal(usuarioLogado).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnVoltarActionPerformed

    /**
     * Método principal para testes independentes da tela.
     *
     * @param args argumentos da linha de comando.
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
            java.util.logging.Logger.getLogger(TelaCadastroPaciente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaCadastroPaciente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaCadastroPaciente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaCadastroPaciente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                br.com.clinica.model.Usuario teste = new br.com.clinica.model.Usuario("admin", "123", "ADMIN");
                new TelaCadastroPaciente(teste).setVisible(true);
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
    private javax.swing.JLabel lblBuscarPaciente;
    private javax.swing.JLabel lblCPF;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblTelefone;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlPacientes;
    private javax.swing.JTable tblPacientes;
    private javax.swing.JTextField txtBuscarPaciente;
    private javax.swing.JTextField txtCPF;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtTelefone;
    // End of variables declaration//GEN-END:variables
}
