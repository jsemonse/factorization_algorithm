
import java.awt.Graphics;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Justin
 */
public class GridPanel extends javax.swing.JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int x = grid.seqLen();
        setBounds(0, 0, getDrawSize()+1, (int)Math.ceil(getDrawSize() / x)+1);
        revalidate();
        if(g == null) {
            System.out.println("no graphics");
            return;
        }
        grid.drawGrid(g, getDrawSize());
    }
}