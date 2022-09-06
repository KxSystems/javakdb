package com.kx.examples;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import com.kx.c;
/**
 * Creates a Swing GUI that presents the contents of a KDB+ table (Flip). 
 * It shows the mapping of the Flip class to a Swing TableModel. 
 * The contents of the table are some random data that we instruct KDB+ to generate.
 */
public class GridViewer {
    private GridViewer(){}
    static class KxTableModel extends AbstractTableModel {
        /** kdb table to display in gui as result of query */
        private c.Flip flip;
        void setFlip(c.Flip data) {
            this.flip = data;
        }

        public int getRowCount() {
            return Array.getLength(flip.y[0]);
        }

        public int getColumnCount() {
            return flip.y.length;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return c.at(flip.y[columnIndex], rowIndex);
        }

        @Override
        public String getColumnName(int columnIndex) {
            return flip.x[columnIndex];
        }
    }

    /**
     * Creates a GUI to show contents of a table from KDB+
     * Requires a KDB+ server running on port 5001 on your machine i.e. q -p 5001
     * @param args not used
     */
    public static void main(String[] args) {
        KxTableModel model = new KxTableModel();
        c c = null;
        try {
            c = new c("localhost", 5001,System.getProperty("user.name")+":mypassword");
            String query="([]date:.z.D;time:.z.T;sym:10?`8;price:`float$10?500.0;size:10?100)";
            model.setFlip((c.Flip) c.k(query));
            JTable table = new JTable(model);
            table.setGridColor(Color.BLACK);
            String title = "kdb+ Example - "+model.getRowCount()+" Rows";
            JFrame frame = new JFrame(title);
            frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            frame.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
            frame.setSize(300, 300);
            frame.setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(GridViewer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.close();
                }
                catch (IOException ex) {
                    // ingnore exception
                }
            }
        }
    }
}
