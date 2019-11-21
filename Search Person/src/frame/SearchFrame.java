package frame;

import javax.swing.*;
import java.awt.event.*;

public class SearchFrame extends JDialog {
    private JPanel contentPane;
    private JButton closeBtn;
    private JButton searchBtn;
    private JTextField nameField;
    private JTextField lastNameField;
    private JLabel nameLabel;
    private JLabel lastNameLabel;
    private JPanel mainPanel;
    private JPanel exitPanel;

    private String srhName;
    private String srhLastName;
    private boolean result = false;

    public SearchFrame(JFrame parent) {
        super(parent, true);
        frame();
    }
    private void frame(){

        setContentPane(contentPane);
        getRootPane().setDefaultButton(closeBtn);
        pack();
        setLocationRelativeTo(getParent());


        closeBtn.addActionListener(exit -> onExit());

        searchBtn.addActionListener(search -> onSearch());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });
    }

    private void onExit() {
        dispose();
        result = false;
    }

    private void onSearch() {
        srhName = nameField.getText();
        srhLastName = lastNameField.getText();
        if(srhName.equals("") && srhLastName.equals("")){
            JOptionPane.showMessageDialog(SearchFrame.this, "Недостаточно даных, введите имя и/или фамилию", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }else if(!srhName.equals("")){
            srhLastName = null;
        }else{
            srhName = null;
        }
        result = true;
        dispose();


    }

    public boolean execute(){
        this.setVisible(true);

        return result;
    }

    public String getSrhName(){
        return srhName;
    }

    public String getSrhLastName(){
        return srhLastName;
    }
}
