/*-
 * Copyright (C) 2006 Erik Larsson
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * GeneralInfoPanel.java
 *
 * Created on den 7 november 2006, 09:52
 */

package org.catacombae.dmgextractor.utils.gui;

/**
 * @author <a href="http://www.catacombae.org/" target="_top">Erik Larsson</a>
 */
public class GeneralInfoPanel extends javax.swing.JPanel {

    /** Creates new form GeneralInfoPanel */
    public GeneralInfoPanel() {
        initComponents();

        filenameField.setOpaque(false);
        sizeField.setOpaque(false);
        numberOfPartitionsField.setOpaque(false);
    }

    public void setFields(String filename, long size,
                          long numberOfPartitions) {
        filenameField.setText(filename);
        sizeField.setText(size + " bytes");
        numberOfPartitionsField.setText(numberOfPartitions + "");
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileNameLabel = new javax.swing.JLabel();
        sizeLabel = new javax.swing.JLabel();
        numberOfPartitionsLabel = new javax.swing.JLabel();
        filenameField = new javax.swing.JTextField();
        sizeField = new javax.swing.JTextField();
        numberOfPartitionsField = new javax.swing.JTextField();

        fileNameLabel.setText("File name:");

        sizeLabel.setText("Size:");

        numberOfPartitionsLabel.setText("Number of partitions:");

        filenameField.setEditable(false);
        filenameField.setText("jTextField1");
        filenameField.setBorder(null);

        sizeField.setEditable(false);
        sizeField.setText("jTextField2");
        sizeField.setBorder(null);

        numberOfPartitionsField.setEditable(false);
        numberOfPartitionsField.setText("jTextField3");
        numberOfPartitionsField.setBorder(null);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(layout.createSequentialGroup()
                                                .add(fileNameLabel)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(filenameField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE))
                                        .add(layout.createSequentialGroup()
                                                .add(sizeLabel)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(sizeField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE))
                                        .add(layout.createSequentialGroup()
                                                .add(numberOfPartitionsLabel)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(numberOfPartitionsField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                                .addContainerGap()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(fileNameLabel)
                                        .add(filenameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(sizeLabel)
                                        .add(sizeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                        .add(numberOfPartitionsLabel)
                                        .add(numberOfPartitionsField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(222, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel fileNameLabel;
    private javax.swing.JTextField filenameField;
    private javax.swing.JTextField numberOfPartitionsField;
    private javax.swing.JLabel numberOfPartitionsLabel;
    private javax.swing.JTextField sizeField;
    private javax.swing.JLabel sizeLabel;
    // End of variables declaration//GEN-END:variables

}
