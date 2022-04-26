package persistence;

import model.Day;
import model.DietLog;
import model.FoodItem;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkDietLog(int logSize, DietLog d) {
        assertEquals(logSize, d.getLogSize());
    }

    protected void checkDay(LocalDate date, int food, Day day) {
        assertEquals(date,day.getDate());
        assertEquals(food, day.getAmountAte());
    }

    protected void checkFood(String des, int cal, FoodItem.FoodOrDrink type, FoodItem.MealType me, FoodItem f) {
        assertEquals(des, f.getDescription());
        assertEquals(cal, f.getCalories());
        assertEquals(type,f.getType());
        assertEquals(me, f.getMealTime());
    }



}
