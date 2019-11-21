package frame;

import javax.swing.*;

public class SelectionFrame extends JDialog{
    private JList list1;
    private JPanel panel1;

    public SelectionFrame(JFrame parent){
        super(parent, true);
    }

    private void frame(){
        setContentPane(panel1);
        setLocationRelativeTo(getParent());
        setVisible(true);
    }

}
