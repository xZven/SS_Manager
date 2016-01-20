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
package SS_Manager_IHM;

/**
 *
 * @author Manavai
 */
public class ManagerIHM extends java.awt.Frame {

    /**
     * Creates new form ManagerIHM
     */
    public ManagerIHM() {
        initComponents();
      
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
        label_soft_name = new javax.swing.JLabel();
        label_logo_UPSSITECH = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        network_tree = new javax.swing.JTree();
        GetBouton = new javax.swing.JButton();
        GetNextBouton = new javax.swing.JButton();
        SetBouton = new javax.swing.JButton();
        GetValueField = new javax.swing.JTextField();
        SetValueField = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        LogTextArea = new javax.swing.JTextArea();
        ClearFieldBouton = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        main_panel.setBackground(new java.awt.Color(153, 153, 153));

        label_logo_stri.setAutoscrolls(true);

        label_soft_name.setFont(new java.awt.Font("Ti92Pluspc", 1, 36)); // NOI18N
        label_soft_name.setText("STRI SNMP Manager v1");

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

        GetBouton.setText("Get");

        GetNextBouton.setText("GetNext");

        SetBouton.setText("Set");

        SetValueField.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        SetValueField.setText("Value to set");
        SetValueField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SetValueFieldMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SetValueFieldMouseExited(evt);
            }
        });
        SetValueField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SetValueFieldActionPerformed(evt);
            }
        });

        LogTextArea.setBackground(new java.awt.Color(0, 0, 0));
        LogTextArea.setColumns(20);
        LogTextArea.setForeground(new java.awt.Color(0, 204, 0));
        LogTextArea.setRows(5);
        LogTextArea.setText("Affichages ici");
        jScrollPane2.setViewportView(LogTextArea);

        ClearFieldBouton.setBackground(new java.awt.Color(255, 0, 0));
        ClearFieldBouton.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ClearFieldBouton.setText("CLEAR");
        ClearFieldBouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearFieldBoutonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout main_panelLayout = new javax.swing.GroupLayout(main_panel);
        main_panel.setLayout(main_panelLayout);
        main_panelLayout.setHorizontalGroup(
            main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(main_panelLayout.createSequentialGroup()
                        .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(main_panelLayout.createSequentialGroup()
                                .addComponent(SetBouton, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(SetValueField, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, main_panelLayout.createSequentialGroup()
                                    .addComponent(GetNextBouton, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(ClearFieldBouton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(main_panelLayout.createSequentialGroup()
                                    .addComponent(GetBouton, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(GetValueField, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2)
                        .addContainerGap())
                    .addGroup(main_panelLayout.createSequentialGroup()
                        .addComponent(label_logo_stri)
                        .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(main_panelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(label_logo_UPSSITECH))
                            .addGroup(main_panelLayout.createSequentialGroup()
                                .addGap(320, 320, 320)
                                .addComponent(label_soft_name)
                                .addGap(0, 335, Short.MAX_VALUE))))
                    .addGroup(main_panelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        main_panelLayout.setVerticalGroup(
            main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_panelLayout.createSequentialGroup()
                .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(main_panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_logo_stri)
                            .addGroup(main_panelLayout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(label_logo_UPSSITECH))))
                    .addComponent(label_soft_name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(main_panelLayout.createSequentialGroup()
                        .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(GetBouton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(GetValueField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(GetNextBouton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ClearFieldBouton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(SetValueField)
                            .addComponent(SetBouton, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );

        add(main_panel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

    private void SetValueFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SetValueFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SetValueFieldActionPerformed

    private void SetValueFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SetValueFieldMouseClicked
        // TODO add your handling code here:
        // on nettoie le champs
        SetValueField.setText("");
    }//GEN-LAST:event_SetValueFieldMouseClicked

    private void SetValueFieldMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SetValueFieldMouseExited
        // TODO add your handling code here:
        // TODO add your handling code here:
        if(SetValueField.getText().isEmpty() == true){
            SetValueField.setText("Value to set");
        }else{
            // il y a une valeur dans le champs donc on ne fait rien
        }
    }//GEN-LAST:event_SetValueFieldMouseExited

    private void ClearFieldBoutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearFieldBoutonActionPerformed
        // TODO add your handling code here:
        // on vide les champs
        GetValueField.setText("");
        SetValueField.setText("");
    }//GEN-LAST:event_ClearFieldBoutonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManagerIHM().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ClearFieldBouton;
    private javax.swing.JButton GetBouton;
    private javax.swing.JButton GetNextBouton;
    private javax.swing.JTextField GetValueField;
    private javax.swing.JTextArea LogTextArea;
    private javax.swing.JButton SetBouton;
    private javax.swing.JTextField SetValueField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel label_logo_UPSSITECH;
    private javax.swing.JLabel label_logo_stri;
    private javax.swing.JLabel label_soft_name;
    private javax.swing.JPanel main_panel;
    private javax.swing.JTree network_tree;
    // End of variables declaration//GEN-END:variables
}
