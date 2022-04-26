package persistence;

import model.Day;
import model.DietLog;
import model.FoodItem;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            DietLog log = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyLog() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyLog.json");
        try {
            DietLog log = reader.read();
            checkDietLog(0,log);

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralLog() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralLog.json");
        try {
            DietLog log = reader.read();
            checkDietLog(2,log);
            LocalDate date1 = LocalDate.of(2020,01,01);
            LocalDate date2 = LocalDate.of(2020,01,02);

            checkDay(date1,2,log.getDay(0));
            checkDay(date2, 1, log.getDay(1));

            Day day1Test = log.getDay(0);
            Day day2Test = log.getDay(1);

            checkFood("Steak",500, FoodItem.FoodOrDrink.FOOD, FoodItem.MealType.SNACK, day1Test.getFoodInList(0));
            checkFood("Smoothie", 250, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.DINNER, day1Test.getFoodInList(1));
            checkFood("Fries", 750, FoodItem.FoodOrDrink.FOOD, FoodItem.MealType.SNACK, day2Test.getFoodInList(0));

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
