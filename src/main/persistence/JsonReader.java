// Based of Workroom demo provided

package persistence;

import model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public DietLog read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        EventLog.getInstance().logEvent(new Event("Cleared DietLog."));
        EventLog.getInstance().logEvent(new Event("Loaded DietLog."));
        return parseDietLog(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses workroom from JSON object and returns it
    private DietLog parseDietLog(JSONObject jsonObject) {
        DietLog log = new DietLog();
        addDays(log, jsonObject);
        return log;
    }

    // MODIFIES: log
    // EFFECTS: parses Days from JSON object and adds them to DietLog
    private void addDays(DietLog log, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Log");
        for (Object json : jsonArray) {
            JSONObject nextThingy = (JSONObject) json;
            addDay(log, nextThingy);
        }
    }

    // MODIFIES: log
    // EFFECTS: parses Day from JSON object and adds it to the DietLog
    private void addDay(DietLog log, JSONObject jsonObject) {
        String d = jsonObject.getString("date");

        int year = Integer.parseInt(d.substring(0,4));
        int month = Integer.parseInt(d.substring(5,7));
        int day = Integer.parseInt(d.substring(8));

        Day newDay = new Day(day,month,year);
        addFoods(newDay, jsonObject);
        log.addDay(newDay);
    }

    // MODIFIES: date
    // EFFECTS: parses FoodItems from JSON Object and adds them to date
    private void addFoods(Day date, JSONObject obj) {
        JSONArray array = obj.getJSONArray("ListOfFood");

        for (Object json: array) {
            JSONObject nextFood = (JSONObject) json;
            addFood(date, nextFood);
        }


    }

    // MODIFIES: date
    // EFFECTS: parses FoodItem from JSON object and adds it to date
    private void addFood(Day date, JSONObject obj) {
        String description = obj.getString("Description");
        int cal = obj.getInt("Calories");
        FoodItem.FoodOrDrink type = FoodItem.FoodOrDrink.valueOf(obj.getString("FoodOrBeverage"));
        FoodItem.MealType mealtype = FoodItem.MealType.valueOf(obj.getString("MealTime"));

        date.addFood(description,cal,type,mealtype);
    }
}
