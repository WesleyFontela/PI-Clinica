package br.com.clinica.gui;

import br.com.clinica.dao.ConsultaDAO;
import br.com.clinica.dao.MedicoDAO;
import br.com.clinica.dao.PacienteDAO;
import br.com.clinica.model.Consulta;
import br.com.clinica.model.Medico;
import br.com.clinica.model.Paciente;
import br.com.clinica.enums.StatusConsulta;
import br.com.clinica.model.Usuario;
import br.com.clinica.util.UIStyle;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 * Tela responsável pela exibição, filtragem e manipulação de relatórios de
 * consultas do sistema de clínica médica.
 *
 * <p>
 * Esta interface gráfica permite visualizar consultas registradas, aplicar
 * filtros por paciente, médico, status e período, além de possibilitar a edição
 * de status e remoção de consultas conforme permissões do usuário logado. 
 *
 * <p>
 * Os componentes visuais utilizam o padrão de estilização definido em
 * {@link UIStyle}. As consultas são carregadas conforme o perfil do usuário:
 * ADMIN, RECEP ou MEDICO. 
 *
 * @author Wesley
 */
public class TelaRelatorios extends javax.swing.JFrame {

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
     * Cria a tela de relatórios e inicializa os componentes visuais, listeners,
     * regras de permissão e dados iniciais.
     *
     * @param usuarioLogado usuário autenticado no sistema, utilizado para
     * definir permissões e filtros específicos
     */
    public TelaRelatorios(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;

        initComponents();

        UIStyle.aplicarAzul(
                lblTitulo,
                lblFiltro,
                lblDe,
                lblA,
                lblBuscar
        );

        UIStyle.primaryButton(btnGerarRelatorio, btnEditarStatus);
        UIStyle.removeButton(btnRemover);
        UIStyle.ghostButton(btnVoltar);

        TitledBorder borda = (TitledBorder) pnlRelatorios.getBorder();
        borda.setTitleColor(UIStyle.AZUL);

        UIStyle.radioAzul(
                rdbPaciente,
                rdbMedico,
                rdbStatus,
                rdbPeriodo
        );

        /**
         * Adiciona um {@link javax.swing.event.DocumentListener} ao campo de
         * busca, permitindo que a pesquisa seja realizada de forma dinâmica
         * conforme o usuário digita.
         *
         * <p>
         * Cada evento do DocumentListener — inserção, remoção ou alteração de
         * estilo — aciona o método {@code buscarRelatorios()}, garantindo
         * atualização imediata dos resultados exibidos.         
         */
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            /**
             * Evento disparado quando caracteres são inseridos no campo de
             * busca.
             *
             * @param e Evento de alteração no documento.
             */
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                buscarRelatorios();
            }

            /**
             * Evento disparado quando caracteres são removidos do campo de
             * busca.
             *
             * @param e Evento de alteração no documento.
             */
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                buscarRelatorios();
            }

            /**
             * Evento disparado quando há alteração no estilo do documento.
             *
             * @param e Evento de alteração no documento.
             */
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                buscarRelatorios();
            }
        });

        aplicarPermissoes();
        carregarCombos();
        carregarConsultasIniciais();
    }

    /**
     * Normaliza o perfil do usuário logado para letras maiúsculas, evitando
     * problemas com espaços ou formatação incorreta.
     *
     * @return perfil normalizado ou string vazia se não houver usuário logado
     */
    private String perfilNormalizado() {
        if (usuarioLogado == null || usuarioLogado.getPerfil() == null) {
            return "";
        }
        return usuarioLogado.getPerfil().trim().toUpperCase();
    }

    /**
     * Aplica regras de permissão na interface com base no perfil do usuário
     * logado.
     *
     * <p>
     * Define visibilidade e acesso a botões:
     * <ul>
     * <li>ADMIN: acesso total</li>
     * <li>RECEP: sem acesso à remoção</li>
     * <li>MEDICO: vê apenas suas consultas e não pode remover</li>
     * <li>Outros perfis: acesso mínimo</li>
     * </ul>     
     */
    private void aplicarPermissoes() {
        String perfil = perfilNormalizado();

        switch (perfil) {
            case "ADMIN":
                break;
            case "RECEP":
                btnRemover.setVisible(false);
                break;
            case "MEDICO":
                btnRemover.setVisible(false);
                carregarConsultasDoMedicoLogado();
                break;
            default:
                btnRemover.setVisible(false);
                btnEditarStatus.setVisible(false);
                btnGerarRelatorio.setVisible(false);
                btnVoltar.setVisible(true);
                break;
        }
    }

    /**
     * Carrega os combos de Paciente e Médico conforme o perfil do usuário.
     *
     * <p>
     * ADMIN e RECEP possuem acesso a todas as listas. MÉDICO não utiliza estes
     * filtros.     
     *
     * Em caso de falha, uma mensagem de aviso é exibida ao usuário.
     */
    private void carregarCombos() {
        if (usuarioLogado == null) {
            return;
        }

        String perfil = perfilNormalizado();

        if ("ADMIN".equals(perfil) || "RECEP".equals(perfil) || "RECEPCIONISTA".equals(perfil)) {
            try {
                PacienteDAO pacienteDAO = new PacienteDAO();
                List<Paciente> pacientes = pacienteDAO.listarTodos();
                comboPaciente.removeAllItems();
                for (Paciente p : pacientes) {
                    comboPaciente.addItem(p);
                }
                comboPaciente.setSelectedItem(null);

                MedicoDAO medicoDAO = new MedicoDAO();
                List<Medico> medicos = medicoDAO.listarTodos();
                comboMedico.removeAllItems();
                for (Medico m : medicos) {
                    comboMedico.addItem(m);
                }
                comboMedico.setSelectedItem(null);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Falha ao carregar filtros de Paciente/Médico: " + e.getMessage(),
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            comboPaciente.removeAllItems();
            comboMedico.removeAllItems();
        }
    }

    /**
     * Realiza a busca dinâmica na tabela conforme o texto digitado no campo de
     * pesquisa.
     *
     * <p>
     * Se o campo estiver vazio, recarrega a listagem padrão de consultas
     * conforme o perfil do usuário. Caso contrário, usa o método
     * {@link ConsultaDAO#buscarConsulta(String)}.     
     */
    private void buscarRelatorios() {
        String termo = txtBuscar.getText().trim();

        ConsultaDAO dao = new ConsultaDAO();
        List<Consulta> consultas;

        if (termo.isEmpty()) {
            carregarConsultasIniciais();
            return;
        }

        consultas = dao.buscarConsulta(termo);
        preencherTabela(consultas);
    }

    /**
     * Carrega as consultas iniciais exibidas ao abrir a tela.
     *
     * <p>
     * Para médicos, carrega apenas consultas do médico logado. Para outros
     * perfis, carrega todas as consultas.     
     */
    private void carregarConsultasIniciais() {
        if (usuarioLogado != null && "MEDICO".equalsIgnoreCase(usuarioLogado.getPerfil())) {
            carregarConsultasDoMedicoLogado();
        } else {
            carregarTodasConsultas();
        }
    }

    /**
     * Carrega todas as consultas cadastradas e preenche a tabela com elas.
     */
    private void carregarTodasConsultas() {
        ConsultaDAO dao = new ConsultaDAO();
        preencherTabela(dao.listarTodos());
    }

    /**
     * Carrega e exibe apenas as consultas pertencentes ao médico logado.
     */
    private void carregarConsultasDoMedicoLogado() {
        ConsultaDAO dao = new ConsultaDAO();
        preencherTabela(dao.listarPorMedico(usuarioLogado.getId()));
    }

    /**
     * Gera o relatório com base no filtro selecionado: paciente, médico, status
     * ou período.
     *
     * <p>
     * Considera também o perfil do usuário para garantir que médicos só
     * visualizem suas próprias consultas.    
     *
     * <p>
     * Em caso de erro de formatação ou falha na busca, uma mensagem apropriada
     * é exibida ao usuário.     
     */
    private void gerarRelatorio() {
        ConsultaDAO dao = new ConsultaDAO();
        List<Consulta> consultas = null;

        try {
            String perfil = perfilNormalizado();

            if (rdbPaciente.isSelected() && comboPaciente.getSelectedItem() != null) {
                Paciente pacienteSelecionado = (Paciente) comboPaciente.getSelectedItem();
                int pacienteId = pacienteSelecionado.getId();
                if ("MEDICO".equals(perfil)) {
                    consultas = dao.listarPorPacienteEMedico(pacienteId, usuarioLogado.getId());
                } else {
                    consultas = dao.listarPorPaciente(pacienteId);
                }
            } else if (rdbMedico.isSelected() && comboMedico.getSelectedItem() != null) {
                Medico medicoSelecionado = (Medico) comboMedico.getSelectedItem();
                int medicoId = medicoSelecionado.getId();
                if ("MEDICO".equals(perfil)) {                    
                    consultas = dao.listarPorMedico(usuarioLogado.getId());
                } else {
                    consultas = dao.listarPorMedico(medicoId);
                }
            } else if (rdbStatus.isSelected() && comboStatus.getSelectedIndex() != -1) {
                String statusSelecionado = comboStatus.getSelectedItem().toString().toUpperCase();
                StatusConsulta status = StatusConsulta.valueOf(statusSelecionado);
                if ("MEDICO".equals(perfil)) {
                    consultas = dao.listarPorStatusEMedico(status, usuarioLogado.getId());
                } else {
                    consultas = dao.listarPorStatus(status);
                }
            } else if (rdbPeriodo.isSelected()) {
                String de = txtDataInicial.getText().trim();
                String ate = txtDataFinal.getText().trim();

                if (de.isEmpty() || ate.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Informe a data inicial e final para o filtro por período.",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                LocalDate dataInicial = LocalDate.parse(de, fmtData);
                LocalDate dataFinal = LocalDate.parse(ate, fmtData);

                if ("MEDICO".equals(perfil)) {
                    consultas = dao.listarPorPeriodoEMedico(dataInicial, dataFinal, usuarioLogado.getId());
                } else {
                    consultas = dao.listarPorPeriodo(dataInicial, dataFinal);
                }
            } else {
                if ("MEDICO".equals(perfil)) {
                    consultas = dao.listarPorMedico(usuarioLogado.getId());
                } else {
                    consultas = dao.listarTodos();
                }
            }

            preencherTabela(consultas);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao gerar relatório: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Preenche a tabela de relatórios com a lista fornecida de consultas.
     *
     * @param consultas lista de consultas que será exibida na tabela
     */
    private void preencherTabela(List<Consulta> consultas) {
        DefaultTableModel model = (DefaultTableModel) tblRelatorios.getModel();
        model.setRowCount(0);
        for (Consulta c : consultas) {
            model.addRow(new Object[]{
                c.getId(),
                c.getPaciente() != null ? c.getPaciente().getNome() : "",
                c.getMedico() != null ? c.getMedico().getNome() : "",
                c.getDataAgendada() != null ? c.getDataAgendada().format(fmtData) : "",
                c.getHoraAgendada() != null ? c.getHoraAgendada().toString() : "",
                c.getStatus() != null ? c.getStatus().name() : ""
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

        grupoFiltroRelatorio = new javax.swing.ButtonGroup();
        btnVoltar = new javax.swing.JButton();
        txtDataInicial = new javax.swing.JTextField();
        btnGerarRelatorio = new javax.swing.JButton();
        pnlRelatorios = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblRelatorios = new javax.swing.JTable();
        lblBuscar = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnRemover = new javax.swing.JButton();
        btnEditarStatus = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();
        comboPaciente = new javax.swing.JComboBox<>();
        comboMedico = new javax.swing.JComboBox<>();
        lblFiltro = new javax.swing.JLabel();
        rdbPaciente = new javax.swing.JRadioButton();
        rdbMedico = new javax.swing.JRadioButton();
        rdbPeriodo = new javax.swing.JRadioButton();
        txtDataFinal = new javax.swing.JTextField();
        lblA = new javax.swing.JLabel();
        lblDe = new javax.swing.JLabel();
        rdbStatus = new javax.swing.JRadioButton();
        comboStatus = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("RELATÓRIOS");
        setBounds(new java.awt.Rectangle(0, 0, 800, 600));
        setResizable(false);

        btnVoltar.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnVoltar.setText("Voltar ao Menu");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        txtDataInicial.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtDataInicial.setToolTipText("Data Inicial dd/mm/aaaa");

        btnGerarRelatorio.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnGerarRelatorio.setText("Gerar Relatório");
        btnGerarRelatorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGerarRelatorioActionPerformed(evt);
            }
        });

        pnlRelatorios.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Relatórios", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 16))); // NOI18N

        tblRelatorios.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        tblRelatorios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Paciente", "Médico", "Data", "Hora", "Status"
            }
        ));
        jScrollPane2.setViewportView(tblRelatorios);

        lblBuscar.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblBuscar.setText("Buscar:");

        btnRemover.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnRemover.setText("Remover");
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });

        btnEditarStatus.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnEditarStatus.setText("Editar Status");
        btnEditarStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarStatusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlRelatoriosLayout = new javax.swing.GroupLayout(pnlRelatorios);
        pnlRelatorios.setLayout(pnlRelatoriosLayout);
        pnlRelatoriosLayout.setHorizontalGroup(
            pnlRelatoriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 778, Short.MAX_VALUE)
            .addGroup(pnlRelatoriosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBuscar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEditarStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlRelatoriosLayout.setVerticalGroup(
            pnlRelatoriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRelatoriosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRelatoriosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBuscar)
                    .addComponent(btnRemover)
                    .addComponent(btnEditarStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitulo.setText("Relatórios");

        lblFiltro.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblFiltro.setText("Filtro:");

        grupoFiltroRelatorio.add(rdbPaciente);
        rdbPaciente.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        rdbPaciente.setText("Por Paciente");

        grupoFiltroRelatorio.add(rdbMedico);
        rdbMedico.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        rdbMedico.setText("Por Médico");

        grupoFiltroRelatorio.add(rdbPeriodo);
        rdbPeriodo.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        rdbPeriodo.setText("Por Período");

        txtDataFinal.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtDataFinal.setToolTipText("Data Final dd/mm/aaaa");

        lblA.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblA.setText("a");

        lblDe.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblDe.setText("De");

        grupoFiltroRelatorio.add(rdbStatus);
        rdbStatus.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        rdbStatus.setText("Por Status");

        comboStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Agendada", "Realizada", "Cancelada" }));
        comboStatus.setSelectedIndex(-1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnVoltar))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlRelatorios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(182, 182, 182)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFiltro)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rdbPeriodo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDe)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDataInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblA)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDataFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnGerarRelatorio, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(rdbStatus)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(comboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(rdbPaciente)
                                            .addComponent(rdbMedico))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(comboMedico, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(comboPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(144, 144, 144)
                                .addComponent(lblTitulo)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(lblTitulo)
                .addGap(20, 20, 20)
                .addComponent(lblFiltro)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboPaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdbPaciente))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboMedico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdbMedico))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdbStatus)
                    .addComponent(comboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdbPeriodo)
                    .addComponent(txtDataInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDe)
                    .addComponent(lblA)
                    .addComponent(txtDataFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnGerarRelatorio, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlRelatorios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnVoltar)
                .addGap(18, 18, 18))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Manipula o evento de remoção de uma consulta.
     *
     * <p>
     * Solicita confirmação ao usuário antes de excluir a consulta selecionada.
     * Após a remoção, recarrega os dados conforme o perfil.     
     *
     * @param evt evento do botão
     */
    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        int linha = tblRelatorios.getSelectedRow();
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
            int id = (Integer) tblRelatorios.getValueAt(linha, 0);
            ConsultaDAO dao = new ConsultaDAO();
            dao.deletar(id);
            JOptionPane.showMessageDialog(this, "Consulta removida com sucesso!");
            carregarConsultasIniciais();
        }
    }//GEN-LAST:event_btnRemoverActionPerformed

    /**
     * Manipula o clique no botão de gerar relatório, delegando ao método
     * {@link #gerarRelatorio()}.
     *
     * @param evt evento do botão
     */
    private void btnGerarRelatorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGerarRelatorioActionPerformed
        gerarRelatorio();
    }//GEN-LAST:event_btnGerarRelatorioActionPerformed

    /**
     * Retorna ao menu principal, fechando a tela atual.
     *
     * @param evt evento do botão
     */
    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        new TelaMenuPrincipal(usuarioLogado).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnVoltarActionPerformed

    /**
     * Manipula o evento de edição de status da consulta selecionada.
     *
     * <p>
     * Atualiza o status no banco de dados e recarrega a tabela. Exibe mensagens
     * de aviso caso nenhum item ou status seja selecionado.     
     *
     * @param evt evento do botão
     */
    private void btnEditarStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarStatusActionPerformed
        int linha = tblRelatorios.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma consulta para editar o status.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (comboStatus.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um status para aplicar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (Integer) tblRelatorios.getValueAt(linha, 0);
        StatusConsulta novoStatus = StatusConsulta.valueOf(
                comboStatus.getSelectedItem().toString().toUpperCase());

        ConsultaDAO dao = new ConsultaDAO();
        Consulta c = dao.buscarPorId(id);
        c.setStatus(novoStatus);
        dao.atualizar(c);

        JOptionPane.showMessageDialog(this, "Status atualizado com sucesso!");
        carregarConsultasIniciais();
    }//GEN-LAST:event_btnEditarStatusActionPerformed

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
            java.util.logging.Logger.getLogger(TelaRelatorios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaRelatorios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaRelatorios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaRelatorios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                br.com.clinica.model.Usuario teste = new br.com.clinica.model.Usuario("admin", "123", "ADMIN");
                new TelaRelatorios(teste).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditarStatus;
    private javax.swing.JButton btnGerarRelatorio;
    private javax.swing.JButton btnRemover;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JComboBox<Medico> comboMedico;
    private javax.swing.JComboBox<Paciente> comboPaciente;
    private javax.swing.JComboBox<String> comboStatus;
    private javax.swing.ButtonGroup grupoFiltroRelatorio;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblA;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblDe;
    private javax.swing.JLabel lblFiltro;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlRelatorios;
    private javax.swing.JRadioButton rdbMedico;
    private javax.swing.JRadioButton rdbPaciente;
    private javax.swing.JRadioButton rdbPeriodo;
    private javax.swing.JRadioButton rdbStatus;
    private javax.swing.JTable tblRelatorios;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtDataFinal;
    private javax.swing.JTextField txtDataInicial;
    // End of variables declaration//GEN-END:variables
}
