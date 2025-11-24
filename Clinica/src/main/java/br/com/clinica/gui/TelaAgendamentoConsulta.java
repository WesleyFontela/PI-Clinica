package br.com.clinica.gui;

import br.com.clinica.dao.ConsultaDAO;
import br.com.clinica.model.Consulta;
import br.com.clinica.model.Medico;
import br.com.clinica.model.Paciente;
import br.com.clinica.model.StatusConsulta;
import br.com.clinica.model.Usuario;
import br.com.clinica.util.UIStyle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Tela gráfica responsável pelo agendamento, edição, remoção e listagem de
 * consultas médicas no sistema.
 *
 * <p>
 * A interface permite que usuários dos perfis ADMIN, RECEP e MEDICO realizem
 * operações conforme suas permissões, incluindo salvar novas consultas, alterar
 * registros existentes e pesquisar consultas de forma dinâmica.
 *
 * <p>
 * A tela integra-se com os modelos {@link Consulta}, {@link Paciente},
 * {@link Medico} e utiliza o {@link ConsultaDAO} para persistência.
 *
 * <p>
 * Também aplica estilos visuais definidos pela classe {@link UIStyle}.
 *
 * @author Wesley
 */
public class TelaAgendamentoConsulta extends javax.swing.JFrame {

    /**
     * Usuário atualmente autenticado no sistema.
     *
     * <p>
     * É utilizado para definir permissões de acesso e determinar quais
     * componentes podem ser visualizadas ou manipuladas na interface.
     
     */
    private Usuario usuarioLogado;

    /**
     * Formatador de datas utilizado para exibir valores no padrão
     * {@code dd/MM/yyyy} em componentes da interface.
     */
    private final DateTimeFormatter fmtData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Constrói a tela de agendamento, aplicando estilos visuais, carregando
     * pacientes, médicos, permissões do usuário logado, além de configurar a
     * busca dinâmica na tabela.
     *
     * @param usuarioLogado Usuário autenticado no sistema, utilizado para
     * verificação de permissões e navegação.
     */
    public TelaAgendamentoConsulta(Usuario usuarioLogado) {
        initComponents();
        this.usuarioLogado = usuarioLogado;

        UIStyle.aplicarAzul(
                lblTitulo,
                lblPaciente,
                lblMedico,
                lblStatus,
                lblData,
                lblHora,
                lblBuscarMedico
        );

        UIStyle.primaryButton(btnSalvar, btnEditar);
        UIStyle.ghostButton(btnLimpar, btnEditar, btnVoltar);
        UIStyle.removeButton(btnRemover);

        TitledBorder borda = (TitledBorder) pnlConsultas.getBorder();
        borda.setTitleColor(UIStyle.AZUL);

        aplicarPermissoes();
        carregarPacientes();
        carregarMedicos();
        atualizarTabela();

        /**
         * Adiciona um {@link javax.swing.event.DocumentListener} ao campo de
         * busca, permitindo que a pesquisa seja realizada de forma dinâmica
         * conforme o usuário digita.
         *
         * <p>
         * Cada evento do DocumentListener — inserção, remoção ou alteração de
         * estilo — aciona o método {@code buscarConsultas()}, garantindo
         * atualização imediata dos resultados exibidos.         
         */
        txtBuscarConsulta.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            /**
             * Evento disparado quando caracteres são inseridos no campo de
             * busca.
             *
             * @param e Evento de alteração no documento.
             */
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                buscarConsultas(txtBuscarConsulta.getText().trim());
            }

            /**
             * Evento disparado quando caracteres são removidos do campo de
             * busca.
             *
             * @param e Evento de alteração no documento.
             */
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                buscarConsultas(txtBuscarConsulta.getText().trim());
            }

            /**
             * Evento disparado quando há alteração no estilo do documento.
             *
             * @param e Evento de alteração no documento.
             */
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                buscarConsultas(txtBuscarConsulta.getText().trim());
            }
        });
    }

    /**
     * Aplica as regras de permissão de acordo com o perfil do usuário logado.
     *
     * <p>
     * Perfis e permissões:
     * <ul>
     * <li><b>ADMIN:</b> acesso total</li>
     * <li><b>RECEP:</b> não pode remover consultas</li>
     * <li><b>MEDICO:</b> apenas visualiza as consultas</li>
     * </ul>
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
     * Carrega todos os pacientes cadastrados no banco de dados e os insere no
     * combobox de seleção.
     *
     * <p>
     * A consulta é executada via JPA utilizando o persistence unit
     * <code>clinicaPU</code>.
     */
    private void carregarPacientes() {
        EntityManager em = Persistence.createEntityManagerFactory("clinicaPU").createEntityManager();
        List<Paciente> pacientes = em.createQuery("SELECT p FROM Paciente p", Paciente.class).getResultList();
        em.close();

        comboPaciente.removeAllItems();
        for (Paciente p : pacientes) {
            comboPaciente.addItem(p);
        }
    }

    /**
     * Carrega todos os médicos cadastrados no banco de dados e os adiciona ao
     * combobox de seleção.
     */
    private void carregarMedicos() {
        EntityManager em = Persistence.createEntityManagerFactory("clinicaPU").createEntityManager();
        List<Medico> medicos = em.createQuery("SELECT m FROM Medico m", Medico.class).getResultList();
        em.close();

        comboMedico.removeAllItems();
        for (Medico m : medicos) {
            comboMedico.addItem(m);
        }
    }

    /**
     * Atualiza a tabela de consultas exibida na interface, preenchendo todas as
     * colunas com os dados fornecidos pelo {@link ConsultaDAO}.
     *
     * <p>
     * Remove todas as linhas existentes e popula novamente a tabela.
     */
    private void atualizarTabela() {
        ConsultaDAO dao = new ConsultaDAO();
        List<Consulta> lista = dao.listarTodos();

        DefaultTableModel modelo = (DefaultTableModel) tblConsultas.getModel();
        modelo.setRowCount(0);

        for (Consulta c : lista) {
            modelo.addRow(new Object[]{
                c.getId(),
                c.getPaciente().getNome(),
                c.getMedico().getNome(),
                c.getDataAgendada() != null ? c.getDataAgendada().format(fmtData) : "",
                c.getHoraAgendada(),
                c.getStatus()
            });
        }
    }

    /**
     * Realiza uma busca dinâmica de consultas na base de dados conforme o termo
     * informado pelo usuário.
     *
     * <p>
     * Se o campo estiver vazio, todas as consultas são exibidas. Do contrário,
     * a busca é delegada ao método
     * {@link ConsultaDAO#buscarConsulta(String)}.
     *
     * @param termo Texto digitado pelo usuário para filtragem de consultas.
     */
    private void buscarConsultas(String termo) {
        ConsultaDAO dao = new ConsultaDAO();
        List<Consulta> lista;

        if (termo.isEmpty()) {
            lista = dao.listarTodos();
        } else {
            lista = dao.buscarConsulta(termo);
        }

        DefaultTableModel modelo = (DefaultTableModel) tblConsultas.getModel();
        modelo.setRowCount(0);

        for (Consulta c : lista) {
            modelo.addRow(new Object[]{
                c.getId(),
                c.getPaciente().getNome(),
                c.getMedico().getNome(),
                c.getDataAgendada() != null ? c.getDataAgendada().format(fmtData) : "",
                c.getHoraAgendada(),
                c.getStatus()
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

        lblMedico = new javax.swing.JLabel();
        btnVoltar = new javax.swing.JButton();
        lblData = new javax.swing.JLabel();
        txtData = new javax.swing.JTextField();
        lblHora = new javax.swing.JLabel();
        txtHora = new javax.swing.JTextField();
        btnSalvar = new javax.swing.JButton();
        pnlConsultas = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblConsultas = new javax.swing.JTable();
        lblBuscarMedico = new javax.swing.JLabel();
        txtBuscarConsulta = new javax.swing.JTextField();
        btnRemover = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();
        lblPaciente = new javax.swing.JLabel();
        comboPaciente = new javax.swing.JComboBox<>();
        comboMedico = new javax.swing.JComboBox<>();
        lblStatus = new javax.swing.JLabel();
        comboStatus = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("AGENDAMENTO DE CONSULTA");
        setBounds(new java.awt.Rectangle(0, 0, 800, 600));
        setResizable(false);

        lblMedico.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblMedico.setText("Médico:");

        btnVoltar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnVoltar.setText("Voltar ao Menu");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        lblData.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblData.setText("Data:");

        txtData.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        lblHora.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblHora.setText("Hora:");

        txtHora.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        btnSalvar.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        pnlConsultas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Consultas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        tblConsultas.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        tblConsultas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Paciente", "Médico", "Data", "Hora", "Status"
            }
        ));
        jScrollPane2.setViewportView(tblConsultas);

        lblBuscarMedico.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblBuscarMedico.setText("Buscar Consulta:");

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

        javax.swing.GroupLayout pnlConsultasLayout = new javax.swing.GroupLayout(pnlConsultas);
        pnlConsultas.setLayout(pnlConsultasLayout);
        pnlConsultasLayout.setHorizontalGroup(
            pnlConsultasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(pnlConsultasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBuscarMedico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBuscarConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(229, Short.MAX_VALUE))
        );
        pnlConsultasLayout.setVerticalGroup(
            pnlConsultasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlConsultasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlConsultasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        lblTitulo.setText("Agendamento de Consulta");

        lblPaciente.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblPaciente.setText("Paciente:");

        lblStatus.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblStatus.setText("Status:");

        comboStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Agendada", "Realizada", "Cancelada" }));
        comboStatus.setSelectedIndex(-1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlConsultas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnVoltar)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(217, 217, 217)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitulo)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblHora)
                            .addComponent(lblMedico)
                            .addComponent(lblData)
                            .addComponent(lblPaciente)
                            .addComponent(lblStatus))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtHora)
                            .addComponent(txtData)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(comboPaciente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboMedico, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(comboStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(lblTitulo)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPaciente)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(comboPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblMedico)
                        .addGap(2, 2, 2))
                    .addComponent(comboMedico, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus)
                    .addComponent(comboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblData)
                    .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHora)
                    .addComponent(txtHora, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlConsultas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnVoltar)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Evento acionado ao clicar no botão "Remover".
     *
     * <p>
     * Remove a consulta selecionada na tabela após solicitação de confirmação
     * do usuário. Caso nenhuma linha esteja selecionada, exibe uma mensagem de
     * aviso.
     *
     * @param evt Evento do botão.
     */

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        int linha = tblConsultas.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma consulta para remover.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente remover esta consulta?",
                "Confirmação", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tblConsultas.getValueAt(linha, 0);
            ConsultaDAO dao = new ConsultaDAO();
            dao.deletar(id);

            JOptionPane.showMessageDialog(this, "Consulta removida com sucesso!");
            atualizarTabela();
        }
    }//GEN-LAST:event_btnRemoverActionPerformed

    /**
     * Evento acionado ao clicar no botão "Salvar".
     *
     * <p>
     * Valida os campos obrigatórios, cria uma nova instância de
     * {@link Consulta}, popula seus dados e a insere no banco através do
     * {@link ConsultaDAO}.
     *
     * <p>
     * Em caso de erro de validação ou conversão de datas/horário, uma mensagem
     * apropriada é exibida ao usuário.
     *
     * @param evt Evento do botão.
     */

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        try {
            if (comboPaciente.getSelectedItem() == null
                    || comboMedico.getSelectedItem() == null
                    || txtData.getText().trim().isEmpty()
                    || txtHora.getText().trim().isEmpty()
                    || comboStatus.getSelectedItem() == null) {

                JOptionPane.showMessageDialog(this,
                        "Por favor, preencha todos os campos antes de salvar a consulta.",
                        "Campos obrigatórios",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Consulta c = new Consulta();
            c.setPaciente((Paciente) comboPaciente.getSelectedItem());
            c.setMedico((Medico) comboMedico.getSelectedItem());
            c.setDataAgendada(LocalDate.parse(txtData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            c.setHoraAgendada(LocalTime.parse(txtHora.getText(), DateTimeFormatter.ofPattern("HH:mm")));
            c.setStatus(StatusConsulta.valueOf(comboStatus.getSelectedItem().toString().toUpperCase()));

            ConsultaDAO dao = new ConsultaDAO();
            dao.inserir(c);

            JOptionPane.showMessageDialog(this, "Consulta salva com sucesso!");
            atualizarTabela();
            btnLimparActionPerformed(evt);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar consulta: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    /**
     * Limpa todos os campos de entrada da tela, incluindo os comboboxes, data,
     * hora, campo de busca e seleção da tabela.
     *
     * @param evt Evento do botão.
     */

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        txtData.setText("");
        txtHora.setText("");
        txtBuscarConsulta.setText("");
        comboPaciente.setSelectedIndex(-1);
        comboMedico.setSelectedIndex(-1);
        comboStatus.setSelectedIndex(-1);
        tblConsultas.clearSelection();
    }//GEN-LAST:event_btnLimparActionPerformed

    /**
     * Evento acionado ao clicar no botão "Editar".
     *
     * <p>
     * Valida os campos obrigatórios, carrega a consulta selecionada na tabela e
     * atualiza seus dados antes de enviá-los ao {@link ConsultaDAO}.
     *
     * @param evt Evento do botão.
     */

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        int linha = tblConsultas.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma consulta na tabela para editar.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (comboPaciente.getSelectedItem() == null
                || comboMedico.getSelectedItem() == null
                || txtData.getText().trim().isEmpty()
                || txtHora.getText().trim().isEmpty()
                || comboStatus.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos antes de atualizar a consulta.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = (Integer) tblConsultas.getValueAt(linha, 0);
            ConsultaDAO dao = new ConsultaDAO();
            Consulta c = dao.buscarPorId(id);

            c.setPaciente((Paciente) comboPaciente.getSelectedItem());
            c.setMedico((Medico) comboMedico.getSelectedItem());
            c.setDataAgendada(LocalDate.parse(txtData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            c.setHoraAgendada(LocalTime.parse(txtHora.getText(), DateTimeFormatter.ofPattern("HH:mm")));
            c.setStatus(StatusConsulta.valueOf(comboStatus.getSelectedItem().toString().toUpperCase()));

            dao.atualizar(c);

            JOptionPane.showMessageDialog(this, "Consulta atualizada com sucesso!");
            atualizarTabela();
            btnLimparActionPerformed(evt);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao atualizar consulta: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    /**
     * Retorna ao menu principal, fechando a tela atual e abrindo a tela
     * {@link TelaMenuPrincipal}.
     *
     * @param evt Evento do botão.
     */

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        this.dispose();
        new TelaMenuPrincipal(usuarioLogado).setVisible(true);
    }//GEN-LAST:event_btnVoltarActionPerformed

    /**
     * Método principal para execução isolada da tela.
     *
     * <p>
     * Carrega o Look and Feel Nimbus, cria um usuário de teste e exibe a
     * interface.
     *
     * @param args Argumentos da linha de comando (não utilizados).
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
            java.util.logging.Logger.getLogger(TelaAgendamentoConsulta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaAgendamentoConsulta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaAgendamentoConsulta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaAgendamentoConsulta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                br.com.clinica.model.Usuario teste = new br.com.clinica.model.Usuario("admin", "123", "ADMIN");
                new TelaAgendamentoConsulta(teste).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnRemover;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JComboBox<Medico> comboMedico;
    private javax.swing.JComboBox<Paciente> comboPaciente;
    private javax.swing.JComboBox<String> comboStatus;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBuscarMedico;
    private javax.swing.JLabel lblData;
    private javax.swing.JLabel lblHora;
    private javax.swing.JLabel lblMedico;
    private javax.swing.JLabel lblPaciente;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlConsultas;
    private javax.swing.JTable tblConsultas;
    private javax.swing.JTextField txtBuscarConsulta;
    private javax.swing.JTextField txtData;
    private javax.swing.JTextField txtHora;
    // End of variables declaration//GEN-END:variables
}
