package frame;

import data.Person;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class SelectFrame extends JDialog {
    private JPanel contentPane;
    private JList<String> selectionLst;
    private HashMap<Integer, Integer> elements;
    private DefaultListModel<String> model;
    private ShowEmployees parent;

    public SelectFrame(ShowEmployees parent) {
        this.parent = parent;
        frame();
    }

    private void frame(){
        setContentPane(contentPane);
        setLocation(parent.getX(), parent.getY() + parent.getHeight());
        setTitle("Выборка");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        elements = new HashMap<>();
        setSize(parent.getSize());
        model = new DefaultListModel<>();
        selectionLst.setModel(model);
        selectionLst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionLst.setSelectedIndex(0);
        selectionLst.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int count = selectionLst.locationToIndex(e.getPoint());
                parent.showEmployee(elements.get(count));
            }
        });
    }

    public void setSelectionLst(Person p, int count) {
        String foundPerson = p.getSername() + " " + p.getName();
        model.addElement(foundPerson);
        elements.put(model.indexOf(foundPerson), count);
    }

    private void onCancel(){
        parent.showEmployee(0);
        dispose();
    }
}
