package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a single food or drink item
public class FoodItem implements Writable {
    private String item;
    private int calories;
    private FoodOrDrink type;
    private MealType mealType;

    public enum FoodOrDrink {
        FOOD, BEVERAGE
    }

    public enum MealType {
        BREAKFAST, LUNCH, DINNER, SNACK
    }

    // EFFECTS: Constructs a new FoodOrDrink with parameters
    public FoodItem(String item, int cal, FoodOrDrink type, MealType mealType) {
        this.item = item;
        this.calories = cal;
        this.type = type;
        this.mealType = mealType;
    }

    // getters
    public int getCalories() {
        return calories;
    }

    public MealType getMealTime() {
        return mealType;
    }

    public String getDescription() {
        return item;
    }

    public FoodOrDrink getType() {
        return type;
    }

    // EFFECTS: Converts the FoodItem into a JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Description", item);
        json.put("Calories", calories);
        json.put("FoodOrBeverage", type);
        json.put("MealTime", mealType);
        return json;
    }


}
