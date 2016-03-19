/*
 * Copyright (C) 2015 Manavai
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package stri.ss_manager.SS_Manager_IHM;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Scanner;
import stri.ss_manager.SNMP.smi.OID;
import stri.ss_manager.SNMP.smi.VarBind;
import stri.ss_manager.SNMPKernel.*;

import stri.ss_manager.SNMPMessage.SNMPMessage;
import stri.ss_manager.SNMPMessage.payload.SNMPMessagePayload;

/**
 *
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * 
 *  * Cette classe permet de tester le Manager SNMP en initialisant
 * les Threads nécessaire et en créant des messages de tests.
 * 
 * @version 4
 */
public class ManagerIHM extends java.awt.Frame {
    
    private boolean BLINKTRAPBOUTON;

    /**
     * Creates new form ManagerIHM
     * @param snmpHandler
     */
    public ManagerIHM(SNMPHandler snmpHandler) {
        initComponents();
        //
        this.setVisible(true);
        //
        this.snmpHandler = snmpHandler;
        // désactivation des boutons
        this.GetBouton.setEnabled(false);
        this.GetNextBouton.setEnabled(false);
        this.SetBouton.setEnabled(false);
    }
    
    // méthodes non générées
    public static boolean validate(final String ip) { // fonction pour validation addresse IPV4
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        return ip.matches(PATTERN);
    }
    
    private void setOidandValueOnIhmByVarBin(VarBind vb){
        // OID
        this.SetOIDField.setText(vb.getObjectId().getObjectIdStringFormat());
        // Value
        this.ValueField.setText(new String(vb.getObjectValue()));
        
    }
    
    /**
     * A la réception d'un TRAP SNMP, se bouton servira à faire
     * clignoter le bouton TRAP SNMP
     * @param trap
     */
    public void blinkTrapBouton(SNMPMessage trap){
        
        // activation du bouton
        this.trap_bouton.setEnabled(true);        
        //
        this.BLINKTRAPBOUTON = true;
        while(this.BLINKTRAPBOUTON){ // tant qu'on a pas cliqué sur le bouton
           //
            try{
                this.trap_bouton.setBackground(Color.red);
                sleep(500); // 0.5s
                this.trap_bouton.setBackground(Color.gray);
                sleep(500);
            }catch(Exception e){
                // affichage d'un message d'erreur dans la console
                System.err.println("[IHM.BlinkTrapBouton]: ERROR --> "+e.getMessage());
            }
        }
        
        // affichage du message trap
        new TrapDisplayer(trap);
        
    }
    /**
     * Cette fonction peremt de résoudre le nom de l'v_oid en allant
 interroger les mibs chargées.
     * 
     * Ex: 1.3.6.1.2.1.1.5.0 ==> sysName
     * 
     * @param oid v_oid à résoudre
     * @return le nom de l'v_oid
     */
    private String oidLookUp(OID oid){
         // ouverture du fichier mib
        try{
            BufferedReader br=new BufferedReader(
                                new InputStreamReader(
                                    new FileInputStream(this.mibFile)));
            String ligne;
            //                // variable utilisé pour les erreurs de mathcing
            while ((ligne=br.readLine())!=null){ // lecture lgine par ligne
                //System.out.println(ligne);
                if(ligne.charAt(0) == '#'){      // ligne de commentaire
                    // Ignoré
                }else{                           // ligne normale
                    //
                    Scanner scanner = new Scanner(ligne);
                    //
                    String v_oid       = scanner.next();
                    //
                    String objectName  = scanner.next();
                    // matching...
                    String temp = SetOIDField.getText();
                    if(temp.matches(v_oid)){
                        System.out.println("[IHM.oidLookUp()]: "+v_oid+" ("+objectName+")");
                        //fermeture des flux
                        scanner.close();
                        br.close(); 
                        return objectName;
                    }
                    // fermeture scanner
                    scanner.close();
                    // s'il n'y a aucun match, on générera une erreur.
                }
            }       
            br.close(); 
        } catch (Exception e){
            //
            System.err.println("[IHM.oidLookUp()]: ERROR --> "+e.toString());
        }
        return "Void";
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        main_panel = new javax.swing.JPanel();
        label_logo_stri = new javax.swing.JLabel();
        label_logo_UPSSITECH = new javax.swing.JLabel();
        ManagerPanel = new javax.swing.JTabbedPane();
        AgentPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        ValiderConfiguration = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        AddressIPField = new javax.swing.JTextField();
        choixversionSNMP = new javax.swing.JComboBox();
        CommunauteField = new javax.swing.JTextField();
        resultatValidationIP = new javax.swing.JLabel();
        SNMPPanel = new javax.swing.JPanel();
        label_soft_name = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        SetBouton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        network_tree = new javax.swing.JTree();
        jLabel1 = new javax.swing.JLabel();
        SetOIDField = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        LogTextArea = new javax.swing.JTextArea();
        ClearFieldBouton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        ValueField = new javax.swing.JTextField();
        oid_name_label = new javax.swing.JLabel();
        trap_bouton = new javax.swing.JButton();
        GetBouton = new javax.swing.JButton();
        GetNextBouton = new javax.swing.JButton();
        rmi_panel = new javax.swing.JPanel();
        rmi_getBouton = new javax.swing.JButton();
        rmi_getnextBouton = new javax.swing.JButton();
        rmi_setBouton = new javax.swing.JButton();
        rmi_oid_TextField = new javax.swing.JTextField();
        rmi_value_TextField = new javax.swing.JTextField();
        rmi_oid_label = new javax.swing.JLabel();
        rmi_oidValue_label = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        rmi_console = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        rmi_remote_manager_ip = new javax.swing.JTextField();
        rmi_connect_bouton = new javax.swing.JButton();
        rmi_state_label = new javax.swing.JLabel();
        rmi_label = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        main_panel.setBackground(new java.awt.Color(153, 153, 153));

        label_logo_stri.setAutoscrolls(true);

        ManagerPanel.setToolTipText("");

        ValiderConfiguration.setText("Valider");
        ValiderConfiguration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ValiderConfigurationActionPerformed(evt);
            }
        });

        jLabel8.setText("Adresse IP");

        jLabel9.setText("Communauté");

        jLabel10.setText("Version SNMP");

        AddressIPField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddressIPFieldActionPerformed(evt);
            }
        });

        choixversionSNMP.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "version 1", "version 2" }));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(jLabel10)
                                .addGap(49, 49, 49))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addGap(51, 51, 51))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(choixversionSNMP, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(AddressIPField, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CommunauteField, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(resultatValidationIP))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(ValiderConfiguration, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddressIPField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(resultatValidationIP))
                .addGap(25, 25, 25)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CommunauteField, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(jLabel9))
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(choixversionSNMP, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(34, 34, 34)
                .addComponent(ValiderConfiguration, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        javax.swing.GroupLayout AgentPanelLayout = new javax.swing.GroupLayout(AgentPanel);
        AgentPanel.setLayout(AgentPanelLayout);
        AgentPanelLayout.setHorizontalGroup(
            AgentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AgentPanelLayout.createSequentialGroup()
                .addGap(253, 253, 253)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(425, Short.MAX_VALUE))
        );
        AgentPanelLayout.setVerticalGroup(
            AgentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AgentPanelLayout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(179, Short.MAX_VALUE))
        );

        ManagerPanel.addTab("Configuration", AgentPanel);

        label_soft_name.setFont(new java.awt.Font("Ti92Pluspc", 1, 36)); // NOI18N
        label_soft_name.setText("STRI SNMP Manager v2");

        jButton1.setText("Charger MIB");
        jButton1.setActionCommand("ChargerMIB");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Decharger MIB");
        jButton2.setActionCommand("DechargerMIB");
        jButton2.setEnabled(false);

        SetBouton.setText("Set");
        SetBouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SetBoutonActionPerformed(evt);
            }
        });

        network_tree.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 153, 0), 1, true));
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Networks");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("192.168.1.0/24");
        javax.swing.tree.DefaultMutableTreeNode treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Agent name A");
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Agent name B");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("192.168.2.0/24");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("...");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("192.168.3.0/24");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("...");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Manager");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Manager A");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Agent SNMP géré");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Manager B");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Agent SNMP géré");
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        network_tree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        network_tree.setDragEnabled(true);
        network_tree.setEditable(true);
        network_tree.setEnabled(false);
        network_tree.setRootVisible(false);
        jScrollPane1.setViewportView(network_tree);

        jLabel1.setText("OID");

        SetOIDField.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        SetOIDField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SetOIDFieldMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SetOIDFieldMouseExited(evt);
            }
        });
        SetOIDField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SetOIDFieldActionPerformed(evt);
            }
        });

        LogTextArea.setBackground(new java.awt.Color(0, 0, 0));
        LogTextArea.setColumns(20);
        LogTextArea.setForeground(new java.awt.Color(0, 204, 0));
        LogTextArea.setRows(5);
        LogTextArea.setText("Affichages ici");
        jScrollPane2.setViewportView(LogTextArea);

        ClearFieldBouton.setBackground(new java.awt.Color(153, 153, 153));
        ClearFieldBouton.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ClearFieldBouton.setText("CLEAR");
        ClearFieldBouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearFieldBoutonActionPerformed(evt);
            }
        });

        jLabel5.setText("Valeur");

        ValueField.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        ValueField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ValueFieldMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ValueFieldMouseExited(evt);
            }
        });
        ValueField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ValueFieldActionPerformed(evt);
            }
        });

        oid_name_label.setText("void");

        trap_bouton.setBackground(new java.awt.Color(204, 204, 204));
        trap_bouton.setText("NEW TRAP");
        trap_bouton.setEnabled(false);
        trap_bouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trap_boutonActionPerformed(evt);
            }
        });

        GetBouton.setText("Get");
        GetBouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GetBoutonActionPerformed(evt);
            }
        });

        GetNextBouton.setText("GetNext");
        GetNextBouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GetNextBoutonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SNMPPanelLayout = new javax.swing.GroupLayout(SNMPPanel);
        SNMPPanel.setLayout(SNMPPanelLayout);
        SNMPPanelLayout.setHorizontalGroup(
            SNMPPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SNMPPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SNMPPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(SNMPPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(label_soft_name)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(SNMPPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(SNMPPanelLayout.createSequentialGroup()
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(GetBouton, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(GetNextBouton, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(SetBouton, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(SNMPPanelLayout.createSequentialGroup()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(SNMPPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(SNMPPanelLayout.createSequentialGroup()
                                    .addGap(27, 27, 27)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 602, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(SNMPPanelLayout.createSequentialGroup()
                                    .addGap(24, 24, 24)
                                    .addGroup(SNMPPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SNMPPanelLayout.createSequentialGroup()
                                            .addComponent(jLabel1)
                                            .addGap(18, 18, 18))
                                        .addGroup(SNMPPanelLayout.createSequentialGroup()
                                            .addComponent(jLabel5)
                                            .addGap(10, 10, 10)))
                                    .addGroup(SNMPPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(SNMPPanelLayout.createSequentialGroup()
                                            .addComponent(ValueField, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(ClearFieldBouton, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(2, 2, 2))
                                        .addGroup(SNMPPanelLayout.createSequentialGroup()
                                            .addComponent(SetOIDField, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(oid_name_label, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(trap_bouton, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
                .addContainerGap(89, Short.MAX_VALUE))
        );
        SNMPPanelLayout.setVerticalGroup(
            SNMPPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SNMPPanelLayout.createSequentialGroup()
                .addGroup(SNMPPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SNMPPanelLayout.createSequentialGroup()
                        .addGroup(SNMPPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(SNMPPanelLayout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(SNMPPanelLayout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(label_soft_name, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(SNMPPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(SNMPPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(GetBouton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(GetNextBouton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(SetBouton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(SNMPPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(SNMPPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SNMPPanelLayout.createSequentialGroup()
                        .addGroup(SNMPPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(SNMPPanelLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(SNMPPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(SetOIDField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1)
                                    .addComponent(oid_name_label, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(SNMPPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(trap_bouton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(SNMPPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ValueField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(ClearFieldBouton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addGap(3, 3, 3))
        );

        SetOIDField.getAccessibleContext().setAccessibleName("GetValueField");
        jLabel2.getAccessibleContext().setAccessibleName("wIcon");
        jLabel5.getAccessibleContext().setAccessibleName("ValeurOID");

        ManagerPanel.addTab("SNMP", SNMPPanel);
        SNMPPanel.getAccessibleContext().setAccessibleName("SNMP");

        rmi_panel.setEnabled(false);

        rmi_getBouton.setText("GET");
        rmi_getBouton.setEnabled(false);
        rmi_getBouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rmi_getBoutonActionPerformed(evt);
            }
        });

        rmi_getnextBouton.setText("GetNext");
        rmi_getnextBouton.setEnabled(false);
        rmi_getnextBouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rmi_getnextBoutonActionPerformed(evt);
            }
        });

        rmi_setBouton.setText("SET");
        rmi_setBouton.setEnabled(false);
        rmi_setBouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rmi_setBoutonActionPerformed(evt);
            }
        });

        rmi_oid_TextField.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        rmi_oid_TextField.setForeground(new java.awt.Color(204, 204, 204));
        rmi_oid_TextField.setText("1.3.X.X.X.X.X.X.0");
        rmi_oid_TextField.setEnabled(false);

        rmi_value_TextField.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        rmi_value_TextField.setForeground(new java.awt.Color(153, 153, 153));
        rmi_value_TextField.setText("Valeur de l'OID pour le SET");
        rmi_value_TextField.setEnabled(false);
        rmi_value_TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rmi_value_TextFieldActionPerformed(evt);
            }
        });

        rmi_oid_label.setText("OID");

        rmi_oidValue_label.setText("Value");

        rmi_console.setBackground(new java.awt.Color(0, 0, 0));
        rmi_console.setColumns(20);
        rmi_console.setForeground(new java.awt.Color(0, 255, 0));
        rmi_console.setRows(5);
        rmi_console.setText("RMI_CONSOLE > ...");
        jScrollPane3.setViewportView(rmi_console);

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));

        rmi_remote_manager_ip.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rmi_remote_manager_ip.setText("Remote Manager IP addr");
        rmi_remote_manager_ip.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rmi_remote_manager_ipMouseClicked(evt);
            }
        });
        rmi_remote_manager_ip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rmi_remote_manager_ipActionPerformed(evt);
            }
        });
        rmi_remote_manager_ip.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                rmi_remote_manager_ipKeyReleased(evt);
            }
        });

        rmi_connect_bouton.setText("CONNECT");
        rmi_connect_bouton.setEnabled(false);
        rmi_connect_bouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rmi_connect_boutonActionPerformed(evt);
            }
        });

        rmi_state_label.setBackground(new java.awt.Color(204, 0, 0));
        rmi_state_label.setText("DISCONNECTED");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rmi_connect_bouton, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rmi_remote_manager_ip, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(rmi_state_label, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(rmi_state_label, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(rmi_remote_manager_ip, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rmi_connect_bouton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(21, 21, 21))
        );

        rmi_label.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        rmi_label.setText("Remote Manager Interface");

        javax.swing.GroupLayout rmi_panelLayout = new javax.swing.GroupLayout(rmi_panel);
        rmi_panel.setLayout(rmi_panelLayout);
        rmi_panelLayout.setHorizontalGroup(
            rmi_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rmi_panelLayout.createSequentialGroup()
                .addGroup(rmi_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rmi_panelLayout.createSequentialGroup()
                        .addGroup(rmi_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(rmi_panelLayout.createSequentialGroup()
                                .addGap(89, 89, 89)
                                .addComponent(rmi_label, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(rmi_panelLayout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                        .addGroup(rmi_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rmi_getBouton, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rmi_setBouton, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rmi_getnextBouton, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(48, 48, 48)
                        .addGroup(rmi_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(rmi_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(rmi_oid_TextField)
                                .addComponent(rmi_value_TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rmi_panelLayout.createSequentialGroup()
                                .addComponent(rmi_oid_label, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(98, 98, 98))
                            .addGroup(rmi_panelLayout.createSequentialGroup()
                                .addGap(135, 135, 135)
                                .addComponent(rmi_oidValue_label))))
                    .addGroup(rmi_panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3)))
                .addGap(18, 18, 18))
        );
        rmi_panelLayout.setVerticalGroup(
            rmi_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rmi_panelLayout.createSequentialGroup()
                .addGroup(rmi_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rmi_panelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(rmi_oid_label, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(rmi_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rmi_getBouton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rmi_oid_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addComponent(rmi_getnextBouton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(rmi_oidValue_label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(rmi_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(rmi_value_TextField)
                            .addComponent(rmi_setBouton, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)))
                    .addGroup(rmi_panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(rmi_label, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(34, 34, 34)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                .addContainerGap())
        );

        ManagerPanel.addTab("A propos", rmi_panel);

        ManagerPanel.setSelectedIndex(1);

        javax.swing.GroupLayout main_panelLayout = new javax.swing.GroupLayout(main_panel);
        main_panel.setLayout(main_panelLayout);
        main_panelLayout.setHorizontalGroup(
            main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(main_panelLayout.createSequentialGroup()
                        .addComponent(label_logo_stri)
                        .addGap(0, 0, 0)
                        .addComponent(label_logo_UPSSITECH)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(ManagerPanel))
                .addContainerGap())
        );
        main_panelLayout.setVerticalGroup(
            main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ManagerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_logo_stri)
                    .addComponent(label_logo_UPSSITECH))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ManagerPanel.getAccessibleContext().setAccessibleName("SNMP");
        ManagerPanel.getAccessibleContext().setAccessibleDescription("SNMP");

        add(main_panel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

   // TRAP BOUTON
    private void trap_boutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trap_boutonActionPerformed
        // TODO add your handling code here:

        // désactivation du clignotement du bouton
        this.BLINKTRAPBOUTON = false;
        this.trap_bouton.setEnabled(false);
    }//GEN-LAST:event_trap_boutonActionPerformed

    private void ValueFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ValueFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ValueFieldActionPerformed

    private void ValueFieldMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ValueFieldMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_ValueFieldMouseExited

    private void ValueFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ValueFieldMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_ValueFieldMouseClicked

    private void ClearFieldBoutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearFieldBoutonActionPerformed
        SetOIDField.setText("");    //
        ValueField.setText("");     //
        LogTextArea.setText("");    //
    }//GEN-LAST:event_ClearFieldBoutonActionPerformed

    private void SetOIDFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SetOIDFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SetOIDFieldActionPerformed

    private void SetOIDFieldMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SetOIDFieldMouseExited
        // TODO add your handling code here:
        // TODO add your handling code here:
        if (SetOIDField.getText().isEmpty() == true) {
            SetOIDField.setText("Value to set");
        } else {
            // il y a une valeur dans le champs on va résoudre l'oid
            //
            oid_name_label.setText(oidLookUp(new OID(this.SetOIDField.getText())));
        }
    }//GEN-LAST:event_SetOIDFieldMouseExited

    private void SetOIDFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SetOIDFieldMouseClicked
        // TODO add your handling code here:
        // on sélectionne le text pour que l'utilisateur l'efface ou puisse le modifier
        SetOIDField.selectAll();
    }//GEN-LAST:event_SetOIDFieldMouseClicked
   // SET OK
    private void SetBoutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SetBoutonActionPerformed
        // TODO add your handling code here:
        // désactivation du bouton
        this.SetBouton.setEnabled(false);

        ArrayList<VarBind> varBindingsList = new ArrayList<>();
        //
        varBindingsList.add(new VarBind(new OID(this.SetOIDField.getText()), this.ValueField.getText().getBytes()));
        // reqID, pas d'erreur, pas d'erreurn, list varbind
        SNMPMessagePayload payload      = new SNMPMessagePayload(0X0F000001, 0, 0, varBindingsList);
        // création du message SNMP
        try{
            //
            InetAddress Receiver        = InetAddress.getByName(this.AddressIPField.getText());            //
            //
            SNMPMessage req_msg         = new SNMPMessage(null, Receiver, 161, 2, this.CommunauteField.getText().getBytes(), (byte) 0xA4, payload);
            // envoi de la requête
            SNMPMessage res_msg         = snmpHandler.sendGetNextRequest(req_msg);
            // affichage du résultat
            this.LogTextArea.setText(res_msg.toStringforIHM());
            //
            setOidandValueOnIhmByVarBin(res_msg.getPayload().getVarBindingsList().get(0));
        }catch(Exception e){
            this.SetBouton.setEnabled(true);
            System.err.println("[IHM_ERROR.SetBouton]: " +e.getMessage());
        }
        //
        this.SetBouton.setEnabled(true);
    }//GEN-LAST:event_SetBoutonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void AddressIPFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddressIPFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddressIPFieldActionPerformed

    private void ValiderConfigurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ValiderConfigurationActionPerformed

        InetAddress ipv4 = null;
        String versionSNMP;
        int version;
        byte[] Communautebytes;
        SNMPMessagePayload payload = null; // ?
        String AddressIPAverifier = AddressIPField.getText(); // recuperation de l'addresse IP
        if (validate(AddressIPAverifier) == true) {           // validation
            //réactivation des boutons
            this.GetBouton.setEnabled(true);
            this.GetNextBouton.setEnabled(true);
            this.SetBouton.setEnabled(true);
            // changement de tab
            ManagerPanel.setSelectedIndex(1);   // il aura des conditions avant de passer au tab SNMP
            //
            resultatValidationIP.setText("");
        } else {
            // on les désactive
            this.GetBouton.setEnabled(false);
            this.GetNextBouton.setEnabled(false);
            this.SetBouton.setEnabled(false);
            //JOptionPane.showMessageDialog (null, "Addresse IP invalide", "Attention", JOptionPane.WARNING_MESSAGE);
            resultatValidationIP.setText("Addresse IP invalide"); // warning si invalide
        }
        /*       String Communaute = CommunauteField.getText();        // recuperation de la communauté

        version = choixversionSNMP.getSelectedIndex() + 1;    // recuperation du numero de la version choisi selon l'index+1 car ca commence par 0. (ici on peux pas utiliser getSelectedValue())
        try {
            ipv4 = InetAddress.getByName(AddressIPAverifier); // conversion de l'adresse ip valide en InetAddress
        } catch (UnknownHostException ex) {
            Logger.getLogger(ManagerIHM.class.getName()).log(Level.SEVERE, null, ex);
        }
        Communautebytes = Communaute.getBytes(); // conversion du string communaute en byte[]
        SNMPMessage msg;
        msg = new SNMPMessage(ipv4, ipv4, 161, version, Communautebytes, (byte)0xA0, payload); // constructeur (probablement faux ici)*/

    }//GEN-LAST:event_ValiderConfigurationActionPerformed

    private void rmi_value_TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rmi_value_TextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rmi_value_TextFieldActionPerformed

    private void rmi_remote_manager_ipMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rmi_remote_manager_ipMouseClicked
        // TODO add your handling code here:
        // on sélectione le text contenu dans le champs
        this.rmi_remote_manager_ip.selectAll();
    }//GEN-LAST:event_rmi_remote_manager_ipMouseClicked

    private void rmi_getBoutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rmi_getBoutonActionPerformed
        // TODO add your handling code here:
        this.rmi_getBouton.setEnabled(false);

        ArrayList<VarBind> varBindingsList = new ArrayList<>();
        //
        varBindingsList.add(new VarBind(new OID(this.rmi_oid_TextField.getText()), null));
        // reqID, pas d'erreur, pas d'erreurn, list varbind
        SNMPMessagePayload payload      = new SNMPMessagePayload(0X0F000001, 0, 0, varBindingsList);
        // création du message SNMP
        try{
            //
            InetAddress Receiver        = InetAddress.getByName(this.AddressIPField.getText());            //
            //
            SNMPMessage req_msg         = new SNMPMessage(null, Receiver, 161, 2, this.CommunauteField.getText().getBytes(), (byte) 0xA0, payload);
            // envoi de la requête
            SNMPMessage res_msg         = snmpRemoteManagerInterface.sendSetRequest(req_msg);
            // affichage du résultat
            this.rmi_console.setText(res_msg.toStringforIHM());
            //
            setOidandValueOnIhmByVarBin(res_msg.getPayload().getVarBindingsList().get(0));
        }catch(Exception e){
            this.rmi_getBouton.setEnabled(true);
            System.err.println("[IHM_ERROR.RMI_GetBouton]: " +e.getMessage());
        }
        //
        this.rmi_getBouton.setEnabled(true);
    }//GEN-LAST:event_rmi_getBoutonActionPerformed

    private void rmi_remote_manager_ipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rmi_remote_manager_ipActionPerformed
        // TODO add your handling code here:
        if(this.validate(this.rmi_remote_manager_ip.getText())){
            // activation du bouton
            this.rmi_connect_bouton.setEnabled(true);
            //
            this.rmi_state_label.setText("DISCONNECTED");
        }else{
            this.rmi_connect_bouton.setEnabled(false);
            // affichage erreur
            this.rmi_state_label.setText("BAD IP FORMAT !");
        }
        
    }//GEN-LAST:event_rmi_remote_manager_ipActionPerformed

    private void rmi_remote_manager_ipKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rmi_remote_manager_ipKeyReleased
        // TODO add your handling code here:
        if(this.validate(this.rmi_remote_manager_ip.getText())){
            // activation du bouton
            this.rmi_connect_bouton.setEnabled(true);
            //
            this.rmi_state_label.setText("DISCONNECTED");
        }else{
            this.rmi_connect_bouton.setEnabled(false);
            // affichage erreur
            this.rmi_state_label.setText("BAD IP FORMAT !");
        }
    }//GEN-LAST:event_rmi_remote_manager_ipKeyReleased

    private void rmi_connect_boutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rmi_connect_boutonActionPerformed
        // TODO add your handling code here:
        // on tente de se connecter au remote agent via RMI:
        if(this.rmi_connect_bouton.getText().matches("CONNECT")){
            try{
                this.snmpRemoteManagerInterface = (SNMPRemoteManagerInterface) Naming.lookup("rmi://"+this.rmi_remote_manager_ip.getText()+"/RemoteManagerInterface");
                // affichage des messages
                System.out.println("[IHM_RMI]: CONNECTED TO REMOTE Manager !");
                //
                this.rmi_state_label.setText("CONNECTED");
                //
                this.rmi_connect_bouton.setText("DISCONNECT");
                // activation des boutons et des champs
                this.rmi_getBouton.setEnabled(true);
                this.rmi_getnextBouton.setEnabled(true);
                this.rmi_setBouton.setEnabled(true);
                this.rmi_oid_TextField.setEnabled(true);
                this.rmi_value_TextField.setEnabled(true);
                //
                this.rmi_console.setText("RMI_CONSOLE >");

            }catch(Exception e){
                System.out.println("[IHM_RMI_ERROR]: "+e.getMessage());
                //
                this.rmi_state_label.setText("FAILED to CONNECT");
                //
                this.rmi_connect_bouton.setText("CONNECT");
                // activation des boutons et des champs
                this.rmi_getBouton.setEnabled(false);
                this.rmi_getnextBouton.setEnabled(false);
                this.rmi_setBouton.setEnabled(false);
                this.rmi_oid_TextField.setEnabled(false);
                this.rmi_value_TextField.setEnabled(false);
                //
                this.rmi_console.setText("RMI_CONSOLE >...");
            }
            // on ajoute cet agent dans le jTree
        }else{
                System.out.println("[IHM_RMI]: DISCONNECTED FROM REMOTE Manager !");
                //
                this.rmi_state_label.setText("DISCONNECTED");
                //
                this.rmi_connect_bouton.setText("CONNECT");
                // activation des boutons et des champs
                this.rmi_getBouton.setEnabled(false);
                this.rmi_getnextBouton.setEnabled(false);
                this.rmi_setBouton.setEnabled(false);
                this.rmi_oid_TextField.setEnabled(false);
                this.rmi_value_TextField.setEnabled(false);
                //
                this.rmi_console.setText("RMI_CONSOLE >...");
                //
        }
        
        
    }//GEN-LAST:event_rmi_connect_boutonActionPerformed

    private void rmi_setBoutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rmi_setBoutonActionPerformed
        // TODO add your handling code here:
        // désactivation du bouton
        this.rmi_setBouton.setEnabled(false);

        ArrayList<VarBind> varBindingsList = new ArrayList<>();
        //
        varBindingsList.add(new VarBind(new OID(this.rmi_oid_TextField.getText()), this.rmi_value_TextField.getText().getBytes()));
        // reqID, pas d'erreur, pas d'erreurn, list varbind
        SNMPMessagePayload payload      = new SNMPMessagePayload(0X0F000001, 0, 0, varBindingsList);
        // création du message SNMP
        try{
            //
            InetAddress Receiver        = InetAddress.getByName(this.AddressIPField.getText());            //
            //
            SNMPMessage req_msg         = new SNMPMessage(null, Receiver, 161, 2, this.CommunauteField.getText().getBytes(), (byte) 0xA3, payload);
            // envoi de la requête
            SNMPMessage res_msg         = snmpRemoteManagerInterface.sendSetRequest(req_msg);
            // affichage du résultat
            this.rmi_console.setText(res_msg.toStringforIHM());
            //
            setOidandValueOnIhmByVarBin(res_msg.getPayload().getVarBindingsList().get(0));
        }catch(Exception e){
            this.rmi_setBouton.setEnabled(true);
            System.err.println("[IHM_ERROR.rmi_SetBouton]: " +e.getMessage());
        }
        //
        this.rmi_setBouton.setEnabled(true);
    }//GEN-LAST:event_rmi_setBoutonActionPerformed

    private void rmi_getnextBoutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rmi_getnextBoutonActionPerformed
        // TODO add your handling code here:
        this.rmi_getnextBouton.setEnabled(false);

        ArrayList<VarBind> varBindingsList = new ArrayList<>();
        //
        varBindingsList.add(new VarBind(new OID(this.rmi_oid_TextField.getText()), null));
        // reqID, pas d'erreur, pas d'erreurn, list varbind
        SNMPMessagePayload payload      = new SNMPMessagePayload(0X0F000001, 0, 0, varBindingsList);
        // création du message SNMP
        try{
            //
            InetAddress Receiver        = InetAddress.getByName(this.AddressIPField.getText());            //
            //
            SNMPMessage req_msg         = new SNMPMessage(null, Receiver, 161, 2, this.CommunauteField.getText().getBytes(), (byte) 0xA3, payload);
            // envoi de la requête
            SNMPMessage res_msg         = snmpRemoteManagerInterface.sendSetRequest(req_msg);
            // affichage du résultat
            this.rmi_console.setText(res_msg.toStringforIHM());
            //
            setOidandValueOnIhmByVarBin(res_msg.getPayload().getVarBindingsList().get(0));
        }catch(Exception e){
            this.rmi_getnextBouton.setEnabled(true);
            System.err.println("[IHM_ERROR.rmi_GetNextBouton]: " +e.getMessage());
        }
        //
        this.rmi_getnextBouton.setEnabled(true);
    }//GEN-LAST:event_rmi_getnextBoutonActionPerformed

    private void GetBoutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GetBoutonActionPerformed
        // TODO add your handling code here:
        // désactivation du bouton
        this.GetBouton.setEnabled(false);

        ArrayList<VarBind> varBindingsList = new ArrayList<>();
        //
        varBindingsList.add(new VarBind(new OID(this.SetOIDField.getText()), null));
        // reqID, pas d'erreur, pas d'erreurn, list varbind
        SNMPMessagePayload payload      = new SNMPMessagePayload(0X0F000001, 0, 0, varBindingsList);
        // création du message SNMP
        try{
            //
            InetAddress Receiver        = InetAddress.getByName(this.AddressIPField.getText());            //
            //
            SNMPMessage req_msg         = new SNMPMessage(null, Receiver, 161, 2, this.CommunauteField.getText().getBytes(), (byte) 0xA0, payload);
            // envoi de la requête
            SNMPMessage res_msg         = snmpHandler.sendGetNextRequest(req_msg);
            // affichage du résultat
            this.LogTextArea.setText(res_msg.toStringforIHM());
            //
            setOidandValueOnIhmByVarBin(res_msg.getPayload().getVarBindingsList().get(0));
        }catch(Exception e){
            this.GetBouton.setEnabled(true);
            System.err.println("[IHM_ERROR.GetBouton]: " +e.getMessage());
        }
        //
        this.GetBouton.setEnabled(true);
    }//GEN-LAST:event_GetBoutonActionPerformed

    private void GetNextBoutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GetNextBoutonActionPerformed
        // TODO add your handling code here:
        // désactivation du bouton
        this.GetNextBouton.setEnabled(false);

        ArrayList<VarBind> varBindingsList = new ArrayList<>();
        //
        varBindingsList.add(new VarBind(new OID(this.SetOIDField.getText()), null));
        // reqID, pas d'erreur, pas d'erreurn, list varbind
        SNMPMessagePayload payload      = new SNMPMessagePayload(0X0F000001, 0, 0, varBindingsList);
        // création du message SNMP
        try{
            //
            InetAddress Receiver        = InetAddress.getByName(this.AddressIPField.getText());            //
            //
            SNMPMessage req_msg         = new SNMPMessage(null, Receiver, 161, 2, this.CommunauteField.getText().getBytes(), (byte) 0xA1, payload);
            // envoi de la requête
            SNMPMessage res_msg         = snmpHandler.sendGetNextRequest(req_msg);
            // affichage du résultat
            this.LogTextArea.setText(res_msg.toStringforIHM());
            //
            setOidandValueOnIhmByVarBin(res_msg.getPayload().getVarBindingsList().get(0));
        }catch(Exception e){
            this.GetNextBouton.setEnabled(true);
            System.err.println("[IHM_ERROR.GetBouton]: " +e.getMessage());
        }
        //
        this.GetNextBouton.setEnabled(true);
    }//GEN-LAST:event_GetNextBoutonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AddressIPField;
    private javax.swing.JPanel AgentPanel;
    private javax.swing.JButton ClearFieldBouton;
    private javax.swing.JTextField CommunauteField;
    private javax.swing.JButton GetBouton;
    private javax.swing.JButton GetNextBouton;
    private javax.swing.JTextArea LogTextArea;
    private javax.swing.JTabbedPane ManagerPanel;
    private javax.swing.JPanel SNMPPanel;
    private javax.swing.JButton SetBouton;
    private javax.swing.JTextField SetOIDField;
    private javax.swing.JButton ValiderConfiguration;
    private javax.swing.JTextField ValueField;
    private javax.swing.JComboBox choixversionSNMP;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel label_logo_UPSSITECH;
    private javax.swing.JLabel label_logo_stri;
    private javax.swing.JLabel label_soft_name;
    private javax.swing.JPanel main_panel;
    private javax.swing.JTree network_tree;
    private javax.swing.JLabel oid_name_label;
    private javax.swing.JLabel resultatValidationIP;
    private javax.swing.JButton rmi_connect_bouton;
    private javax.swing.JTextArea rmi_console;
    private javax.swing.JButton rmi_getBouton;
    private javax.swing.JButton rmi_getnextBouton;
    private javax.swing.JLabel rmi_label;
    private javax.swing.JLabel rmi_oidValue_label;
    private javax.swing.JTextField rmi_oid_TextField;
    private javax.swing.JLabel rmi_oid_label;
    private javax.swing.JPanel rmi_panel;
    private javax.swing.JTextField rmi_remote_manager_ip;
    private javax.swing.JButton rmi_setBouton;
    private javax.swing.JLabel rmi_state_label;
    private javax.swing.JTextField rmi_value_TextField;
    private javax.swing.JButton trap_bouton;
    // End of variables declaration//GEN-END:variables

    private SNMPHandler snmpHandler;
    private SNMPRemoteManagerInterface snmpRemoteManagerInterface;
    
    // fichier mib
    
    private String mibFile = "./mib/SNMPv2.mib";
    
    // GETTER
    /**
     * Cette fonction permet d'obtenir les SNMPHandler du Manager
     * pour initier le service RMI.
     * 
     * @return SNMPHndler
     */
    public SNMPHandler getSnmpHandler() {
        return snmpHandler;
    }
    
    
}
