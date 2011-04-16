/*
 * Fynche - a Framework for Multiagent Computational Creativity
 * Copyright 2011 Josh Hansen
 * 
 * This file is part of the Fynche <https://github.com/joshhansen/fynche>.
 * 
 * Fynche is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Fynche is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with Fynche.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * If you have inquiries regarding any further use of Fynche, please
 * contact Josh Hansen <http://joshhansen.net/>
 */

/*
 * ColorViewer.java
 *
 * Created on Mar 17, 2011, 12:52:56 PM
 */

package fynche.ui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;

import fynche.artefacts.NamedColor;
import fynche.interfaces.Agent;

/**
 *
 * @author Josh Hansen
 */
public class ColorViewer extends javax.swing.JFrame {
    private final Map<String,AgentColorPanel> agentPanels = new HashMap<String,AgentColorPanel>();

    /** Creates new form ColorViewer */
    public ColorViewer() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabPane = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Color Viewer");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 753, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                final ColorViewer viewer = new ColorViewer();
                viewer.setVisible(true);
                viewer.addColor("test", new NamedColor(Color.BLUE, "cool blue"));
                viewer.addColor("test", new NamedColor(Color.RED, "red"));
                viewer.addColor("a", new NamedColor(Color.RED, "red"));
                viewer.addColor("a", new NamedColor(Color.GREEN, "green"));
            }
        });
    }

    public synchronized void addColor(final String agentName, final NamedColor color) {
        AgentColorPanel panel = agentPanels.get(agentName);
        if(panel == null) {
            final AgentColorPanel tmpPanel = new AgentColorPanel();
            agentPanels.put(agentName, tmpPanel);
            java.awt.EventQueue.invokeLater(new Runnable(){
                public void run() {
                    tabPane.addTab(agentName, tmpPanel);
                }
            });
            panel = tmpPanel;
        }
        panel.addColor(color);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabPane;
    // End of variables declaration//GEN-END:variables

}