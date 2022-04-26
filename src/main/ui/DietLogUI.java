// NOTE: THE STRUCTURE OF UI WAS BASED OF THE TELLER EXAMPLE PROVIDED IN
//       PHASE1 OF THE PROJECT. FOR EXAMPLE, HAVING MAIN CALL A METHOD
//       THAT CALLS YOUR ACTUAL runApp() METHOD.

package ui;

import model.FoodItem;
import model.Day;
import model.DietLog;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class DietLogUI {
    private static final String JSON_STORE = "./data/DietLog.json";

    private DietLog dietTracker;
    private Scanner key;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;


    // EFFECTS: Runs the dietTrackerApp
    public DietLogUI() {
        runApp();
    }


    // EFFECTS: Processes user inputs
    private void runApp() {
        boolean keepGoing = true;
        String command;

        initialize();

        while (keepGoing) {
            menu();
            command = key.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommandMain(command);
            }
        }
    }


    // EFFECTS: Constructs a new DietTrackerApp
    private void initialize() {
        dietTracker = new DietLog();
        key = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // EFFECTS: Displays the start menu
    private void menu() {
        System.out.println("You currently have " + dietTracker.getLogSize() + " dates recorded.");
        System.out.println("Would you like to:");
        System.out.println("\t---> Make an entry (e)");
        System.out.println("\t---> Analyze your entries (a)");
        System.out.println("\t---> Save your entries (s)");
        System.out.println("\t---> Load your entries (l)");
        System.out.println("\t---> Or quit (q)");
    }


    // EFFECTS: Processes user input at the main menu
    private void processCommandMain(String c) {
        if (c.equals("e")) {
            dateUI();
        } else if (c.equals("a")) {
            analysisUI();
        } else if (c.equals("s")) {
            saveDietLog();
        } else if (c.equals("l")) {
            loadDietLog();
        } else {
            System.out.println("Selection not valid");
        }
    }


    // EFFECTS: Processes user input at the analysis menu
    private void processCommandAnalysis(String c) {
        if (c.equals("a")) {
            runAnalysisAll();
        } else if (c.equals("r")) {
            runAnalysisRange();
        } else {
            System.out.println("Selection not valid");
        }
    }


    // REQUIRES: valid date input (DD/MM/YYYY). Requires users input one of the strings listed
    // EFFECTS: Displays the initial make entry (e) UI
    private void dateUI() {
        System.out.println("Enter in date DD/MM/YYYY");
        String date = key.next();
        String command;

        if (dietTracker.checkDayInLog(date)) {
            System.out.println("Date exists. Would you like to add items (a), re-enter date (r), or go to main (m)?");
            command = key.next();

            if (command.equals("a")) {
                existingDayInput(date);
            } else if (command.equals("r")) {
                dateUI();
            } else if (command.equals("m")) {
                //stub;
            }

        } else {
            newDayInput(date);
        }
    }


    // EFFECTS: Processes user input after having entered in one day or item
    private void afterAddedFoodUI(String date) {
        System.out.println("Would you like to add more items (a), add another day (d), or go back to main (m)?");
        String c = key.next();
        if (c.equals("a")) {
            existingDayInput(date);
        } else if (c.equals("d")) {
            dateUI();
        } else if (c.equals("m")) {
            //stub
        }
    }


    // EFFECTS: Displays the initial analysis (a) UI
    private void analysisUI() {
        if (dietTracker.getLogSize() > 0) {
            System.out.println("Would you like to run an analysis on all days (a) or a set range (r)?");
            String command = key.next();
            processCommandAnalysis(command);

        } else {
            System.out.println("You have no entries. Please make an entry first");
        }
    }


    // REQUIRES: date must be in valid DD/MM/YYYY format. Subsequent inputs must be of the provided options.
    // MODIFIES: this
    // EFFECTS: Adds a FoodOrBeverage item to a new Day in your DietTracker
    private void newDayInput(String date) {
        System.out.println("Food (f) or drink? (d)");
        String command = key.next();
        FoodItem.FoodOrDrink typeOfFood = foodOrBeverageHelper(command);

        System.out.println("When did you eat it? Breakfast (b), Lunch (l), Dinner (d), Snack (s)");
        command = key.next();
        FoodItem.MealType typeOfMeal = mealTypeHelper(command);

        System.out.println("How many calories?");
        int cal = key.nextInt();

        System.out.println("Write a short description");
        String description = key.next();

        addDayToLog(date, description, typeOfFood, typeOfMeal, cal);

        afterAddedFoodUI(date);
    }


    // REQUIRES: date must be in valid DD/MM/YYYY format. Subsequent inputs must be of the provided options.
    // MODIFIES: this
    // EFFECTS: Adds a FoodOrBeverage item to an existing Day in your DietTracker
    private void existingDayInput(String date) {
        int position = dietTracker.getDayPosition(date);

        System.out.println("Food (f) or drink? (d)");
        String command = key.next();
        FoodItem.FoodOrDrink typeOfFood = foodOrBeverageHelper(command);

        System.out.println("When did you eat it? Breakfast (b), Lunch (l), Dinner (d), Snack (s)");
        command = key.next();
        FoodItem.MealType typeOfMeal = mealTypeHelper(command);

        System.out.println("How many calories?");
        int cal = key.nextInt();

        System.out.println("Write a short description");
        String description = key.next();

        dietTracker.getDay(position).addFood(description, cal, typeOfFood, typeOfMeal);
        System.out.println("Recorded. " + date + " has " + dietTracker.getDay(position).getAmountAte() + " items");

        afterAddedFoodUI(date);
    }


    // REQUIRES: String day must be valid in DD/MM/YYYY format,
    // MODIFIES: this
    // EFFECTS: Adds a Day to your DietTracker
    private void addDayToLog(String day, String d, FoodItem.FoodOrDrink c, FoodItem.MealType m, int cal) {
        int dayOfMonth = Integer.parseInt(day.substring(0,2));
        int month = Integer.parseInt(day.substring(3,5));
        int year = Integer.parseInt(day.substring(6));

        Day newDay = new Day(dayOfMonth, month, year);
        newDay.addFood(d, cal, c, m);
        dietTracker.addDay(newDay);
        System.out.println("Recorded. " + day + " has " + newDay.getAmountAte() + " items");
    }


    // EFFECTS: Runs analysis for all days
    private void runAnalysisAll() {
        System.out.println("======================================================");
        System.out.println("\t \t \t \t \tAnalysis");
        System.out.println("======================================================");
        System.out.println("Your average calories for all "
                + dietTracker.getLogSize() + " days is " + dietTracker.averageCal());
        System.out.println("Most calories consumed is " + dietTracker.highestCalDay());
        System.out.println("======================================================\n");
    }

    // REQUIRES: Inputted dates must be in valid DD/MM/YYYY format
    // EFFECTS: Runs analysis for days between [from,to]
    private void runAnalysisRange() {
        String from;
        String to;

        System.out.println("Enter in the start date (DD/MM/YYYY)");
        from = key.next();
        LocalDate f = stringToLocalDate(from);
        System.out.println("Enter in the end date (DD/MM/YYYY)");
        to = key.next();
        LocalDate t = stringToLocalDate(to);

        int averageCal = dietTracker.rangeAverageCal(f,t);
        int countRange = dietTracker.daysInRange(f,t);

        System.out.println("======================================================");
        System.out.println("\t \t \t \t \tAnalysis");
        System.out.println("======================================================");
        System.out.println(countRange + " days recorded between " + from + " and " + to);
        System.out.println("Average calories was " + averageCal);
        System.out.println("Most calories consumed is " + dietTracker.rangeHighestCalDay(f,t));
        System.out.println("======================================================\n");
    }

    // REQUIRES: date must be in valid DD/MM/YYYY format
    // EFFECTS: Converts DD/MM/YYYY into LocalDate
    private LocalDate stringToLocalDate(String date) {
        int dayOfMonth = Integer.parseInt(date.substring(0,2));
        int month = Integer.parseInt(date.substring(3,5));
        int year = Integer.parseInt(date.substring(6));

        return LocalDate.of(year, month,dayOfMonth);
    }



    // EFFECTS: Helper function for Date Entries. Processes command c
    //          and return the corresponding ConsumeType
    private FoodItem.FoodOrDrink foodOrBeverageHelper(String c) {
        if (c.equals("f")) {
            return FoodItem.FoodOrDrink.FOOD;
        } else if (c.equals("d")) {
            return FoodItem.FoodOrDrink.BEVERAGE;
        } else {
            return null;
        }
    }


    // EFFECTS: Helper function for Date Entries. Processes command c
    //          and returns the corresponding MealType
    private FoodItem.MealType mealTypeHelper(String c) {
        switch (c) {
            case "b":
                return FoodItem.MealType.BREAKFAST;
            case "l":
                return FoodItem.MealType.LUNCH;
            case "d":
                return FoodItem.MealType.DINNER;
            case "s":
                return FoodItem.MealType.SNACK;
            default:
                return null;
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
    // EFFECTS: loads Dietlog from file
    private void loadDietLog() {
        try {
            dietTracker = jsonReader.read();
            System.out.println("Loaded your log from " + JSON_STORE + "\n");
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE + "\n");
        }
    }
}
