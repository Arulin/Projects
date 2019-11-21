package frame;

import data.Data;
import data.Person;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ShowEmployees extends JFrame {
    private final String FILENAME = "/data.txt";

    private JPanel mainPanel;
    private JPanel fieldPanel;
    private JPanel btnPanel;
    private JPanel changePanel;


    private JButton lastPerson;
    private JButton nextPerson;

    private JFormattedTextField lastNameField;
    private JFormattedTextField nameField;
    private JFormattedTextField patronField;
    private JFormattedTextField bdField;
    private JFormattedTextField phoneField;

    private JLabel lastName;
    private JLabel name;
    private JLabel patronymic;
    private JLabel birthday;
    private JLabel phone;


    private JButton addBtn;
    private JButton deleteBtn;
    private JButton saveBtn;
    private JButton packBtn;
    private JButton searchBtn;
    private JPanel termPanel;
    private JTextField filterSernameField;
    private JTextField filterNameField;
    private JButton filterButton;
    private JButton selectionButton;
    private JLabel filterSernameLabel;
    private JLabel filterNameLabel;




    private ArrayList<Person> employees;
    private int count;
    private Data dataOfPersons;

    private boolean isFilter = false;
    private boolean newPerson = false;


    private SearchFrame dialog;
    private String searchName;
    private String searchLastName;
    private int searchID;

    private String filterName;
    private String filterSername;
    private int firstZapis;


    public ShowEmployees(){
        dataOfPersons = new Data(FILENAME);
        this.employees = dataOfPersons.startRead(128);
        count = 0;
    }

    private void frame(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setVisible(true);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        lastPerson.setEnabled(false);
        nextPerson.addActionListener(new NextPersonListener());
        lastPerson.addActionListener(new LastPersonListener());
        saveBtn.addActionListener(new SavePersonListener());
        addBtn.addActionListener(new AddPersonListener());
        deleteBtn.addActionListener(new DeletePersonListener());
        packBtn.addActionListener(new PackPersonListener());
        searchBtn.addActionListener(new SearchPersonListener());
        filterButton.addActionListener(new FilterListener());
        selectionButton.addActionListener(new SelectionListener());

    }

    public void start(){
        if(employees == null){
            return;
        }
        frame();
        showEmployee();

    }

    private class NextPersonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            if(isFilter) {
                for(int i = count; i < employees.size(); ){
                    i++;
                    if(isEqual(filterName, filterSername, i)) {
                        count = i;
                        showEmployee();
                        checkBtn();
                        return;
                    }
                }
                nextPerson.setEnabled(false);
            }else{
                count++;
                showEmployee();
                checkBtn();
            }

        }
    }

    private class LastPersonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(isFilter) {
                for(int i = count; i > 0; ){
                    i--;
                    if(isEqual(filterName, filterSername, i)) {
                        count = i;
                        showEmployee();
                        checkBtn();
                        if(firstZapis == i){
                            lastPerson.setEnabled(false);
                        }
                        return;
                    }
                }
                lastPerson.setEnabled(false);
            }else{
                count--;
                showEmployee();
                checkBtn();
            }

        }

    }

    private class AddPersonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            patronField.setText("");
            nameField.setText("");
            lastNameField.setText("");
            bdField.setText("");
            phoneField.setText("");
            newPerson = true;
        }
    }

    private class SavePersonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String lastname = lastNameField.getText();
            String patron = patronField.getText();
            String phone = phoneField.getText();
            String bd = bdField.getText();


            try {
                if(!newPerson){
                    employees.set(count, new Person(lastname, name, patron, bd.replaceAll("\\.", ""), phone));
                    dataOfPersons.startWrite(employees);
                    return;
                }
                if(bd.length() != 8) throw new IllegalArgumentException();
                Calendar date = new GregorianCalendar();
                date.setLenient(false);
                date.set(Integer.parseInt(bd.substring(4)), Integer.parseInt(bd.substring(2,4)) - 1, Integer.parseInt(bd.substring(0, 2)));
                date.getTime();


                int sumOfSize = name.length() + lastname.length()+ patron.length() + bd.length() + phone.length();

                if(sumOfSize <= 128) {
                    employees.add(new Person(lastname, name, patron, bd, phone));
                    count = employees.size() - 1;
                    dataOfPersons.startWrite(employees.get(count), true);
                    lastPerson.setEnabled(true);
                    nextPerson.setEnabled(false);
                }
                newPerson = false;
            }catch (IllegalArgumentException ex){
                JOptionPane.showMessageDialog(ShowEmployees.this, "Некорректная дата рождения", "Ошибка", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private class DeletePersonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            int result = JOptionPane.showConfirmDialog(ShowEmployees.this, "Удалить запись?", "Окно потверждения", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(result == 1){
                return;
            }else {
                employees.remove(count);
                if (count == 0) {
                    showEmployee();
                }else{
                    lastPerson.doClick();
                }
            }

        }
    }

    private class PackPersonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            dataOfPersons.startWrite(employees);
        }
    }

    private class SearchPersonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            startSearch();
        }
    }

    private class FilterListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(isFilter){
                filterButton.setText("Фильтр");
                isFilter = false;
                count = 0;
                showEmployee();
            }else{
                filterName = filterNameField.getText();
                filterSername = filterSernameField.getText();
                if(isEmpty(filterName, filterSername)){
                    JOptionPane.showMessageDialog(ShowEmployees.this, "Введите имя/фамилию для поиска", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }else{
                    if(filterName.equals("")){
                        filterName = null;
                    }else if(filterSername.equals("")){
                        filterSername = null;
                    }
                    for(int i = 0; i < employees.size(); i++){
                        if(isEqual(filterName, filterSername, i)){
                            count = i;
                            firstZapis = i;
                            filterButton.setText("Отменить фильтр");
                            isFilter = true;
                            showEmployee();
                            lastPerson.setEnabled(false);
                            return;
                        }
                    }
                    JOptionPane.showMessageDialog(ShowEmployees.this,"Запись не найдена", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }

            }
        }
    }

    private class SelectionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(isFilter){
                JOptionPane.showMessageDialog(ShowEmployees.this,"", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }else{
                filterName = filterNameField.getText();
                filterSername = filterSernameField.getText();
                if(filterName.equals("")){
                    filterName = null;
                }else if(filterSername.equals("")){
                    filterSername = null;
                }
                SelectFrame selection = new SelectFrame(ShowEmployees.this);
                for(int i = 0; i < employees.size(); i++){
                    if(isEqual(filterName, filterSername, i)){
                        selection.setSelectionLst(employees.get(i), i);
                    }
                }
                selection.setModal(true);
                selection.setVisible(true);

            }
        }
    }

    private void startSearch(){
        if(searchName != null || searchLastName != null){
            if(count == employees.size() - 1){
                searchLastName = null;
                searchName = null;
                count = 0;
                startSearch();
            }else{
                while(count != employees.size() - 1){
                    searchID = ++count;
                    if(isEqual(searchName, searchLastName, searchID)){
                        showEmployee();
                        return;
                    }
                }
                count = employees.size() -1;
                startSearch();
            }
        }else{
            dialog = new SearchFrame(this);
            if(this.dialog.execute()) {
                searchName = dialog.getSrhName();
                searchLastName = dialog.getSrhLastName();
                searchID = 0;
                for (int i = 0; i < employees.size() - 1; i++) {
                    if (isEqual(searchName, searchLastName, i)) {
                        count = i;
                        showEmployee();
                        searchID = i + 1;
                        return;
                    }
                }
                JOptionPane.showMessageDialog(ShowEmployees.this, "Совпадений не найдено", "Ошибка", JOptionPane.ERROR_MESSAGE);
                searchLastName = null;
                searchName = null;
            }
        }
    }

    private boolean isEqual(String name, String lastName, int searchID){
        if(searchID >= employees.size()){
            return false;
        }
        Person person = employees.get(searchID);
            if(name != null && lastName != null) {
                if (person.getName().equals(name) && person.getSername().equals(lastName)) {
                    return true;
                }
            }else if(name != null){
                if(person.getName().equals(name)){
                    return true;
                }
            }else if(lastName != null){
                if(person.getSername().equals(lastName)){
                    return true;
                }
            }
        return false;
    }

    private void checkBtn(){
        if((employees.size() - 1) <= count){
            if(count != 0) {
                nextPerson.setEnabled(false);
                lastPerson.setEnabled(true);
            }else{
                nextPerson.setEnabled(false);
                lastPerson.setEnabled(false);
            }
        }else if(count == 0){
            lastPerson.setEnabled(false);
            nextPerson.setEnabled(true);
        }else{
            lastPerson.setEnabled(true);
            nextPerson.setEnabled(true);
        }
    }

    private void showEmployee(){
        checkBtn();
        patronField.setText(employees.get(count).getLastname());
        nameField.setText(employees.get(count).getName());
        lastNameField.setText(employees.get(count).getSername());
        bdField.setText(employees.get(count).getBirthday());
        phoneField.setText(employees.get(count).getPhoneNumber());
    }

    public void showEmployee(int count){
        this.count = count;
        checkBtn();
        patronField.setText(employees.get(count).getLastname());
        nameField.setText(employees.get(count).getName());
        lastNameField.setText(employees.get(count).getSername());
        bdField.setText(employees.get(count).getBirthday());
        phoneField.setText(employees.get(count).getPhoneNumber());
    }

    private boolean isEmpty(String name, String lastName){
        if(name.equals("") && lastName.equals("")){
            return true;
        }
        return false;
    }
}
