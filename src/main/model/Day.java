package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Represents a single day with a date and list of foods ate
public class Day implements Writable {
    private LocalDate date; // in DD/MM/YYYY format
    private List<FoodItem> foodAteToday;


    // REQUIRES: day/month/year is a valid date
    // EFFECTS: Constructs a new day
    public Day(int day, int month, int year) {
        date = LocalDate.of(year, month, day);
        foodAteToday = new ArrayList<>();
    }

    // EFFECTS: Returns the LocalDate of this as a string in DD/MM/YYYY
    public String getDateAsString() {
        String temp = date.toString();

        String dayOfMonth = temp.substring(8);
        String month = temp.substring(5,7);
        String year = temp.substring(0,4);
        return dayOfMonth + "/" + month + "/" + year;
    }


    // MODIFIES: this
    // EFFECTS: Adds a FoodOrDrink item into your Day
    public void addFood(String description, int cal, FoodItem.FoodOrDrink c, FoodItem.MealType m) {
        foodAteToday.add(new FoodItem(description, cal, c, m));
        EventLog.getInstance().logEvent(new Event("Added " + description + " (" + cal +  " calories) to "
                + getDateAsString()));
    }

    // EFFECTS: Produces the total calories eaten today. 0 if no items.
    public int caloriesAteToday() {
        int cal = 0;

        for (FoodItem c : foodAteToday) {
            cal += c.getCalories();
        }
        return cal;
    }

    // getters
    public LocalDate getDate() {
        return date;
    }

    public int getAmountAte() {
        return foodAteToday.size();
    }

    public FoodItem getFoodInList(int i) {
        return foodAteToday.get(i);
    }


    // THIS SECTION BELOW OF METHODS IS FOR PIECHART
    public int breakfastCals() {
        int cal = 0;

        for (FoodItem i: foodAteToday) {
            if (i.getMealTime() == FoodItem.MealType.BREAKFAST) {
                cal += i.getCalories();
            }
        }

        return cal;
    }

    public int lunchCal() {
        int cal = 0;

        for (FoodItem i: foodAteToday) {
            if (i.getMealTime() == FoodItem.MealType.LUNCH) {
                cal += i.getCalories();
            }
        }

        return cal;
    }

    public int dinnerCal() {
        int cal = 0;

        for (FoodItem i: foodAteToday) {
            if (i.getMealTime() == FoodItem.MealType.DINNER) {
                cal += i.getCalories();
            }
        }

        return cal;
    }

    public int snackCal() {
        int cal = 0;

        for (FoodItem i: foodAteToday) {
            if (i.getMealTime() == FoodItem.MealType.SNACK) {
                cal += i.getCalories();
            }
        }

        return cal;
    }

    // THIS SECTION ABOVE OF METHODS IS FOR PIECHART

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("date",date);
        json.put("ListOfFood", ateTodayToJson());
        return json;
    }

    // EFFECTS: Returns a string of consumed foods along with their calories
    public String printFoods() {
        StringBuilder ateFoodsLog = new StringBuilder();
        int totalCal = 0;

        if (foodAteToday.size() == 0) {
            return "No foods";
        } else {
            for (FoodItem f: foodAteToday) {
                totalCal += f.getCalories();
                ateFoodsLog.append(f.getDescription()).append(" - ").append(f.getCalories()).append(" calories\n");
            }
            ateFoodsLog.append("\n");
            ateFoodsLog.append("Total Calories consumed: ").append(totalCal);
            return ateFoodsLog.toString();
        }
    }

    // EFFECTS: Returns a list of food ate as a JSONArray
    private JSONArray ateTodayToJson() {
        JSONArray jsonArray = new JSONArray();

        for (FoodItem f: foodAteToday) {
            jsonArray.put(f.toJson());
        }
        return jsonArray;
    }



}
