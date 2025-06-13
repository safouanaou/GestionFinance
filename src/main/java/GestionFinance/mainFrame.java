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
    
    private int selectedTransactionId = -1;
    private String selectedTransactionType = "";
    
    private void initializeDatePickers() {
    java.time.LocalDate today = java.time.LocalDate.now();
    int currentYear = today.getYear(); // <- Declaration of 'currentYear'
    String[] months = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"}; // <- Declaration of 'months'


    AnneeRevenue.setModel(new javax.swing.SpinnerNumberModel(currentYear, 1990, 2030, 1));
    AnneeDepense.setModel(new javax.swing.SpinnerNumberModel(currentYear, 1990, 2030, 1));

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

    RapportFiltreAfficherToutCombox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tout", "Revenues", "Depenses" }));
}
    
    private void generateReport() {
    try {
        int year = (Integer) RapportFiltreAnneeSpinner.getValue();
        int month = RapportFiltreMoisCombox.getSelectedIndex() + 1;
        String showChoice = (String) RapportFiltreAfficherToutCombox.getSelectedItem();

        java.util.List<Revenue> monthlyRevenues = gestionnaireFinance.getRevenuesByMonth(month, year);
        java.util.List<Depense> monthlyExpenses = gestionnaireFinance.getDepensesByMonth(month, year);

        double totalRevenue = monthlyRevenues.stream().mapToDouble(GestionDepense::getMontant).sum();
        double totalExpense = monthlyExpenses.stream().mapToDouble(GestionDepense::getMontant).sum();
        java.text.NumberFormat currencyFormat = java.text.NumberFormat.getCurrencyInstance();
        TotalRevenueMontantLabel.setText(currencyFormat.format(totalRevenue));
        TotalDepenseMontantLabel.setText(currencyFormat.format(totalExpense));
        BalanceMontantLabel.setText(currencyFormat.format(totalRevenue - totalExpense));

        // --- MODIFICATION IS HERE ---
        String[] columnNames = {"ID", "Type", "Description", "Date", "Montant", "Catégorie/Source"};
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columnNames, 0) {
            // Make table cells non-editable by default
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if ("Tout".equals(showChoice) || "Revenues".equals(showChoice)) {
            for (Revenue r : monthlyRevenues) {
                String sourceNames = r.getSource().stream().map(SourceRevenue::getName).collect(java.util.stream.Collectors.joining(", "));
                model.addRow(new Object[]{r.getId(), "Revenu", r.getDescription(), r.getDateOperation(), r.getMontant(), sourceNames});
            }
        }

        if ("Tout".equals(showChoice) || "Dépenses".equals(showChoice)) {
            for (Depense d : monthlyExpenses) {
                model.addRow(new Object[]{d.getId(), "Dépense", d.getDescription(), d.getDateOperation(), d.getMontant(), d.getCategorie().getName()});
            }
        }

        TableRapport.setModel(model);

        // Hide the ID column
        TableRapport.getColumnModel().getColumn(0).setMinWidth(0);
        TableRapport.getColumnModel().getColumn(0).setMaxWidth(0);
        TableRapport.getColumnModel().getColumn(0).setWidth(0);
        // --- END OF MODIFICATION ---

    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage(), "Report Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
    
     private void displayYearlyChart() {
        try {
            // 1. Get the selected year from the spinner
            int year = (Integer) ChartFiltreAnneeSpinner.getValue();

            // 2. Create a dataset for the bar chart
            org.jfree.data.category.DefaultCategoryDataset dataset = new org.jfree.data.category.DefaultCategoryDataset();
            Rapport rapport = new Rapport(gestionnaireFinance.getAllRevenues(), gestionnaireFinance.getAllDepenses());

            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

            // 3. Loop through all 12 months to get the data
            for (int month = 1; month <= 12; month++) {
                java.util.List<Revenue> monthlyRevenues = rapport.getMonthlyRevenues(month, year);
                java.util.List<Depense> monthlyExpenses = rapport.getMonthlyExpenses(month, year);

                double totalRevenue = rapport.calculateTotalRevenue(monthlyRevenues);
                double totalExpense = rapport.calculateTotalExpense(monthlyExpenses);

                // Add the data to the dataset
                dataset.addValue(totalRevenue, "Revenu", months[month - 1]);
                dataset.addValue(totalExpense, "Dépense", months[month - 1]);
            }

            // 4. Create the bar chart object
            org.jfree.chart.JFreeChart barChart = org.jfree.chart.ChartFactory.createBarChart(
                "Yearly Financial Summary for " + year, // Chart title
                "Month",                               // X-axis label
                "Amount ($)",                          // Y-axis label
                dataset,                               // Dataset
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true, true, false);                    //  legend, tooltips, URLs

            // 5. Customize the chart's appearance
            org.jfree.chart.plot.CategoryPlot plot = barChart.getCategoryPlot();
            plot.getRenderer().setSeriesPaint(0, new java.awt.Color(50, 177, 74));  // Color for "Revenu"
            plot.getRenderer().setSeriesPaint(1, new java.awt.Color(231, 21, 21));   // Color for "Dépense"
            plot.setBackgroundPaint(java.awt.Color.white);

            // 6. Create a ChartPanel to display the chart in Swing
            org.jfree.chart.ChartPanel chartPanel = new org.jfree.chart.ChartPanel(barChart);
            chartPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
            chartPanel.setBackground(java.awt.Color.white);

            // 7. Add the chart panel to your designated display area
            actualChartAreaPanel.removeAll(); // Clear the previous content
            actualChartAreaPanel.setLayout(new java.awt.BorderLayout());
            actualChartAreaPanel.add(chartPanel, java.awt.BorderLayout.CENTER);
            actualChartAreaPanel.validate(); // Refresh the panel display

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error creating chart: " + e.getMessage(), "Chart Error", javax.swing.JOptionPane.ERROR_MESSAGE);
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
        
        
        // Place this code inside the public mainFrame() constructor

// This is the corrected code for your TableRapport listener
TableRapport.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
    @Override
    public void valueChanged(javax.swing.event.ListSelectionEvent event) {
        if (!event.getValueIsAdjusting() && TableRapport.getSelectedRow() != -1) {
            int selectedRow = TableRapport.getSelectedRow();

            selectedTransactionId = (int) TableRapport.getValueAt(selectedRow, 0);
            selectedTransactionType = (String) TableRapport.getValueAt(selectedRow, 1);
            String description = (String) TableRapport.getValueAt(selectedRow, 2);
            java.time.LocalDate date = (java.time.LocalDate) TableRapport.getValueAt(selectedRow, 3);
            double amount = (double) TableRapport.getValueAt(selectedRow, 4);
            String categoryOrSource = (String) TableRapport.getValueAt(selectedRow, 5);

            // Clear both forms
            RevenueDescriptionField.setText(""); RevenueMontantField.setText(""); RevenueSourceField.setText("");
            DepenseDescriptionField.setText(""); DepenseMontantField.setText(""); DepenseCategorieField.setText("");

            if ("Revenu".equals(selectedTransactionType)) {
                RevenueDescriptionField.setText(description);
                RevenueMontantField.setText(String.valueOf(amount));
                RevenueSourceField.setText(categoryOrSource);

                // --- THIS BLOCK IS FIXED ---
                // Use setValue() for the JSpinner instead of setSelectedItem()
                AnneeRevenue.setValue(date.getYear());
                MoisRevenue.setSelectedIndex(date.getMonthValue() - 1);
                JoursRevenue.setValue(date.getDayOfMonth());

            } else { // "Dépense"
                DepenseDescriptionField.setText(description);
                DepenseMontantField.setText(String.valueOf(amount));
                DepenseCategorieField.setText(categoryOrSource);

                // --- THIS BLOCK IS FIXED ---
                // Use setValue() for the JSpinner instead of setSelectedItem()
                AnneeDepense.setValue(date.getYear());
                MoisDepense.setSelectedIndex(date.getMonthValue() - 1);
                JoursDepense.setValue(date.getDayOfMonth());
            }
        }
    }
});
        
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
        RevenueDescriptionField = new javax.swing.JTextField();
        RevenueMontantLabel = new javax.swing.JLabel();
        RevenueSourceField = new javax.swing.JTextField();
        RevenueSourceLabel = new javax.swing.JLabel();
        RevenueDateLabel = new javax.swing.JLabel();
        DatePanel = new javax.swing.JPanel();
        JoursRevenue = new javax.swing.JSpinner();
        MoisRevenue = new javax.swing.JComboBox<>();
        AnneeRevenue = new javax.swing.JSpinner();
        RevenueMontantField = new javax.swing.JTextField();
        RevenueButtons = new javax.swing.JPanel();
        AjoutRevenueButton = new javax.swing.JButton();
        SupprimerRevenueButton = new javax.swing.JButton();
        ModifierRevenueButton = new javax.swing.JButton();
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
        AnneeDepense = new javax.swing.JSpinner();
        DepenseMontantField = new javax.swing.JTextField();
        RevenueButtons1 = new javax.swing.JPanel();
        AjoutDepenseButton = new javax.swing.JButton();
        SupprimerDepenseButton = new javax.swing.JButton();
        ModifierDepenseButton = new javax.swing.JButton();
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

        RevenueDescriptionField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RevenueDescriptionFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, -20, 10);
        AjouterRevenue.add(RevenueDescriptionField, gridBagConstraints);

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
        AjouterRevenue.add(RevenueSourceField, gridBagConstraints);

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

        AnneeRevenue.setPreferredSize(new java.awt.Dimension(70, 26));
        DatePanel.add(AnneeRevenue);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        AjouterRevenue.add(DatePanel, gridBagConstraints);

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

        RevenueButtons.setPreferredSize(new java.awt.Dimension(280, 40));

        AjoutRevenueButton.setBackground(new java.awt.Color(50, 177, 74));
        AjoutRevenueButton.setFont(new java.awt.Font("Helvetica", 1, 14)); // NOI18N
        AjoutRevenueButton.setForeground(new java.awt.Color(255, 255, 255));
        AjoutRevenueButton.setText("Ajouter");
        AjoutRevenueButton.setAlignmentY(0.0F);
        AjoutRevenueButton.setMargin(new java.awt.Insets(10, 5, 10, 5));
        AjoutRevenueButton.setPreferredSize(new java.awt.Dimension(100, 35));
        AjoutRevenueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AjoutRevenueButtonActionPerformed(evt);
            }
        });
        RevenueButtons.add(AjoutRevenueButton);

        SupprimerRevenueButton.setBackground(new java.awt.Color(231, 21, 21));
        SupprimerRevenueButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        SupprimerRevenueButton.setForeground(new java.awt.Color(255, 255, 255));
        SupprimerRevenueButton.setText("Supprimer");
        SupprimerRevenueButton.setMargin(new java.awt.Insets(10, 5, 10, 5));
        SupprimerRevenueButton.setPreferredSize(new java.awt.Dimension(100, 35));
        SupprimerRevenueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SupprimerRevenueButtonActionPerformed(evt);
            }
        });
        RevenueButtons.add(SupprimerRevenueButton);

        ModifierRevenueButton.setBackground(new java.awt.Color(255, 153, 51));
        ModifierRevenueButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ModifierRevenueButton.setForeground(new java.awt.Color(255, 255, 255));
        ModifierRevenueButton.setText("Modifier");
        ModifierRevenueButton.setMargin(new java.awt.Insets(10, 5, 10, 5));
        ModifierRevenueButton.setPreferredSize(new java.awt.Dimension(100, 35));
        ModifierRevenueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifierRevenueButtonActionPerformed(evt);
            }
        });
        RevenueButtons.add(ModifierRevenueButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        AjouterRevenue.add(RevenueButtons, gridBagConstraints);

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

        AnneeDepense.setPreferredSize(new java.awt.Dimension(70, 26));
        DepenseDatePanel.add(AnneeDepense);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        AjouterDepense.add(DepenseDatePanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, -20, 10);
        AjouterDepense.add(DepenseMontantField, gridBagConstraints);

        RevenueButtons1.setPreferredSize(new java.awt.Dimension(280, 40));

        AjoutDepenseButton.setBackground(new java.awt.Color(50, 177, 74));
        AjoutDepenseButton.setFont(new java.awt.Font("Helvetica", 1, 14)); // NOI18N
        AjoutDepenseButton.setForeground(new java.awt.Color(255, 255, 255));
        AjoutDepenseButton.setText("Ajouter");
        AjoutDepenseButton.setMargin(new java.awt.Insets(10, 5, 10, 5));
        AjoutDepenseButton.setPreferredSize(new java.awt.Dimension(100, 35));
        AjoutDepenseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AjoutDepenseButtonActionPerformed(evt);
            }
        });
        RevenueButtons1.add(AjoutDepenseButton);

        SupprimerDepenseButton.setBackground(new java.awt.Color(231, 21, 21));
        SupprimerDepenseButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        SupprimerDepenseButton.setForeground(new java.awt.Color(255, 255, 255));
        SupprimerDepenseButton.setText("Supprimer");
        SupprimerDepenseButton.setMargin(new java.awt.Insets(10, 5, 10, 5));
        SupprimerDepenseButton.setPreferredSize(new java.awt.Dimension(100, 35));
        SupprimerDepenseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SupprimerDepenseButtonActionPerformed(evt);
            }
        });
        RevenueButtons1.add(SupprimerDepenseButton);

        ModifierDepenseButton.setBackground(new java.awt.Color(255, 153, 51));
        ModifierDepenseButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ModifierDepenseButton.setForeground(new java.awt.Color(255, 255, 255));
        ModifierDepenseButton.setText("Modifier");
        ModifierDepenseButton.setMargin(new java.awt.Insets(10, 5, 10, 5));
        ModifierDepenseButton.setPreferredSize(new java.awt.Dimension(100, 35));
        ModifierDepenseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifierDepenseButtonActionPerformed(evt);
            }
        });
        RevenueButtons1.add(ModifierDepenseButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        AjouterDepense.add(RevenueButtons1, gridBagConstraints);

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

        RapportFiltreAfficherToutCombox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tout\t", "Revenues", "Depenses", " " }));
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

        ChartFiltreAnneeSpinner.setPreferredSize(new java.awt.Dimension(75, 26));
        chartFilterPanel.add(ChartFiltreAnneeSpinner);

        ChartFiltreAfficherButton.setText("Show Chart");
        ChartFiltreAfficherButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChartFiltreAfficherButtonActionPerformed(evt);
            }
        });
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

    private void RevenueDescriptionFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RevenueDescriptionFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RevenueDescriptionFieldActionPerformed

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
        int annee = (Integer) AnneeDepense.getValue(); // Changed from parsing a string
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

    private void MoisDepenseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MoisDepenseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MoisDepenseActionPerformed

    private void RapportFiltreGenererRapportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RapportFiltreGenererRapportButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RapportFiltreGenererRapportButtonActionPerformed

    private void DepenseCategorieFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DepenseCategorieFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DepenseCategorieFieldActionPerformed

    private void AjoutRevenueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AjoutRevenueButtonActionPerformed
        // TODO add your handling code here:

    try {
        // 1. Get the description from the text field.
        String description = RevenueSourceField.getText();
        if (description.trim().isEmpty()) {
            throw new IllegalArgumentException("La description ne peut pas être vide.");
        }

        // 2. Get and parse the amount from the text field.
        String montantText = RevenueMontantField.getText();
        double montant;

        if (montantText.trim().isEmpty()) {
            throw new IllegalArgumentException("Veuillez entrer un montant.");
        }
        try {
            // This handles different number formats like "1,234.56" or "1234,56"
            String cleanText = montantText.replaceAll("[^\\d,.]", "").replace(',', '.');
            montant = Double.parseDouble(cleanText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le montant entré n'est pas un nombre valide.");
        }

        // 3. Get the source from its text field.
        String sourceName = RevenueDescriptionField.getText();
        if (sourceName.trim().isEmpty()) {
            throw new IllegalArgumentException("La source ne peut pas être vide.");
        }
        // For Revenu, the source is a list. We will create a list with one source.
        java.util.List<SourceRevenue> sources = new java.util.ArrayList<>();
        sources.add(new SourceRevenue(sourceName, "Source de revenu"));

        // 4. Get the date from the revenue date pickers.
        int jour = (Integer) JoursRevenue.getValue();
        int mois = MoisRevenue.getSelectedIndex() + 1; // Add 1 because index is 0-based
        int annee = (Integer) AnneeRevenue.getValue(); // Changed from parsing a string
        java.time.LocalDate dateOperation = java.time.LocalDate.of(annee, mois, jour);

        // 5. Add the new revenue via the finance manager.
        gestionnaireFinance.addRevenu(description, dateOperation, montant, sources);

        // 7. Provide user feedback and clear the input fields for the next entry.
        javax.swing.JOptionPane.showMessageDialog(this, "Revenu ajouté avec succès!", "Succès", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        RevenueSourceField.setText("");
        RevenueMontantField.setText("");
        RevenueDescriptionField.setText("");

    } catch (IllegalArgumentException e) {
        // Show specific error messages for bad input.
        javax.swing.JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur de Saisie", javax.swing.JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        // Show a general error message for any other unexpected problems.
        javax.swing.JOptionPane.showMessageDialog(this, "Une erreur inattendue est survenue: " + e.getMessage(), "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
        e.printStackTrace(); // This is helpful for debugging.
    }
        
    }//GEN-LAST:event_AjoutRevenueButtonActionPerformed

    private void ModifierRevenueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifierRevenueButtonActionPerformed
        // TODO add your handling code here:
    if (selectedTransactionId == -1 || !"Revenu".equals(selectedTransactionType)) {
        javax.swing.JOptionPane.showMessageDialog(this, "Please select a revenue from the table to modify.");
        return;
    }

    try {
        String description = RevenueSourceField.getText();
        double montant = Double.parseDouble(RevenueMontantField.getText());
        java.time.LocalDate date = java.time.LocalDate.of(
            (Integer) AnneeRevenue.getValue(), // Changed from parsing a string
            MoisRevenue.getSelectedIndex() + 1,
            (Integer) JoursRevenue.getValue()
        );

        java.util.List<SourceRevenue> sources = new java.util.ArrayList<>();
        // --- THIS LINE IS FIXED ---
        sources.add(new SourceRevenue(RevenueDescriptionField.getText(), "Source de revenu")); // Changed "" to a placeholder

        gestionnaireFinance.modifierRevenue(selectedTransactionId, description, date, montant, sources);
        generateReport();
        javax.swing.JOptionPane.showMessageDialog(this, "Revenue updated successfully!");
        selectedTransactionId = -1;

    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_ModifierRevenueButtonActionPerformed

    private void ModifierDepenseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifierDepenseButtonActionPerformed
        // TODO add your handling code here:
     if (selectedTransactionId == -1 || !"Dépense".equals(selectedTransactionType)) {
        javax.swing.JOptionPane.showMessageDialog(this, "Please select an expense from the table to modify.");
        return;
    }

    try {
        String description = DepenseDescriptionField.getText();
        double montant = Double.parseDouble(DepenseMontantField.getText());
        java.time.LocalDate date = java.time.LocalDate.of(
             (Integer) AnneeDepense.getValue(), // Changed from parsing a string
            MoisDepense.getSelectedIndex() + 1,
            (Integer) JoursDepense.getValue()
        );

        // --- THIS LINE IS FIXED ---
        CategorieDepense categorie = new CategorieDepense(DepenseCategorieField.getText(), "Catégorie de dépense"); // Changed "" to a placeholder

        gestionnaireFinance.modifierDepense(selectedTransactionId, description, date, montant, categorie);
        generateReport();
        javax.swing.JOptionPane.showMessageDialog(this, "Expense updated successfully!");
        selectedTransactionId = -1;

    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_ModifierDepenseButtonActionPerformed

    private void SupprimerRevenueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SupprimerRevenueButtonActionPerformed
        // TODO add your handling code here:
        if (selectedTransactionId == -1 || !"Revenu".equals(selectedTransactionType)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select a revenue from the table to delete.");
            return;
        }

        // Ask the user for confirmation before deleting.
        int confirmation = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this revenue?",
            "Confirm Deletion",
            javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (confirmation == javax.swing.JOptionPane.YES_OPTION) {
            // If the user confirms, call the remove method.
            gestionnaireFinance.removeRevenuById(selectedTransactionId);

            // Refresh the table to show the change.
            generateReport();

            // Clear the form fields and reset the selection.
            RevenueDescriptionField.setText("");
            RevenueMontantField.setText("");
            RevenueSourceField.setText("");
            selectedTransactionId = -1;

            javax.swing.JOptionPane.showMessageDialog(this, "Revenue deleted successfully.");
        }
    }//GEN-LAST:event_SupprimerRevenueButtonActionPerformed

    private void SupprimerDepenseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SupprimerDepenseButtonActionPerformed
        // TODO add your handling code here:
        
        if (selectedTransactionId == -1 || !"Dépense".equals(selectedTransactionType)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please select an expense from the table to delete.");
            return;
        }

        // Ask the user for confirmation.
        int confirmation = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this expense?",
            "Confirm Deletion",
            javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (confirmation == javax.swing.JOptionPane.YES_OPTION) {
            // If the user confirms, call the remove method.
            gestionnaireFinance.removeDepenseById(selectedTransactionId);

            // Refresh the table.
            generateReport();

            // Clear the form fields and reset the selection.
            DepenseDescriptionField.setText("");
            DepenseMontantField.setText("");
            DepenseCategorieField.setText("");
            selectedTransactionId = -1;

            javax.swing.JOptionPane.showMessageDialog(this, "Expense deleted successfully.");
        }
    }//GEN-LAST:event_SupprimerDepenseButtonActionPerformed

    private void ChartFiltreAfficherButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChartFiltreAfficherButtonActionPerformed
        // TODO add your handling code here:
        displayYearlyChart();
    }//GEN-LAST:event_ChartFiltreAfficherButtonActionPerformed

    
    
    
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AjoutDepenseButton;
    private javax.swing.JButton AjoutRevenueButton;
    private javax.swing.JPanel AjouterDepense;
    private javax.swing.JPanel AjouterRevenue;
    private javax.swing.JSpinner AnneeDepense;
    private javax.swing.JSpinner AnneeRevenue;
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
    private javax.swing.JButton ModifierDepenseButton;
    private javax.swing.JButton ModifierRevenueButton;
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
    private javax.swing.JPanel RevenueButtons;
    private javax.swing.JPanel RevenueButtons1;
    private javax.swing.JLabel RevenueDateLabel;
    private javax.swing.JTextField RevenueDescriptionField;
    private javax.swing.JLabel RevenueDescriptionLabel;
    private javax.swing.JTextField RevenueMontantField;
    private javax.swing.JLabel RevenueMontantLabel;
    private javax.swing.JTextField RevenueSourceField;
    private javax.swing.JLabel RevenueSourceLabel;
    private javax.swing.JPanel Sidebar;
    private javax.swing.JButton SupprimerDepenseButton;
    private javax.swing.JButton SupprimerRevenueButton;
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
