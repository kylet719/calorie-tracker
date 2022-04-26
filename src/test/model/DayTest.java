package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DayTest {
    private Day testDay;

    @BeforeEach
    public void setup() {
        testDay = new Day(01,01,2020);
    }

    @Test
    public void testAddFood() {
        testDay.addFood("eggs",120, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.LUNCH);
        testDay.addFood("milk",500, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.LUNCH);

        assertEquals(2,testDay.getAmountAte());
    }

    @Test
    public void testGetDate() {
        LocalDate d1 = LocalDate.of(2020,01,01);

        assertEquals(d1,testDay.getDate());
    }

    @Test
    public void testTotalCal() {
        testDay.addFood("eggs",120, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.LUNCH);
        testDay.addFood("milk",500, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.LUNCH);

        assertEquals(620,testDay.caloriesAteToday());
    }

    @Test
    public void testPrintFoods() {
        assertEquals("No foods", testDay.printFoods());
        testDay.addFood("eggs",120, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.LUNCH);
        testDay.addFood("milk",500, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.LUNCH);

        assertEquals("eggs - 120 calories\nmilk - 500 calories\n\nTotal Calories consumed: 620",
                testDay.printFoods());
    }

    @Test
    public void testGetDateAsString() {
        assertEquals("01/01/2020",testDay.getDateAsString());
    }

    @Test
    public void testBreakfastCal() {
        testDay.addFood("eggs",120, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.LUNCH);
        testDay.addFood("milk",500, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.LUNCH);
        assertEquals(620,testDay.lunchCal());

        assertEquals(0,testDay.breakfastCals());
        assertEquals(0,testDay.dinnerCal());
        assertEquals(0,testDay.snackCal());
        testDay.addFood("milk",500, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.BREAKFAST);
        assertEquals(500,testDay.breakfastCals());
    }

}
