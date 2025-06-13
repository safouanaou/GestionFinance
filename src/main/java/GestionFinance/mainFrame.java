/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GestionFinance;
import java.awt.CardLayout;
/**
 
 * @author Safouan
 */




public class mainFrame extends javax.swing.JFrame {
    
    private GestionnaireFinance gestionnaireFinance;
    
    private void initializeDatePickers() {
    java.time.LocalDate today = java.time.LocalDate.now();
    int currentYear = today.getYear(); // <- Declaration of 'currentYear'
    String[] months = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"}; // <- Declaration of 'months'


    // --- Setup for the input forms ---
    AnneeRevenue.removeAllItems();
    AnneeDepense.removeAllItems();
    for (int i = 1990; i <= 2030; i++) {
        AnneeRevenue.addItem(String.valueOf(i));
        AnneeDepense.addItem(String.valueOf(i));
    }
    AnneeRevenue.setSelectedItem(String.valueOf(currentYear));
    AnneeDepense.setSelectedItem(String.valueOf(currentYear));

    MoisRevenue.removeAllItems();
    MoisDepense.removeAllItems();
    for (String month : months) {
        MoisRevenue.addItem(month);
        MoisDepense.addItem(month);
    }
    MoisRevenue.setSelectedIndex(today.getMonthValue() - 1);
    MoisDepense.setSelectedIndex(today.getMonthValue() - 1);

    JoursRevenue.setModel(new javax.swing.SpinnerNumberModel(today.getDayOfMonth(), 1, 31, 1));
    JoursDepense.setModel(new javax.swing.SpinnerNumberModel(today.getDayOfMonth(), 1, 31, 1));


    // --- Setup for the filter controls ---
    // Now this code can find and use 'currentYear' and 'months' without error.
    RapportFiltreAnneeSpinner.setModel(new javax.swing.SpinnerNumberModel(currentYear, 1990, 2030, 1));
    RapportFiltreMoisCombox.setModel(new javax.swing.DefaultComboBoxModel<>(months));
    RapportFiltreMoisCombox.setSelectedIndex(today.getMonthValue() - 1);

    RapportFiltreAfficherToutCombox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Revenues", "Depenses" }));
}

    
    private void generateReport() {
    try {
        // 1. Get the user's filter selections
        int year = (Integer) RapportFiltreAnneeSpinner.getValue();
        // The month combo box is 0-indexed (Jan=0), so add 1
        int month = RapportFiltreMoisCombox.getSelectedIndex() + 1;
        String showChoice = (String) RapportFiltreAfficherToutCombox.getSelectedItem();

        // 2. Fetch the filtered lists of revenues and expenses
        java.util.List<Revenue> monthlyRevenues = gestionnaireFinance.getRevenuesByMonth(month, year);
        java.util.List<Depense> monthlyExpenses = gestionnaireFinance.getDepensesByMonth(month, year);

        // 3. Update the summary boxes at the top
        double totalRevenue = monthlyRevenues.stream().mapToDouble(GestionDepense::getMontant).sum();
        double totalExpense = monthlyExpenses.stream().mapToDouble(GestionDepense::getMontant).sum();

        // Use a currency formatter for a nice display (e.g., $1,234.56)
        java.text.NumberFormat currencyFormat = java.text.NumberFormat.getCurrencyInstance();
        TotalRevenueMontantLabel.setText(currencyFormat.format(totalRevenue));
        TotalDepenseMontantLabel.setText(currencyFormat.format(totalExpense));
        BalanceMontantLabel.setText(currencyFormat.format(totalRevenue - totalExpense));

        // 4. Populate the table with the filtered data
        String[] columnNames = {"Type", "Description", "Date", "Montant", "Catégorie/Source"};
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columnNames, 0);

        // Add revenues if "All" or "Revenues" is selected
        if ("All".equals(showChoice) || "Revenues".equals(showChoice)) {
            for (Revenue r : monthlyRevenues) {
                String sourceNames = r.getSource().stream().map(SourceRevenue::getName).collect(java.util.stream.Collectors.joining(", "));
                Object[] row = {"Revenu", r.getDescription(), r.getDateOperation(), r.getMontant(), sourceNames};
                model.addRow(row);
            }
        }

        // Add expenses if "All" or "Expenses" is selected
        if ("All".equals(showChoice) || "Expenses".equals(showChoice)) {
            for (Depense d : monthlyExpenses) {
                Object[] row = {"Dépense", d.getDescription(), d.getDateOperation(), d.getMontant(), d.getCategorie().getName()};
                model.addRow(row);
            }
        }

        TableRapport.setModel(model);

    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage(), "Report Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
    
    
    public static void main(String args[]) throws Exception { 
        
        javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
            new mainFrame().setVisible(true);
        }
    });
}

    /**
     * Creates new form mainFrame
     */

    public mainFrame() {
        
        initComponents(); // This is the NetBeans generated method
        this.gestionnaireFinance = new GestionnaireFinance();
        
        RapportFiltreGenererRapportButton.addActionListener(new java.awt.event.ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            generateReport();
        }
    });
        
        initializeDatePickers();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        Header = new javax.swing.JPanel();
        AppLabel = new javax.swing.JLabel();
        TableBordButton = new javax.swing.JButton();
        GraphButton = new javax.swing.JButton();
        RapportButton = new javax.swing.JButton();
        Sidebar = new javax.swing.JPanel();
        AjouterRevenue = new javax.swing.JPanel();
        RevenueDescriptionLabel = new javax.swing.JLabel();
        RevenueSourceField = new javax.swing.JTextField();
        RevenueMontantLabel = new javax.swing.JLabel();
        RevenueDescriptionField = new javax.swing.JTextField();
        RevenueSourceLabel = new javax.swing.JLabel();
        RevenueDateLabel = new javax.swing.JLabel();
        DatePanel = new javax.swing.JPanel();
        JoursRevenue = new javax.swing.JSpinner();
        MoisRevenue = new javax.swing.JComboBox<>();
        AnneeRevenue = new javax.swing.JComboBox<>();
        AjoutRevenueButton = new javax.swing.JButton();
        RevenueMontantField = new javax.swing.JTextField();
        AjouterDepense = new javax.swing.JPanel();
        DepenseDescriptionLabel = new javax.swing.JLabel();
        DepenseCategorieField = new javax.swing.JTextField();
        DepenseMontantLabel = new javax.swing.JLabel();
        DepenseDescriptionField = new javax.swing.JTextField();
        DepenseCategorieLabel = new javax.swing.JLabel();
        DepenseDateLabel = new javax.swing.JLabel();
        DepenseDatePanel = new javax.swing.JPanel();
        JoursDepense = new javax.swing.JSpinner();
        MoisDepense = new javax.swing.JComboBox<>();
        AnneeDepense = new javax.swing.JComboBox<>();
        AjoutDepenseButton = new javax.swing.JButton();
        DepenseMontantField = new javax.swing.JTextField();
        EcranAffichage = new javax.swing.JPanel();
        TableauBordPanel = new javax.swing.JPanel();
        WelcomeLabel = new javax.swing.JLabel();
        InstructionLabel = new javax.swing.JLabel();
        RapportMensuelPanel = new javax.swing.JPanel();
        reportTopAreaPanel = new javax.swing.JPanel();
        ReportFilterPanel = new javax.swing.JPanel();
        RapportFiltreAnneeLabel = new javax.swing.JLabel();
        RapportFiltreAnneeSpinner = new javax.swing.JSpinner();
        RapportFiltreMoisLabel = new javax.swing.JLabel();
        RapportFiltreMoisCombox = new javax.swing.JComboBox<>();
        RapportFiltreAfficherTout = new javax.swing.JLabel();
        RapportFiltreAfficherToutCombox = new javax.swing.JComboBox<>();
        RapportFiltreGenererRapportButton = new javax.swing.JButton();
        ReportSummaryPanel = new javax.swing.JPanel();
        TotalRevenuePanel = new javax.swing.JPanel();
        TotalRevenueLabel = new javax.swing.JLabel();
        TotalRevenueMontantLabel = new javax.swing.JLabel();
        TotalExpensesPanel = new javax.swing.JPanel();
        TotalDepenseLabel = new javax.swing.JLabel();
        TotalDepenseMontantLabel = new javax.swing.JLabel();
        NetBalancePanel = new javax.swing.JPanel();
        BalanceLabel = new javax.swing.JLabel();
        BalanceMontantLabel = new javax.swing.JLabel();
        TableRapportScroll = new javax.swing.JScrollPane();
        TableRapport = new javax.swing.JTable();
        annualChartViewPanel = new javax.swing.JPanel();
        chartFilterPanel = new javax.swing.JPanel();
        ChartFiltreAnneeLabel = new javax.swing.JLabel();
        ChartFiltreAnneeSpinner = new javax.swing.JSpinner();
        ChartFiltreAfficherButton = new javax.swing.JButton();
        chartDisplayPanel = new javax.swing.JPanel();
        actualChartAreaPanel = new javax.swing.JPanel();
        ChartAnnuelleLabel = new javax.swing.JLabel();
        Footer = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));

        Header.setPreferredSize(new java.awt.Dimension(905, 60));
        java.awt.FlowLayout flowLayout3 = new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 30, 12);
        flowLayout3.setAlignOnBaseline(true);
        Header.setLayout(flowLayout3);

        AppLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        AppLabel.setText("Gestion Depenses et Revenues");
        Header.add(AppLabel);

        TableBordButton.setBackground(new java.awt.Color(50, 177, 74));
        TableBordButton.setFont(new java.awt.Font("Helvetica", 1, 14)); // NOI18N
        TableBordButton.setForeground(new java.awt.Color(255, 255, 255));
        TableBordButton.setText("Table de Bord");
        TableBordButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TableBordButton.setMargin(new java.awt.Insets(10, 14, 10, 14));
        TableBordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TableBordButtonActionPerformed(evt);
            }
        });
        Header.add(TableBordButton);

        GraphButton.setBackground(new java.awt.Color(50, 177, 74));
        GraphButton.setFont(new java.awt.Font("Helvetica", 1, 14)); // NOI18N
        GraphButton.setForeground(new java.awt.Color(255, 255, 255));
        GraphButton.setText("Graph");
        GraphButton.setMargin(new java.awt.Insets(10, 14, 10, 14));
        GraphButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                GraphButtonMouseClicked(evt);
            }
        });
        GraphButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GraphButtonActionPerformed(evt);
            }
        });
        Header.add(GraphButton);

        RapportButton.setBackground(new java.awt.Color(50, 177, 74));
        RapportButton.setFont(new java.awt.Font("Helvetica", 1, 14)); // NOI18N
        RapportButton.setForeground(new java.awt.Color(255, 255, 255));
        RapportButton.setText("Rapport Mensuelle");
        RapportButton.setMargin(new java.awt.Insets(10, 14, 10, 14));
        RapportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RapportButtonActionPerformed(evt);
            }
        });
        Header.add(RapportButton);

        getContentPane().add(Header, java.awt.BorderLayout.PAGE_START);

        Sidebar.setBackground(new java.awt.Color(248, 248, 248));
        Sidebar.setPreferredSize(new java.awt.Dimension(400, 510));
        Sidebar.setLayout(new java.awt.GridLayout(2, 1));

        AjouterRevenue.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ajouter revenue", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        AjouterRevenue.setLayout(new java.awt.GridBagLayout());

        RevenueDescriptionLabel.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, -20, 10);
        AjouterRevenue.add(RevenueDescriptionLabel, gridBagConstraints);

        RevenueSourceField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RevenueSourceFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, -20, 10);
        AjouterRevenue.add(RevenueSourceField, gridBagConstraints);

        RevenueMontantLabel.setText("Montant");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, -20, 10);
        AjouterRevenue.add(RevenueMontantLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, -20, 10);
        AjouterRevenue.add(RevenueDescriptionField, gridBagConstraints);

        RevenueSourceLabel.setText("Source");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, -20, 10);
        AjouterRevenue.add(RevenueSourceLabel, gridBagConstraints);

        RevenueDateLabel.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        AjouterRevenue.add(RevenueDateLabel, gridBagConstraints);

        DatePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));

        JoursRevenue.setMinimumSize(new java.awt.Dimension(67, 26));
        JoursRevenue.setPreferredSize(new java.awt.Dimension(70, 26));
        DatePanel.add(JoursRevenue);

        MoisRevenue.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Janvier", "Item 2", "Item 3", "Item 4" }));
        MoisRevenue.setPreferredSize(new java.awt.Dimension(80, 26));
        MoisRevenue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MoisRevenueActionPerformed(evt);
            }
        });
        DatePanel.add(MoisRevenue);

        AnneeRevenue.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2025", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        AnneeRevenue.setMinimumSize(new java.awt.Dimension(64, 22));
        AnneeRevenue.setPreferredSize(new java.awt.Dimension(80, 26));
        AnneeRevenue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnneeRevenueActionPerformed(evt);
            }
        });
        DatePanel.add(AnneeRevenue);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        AjouterRevenue.add(DatePanel, gridBagConstraints);

        AjoutRevenueButton.setBackground(new java.awt.Color(50, 177, 74));
        AjoutRevenueButton.setFont(new java.awt.Font("Helvetica", 1, 14)); // NOI18N
        AjoutRevenueButton.setForeground(new java.awt.Color(255, 255, 255));
        AjoutRevenueButton.setText("Ajouter Revenue");
        AjoutRevenueButton.setAlignmentY(0.0F);
        AjoutRevenueButton.setMargin(new java.awt.Insets(10, 14, 10, 14));
        AjoutRevenueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AjoutRevenueButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        AjouterRevenue.add(AjoutRevenueButton, gridBagConstraints);

        RevenueMontantField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RevenueMontantFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, -20, 10);
        AjouterRevenue.add(RevenueMontantField, gridBagConstraints);

        Sidebar.add(AjouterRevenue);

        AjouterDepense.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ajouter Depense", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14))); // NOI18N
        AjouterDepense.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        AjouterDepense.setLayout(new java.awt.GridBagLayout());

        DepenseDescriptionLabel.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, -20, 10);
        AjouterDepense.add(DepenseDescriptionLabel, gridBagConstraints);

        DepenseCategorieField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DepenseCategorieFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, -20, 10);
        AjouterDepense.add(DepenseCategorieField, gridBagConstraints);

        DepenseMontantLabel.setText("Montant");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, -20, 10);
        AjouterDepense.add(DepenseMontantLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, -20, 10);
        AjouterDepense.add(DepenseDescriptionField, gridBagConstraints);

        DepenseCategorieLabel.setText("Categorie");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, -20, 10);
        AjouterDepense.add(DepenseCategorieLabel, gridBagConstraints);

        DepenseDateLabel.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        AjouterDepense.add(DepenseDateLabel, gridBagConstraints);

        DepenseDatePanel.setMinimumSize(new java.awt.Dimension(243, 36));
        DepenseDatePanel.setPreferredSize(new java.awt.Dimension(270, 36));

        JoursDepense.setMinimumSize(new java.awt.Dimension(70, 26));
        JoursDepense.setPreferredSize(new java.awt.Dimension(67, 26));
        DepenseDatePanel.add(JoursDepense);

        MoisDepense.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Janvier", "Item 2", "Item 3", "Item 4" }));
        MoisDepense.setPreferredSize(new java.awt.Dimension(80, 26));
        MoisDepense.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MoisDepenseActionPerformed(evt);
            }
        });
        DepenseDatePanel.add(MoisDepense);

        AnneeDepense.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2025", "Item 2", "Item 3", "Item 4" }));
        AnneeDepense.setPreferredSize(new java.awt.Dimension(80, 26));
        DepenseDatePanel.add(AnneeDepense);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        AjouterDepense.add(DepenseDatePanel, gridBagConstraints);

        AjoutDepenseButton.setBackground(new java.awt.Color(50, 177, 74));
        AjoutDepenseButton.setFont(new java.awt.Font("Helvetica", 1, 14)); // NOI18N
        AjoutDepenseButton.setForeground(new java.awt.Color(255, 255, 255));
        AjoutDepenseButton.setText("Ajouter Depense");
        AjoutDepenseButton.setMargin(new java.awt.Insets(10, 14, 10, 14));
        AjoutDepenseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AjoutDepenseButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        AjouterDepense.add(AjoutDepenseButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, -20, 10);
        AjouterDepense.add(DepenseMontantField, gridBagConstraints);

        Sidebar.add(AjouterDepense);

        getContentPane().add(Sidebar, java.awt.BorderLayout.LINE_START);

        EcranAffichage.setBackground(new java.awt.Color(248, 248, 248));
        EcranAffichage.setLayout(new java.awt.CardLayout());

        TableauBordPanel.setLayout(new java.awt.GridBagLayout());

        WelcomeLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        WelcomeLabel.setText("Bienvenue dans votre Application");
        TableauBordPanel.add(WelcomeLabel, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        TableauBordPanel.add(InstructionLabel, gridBagConstraints);

        EcranAffichage.add(TableauBordPanel, "tableauBordView");
        TableauBordPanel.getAccessibleContext().setAccessibleName("");

        RapportMensuelPanel.setBackground(new java.awt.Color(248, 248, 248));
        RapportMensuelPanel.setLayout(new java.awt.BorderLayout());

        reportTopAreaPanel.setLayout(new javax.swing.BoxLayout(reportTopAreaPanel, javax.swing.BoxLayout.Y_AXIS));

        ReportFilterPanel.setPreferredSize(new java.awt.Dimension(505, 40));
        ReportFilterPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        RapportFiltreAnneeLabel.setText("Year");
        ReportFilterPanel.add(RapportFiltreAnneeLabel);
        ReportFilterPanel.add(RapportFiltreAnneeSpinner);

        RapportFiltreMoisLabel.setText("Month");
        ReportFilterPanel.add(RapportFiltreMoisLabel);

        RapportFiltreMoisCombox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ReportFilterPanel.add(RapportFiltreMoisCombox);

        RapportFiltreAfficherTout.setText("Show");
        ReportFilterPanel.add(RapportFiltreAfficherTout);

        RapportFiltreAfficherToutCombox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Revenues", "Expenses", " " }));
        RapportFiltreAfficherToutCombox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RapportFiltreAfficherToutComboxActionPerformed(evt);
            }
        });
        ReportFilterPanel.add(RapportFiltreAfficherToutCombox);

        RapportFiltreGenererRapportButton.setText("Générer Rapport");
        RapportFiltreGenererRapportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RapportFiltreGenererRapportButtonActionPerformed(evt);
            }
        });
        ReportFilterPanel.add(RapportFiltreGenererRapportButton);

        reportTopAreaPanel.add(ReportFilterPanel);

        java.awt.FlowLayout flowLayout2 = new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 15);
        flowLayout2.setAlignOnBaseline(true);
        ReportSummaryPanel.setLayout(flowLayout2);

        TotalRevenuePanel.setBackground(new java.awt.Color(157, 255, 170));
        TotalRevenuePanel.setMaximumSize(new java.awt.Dimension(3500, 3500));
        TotalRevenuePanel.setPreferredSize(new java.awt.Dimension(180, 80));
        TotalRevenuePanel.setLayout(new java.awt.GridBagLayout());

        TotalRevenueLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        TotalRevenueLabel.setText("TOTAL REVENUES");
        TotalRevenuePanel.add(TotalRevenueLabel, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        TotalRevenuePanel.add(TotalRevenueMontantLabel, gridBagConstraints);

        ReportSummaryPanel.add(TotalRevenuePanel);

        TotalExpensesPanel.setBackground(new java.awt.Color(253, 164, 164));
        TotalExpensesPanel.setMaximumSize(new java.awt.Dimension(3500, 3500));
        TotalExpensesPanel.setPreferredSize(new java.awt.Dimension(180, 80));
        TotalExpensesPanel.setLayout(new java.awt.GridBagLayout());

        TotalDepenseLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        TotalDepenseLabel.setText("TOTAL DEPENSES");
        TotalExpensesPanel.add(TotalDepenseLabel, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        TotalExpensesPanel.add(TotalDepenseMontantLabel, gridBagConstraints);

        ReportSummaryPanel.add(TotalExpensesPanel);

        NetBalancePanel.setBackground(new java.awt.Color(237, 197, 255));
        NetBalancePanel.setMaximumSize(new java.awt.Dimension(3500, 3500));
        NetBalancePanel.setPreferredSize(new java.awt.Dimension(180, 80));
        NetBalancePanel.setLayout(new java.awt.GridBagLayout());

        BalanceLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BalanceLabel.setText("NET BALANCE");
        NetBalancePanel.add(BalanceLabel, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        NetBalancePanel.add(BalanceMontantLabel, gridBagConstraints);

        ReportSummaryPanel.add(NetBalancePanel);

        reportTopAreaPanel.add(ReportSummaryPanel);

        RapportMensuelPanel.add(reportTopAreaPanel, java.awt.BorderLayout.NORTH);

        TableRapport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TableRapportScroll.setViewportView(TableRapport);

        RapportMensuelPanel.add(TableRapportScroll, java.awt.BorderLayout.CENTER);

        EcranAffichage.add(RapportMensuelPanel, "RapportView");

        annualChartViewPanel.setLayout(new javax.swing.BoxLayout(annualChartViewPanel, javax.swing.BoxLayout.Y_AXIS));

        ChartFiltreAnneeLabel.setText("Year");
        chartFilterPanel.add(ChartFiltreAnneeLabel);
        chartFilterPanel.add(ChartFiltreAnneeSpinner);

        ChartFiltreAfficherButton.setText("Show Chart");
        chartFilterPanel.add(ChartFiltreAfficherButton);

        annualChartViewPanel.add(chartFilterPanel);

        chartDisplayPanel.setLayout(new java.awt.BorderLayout());

        actualChartAreaPanel.setMinimumSize(new java.awt.Dimension(400, 300));
        actualChartAreaPanel.setPreferredSize(new java.awt.Dimension(400, 300));

        ChartAnnuelleLabel.setText("Annual Revenue & Expense Chart");
        actualChartAreaPanel.add(ChartAnnuelleLabel);

        chartDisplayPanel.add(actualChartAreaPanel, java.awt.BorderLayout.CENTER);

        annualChartViewPanel.add(chartDisplayPanel);

        EcranAffichage.add(annualChartViewPanel, "GraphView");

        getContentPane().add(EcranAffichage, java.awt.BorderLayout.CENTER);

        Footer.setText("Footer");
        getContentPane().add(Footer, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void RevenueSourceFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RevenueSourceFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RevenueSourceFieldActionPerformed

    private void DepenseCategorieFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DepenseCategorieFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DepenseCategorieFieldActionPerformed

    private void RapportFiltreAfficherToutComboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RapportFiltreAfficherToutComboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RapportFiltreAfficherToutComboxActionPerformed

    private void TableBordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TableBordButtonActionPerformed
        // TODO add your handling code here:
    CardLayout layout = (CardLayout) EcranAffichage.getLayout();
    layout.show(EcranAffichage, "tableauBordView");
    }//GEN-LAST:event_TableBordButtonActionPerformed

    private void RapportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RapportButtonActionPerformed
        // TODO add your handling code here:
    CardLayout layout = (CardLayout) EcranAffichage.getLayout();
    layout.show(EcranAffichage, "RapportView");
    }//GEN-LAST:event_RapportButtonActionPerformed

    private void GraphButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GraphButtonActionPerformed
        // TODO add your handling code here:
    CardLayout layout = (CardLayout) EcranAffichage.getLayout();
    layout.show(EcranAffichage, "GraphView");
    }//GEN-LAST:event_GraphButtonActionPerformed

    private void AjoutRevenueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AjoutRevenueButtonActionPerformed
        // TODO add your handling code here:

        try {
        // 1. Get description from the text field
        String description = RevenueDescriptionField.getText();
        if (description.trim().isEmpty()) {
            throw new IllegalArgumentException("La description ne peut pas être vide.");
        }

        // 2. === NEW, MORE ROBUST WAY TO GET THE AMOUNT ===
        String montantText = RevenueMontantField.getText();
        double montant;

        if (montantText.trim().isEmpty()) {
            // Throw an error if the field is empty
            throw new IllegalArgumentException("Veuillez entrer un montant.");
        }
        try {
            // Remove any currency symbols, letters, or spaces.
            // Replace comma with a period for universal decimal parsing.
            String cleanText = montantText.replaceAll("[^\\d,.]", "").replace(',', '.');
            montant = Double.parseDouble(cleanText);
        } catch (NumberFormatException e) {
            // If it's still not a valid number, then show the error.
            throw new IllegalArgumentException("Le montant entré n'est pas un nombre valide.");
        }
        // =======================================================

        // 3. Get the source and create a SourceRevenue object
        String sourceName = RevenueSourceField.getText();
        if (sourceName.trim().isEmpty()) {
            throw new IllegalArgumentException("La source ne peut pas être vide.");
        }
        SourceRevenue source = new SourceRevenue(sourceName, "Source de revenu"); // Default description
        java.util.List<SourceRevenue> sources = new java.util.ArrayList<>();
        sources.add(source);

        // 4. Get the date... (The rest of your code remains the same)
        // Note: You will need to properly populate the month and year JComboBoxes for this to work.
        int jour = (Integer) JoursRevenue.getValue();
        int mois = MoisRevenue.getSelectedIndex() + 1; 
        int annee = Integer.parseInt(AnneeRevenue.getSelectedItem().toString()); 
        java.time.LocalDate dateOperation = java.time.LocalDate.of(annee, mois, jour);

        // 5. Add the new revenue using the GestionnaireFinance instance
        gestionnaireFinance.addRevenu(description, dateOperation, montant, sources);

        // 6. Provide feedback and clear the fields
        javax.swing.JOptionPane.showMessageDialog(this, "Revenu ajouté avec succès!", "Succès", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        RevenueDescriptionField.setText("");
        RevenueMontantField.setText(""); // Use setText("") for this field now
        RevenueSourceField.setText("");


    } catch (IllegalArgumentException e) {
        javax.swing.JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur de Saisie", javax.swing.JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Une erreur inattendue est survenue: " + e.getMessage(), "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
        e.printStackTrace(); // Good for debugging
    
    }
    }//GEN-LAST:event_AjoutRevenueButtonActionPerformed

    private void AjoutDepenseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AjoutDepenseButtonActionPerformed
        // TODO add your handling code here:
        try {
        // 1. Get the description from the text field
        String description = DepenseDescriptionField.getText();
        if (description.trim().isEmpty()) {
            throw new IllegalArgumentException("La description ne peut pas être vide.");
        }

        // 2. Get and parse the amount using the robust method
        // Note: This assumes you have replaced the "Montant" field for Depense
        // with a plain JTextField, just like we did for Revenue.
        String montantText = DepenseMontantField.getText();
        double montant;

        if (montantText.trim().isEmpty()) {
            throw new IllegalArgumentException("Veuillez entrer un montant.");
        }
        try {
            String cleanText = montantText.replaceAll("[^\\d,.]", "").replace(',', '.');
            montant = Double.parseDouble(cleanText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le montant entré n'est pas un nombre valide.");
        }

        // 3. Get the category from its text field
        String categorieName = DepenseCategorieField.getText();
        if (categorieName.trim().isEmpty()) {
            throw new IllegalArgumentException("La catégorie ne peut pas être vide.");
        }
        // Create a CategorieDepense object. We'll use a default description.
        CategorieDepense categorie = new CategorieDepense(categorieName, "Catégorie de dépense");

        // 4. Get the date from the expense date pickers
        int jour = (Integer) JoursDepense.getValue();
        int mois = MoisDepense.getSelectedIndex() + 1;
        int annee = Integer.parseInt(AnneeDepense.getSelectedItem().toString());
        java.time.LocalDate dateOperation = java.time.LocalDate.of(annee, mois, jour);

        // 5. Add the new expense via the finance manager
        gestionnaireFinance.addDepense(description, dateOperation, montant, categorie);

        // 6. Provide user feedback and clear the input fields
        javax.swing.JOptionPane.showMessageDialog(this, "Dépense ajoutée avec succès!", "Succès", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        DepenseDescriptionField.setText("");
        DepenseMontantField.setText("");
        DepenseCategorieField.setText("");


    } catch (IllegalArgumentException e) {
        javax.swing.JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur de Saisie", javax.swing.JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Une erreur inattendue est survenue: " + e.getMessage(), "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
        e.printStackTrace(); // Good for debugging
    }
    }//GEN-LAST:event_AjoutDepenseButtonActionPerformed

    private void MoisRevenueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MoisRevenueActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_MoisRevenueActionPerformed

    private void GraphButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GraphButtonMouseClicked
        // TODO add your handling code here:
  
    }//GEN-LAST:event_GraphButtonMouseClicked

    private void RevenueMontantFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RevenueMontantFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RevenueMontantFieldActionPerformed

    private void AnneeRevenueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnneeRevenueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AnneeRevenueActionPerformed

    private void MoisDepenseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MoisDepenseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MoisDepenseActionPerformed

    private void RapportFiltreGenererRapportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RapportFiltreGenererRapportButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RapportFiltreGenererRapportButtonActionPerformed

    
    
    
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AjoutDepenseButton;
    private javax.swing.JButton AjoutRevenueButton;
    private javax.swing.JPanel AjouterDepense;
    private javax.swing.JPanel AjouterRevenue;
    private javax.swing.JComboBox<String> AnneeDepense;
    private javax.swing.JComboBox<String> AnneeRevenue;
    private javax.swing.JLabel AppLabel;
    private javax.swing.JLabel BalanceLabel;
    private javax.swing.JLabel BalanceMontantLabel;
    private javax.swing.JLabel ChartAnnuelleLabel;
    private javax.swing.JButton ChartFiltreAfficherButton;
    private javax.swing.JLabel ChartFiltreAnneeLabel;
    private javax.swing.JSpinner ChartFiltreAnneeSpinner;
    private javax.swing.JPanel DatePanel;
    private javax.swing.JTextField DepenseCategorieField;
    private javax.swing.JLabel DepenseCategorieLabel;
    private javax.swing.JLabel DepenseDateLabel;
    private javax.swing.JPanel DepenseDatePanel;
    private javax.swing.JTextField DepenseDescriptionField;
    private javax.swing.JLabel DepenseDescriptionLabel;
    private javax.swing.JTextField DepenseMontantField;
    private javax.swing.JLabel DepenseMontantLabel;
    private javax.swing.JPanel EcranAffichage;
    private javax.swing.JLabel Footer;
    private javax.swing.JButton GraphButton;
    private javax.swing.JPanel Header;
    private javax.swing.JLabel InstructionLabel;
    private javax.swing.JSpinner JoursDepense;
    private javax.swing.JSpinner JoursRevenue;
    private javax.swing.JComboBox<String> MoisDepense;
    private javax.swing.JComboBox<String> MoisRevenue;
    private javax.swing.JPanel NetBalancePanel;
    private javax.swing.JButton RapportButton;
    private javax.swing.JLabel RapportFiltreAfficherTout;
    private javax.swing.JComboBox<String> RapportFiltreAfficherToutCombox;
    private javax.swing.JLabel RapportFiltreAnneeLabel;
    private javax.swing.JSpinner RapportFiltreAnneeSpinner;
    private javax.swing.JButton RapportFiltreGenererRapportButton;
    private javax.swing.JComboBox<String> RapportFiltreMoisCombox;
    private javax.swing.JLabel RapportFiltreMoisLabel;
    private javax.swing.JPanel RapportMensuelPanel;
    private javax.swing.JPanel ReportFilterPanel;
    private javax.swing.JPanel ReportSummaryPanel;
    private javax.swing.JLabel RevenueDateLabel;
    private javax.swing.JTextField RevenueDescriptionField;
    private javax.swing.JLabel RevenueDescriptionLabel;
    private javax.swing.JTextField RevenueMontantField;
    private javax.swing.JLabel RevenueMontantLabel;
    private javax.swing.JTextField RevenueSourceField;
    private javax.swing.JLabel RevenueSourceLabel;
    private javax.swing.JPanel Sidebar;
    private javax.swing.JButton TableBordButton;
    private javax.swing.JTable TableRapport;
    private javax.swing.JScrollPane TableRapportScroll;
    private javax.swing.JPanel TableauBordPanel;
    private javax.swing.JLabel TotalDepenseLabel;
    private javax.swing.JLabel TotalDepenseMontantLabel;
    private javax.swing.JPanel TotalExpensesPanel;
    private javax.swing.JLabel TotalRevenueLabel;
    private javax.swing.JLabel TotalRevenueMontantLabel;
    private javax.swing.JPanel TotalRevenuePanel;
    private javax.swing.JLabel WelcomeLabel;
    private javax.swing.JPanel actualChartAreaPanel;
    private javax.swing.JPanel annualChartViewPanel;
    private javax.swing.JPanel chartDisplayPanel;
    private javax.swing.JPanel chartFilterPanel;
    private javax.swing.JPanel reportTopAreaPanel;
    // End of variables declaration//GEN-END:variables
}
