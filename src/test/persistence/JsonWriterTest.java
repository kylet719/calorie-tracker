package persistence;

import model.Day;
import model.DietLog;
import model.FoodItem;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest{
    @Test
    void testWriterInvalidFile() {
        try {
            DietLog log = new DietLog();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testEmptyLog() {
        try {
            DietLog log = new DietLog();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyLog.json");
            writer.open();
            writer.write(log);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyLog.json");
            log = reader.read();
            checkDietLog(0,log);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            DietLog log = new DietLog();
            Day d1 = new Day(01,01,2020);
            d1.addFood("Steak",500, FoodItem.FoodOrDrink.FOOD, FoodItem.MealType.SNACK);
            d1.addFood("Smoothie", 250, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.DINNER);
            Day d2 = new Day(02,01,2020);
            d2.addFood("Fries", 750, FoodItem.FoodOrDrink.FOOD, FoodItem.MealType.SNACK);
            log.addDay(d1);
            log.addDay(d2);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralLog.json");
            writer.open();
            writer.write(log);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralLog.json");
            log = reader.read();
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
            fail("Exception should not have been thrown");
        }
    }
}
