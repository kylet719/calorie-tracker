package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Represents a single DietLog with a list of Days
public class DietLog implements Writable {
    private List<Day> log;


    // EFFECTS: DietLog constructor
    public DietLog() {
        log = new ArrayList<>();
    }

    // getters
    public int getLogSize() {
        return log.size();
    }

    public Day getDay(int i) {
        return log.get(i);
    }

    public List<Day> getLog() {
        return log;
    }

    // setters
    public void addDay(Day d) {
        log.add(d);
        EventLog.getInstance().logEvent(new Event("Added " + d.getDateAsString() + " into log."));
    }

    public void removeDay(Day d) {
        String date = d.getDateAsString();
        if (checkDayInLog(date)) {
            int pos = getDayPosition(date);
            log.remove(pos);
            EventLog.getInstance().logEvent(new Event("Removed " + d.getDateAsString() + " from log."));
        }


    }

    // EFFECTS: Returns average calories a day; 0 if empty log
    public int averageCal() {
        int count = 0;
        int total = 0;

        for (Day d: log) {
            total += d.caloriesAteToday();
            count++;
        }

        if (count == 0) {
            return 0;
        } else {
            return (total / count);
        }
    }

    //EFFECTS: Helper to convert days to PieChart data
    public Map<String, Integer> pieDataAllDays() {
        Map<String, Integer> returnMap = new HashMap<>();
        returnMap.put("Breakfast",0);
        returnMap.put("Lunch",0);
        returnMap.put("Dinner",0);
        returnMap.put("Snack",0);

        System.out.println();


        for (Day day: log) {
            returnMap.replace("Breakfast", returnMap.get("Breakfast") + day.breakfastCals());
            returnMap.replace("Lunch",returnMap.get("Lunch") + day.lunchCal());
            returnMap.replace("Dinner",returnMap.get("Dinner") + day.dinnerCal());
            returnMap.replace("Snack",returnMap.get("Snack") + day.snackCal());
        }

        return returnMap;
    }

    //EFFECTS: Helper to convert days to PieChart data
    public Map<String, Integer> pieDataRangeDays(LocalDate f, LocalDate t) {
        Map<String, Integer> returnMap = new HashMap<>();
        returnMap.put("Breakfast",0);
        returnMap.put("Lunch",0);
        returnMap.put("Dinner",0);
        returnMap.put("Snack",0);


        for (Day day: log) {
            if ((day.getDate().isAfter(f) || day.getDate().isEqual(f))
                    && (day.getDate().isBefore(t) || day.getDate().isEqual(t))) {
                returnMap.replace("Breakfast", returnMap.get("Breakfast") + day.breakfastCals());
                returnMap.replace("Lunch",returnMap.get("Lunch") + day.lunchCal());
                returnMap.replace("Dinner",returnMap.get("Dinner") + day.dinnerCal());
                returnMap.replace("Snack",returnMap.get("Snack") + day.snackCal());
            }
        }

        return returnMap;
    }


    // REQUIRES: f must be before t date-wise.
    // EFFECTS:  Average calories between [f,t]; 0 if empty log
    public int rangeAverageCal(LocalDate f, LocalDate t) {
        int count = 0;
        int total = 0;

        for (Day d: log) {
            if ((d.getDate().isAfter(f) || d.getDate().isEqual(f))
                    && (d.getDate().isBefore(t) || d.getDate().isEqual(t))) {
                count++;
                total += d.caloriesAteToday();
            }
        }
        if (count == 0) {
            return 0;
        } else {
            return total / count;
        }
    }

    // EFFECTS: Returns the highest calorie day as string
    public String highestCalDay() {
        int highest = 0;
        String date = "";

        for (Day d: log) {
            if (d.caloriesAteToday() > highest) {
                date = d.getDate().toString();
                highest = d.caloriesAteToday();
            }
        }

        if (highest == 0) {
            return "No calories consumed";
        } else {
            date = dateFormat(date);
            return (highest + "cal on " + date + " (DD/MM/YYYY)");
        }
    }

    // REQUIRES: f must be before t date-wise
    // EFFECTS: Returns the highest calorie day between [f,t] as string
    public String rangeHighestCalDay(LocalDate f, LocalDate t) {
        int highest = 0;
        String date = "";

        for (Day d: log) {
            if ((d.getDate().isAfter(f) || d.getDate().isEqual(f))
                    && (d.getDate().isBefore(t) || d.getDate().isEqual(t))) {
                if (d.caloriesAteToday() > highest) {
                    date = d.getDate().toString();
                    highest = d.caloriesAteToday();
                }
            }
        }
        if (highest == 0) {
            return "No calories consumed in range.";
        } else {
            date = dateFormat(date);
            return (highest + "cal on " + date + " (DD/MM/YYYY)");
        }
    }

    // REQUIRES: f must be before t date-wise
    // EFFECTS: Returns number of logged days between [f,t]
    public int daysInRange(LocalDate f, LocalDate t) {
        int count = 0;

        for (Day d: log) {
            if ((d.getDate().isAfter(f) || d.getDate().isEqual(f))
                    && (d.getDate().isBefore(t) || d.getDate().isEqual(t))) {
                count++;
            }
        }
        return count;
    }


    // REQUIRES: day is in valid DD/MM/YYYY format
    // EFFECTS: Returns String day's position in the List<Day>.
    public int getDayPosition(String day) {
        int count = 0;
        int date1 = Integer.parseInt(day.substring(0,2));
        int date2 = Integer.parseInt(day.substring(3,5));
        int date3 = Integer.parseInt(day.substring(6));

        LocalDate date = LocalDate.of(date3, date2, date1);

        for (Day d : log) {
            if (d.getDate().equals(date)) {
                return count;
            } else {
                count++;
            }
        }
        return -1;
    }

    // REQUIRES: day is in valid DD/MM/YYYY format
    // EFFECTS: Returns true day exists in log
    public Boolean checkDayInLog(String day) {
        int date1 = Integer.parseInt(day.substring(0,2));
        int date2 = Integer.parseInt(day.substring(3,5));
        int date3 = Integer.parseInt(day.substring(6));

        LocalDate date = LocalDate.of(date3, date2, date1);

        for (Day d : log) {
            if (d.getDate().equals(date)) {
                return true;
            }
        }
        return false;
    }

    // REQUIRES: String d entered as YYYY-MM-DD
    // EFFECT: helper to reformat string of LocalDate into DD/MM/YYYY
    public String dateFormat(String d) {
        String year = d.substring(0,4);
        String month = d.substring(5,7);
        String day = d.substring(8);

        return (day + "/" + month + "/" + year);
    }

    // EFFECTS: Converts the DietLog into a JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Log", daysToJson());
        return json;
    }

    // EFFECTS: Returns a list of days as a JSONArray
    private JSONArray daysToJson() {
        JSONArray jsonA = new JSONArray();

        for (Day d: log) {
            jsonA.put(d.toJson());
        }
        return jsonA;
    }
}
