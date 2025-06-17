/*
 * Cliquez sur nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt pour modifier cette licence
 * Cliquez sur nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java pour modifier ce modèle
 */
package GestionFinance;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
/**
 * Fenêtre principale de l'application de gestion financière.
 * Cette classe gère l'interface utilisateur et les interactions avec le gestionnaire de finances.
 * Elle permet d'ajouter, modifier et supprimer des revenus et dépenses,
 * ainsi que de générer des rapports et des graphiques.
 * @author Safouan
 */




public class mainFrame extends javax.swing.JFrame {
    
    /** Gestionnaire de finances pour gérer les opérations financières */
    private GestionnaireFinance gestionnaireFinance;
    
    /** ID de la transaction sélectionnée dans le tableau */
    private int idTransactionSelectionnee = -1;
    /** Type de la transaction sélectionnée (Revenu ou Dépense) */
    private String typeTransactionSelectionne = "";
    
    /**
     * Initialise les sélecteurs de date pour les revenus et dépenses.
     * Configure les spinners et combobox pour les jours, mois et années.
     */
    private void initialiserSelecteurDate() {
    java.time.LocalDate today = java.time.LocalDate.now();
    int currentYear = today.getYear(); // <- Déclaration de 'currentYear'
    String[] months = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"}; // <- Déclaration de 'months'


    AnneeRevenue.setModel(new javax.swing.SpinnerNumberModel(currentYear, 1990, 2030, 1));
    AnneeDepense.setModel(new javax.swing.SpinnerNumberModel(currentYear, 1990, 2030, 1));
    
    AnneeRevenue.setEditor(new javax.swing.JSpinner.NumberEditor(AnneeRevenue, "#"));
    AnneeDepense.setEditor(new javax.swing.JSpinner.NumberEditor(AnneeDepense, "#"));
    RapportFiltreAnneeSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(RapportFiltreAnneeSpinner, "#"));
    ChartFiltreAnneeSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(ChartFiltreAnneeSpinner, "#"));

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


    // --- Configuration des contrôles de filtrage ---
    // Maintenant ce code peut trouver et utiliser 'currentYear' et 'months' sans erreur.
    RapportFiltreAnneeSpinner.setModel(new javax.swing.SpinnerNumberModel(currentYear, 1990, 2030, 1));
    RapportFiltreMoisCombox.setModel(new javax.swing.DefaultComboBoxModel<>(months));
    RapportFiltreMoisCombox.setSelectedIndex(today.getMonthValue() - 1);

    RapportFiltreAfficherToutCombox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tout", "Revenus", "Dépenses" }));
}
    
    /**
     * Génère un rapport financier basé sur les filtres sélectionnés.
     * Affiche les revenus et dépenses dans un tableau et calcule les totaux.
     */
    private void genererRapport() {
    try {
        int annee = (Integer) RapportFiltreAnneeSpinner.getValue();
        int mois = RapportFiltreMoisCombox.getSelectedIndex() + 1;
        String choixAffichage = (String) RapportFiltreAfficherToutCombox.getSelectedItem();

        java.util.List<Revenue> revenusMensuels = gestionnaireFinance.obtenirRevenusParMois(mois, annee);
        java.util.List<Depense> depensesMensuelles = gestionnaireFinance.obtenirDepensesParMois(mois, annee);

        double totalRevenus = revenusMensuels.stream().mapToDouble(GestionDepense::getMontant).sum();
        double totalDepenses = depensesMensuelles.stream().mapToDouble(GestionDepense::getMontant).sum();
        java.text.NumberFormat formatMonetaire = java.text.NumberFormat.getCurrencyInstance();
        TotalRevenueMontantLabel.setText(formatMonetaire.format(totalRevenus));
        TotalDepenseMontantLabel.setText(formatMonetaire.format(totalDepenses));
        BalanceMontantLabel.setText(formatMonetaire.format(totalRevenus - totalDepenses));

        String[] nomsColonnes = {"ID", "Type", "Description", "Date", "Montant", "Catégorie/Source"};
        javax.swing.table.DefaultTableModel modele = new javax.swing.table.DefaultTableModel(nomsColonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if ("Tout".equals(choixAffichage) || "Revenus".equals(choixAffichage)) {
            for (Revenue r : revenusMensuels) {
                String nomsSources = r.getSource().getNom();
                modele.addRow(new Object[]{r.getId(), "Revenu", r.getDescription(), r.getDateOperation(), r.getMontant(), nomsSources});
            }
        }

        if ("Tout".equals(choixAffichage) || "Dépenses".equals(choixAffichage)) {
            for (Depense d : depensesMensuelles) {
                modele.addRow(new Object[]{d.getId(), "Dépense", d.getDescription(), d.getDateOperation(), d.getMontant(), d.getCategorie().getNom()});
            }
        }

        TableRapport.setModel(modele);

        TableRapport.getColumnModel().getColumn(0).setMinWidth(0);
        TableRapport.getColumnModel().getColumn(0).setMaxWidth(0);
        TableRapport.getColumnModel().getColumn(0).setWidth(0);

    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Erreur lors de la génération du rapport: " + e.getMessage(), "Erreur de Rapport", javax.swing.JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
    
    /**
     * Affiche un graphique annuel des revenus et dépenses.
     * Crée un graphique à barres montrant l'évolution mensuelle.
     */
    private void afficherGraphiqueAnnuel() {
        try {
            int annee = (Integer) ChartFiltreAnneeSpinner.getValue();

            org.jfree.data.category.DefaultCategoryDataset dataset = new org.jfree.data.category.DefaultCategoryDataset();
            Rapport rapport = new Rapport(gestionnaireFinance.obtenirTousRevenus(), gestionnaireFinance.obtenirToutesDepenses());

            String[] mois = {"Jan", "Fév", "Mar", "Avr", "Mai", "Juin", "Juil", "Août", "Sep", "Oct", "Nov", "Déc"};

            for (int moisIndex = 1; moisIndex <= 12; moisIndex++) {
                java.util.List<Revenue> revenusMensuels = rapport.getRevenuesMensuelle(moisIndex, annee);
                java.util.List<Depense> depensesMensuelles = rapport.getDepensesMensuelle(moisIndex, annee);

                double totalRevenus = rapport.calculerTotalRevenue(revenusMensuels);
                double totalDepenses = rapport.calculerTotalDepense(depensesMensuelles);

                dataset.addValue(totalRevenus, "Revenu", mois[moisIndex - 1]);
                dataset.addValue(totalDepenses, "Dépense", mois[moisIndex - 1]);
            }

            org.jfree.chart.JFreeChart graphiqueBarres = org.jfree.chart.ChartFactory.createBarChart(
                "Résumé Financier Annuel pour " + annee,
                "Mois",
                "Montant (€)",
                dataset,
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true, true, false);

            org.jfree.chart.plot.CategoryPlot graphique = graphiqueBarres.getCategoryPlot();
            graphique.getRenderer().setSeriesPaint(0, new java.awt.Color(50, 177, 74));
            graphique.getRenderer().setSeriesPaint(1, new java.awt.Color(231, 21, 21));
            graphique.setBackgroundPaint(java.awt.Color.white);

            org.jfree.chart.ChartPanel panneauGraphique = new org.jfree.chart.ChartPanel(graphiqueBarres);
            panneauGraphique.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
            panneauGraphique.setBackground(java.awt.Color.white);

            actualChartAreaPanel.removeAll();
            actualChartAreaPanel.setLayout(new java.awt.BorderLayout());
            actualChartAreaPanel.add(panneauGraphique, java.awt.BorderLayout.CENTER);
            actualChartAreaPanel.validate();

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Erreur lors de la création du graphique: " + e.getMessage(), "Erreur de Graphique", javax.swing.JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
     
    /**
     * Configure les listes déroulantes pour les catégories de dépenses et sources de revenus.
     * Initialise les valeurs prédéfinies pour ces listes.
     */
    private void configurerCombobox() {
        // Remplir la liste déroulante des catégories de dépenses
        javax.swing.DefaultComboBoxModel<CategorieDepense> modeleCategories = new javax.swing.DefaultComboBoxModel<>();
        for (CategorieDepense categorie : gestionnaireFinance.getCategories()) {
            modeleCategories.addElement(categorie);
        }
        DepenseCategorieField.setModel(modeleCategories);

        // Remplir la liste déroulante des sources de revenus
        javax.swing.DefaultComboBoxModel<SourceRevenue> modeleSources = new javax.swing.DefaultComboBoxModel<>();
        for (SourceRevenue source : gestionnaireFinance.getSourcesDeRevenu()) {
            modeleSources.addElement(source);
        }
        RevenueSourceField.setModel(modeleSources);
    }

    /**
     * Point d'entrée principal de l'application.
     * Configure l'apparence de l'interface et lance la fenêtre principale.
     */
    public static void main(String args[]) throws Exception { 
        
        javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        

    /* Créer et afficher le formulaire */
    java.awt.EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
            new mainFrame().setVisible(true);
        }
    });
}

    /**
     * Constructeur de la fenêtre principale.
     * Initialise les composants, charge les données et configure les écouteurs d'événements.
     */
    public mainFrame() {
        // Cette méthode NetBeans doit être appelée en premier
        initComponents();

        // Initialiser le gestionnaire de finances
        this.gestionnaireFinance = new GestionnaireFinance();
        
        // Configurer les ComboBoxes avec les données du gestionnaire
        configurerCombobox();
        
        // Charger les données existantes du fichier au démarrage
        gestionnaireFinance.chargerDonnees();
        
        // Rafraîchir le tableau avec les données chargées
        genererRapport();

        // Cet écouteur sauvegarde les données lorsque l'utilisateur clique sur le bouton 'X' pour fermer la fenêtre
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Fermeture de la fenêtre, tentative de sauvegarde...");
                gestionnaireFinance.sauvegarderDonnees();
                super.windowClosing(e);
            }
        });

        // Le reste du code du constructeur reste le même
    TableRapport.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
        @Override
        public void valueChanged(javax.swing.event.ListSelectionEvent evenement) {
            // S'assure que l'événement n'est pas déclenché pendant que la sélection est encore en train de changer,
            // et qu'une ligne est bien sélectionnée.
            if (!evenement.getValueIsAdjusting() && TableRapport.getSelectedRow() != -1) {

                // Récupère l'index de la ligne sélectionnée par l'utilisateur.
                int ligneSelectionnee = TableRapport.getSelectedRow();

                // Extrait les données de chaque cellule de la ligne sélectionnée à partir du modèle de la table.
                int id = (Integer) TableRapport.getValueAt(ligneSelectionnee, 0);
                String type = (String) TableRapport.getValueAt(ligneSelectionnee, 1);
                String description = (String) TableRapport.getValueAt(ligneSelectionnee, 2);
                java.time.LocalDate date = (java.time.LocalDate) TableRapport.getValueAt(ligneSelectionnee, 3);
                double montant = (Double) TableRapport.getValueAt(ligneSelectionnee, 4);
                // La colonne 5 contient soit une catégorie (pour une dépense), soit une source (pour un revenu).
                String categorieOuSource = (String) TableRapport.getValueAt(ligneSelectionnee, 5);

                // Mémorise l'ID et le type de la transaction pour que les boutons "Modifier" ou "Supprimer" puissent les utiliser.
                idTransactionSelectionnee = id;
                typeTransactionSelectionne = type;

                // Vide les deux formulaires pour éviter d'afficher des données obsolètes avant de remplir le bon.
                RevenueDescriptionField.setText("");
                RevenueMontantField.setText("");
                DepenseDescriptionField.setText("");
                DepenseMontantField.setText("");

                // Remplit le formulaire approprié en fonction du type de transaction.
                if ("Revenu".equals(type)) {
                    // Si la transaction est un revenu, on remplit le formulaire des revenus.
                    RevenueDescriptionField.setText(description);
                    RevenueMontantField.setText(String.valueOf(montant));
                    RevenueSourceField.setSelectedItem(categorieOuSource);

                    // Met à jour les sélecteurs de date pour le panneau des revenus.
                    AnneeRevenue.setValue(date.getYear());
                    MoisRevenue.setSelectedIndex(date.getMonthValue() - 1); // Les mois sont indexés de 0 à 11.
                    JoursRevenue.setValue(date.getDayOfMonth());

                } else if ("Dépense".equals(type)) {
                    // Si la transaction est une dépense, on remplit le formulaire des dépenses.
                    DepenseDescriptionField.setText(description);
                    DepenseMontantField.setText(String.valueOf(montant));
                    DepenseCategorieField.setSelectedItem(categorieOuSource);

                    // Met à jour les sélecteurs de date pour le panneau des dépenses.
                    AnneeDepense.setValue(date.getYear());
                    MoisDepense.setSelectedIndex(date.getMonthValue() - 1);
                    JoursDepense.setValue(date.getDayOfMonth());
                }
            }
        }
});
        
        /**RapportFiltreGenererRapportButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genererRapport();
            }
        });*/
        
        initialiserSelecteurDate();
    }

    /**
     * Cette méthode est appelée depuis le constructeur pour initialiser le formulaire.
     * ATTENTION: Ne PAS modifier ce code. Le contenu de cette méthode est toujours
     * régénéré par l'éditeur de formulaire.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        MenuPanel = new javax.swing.JPanel();
        AppLabel = new javax.swing.JLabel();
        TableBordButton = new javax.swing.JButton();
        GraphButton = new javax.swing.JButton();
        RapportButton = new javax.swing.JButton();
        BarreGauchePanel = new javax.swing.JPanel();
        AjouterRevenue = new javax.swing.JPanel();
        RevenueDescriptionLabel = new javax.swing.JLabel();
        RevenueDescriptionField = new javax.swing.JTextField();
        RevenueMontantLabel = new javax.swing.JLabel();
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
        RevenueSourceField = new javax.swing.JComboBox<>();
        AjouterDepense = new javax.swing.JPanel();
        DepenseDescriptionLabel = new javax.swing.JLabel();
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
        DepenseCategorieField = new javax.swing.JComboBox<>();
        EcranAffichage = new javax.swing.JPanel();
        TableauBordPanel = new javax.swing.JPanel();
        WelcomeLabel = new javax.swing.JLabel();
        RapportMensuelPanel = new javax.swing.JPanel();
        RapportNordPanel = new javax.swing.JPanel();
        FiltreRapportPanel = new javax.swing.JPanel();
        RapportFiltreAnneeLabel = new javax.swing.JLabel();
        RapportFiltreAnneeSpinner = new javax.swing.JSpinner();
        RapportFiltreMoisLabel = new javax.swing.JLabel();
        RapportFiltreMoisCombox = new javax.swing.JComboBox<>();
        RapportFiltreAfficherTout = new javax.swing.JLabel();
        RapportFiltreAfficherToutCombox = new javax.swing.JComboBox<>();
        RapportFiltreGenererRapportButton = new javax.swing.JButton();
        SommaireRapportPanel = new javax.swing.JPanel();
        TotalRevenuePanel = new javax.swing.JPanel();
        TotalRevenueLabel = new javax.swing.JLabel();
        TotalRevenueMontantLabel = new javax.swing.JLabel();
        TotalDepensePanel = new javax.swing.JPanel();
        TotalDepenseLabel = new javax.swing.JLabel();
        TotalDepenseMontantLabel = new javax.swing.JLabel();
        NetBalancePanel = new javax.swing.JPanel();
        BalanceLabel = new javax.swing.JLabel();
        BalanceMontantLabel = new javax.swing.JLabel();
        TableRapportScroll = new javax.swing.JScrollPane();
        TableRapport = new javax.swing.JTable();
        AfficherGraphAnnuellePanel = new javax.swing.JPanel();
        chartFilterPanel = new javax.swing.JPanel();
        ChartFiltreAnneeLabel = new javax.swing.JLabel();
        ChartFiltreAnneeSpinner = new javax.swing.JSpinner();
        ChartFiltreAfficherButton = new javax.swing.JButton();
        AfficherGraphPanel = new javax.swing.JPanel();
        actualChartAreaPanel = new javax.swing.JPanel();
        Footer = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));

        MenuPanel.setPreferredSize(new java.awt.Dimension(905, 60));
        java.awt.FlowLayout flowLayout3 = new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 30, 12);
        flowLayout3.setAlignOnBaseline(true);
        MenuPanel.setLayout(flowLayout3);

        AppLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        AppLabel.setText("Gestion Depenses et Revenues");
        MenuPanel.add(AppLabel);

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
        MenuPanel.add(TableBordButton);

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
        MenuPanel.add(GraphButton);

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
        MenuPanel.add(RapportButton);

        getContentPane().add(MenuPanel, java.awt.BorderLayout.PAGE_START);

        BarreGauchePanel.setBackground(new java.awt.Color(248, 248, 248));
        BarreGauchePanel.setPreferredSize(new java.awt.Dimension(400, 510));
        BarreGauchePanel.setLayout(new java.awt.GridLayout(2, 1));

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

        RevenueMontantField.setPreferredSize(new java.awt.Dimension(72, 22));
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, -20, 10);
        AjouterRevenue.add(RevenueSourceField, gridBagConstraints);

        BarreGauchePanel.add(AjouterRevenue);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, -20, 10);
        AjouterDepense.add(DepenseCategorieField, gridBagConstraints);

        BarreGauchePanel.add(AjouterDepense);

        getContentPane().add(BarreGauchePanel, java.awt.BorderLayout.LINE_START);

        EcranAffichage.setBackground(new java.awt.Color(248, 248, 248));
        EcranAffichage.setLayout(new java.awt.CardLayout());

        TableauBordPanel.setLayout(new java.awt.GridBagLayout());

        WelcomeLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        WelcomeLabel.setText("Bienvenue dans votre Application");
        TableauBordPanel.add(WelcomeLabel, new java.awt.GridBagConstraints());

        EcranAffichage.add(TableauBordPanel, "tableauBordView");
        TableauBordPanel.getAccessibleContext().setAccessibleName("");

        RapportMensuelPanel.setBackground(new java.awt.Color(248, 248, 248));
        RapportMensuelPanel.setLayout(new java.awt.BorderLayout());

        RapportNordPanel.setLayout(new javax.swing.BoxLayout(RapportNordPanel, javax.swing.BoxLayout.Y_AXIS));

        FiltreRapportPanel.setPreferredSize(new java.awt.Dimension(505, 40));
        FiltreRapportPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        RapportFiltreAnneeLabel.setText("Année");
        FiltreRapportPanel.add(RapportFiltreAnneeLabel);

        RapportFiltreAnneeSpinner.setMinimumSize(new java.awt.Dimension(67, 26));
        RapportFiltreAnneeSpinner.setName(""); // NOI18N
        RapportFiltreAnneeSpinner.setOpaque(true);
        RapportFiltreAnneeSpinner.setPreferredSize(new java.awt.Dimension(70, 26));
        FiltreRapportPanel.add(RapportFiltreAnneeSpinner);

        RapportFiltreMoisLabel.setText("Mois");
        FiltreRapportPanel.add(RapportFiltreMoisLabel);

        RapportFiltreMoisCombox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        FiltreRapportPanel.add(RapportFiltreMoisCombox);

        RapportFiltreAfficherTout.setText("Afficher");
        FiltreRapportPanel.add(RapportFiltreAfficherTout);

        RapportFiltreAfficherToutCombox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tout\t", "Revenues", "Depenses", " " }));
        RapportFiltreAfficherToutCombox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RapportFiltreAfficherToutComboxActionPerformed(evt);
            }
        });
        FiltreRapportPanel.add(RapportFiltreAfficherToutCombox);

        RapportFiltreGenererRapportButton.setText("Générer Rapport");
        RapportFiltreGenererRapportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RapportFiltreGenererRapportButtonActionPerformed(evt);
            }
        });
        FiltreRapportPanel.add(RapportFiltreGenererRapportButton);

        RapportNordPanel.add(FiltreRapportPanel);

        java.awt.FlowLayout flowLayout2 = new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 15);
        flowLayout2.setAlignOnBaseline(true);
        SommaireRapportPanel.setLayout(flowLayout2);

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

        SommaireRapportPanel.add(TotalRevenuePanel);

        TotalDepensePanel.setBackground(new java.awt.Color(253, 164, 164));
        TotalDepensePanel.setMaximumSize(new java.awt.Dimension(3500, 3500));
        TotalDepensePanel.setPreferredSize(new java.awt.Dimension(180, 80));
        TotalDepensePanel.setLayout(new java.awt.GridBagLayout());

        TotalDepenseLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        TotalDepenseLabel.setText("TOTAL DEPENSES");
        TotalDepensePanel.add(TotalDepenseLabel, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        TotalDepensePanel.add(TotalDepenseMontantLabel, gridBagConstraints);

        SommaireRapportPanel.add(TotalDepensePanel);

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

        SommaireRapportPanel.add(NetBalancePanel);

        RapportNordPanel.add(SommaireRapportPanel);

        RapportMensuelPanel.add(RapportNordPanel, java.awt.BorderLayout.NORTH);

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

        AfficherGraphAnnuellePanel.setLayout(new javax.swing.BoxLayout(AfficherGraphAnnuellePanel, javax.swing.BoxLayout.Y_AXIS));

        ChartFiltreAnneeLabel.setText("Année");
        chartFilterPanel.add(ChartFiltreAnneeLabel);

        ChartFiltreAnneeSpinner.setPreferredSize(new java.awt.Dimension(75, 26));
        chartFilterPanel.add(ChartFiltreAnneeSpinner);

        ChartFiltreAfficherButton.setText("Afficher Graph");
        ChartFiltreAfficherButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChartFiltreAfficherButtonActionPerformed(evt);
            }
        });
        chartFilterPanel.add(ChartFiltreAfficherButton);

        AfficherGraphAnnuellePanel.add(chartFilterPanel);

        AfficherGraphPanel.setLayout(new java.awt.BorderLayout());
        AfficherGraphPanel.add(actualChartAreaPanel, java.awt.BorderLayout.CENTER);

        AfficherGraphAnnuellePanel.add(AfficherGraphPanel);

        EcranAffichage.add(AfficherGraphAnnuellePanel, "GraphView");

        getContentPane().add(EcranAffichage, java.awt.BorderLayout.CENTER);

        Footer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Footer.setText("   ");
        getContentPane().add(Footer, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void RevenueDescriptionFieldActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO Ajouter votre code de gestion ici
    }

    private void RapportFiltreAfficherToutComboxActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO Ajouter votre code de gestion ici
    }

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
        genererRapport();
    }//GEN-LAST:event_RapportFiltreGenererRapportButtonActionPerformed

    private void ChartFiltreAfficherButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChartFiltreAfficherButtonActionPerformed
        // TODO add your handling code here:
        afficherGraphiqueAnnuel();
    }//GEN-LAST:event_ChartFiltreAfficherButtonActionPerformed


    private void SupprimerDepenseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SupprimerDepenseButtonActionPerformed
        // TODO Ajouter votre code de gestion ici

        if (idTransactionSelectionnee == -1 || !"Dépense".equals(typeTransactionSelectionne)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Veuillez sélectionner une dépense dans le tableau pour la supprimer.");
            return;
        }

        int confirmation = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Êtes-vous sûr de vouloir supprimer cette dépense ?",
            "Confirmer la suppression",
            javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (confirmation == javax.swing.JOptionPane.YES_OPTION) {
            gestionnaireFinance.supprimerDepenseParId(idTransactionSelectionnee);
            genererRapport(); // Rafraîchir le tableau

            // Effacer les champs du formulaire et réinitialiser la sélection
            DepenseDescriptionField.setText("");
            DepenseMontantField.setText("");
            DepenseCategorieField.setSelectedIndex(0); // Réinitialiser la liste déroulante au premier élément
            idTransactionSelectionnee = -1;

            javax.swing.JOptionPane.showMessageDialog(this, "Dépense supprimée avec succès.");
        }
    }//GEN-LAST:event_SupprimerDepenseButtonActionPerformed



    private void SupprimerRevenueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SupprimerRevenueButtonActionPerformed
        // TODO Ajouter votre code de gestion ici
        if (idTransactionSelectionnee == -1 || !"Revenu".equals(typeTransactionSelectionne)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Veuillez sélectionner un revenu dans le tableau pour le supprimer.");
            return;
        }

        int confirmation = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Êtes-vous sûr de vouloir supprimer ce revenu ?",
            "Confirmer la suppression",
            javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (confirmation == javax.swing.JOptionPane.YES_OPTION) {
            gestionnaireFinance.supprimerRevenuParId(idTransactionSelectionnee);
            genererRapport(); // Rafraîchir le tableau

            // Effacer les champs du formulaire et réinitialiser la sélection
            RevenueDescriptionField.setText("");
            RevenueMontantField.setText("");
            RevenueSourceField.setSelectedIndex(0); // Réinitialiser la liste déroulante au premier élément
            idTransactionSelectionnee = -1;

            javax.swing.JOptionPane.showMessageDialog(this, "Revenu supprimé avec succès.");
        }
    }//GEN-LAST:event_SupprimerRevenueButtonActionPerformed

    private void AjoutRevenueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AjoutRevenueButtonActionPerformed
        // TODO add your handling code here:
               try {
            String description = RevenueDescriptionField.getText();
            if (description.trim().isEmpty()) {
                throw new IllegalArgumentException("La description ne peut pas être vide.");
            }

            String montantText = RevenueMontantField.getText();
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

            SourceRevenue source = (SourceRevenue) RevenueSourceField.getSelectedItem();

            int jour = (Integer) JoursRevenue.getValue();
            int mois = MoisRevenue.getSelectedIndex() + 1;
            int annee = (Integer) AnneeRevenue.getValue();
            java.time.LocalDate dateOperation = java.time.LocalDate.of(annee, mois, jour);

            gestionnaireFinance.ajouterRevenu(description, dateOperation, montant, source);

            javax.swing.JOptionPane.showMessageDialog(this, "Revenu ajouté avec succès!", "Succès", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            RevenueDescriptionField.setText("");
            RevenueMontantField.setText("");

        } catch (IllegalArgumentException e) {
            javax.swing.JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur de Saisie", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Une erreur inattendue est survenue: " + e.getMessage(), "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_AjoutRevenueButtonActionPerformed

    private void AjoutDepenseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AjoutDepenseButtonActionPerformed
        // TODO add your handling code here:
              try {
            String description = DepenseDescriptionField.getText();
            if (description.trim().isEmpty()) {
                throw new IllegalArgumentException("La description ne peut pas être vide.");
            }

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

            CategorieDepense categorie = (CategorieDepense) DepenseCategorieField.getSelectedItem();

            int jour = (Integer) JoursDepense.getValue();
            int mois = MoisDepense.getSelectedIndex() + 1;
            int annee = (Integer) AnneeDepense.getValue();
            java.time.LocalDate dateOperation = java.time.LocalDate.of(annee, mois, jour);

            gestionnaireFinance.ajouterDepense(description, dateOperation, montant, categorie);

            javax.swing.JOptionPane.showMessageDialog(this, "Dépense ajoutée avec succès!", "Succès", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            DepenseDescriptionField.setText("");
            DepenseMontantField.setText("");

        } catch (IllegalArgumentException e) {
            javax.swing.JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur de Saisie", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Une erreur inattendue est survenue: " + e.getMessage(), "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

 
    }//GEN-LAST:event_AjoutDepenseButtonActionPerformed

    private void ModifierDepenseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifierDepenseButtonActionPerformed
        // TODO add your handling code here:
                if (idTransactionSelectionnee == -1 || !"Dépense".equals(typeTransactionSelectionne)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Veuillez sélectionner une dépense dans le tableau pour la modifier.");
            return;
        }

        try {
            String description = DepenseDescriptionField.getText();
            double montant = Double.parseDouble(DepenseMontantField.getText());
            java.time.LocalDate date = java.time.LocalDate.of(
                (Integer) AnneeDepense.getValue(),
                MoisDepense.getSelectedIndex() + 1,
                (Integer) JoursDepense.getValue()
            );

            CategorieDepense categorie = (CategorieDepense) DepenseCategorieField.getSelectedItem();

            gestionnaireFinance.modifierDepense(idTransactionSelectionnee, description, date, montant, categorie);
            genererRapport();
            javax.swing.JOptionPane.showMessageDialog(this, "Dépense mise à jour avec succès!");
            idTransactionSelectionnee = -1;

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Une erreur s'est produite : " + e.getMessage(), "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_ModifierDepenseButtonActionPerformed

    private void ModifierRevenueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifierRevenueButtonActionPerformed
        // TODO add your handling code here:
               if (idTransactionSelectionnee == -1 || !"Revenu".equals(typeTransactionSelectionne)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Veuillez sélectionner un revenu dans le tableau pour le modifier.");
            return;
        }

        try {
            String description = RevenueDescriptionField.getText();
            double montant = Double.parseDouble(RevenueMontantField.getText());
            java.time.LocalDate date = java.time.LocalDate.of(
                (Integer) AnneeRevenue.getValue(),
                MoisRevenue.getSelectedIndex() + 1,
                (Integer) JoursRevenue.getValue()
            );

            SourceRevenue source = (SourceRevenue) RevenueSourceField.getSelectedItem();

            gestionnaireFinance.modifierRevenue(idTransactionSelectionnee, description, date, montant, source);
            genererRapport();
            javax.swing.JOptionPane.showMessageDialog(this, "Revenu mis à jour avec succès!");
            idTransactionSelectionnee = -1;

        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Une erreur s'est produite : " + e.getMessage(), "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_ModifierRevenueButtonActionPerformed

    
    
    
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AfficherGraphAnnuellePanel;
    private javax.swing.JPanel AfficherGraphPanel;
    private javax.swing.JButton AjoutDepenseButton;
    private javax.swing.JButton AjoutRevenueButton;
    private javax.swing.JPanel AjouterDepense;
    private javax.swing.JPanel AjouterRevenue;
    private javax.swing.JSpinner AnneeDepense;
    private javax.swing.JSpinner AnneeRevenue;
    private javax.swing.JLabel AppLabel;
    private javax.swing.JLabel BalanceLabel;
    private javax.swing.JLabel BalanceMontantLabel;
    private javax.swing.JPanel BarreGauchePanel;
    private javax.swing.JButton ChartFiltreAfficherButton;
    private javax.swing.JLabel ChartFiltreAnneeLabel;
    private javax.swing.JSpinner ChartFiltreAnneeSpinner;
    private javax.swing.JPanel DatePanel;
    private javax.swing.JComboBox<CategorieDepense> DepenseCategorieField;
    private javax.swing.JLabel DepenseCategorieLabel;
    private javax.swing.JLabel DepenseDateLabel;
    private javax.swing.JPanel DepenseDatePanel;
    private javax.swing.JTextField DepenseDescriptionField;
    private javax.swing.JLabel DepenseDescriptionLabel;
    private javax.swing.JTextField DepenseMontantField;
    private javax.swing.JLabel DepenseMontantLabel;
    private javax.swing.JPanel EcranAffichage;
    private javax.swing.JPanel FiltreRapportPanel;
    private javax.swing.JLabel Footer;
    private javax.swing.JButton GraphButton;
    private javax.swing.JSpinner JoursDepense;
    private javax.swing.JSpinner JoursRevenue;
    private javax.swing.JPanel MenuPanel;
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
    private javax.swing.JPanel RapportNordPanel;
    private javax.swing.JPanel RevenueButtons;
    private javax.swing.JPanel RevenueButtons1;
    private javax.swing.JLabel RevenueDateLabel;
    private javax.swing.JTextField RevenueDescriptionField;
    private javax.swing.JLabel RevenueDescriptionLabel;
    private javax.swing.JTextField RevenueMontantField;
    private javax.swing.JLabel RevenueMontantLabel;
    private javax.swing.JComboBox<SourceRevenue> RevenueSourceField;
    private javax.swing.JLabel RevenueSourceLabel;
    private javax.swing.JPanel SommaireRapportPanel;
    private javax.swing.JButton SupprimerDepenseButton;
    private javax.swing.JButton SupprimerRevenueButton;
    private javax.swing.JButton TableBordButton;
    private javax.swing.JTable TableRapport;
    private javax.swing.JScrollPane TableRapportScroll;
    private javax.swing.JPanel TableauBordPanel;
    private javax.swing.JLabel TotalDepenseLabel;
    private javax.swing.JLabel TotalDepenseMontantLabel;
    private javax.swing.JPanel TotalDepensePanel;
    private javax.swing.JLabel TotalRevenueLabel;
    private javax.swing.JLabel TotalRevenueMontantLabel;
    private javax.swing.JPanel TotalRevenuePanel;
    private javax.swing.JLabel WelcomeLabel;
    private javax.swing.JPanel actualChartAreaPanel;
    private javax.swing.JPanel chartFilterPanel;
    // End of variables declaration//GEN-END:variables
}
