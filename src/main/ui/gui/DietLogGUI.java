//Based off ListDemo from docs.oracle.com. Icon images taken from Apple Emojis

package ui.gui;



import model.Day;
import model.DietLog;
import model.EventLog;
import model.FoodItem;
import org.knowm.xchart.*;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.LogPrinter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;


public class DietLogGUI extends JPanel implements ListSelectionListener {
    //JList Fields
    private JList listDisplay;
    private DefaultListModel listData;

    //JButton Fields
    private static final String addDateString = "Add Date";
    private static final String viewFoodsString = "View Foods";
    private JButton viewFoodsButton;
    private JButton addFoodsButton;
    private JButton analysisButton;
    private JButton deleteDayButton;
    private JButton saveItem;
    private JButton loadItem;

    //DietLog Fields
    private DietLog dietTracker;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/DietLog.json";


    // EFFECTS: Constructs a new DietLog Graphical User Interface
    public DietLogGUI() {
        super(new BorderLayout());
        dietTracker = new DietLog();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        listData = new DefaultListModel();
        initializeJList();
        initializeAllButtons();
        initializePanels();
    }

    // MODIFIES: this
    // EFFECTS: Initializes the List of Days in the GUI
    private void initializeJList() {
        listDisplay = new JList(listData);
        listDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listDisplay.setSelectedIndex(0);
        listDisplay.addListSelectionListener(this);
        listDisplay.setVisibleRowCount(5);
    }

    // MODIFIES: this
    // EFFECTS: Creates the set of buttons to be added
    private void initializeAllButtons() {
        initializeAddDateButton();
        initializeViewFoodsButton();
        initializeAnalysisButton();
        initializeFileButtons();
        initializeDeleteButton();
    }

    // MODIFIES: this
    // EFFECTS: Initializes the "Add Date" button
    private void initializeAddDateButton() {
        addFoodsButton = new JButton(addDateString);
        AddDateListener addDateListener = new AddDateListener(addFoodsButton);
        addFoodsButton.setActionCommand(addDateString);
        addFoodsButton.addActionListener(addDateListener);
        addFoodsButton.setEnabled(true);
        addFoodsButton.setFocusable(false);

        ImageIcon icon = new ImageIcon("./data/AddDateIcon.png");
        Image scaleImage = icon.getImage();
        Image scaledImage = scaleImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);
        addFoodsButton.setIcon(icon);
    }

    // MODIFIES: this
    // EFFECTS: Initializes the "Delete Day" button
    private void initializeDeleteButton() {
        deleteDayButton = new JButton("Delete Day");
        deleteDayButton.addActionListener(new DeleteListener());
        deleteDayButton.setEnabled(false);
        deleteDayButton.setFocusable(false);

        ImageIcon icon = new ImageIcon("./data/DeleteDayIcon.png");
        Image scaleImage = icon.getImage();
        Image scaledImage = scaleImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);
        deleteDayButton.setIcon(icon);
    }

    // MODIFIES: this
    // EFFECTS: Initialize the "View Foods" Button
    private void initializeViewFoodsButton() {
        viewFoodsButton = new JButton(viewFoodsString);
        viewFoodsButton.setActionCommand(viewFoodsString);
        viewFoodsButton.addActionListener(new ViewFoodsListener());
        viewFoodsButton.setEnabled(false);
        viewFoodsButton.setFocusable(false);

        ImageIcon icon = new ImageIcon("./data/ViewFoodsIcon.png");
        Image scaleImage = icon.getImage();
        Image scaledImage = scaleImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);
        viewFoodsButton.setIcon(icon);
    }

    // MODIFIES: this
    // EFFECTS: Initialize the "Run Analysis" Button
    private void initializeAnalysisButton() {
        analysisButton = new JButton("Run Analysis");
        analysisButton.addActionListener(new AnalysisListener());
        analysisButton.setFocusable(false);

        ImageIcon icon = new ImageIcon("./data/AnalysisIcon.png");
        Image scaleImage = icon.getImage();
        Image scaledImage = scaleImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);
        analysisButton.setIcon(icon);
    }

    // MODIFIES: this
    // EFFECTS: Initializes the Save and Load buttons
    private void initializeFileButtons() {
        saveItem = new JButton("Save");
        saveItem.addActionListener(e -> saveDietLog());
        saveItem.setBackground(new Color(238, 238, 238));
        saveItem.setFocusable(false);
        loadItem = new JButton("Load");
        loadItem.addActionListener(e -> loadDietLog());
        loadItem.setBackground(new Color(238, 238, 238));
        loadItem.setFocusable(false);

    }

    // MODIFIES: this
    // EFFECTS: Creates a FilePanel for the save/load buttons & a Main Panel for everything else
    private void initializePanels() {
        JScrollPane listScrollPane = new JScrollPane(listDisplay);

        JPanel filePane = new JPanel();
        filePane.setLayout(new GridLayout(2, 2));
        filePane.add(saveItem);
        filePane.add(loadItem);
        JLabel label = new JLabel();
        label.setText("(DD/MM/YYY)");
        filePane.add(label);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new GridLayout(4, 1));
        buttonPane.add(viewFoodsButton);
        buttonPane.add(deleteDayButton);
        buttonPane.add(addFoodsButton);
        buttonPane.add(analysisButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
        add(filePane, BorderLayout.PAGE_START);
    }

    // EventListener Class for View Foods Button
    class ViewFoodsListener implements ActionListener {

        // EFFECTS: Outputs a JOptionPane with a list of foods for the selected day in JList
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedDay = (String) listDisplay.getSelectedValue();
            int pos = dietTracker.getDayPosition(selectedDay);
            Day tempDay = dietTracker.getDay(pos);
            JOptionPane.showMessageDialog(null,
                    tempDay.printFoods(), "Report for: " + selectedDay, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // EventListener Class for AddDateListener Button
    class AddDateListener implements ActionListener {
        private JButton button;

        // EFFECTS: Constructs a new AddDateListener
        public AddDateListener(JButton button) {
            this.button = button;
        }

        // EFFECTS: Prompts the user for a date and processes the input
        @Override
        public void actionPerformed(ActionEvent e) {
            String date = JOptionPane.showInputDialog("Enter in date (DD/MM/YYYY):");
            if (date == null) {
                //Closed window
            } else if (listData.contains(date)) {
                existingDatePrompt(date, e);
            } else {
                newDatePrompt(date);
            }
        }

        // EFFECT: Notifies user their date exists. Allows for adding of more items or date re-entry
        private void existingDatePrompt(String date, ActionEvent e) {
            String[] responses = {"Add items", "Re-enter date"};
            int ans = JOptionPane.showOptionDialog(null,
                    "Date exists. Add items or re-enter date?",
                    "", JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, null, responses, 0);

            if (ans == 0) {
                new FoodEntryGUI(date);
            } else {
                actionPerformed(e);
            }
        }

        // EFFECT: If date is valid, instantiates FoodEntryGUI for entered date
        private void newDatePrompt(String date) {
            try {
                int dayOfMonth = Integer.parseInt(date.substring(0, 2));
                int month = Integer.parseInt(date.substring(3, 5));
                int year = Integer.parseInt(date.substring(6));
                LocalDate test = LocalDate.of(year, month, dayOfMonth);
                FoodEntryGUI tempGUI = new FoodEntryGUI(date);
                if (listData.size() > 0) {
                    viewFoodsButton.setEnabled(true);
                    deleteDayButton.setEnabled(true);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Invalid Date", "", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Event listener for the Run Analysis Button
    class AnalysisListener implements ActionListener {

        // EFFECTS: Conducts analysis on either all days or a set range
        @Override
        public void actionPerformed(ActionEvent e) {
            if (listData.size() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Please add dates first", "", JOptionPane.ERROR_MESSAGE);
            } else {
                String[] responses = {"All days", "Set Range"};
                int command = JOptionPane.showOptionDialog(null,
                        "Run analysis on all days or a set range?",
                        "", JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, responses, 0);

                if (command == 0) {
                    runAnalysisAll();
                } else if (command == 1) {
                    try {
                        runAnalysisRange();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,
                                "Invalid Entries", "", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }

        // EFFECTS: Helper for converting string to LocalDate
        private LocalDate stringToLocalDate(String date) {
            int dayOfMonth = Integer.parseInt(date.substring(0, 2));
            int month = Integer.parseInt(date.substring(3, 5));
            int year = Integer.parseInt(date.substring(6));

            return LocalDate.of(year, month, dayOfMonth);
        }

        // EFFECTS: Runs analysis for all days
        private void runAnalysisAll() {
            JOptionPane.showMessageDialog(null, "For "
                    + dietTracker.getLogSize() + " day(s), your average calories consumed was: "
                    + dietTracker.averageCal() + " calories.", "Results", JOptionPane.INFORMATION_MESSAGE);

            JLabel message = new JLabel("Fatty");

            PieChart dinDinPie = new PieChartBuilder().width(800).height(600).title("Meal Distribution").build();

            PieChart foodDrinkPie =
                    new PieChartBuilder().width(800).height(600).title("Food/Drink Distribution").build();
            foodDrinkPie.addSeries("Food",250);
            foodDrinkPie.addSeries("Drink",750);

            pieHelper(dinDinPie,dietTracker.pieDataAllDays());

//            pie.addSeries("Gold", 24);
//            pie.addSeries("Silver", 21);
//            pie.addSeries("Platinum", 39);
//            pie.addSeries("Copper", 17);
//            pie.addSeries("Zinc", 40);

            JPanel pieChartPanel = new XChartPanel<PieChart>(dinDinPie);
            JPanel stuffCrustPie = new XChartPanel<PieChart>(foodDrinkPie);

            JFrame newFrame = new JFrame("Report Page");

            double[] dataX = new double[]{0.0, 1.0, 2.0};
            double[] dataY = new double[]{2.0, 1.0, 0.0};

            newFrame.setLayout(new GridLayout(2,2));
            newFrame.add(message);
            newFrame.add(pieChartPanel);
            newFrame.add(stuffCrustPie);
            newFrame.pack();
            newFrame.setVisible(true);

        }

        private void pieHelper(PieChart p, Map<String, Integer> data) {

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
//                System.out.println(entry.getKey() + "/" + entry.getValue());
                if (entry.getValue() != 0) {
                    p.addSeries(entry.getKey(),entry.getValue());
                }

            }
        }

        // EFFECTS: Runs analysis for a set range
        private void runAnalysisRange() {
            String from = JOptionPane.showInputDialog("From - includes the day: (DD/MM/YYYY):");
            String to = JOptionPane.showInputDialog("To - includes the day: (DD/MM/YYYY):");

            LocalDate f = stringToLocalDate(from);
            LocalDate t = stringToLocalDate(to);

            int averageCal = dietTracker.rangeAverageCal(f, t);
            int totalDays = dietTracker.daysInRange(f, t);

            JOptionPane.showMessageDialog(null, "For the "
                    + totalDays + " day(s) between "
                    + from + " and " + to
                    + ", your average calories consumed was: "
                    + averageCal + " calories.", "Results", JOptionPane.INFORMATION_MESSAGE);

            PieChart pie = new PieChartBuilder().width(800).height(600).title("Meal Distribution").build();

            pieHelper(pie,dietTracker.pieDataRangeDays(f,t));


            JPanel pieChartPanel = new XChartPanel<PieChart>(pie);

            JFrame newFrame = new JFrame("Meal Distribution");

            newFrame.add(pieChartPanel);
            newFrame.pack();
            newFrame.setVisible(true);
        }
    }

    // Event Listener for Delete Button. Based off ListDemo from oracle.com
    class DeleteListener implements ActionListener {

        // EFFECTS: Removes selected JList Item
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = listDisplay.getSelectedIndex();
            listData.remove(index);
            dietTracker.removeDay(dietTracker.getDay(index));

            int size = listData.getSize();

            if (size == 0) {
                deleteDayButton.setEnabled(false);

            } else {
                if (index == listData.getSize()) {
                    index--;
                }

                listDisplay.setSelectedIndex(index);
                listDisplay.ensureIndexIsVisible(index);
            }
        }
    }


    // MODIFIES: this
    // EFFECTS: Creates the DietLog Interface
    // NOTES: based off ListDemo from oracle.com
    public static void createAndShowGUI() {
        JFrame frame = new JFrame("DietLog Tracker");

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                LogPrinter lp = new LogPrinter();
                lp.printLog(EventLog.getInstance());
                System.exit(0);
            }
        });

        frame.setResizable(true);

        JComponent newContentPane = new DietLogGUI();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        frame.pack();
        frame.setVisible(true);
        ImageIcon logo = new ImageIcon("./data/AppLogo.png");
        frame.setIconImage(logo.getImage());
        frame.setSize(300, 450);
    }

    // MODIFIES: this
    // New window for users to enter in food data for a specific day
    public class FoodEntryGUI extends JFrame implements ActionListener {
        private JTextField description;
        private JTextField calories;
        private JComboBox comboMealTime;
        private JComboBox comboMealType;
        private JButton button;

        private Day storedDay;
        private String dayAsString;

        // EFFECTS: constructor for the FoodEntryGUI
        public FoodEntryGUI(String d) {
            super();
            dayAsString = d;
            setTitle("Food Entry for: " + d);
            setLayout(new FlowLayout());
            setVisible(true);

            String[] mealtime = {"Breakfast", "Lunch", "Dinner", "Snack"};
            comboMealTime = new JComboBox(mealtime);

            String[] mealtype = {"Food", "Drink"};
            comboMealType = new JComboBox(mealtype);

            description = new JTextField();
            description.setPreferredSize(new Dimension(200, 25));
            description.setText("Description");

            calories = new JTextField();
            calories.setPreferredSize(new Dimension(100, 25));
            calories.setText("Calories");

            button = new JButton("Submit");
            button.addActionListener(this);

            this.add(comboMealTime);
            this.add(comboMealType);
            this.add(calories);
            this.add(description);
            this.add(button);
            this.pack();
        }

        // EFFECTS: Processes user inputs and adds food item to day
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Integer.parseInt(calories.getText());
                String mealTime = (String) comboMealTime.getSelectedItem();
                FoodItem.MealType mealTimeResponse = mealTypeHelper(mealTime);

                String mealType = (String) comboMealType.getSelectedItem();
                FoodItem.FoodOrDrink foodTypeResponse = foodOrBeverageHelper(mealType);

                int cal = Integer.parseInt(calories.getText());
                String description = this.description.getText();

                int dayOfMonth = Integer.parseInt(dayAsString.substring(0, 2));
                int month = Integer.parseInt(dayAsString.substring(3, 5));
                int year = Integer.parseInt(dayAsString.substring(6));

                if (listData.contains(dayAsString)) {
                    addToExistingDate(description, cal, foodTypeResponse, mealTimeResponse);

                } else {
                    storedDay = new Day(dayOfMonth, month, year);
                    addToNewDay(description, cal, foodTypeResponse, mealTimeResponse);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter in a number for calories",
                        "Invalid Entry", JOptionPane.ERROR_MESSAGE);
            }
        }

        // EFFECTS: Adds food specified in parameter to an already existing day
        private void addToExistingDate(String d, int c, FoodItem.FoodOrDrink f, FoodItem.MealType m) {
            int pos = dietTracker.getDayPosition(dayAsString);
            Day dayToAddTo = dietTracker.getDay(pos);
            dayToAddTo.addFood(d, c, f, m);
            JOptionPane.showMessageDialog(null, "Done!\n"
                            + dayAsString + " has " + dayToAddTo.getAmountAte() + " items.\n"
                            + "Close window or add more items.",
                    "Has: " + dayToAddTo.getAmountAte(), JOptionPane.INFORMATION_MESSAGE);
        }

        // EFFECTS: Adds food specified in parameter to new day.
        private void addToNewDay(String d, int c, FoodItem.FoodOrDrink f, FoodItem.MealType m) {
            storedDay.addFood(d, c, f, m);
            dietTracker.addDay(storedDay);
            listData.addElement(dayAsString);
            JOptionPane.showMessageDialog(null, "Done! Feel free to close the window or add more"
                            + " items to: " + dayAsString,
                    "Item Added", JOptionPane.INFORMATION_MESSAGE);

            checkIfEnableButton();
        }

        // EFFECTS: Enables View Foods & Delete buttons if there are entries
        private void checkIfEnableButton() {
            if (listData.size() > 0) {
                viewFoodsButton.setEnabled(true);
                deleteDayButton.setEnabled(true);
            }
        }


        // EFFECTS: Helper method to convert String c into FoodOrDrink enumeration
        private FoodItem.FoodOrDrink foodOrBeverageHelper(String c) {
            if (c.equals("Food")) {
                return FoodItem.FoodOrDrink.FOOD;
            } else if (c.equals("Drink")) {
                return FoodItem.FoodOrDrink.BEVERAGE;
            } else {
                return null;
            }
        }

        // EFFECTS: Helper method to convert String c into MealType enumeration
        private FoodItem.MealType mealTypeHelper(String c) {
            switch (c) {
                case "Breakfast":
                    return FoodItem.MealType.BREAKFAST;
                case "Lunch":
                    return FoodItem.MealType.LUNCH;
                case "Dinner":
                    return FoodItem.MealType.DINNER;
                case "Snack":
                    return FoodItem.MealType.SNACK;
                default:
                    return null;
            }
        }
    }

    // EFFECTS: saves the DietLog to file
    private void saveDietLog() {
        try {
            jsonWriter.open();
            jsonWriter.write(dietTracker);
            jsonWriter.close();
            System.out.println("Saved your log to " + JSON_STORE + "\n");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE + "\n");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads DietLog from file
    private void loadDietLog() {
        try {
            dietTracker = jsonReader.read();
            listData.removeAllElements();
            populateList();
            System.out.println("Loaded your log from " + JSON_STORE + "\n");
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE + "\n");
        }
    }

    // EFFECTS: Helper function to display DietLog data as dates in string
    private void populateList() {
        for (Day d : dietTracker.getLog()) {
            listData.addElement(d.getDateAsString());
        }
    }

    // EFFECTS: Required for List Interface, Disables View Foods & Delete buttons if no day selected
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {

            if (listDisplay.getSelectedIndex() == -1) {
                viewFoodsButton.setEnabled(false);
                deleteDayButton.setEnabled(false);

            } else {
                viewFoodsButton.setEnabled(true);
                deleteDayButton.setEnabled(true);
            }
        }
    }

}
