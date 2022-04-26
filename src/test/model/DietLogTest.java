package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DietLogTest {
    private DietLog testLog;

    @BeforeEach
    public void setup() {
        testLog = new DietLog();

        Day d1 = new Day(01,01,2020);
        d1.addFood("eggs",120, FoodItem.FoodOrDrink.FOOD, FoodItem.MealType.BREAKFAST);

        Day d2 = new Day(02,01,2020);
        d2.addFood("steak",500, FoodItem.FoodOrDrink.FOOD, FoodItem.MealType.DINNER);

        testLog.addDay(d1);
        testLog.addDay(d2);
    }

    @Test
    public void testAverageCal() {
        assertEquals(310,testLog.averageCal());

        DietLog noLog = new DietLog();
        assertEquals(0,noLog.averageCal());
    }

    @Test
    public void testAverageCalRange() {
        Day d3 = new Day(03,01,1990);
        d3.addFood("smoothie",1000, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.SNACK);

        Day d4 = new Day(04,01,1990);
        d4.addFood("bacon",400, FoodItem.FoodOrDrink.FOOD, FoodItem.MealType.LUNCH);

        LocalDate from = LocalDate.of(2020,01,01);
        LocalDate to = LocalDate.of(2020,01,02);

        assertEquals(2,testLog.daysInRange(from,to));
        assertEquals(310,testLog.rangeAverageCal(from,to));
    }

    @Test
    public void testAverageCalNotInRange() {
        DietLog badLog = new DietLog();
        Day badDay1 = new Day(12,12,2020);
        Day badDay2 = new Day(12,12,2019);
        badLog.addDay(badDay1);
        badLog.addDay(badDay2);
        LocalDate from = LocalDate.of(2020,01,01);
        LocalDate to = LocalDate.of(2020,01,02);

        assertEquals(0,badLog.daysInRange(from,to));
        assertEquals(0,badLog.rangeAverageCal(from,to));

    }

    @Test
    public void testMostCalDay () {
        Day d3 = new Day(03,01,2020);
        d3.addFood("smoothie",200, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.SNACK);

        assertEquals("500cal on 02/01/2020 (DD/MM/YYYY)", testLog.highestCalDay());
    }

    @Test
    public void testMostCalNoDays() {
        DietLog badLog = new DietLog();
        Day badDay = new Day(12,12,2020);
        badDay.addFood("nothing",0, FoodItem.FoodOrDrink.FOOD, FoodItem.MealType.SNACK);
        badLog.addDay(badDay);

        assertEquals("No calories consumed", badLog.highestCalDay());



    }

    @Test
    public void testMostCalDayRange() {
        Day d3 = new Day(03,01,1990);
        d3.addFood("smoothie",1000, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.SNACK);

        Day d4 = new Day(04,01,1990);
        d4.addFood("bacon",400, FoodItem.FoodOrDrink.FOOD, FoodItem.MealType.LUNCH);

        LocalDate from = LocalDate.of(2020,01,01);
        LocalDate to = LocalDate.of(2020,01,02);

        assertEquals("500cal on 02/01/2020 (DD/MM/YYYY)",testLog.rangeHighestCalDay(from,to));

    }

    @Test
    public void testMostCalDayNotInRange() {
        DietLog badLog = new DietLog();
        Day badDay1 = new Day(12,12,2020);
        badDay1.addFood("nothing",0, FoodItem.FoodOrDrink.FOOD, FoodItem.MealType.SNACK);
        badLog.addDay(badDay1);

        Day badDay2 = new Day(12,12,2019);
        badLog.addDay(badDay2);

        Day badDay3 = new Day(01,01,2020);
        badDay3.addFood("Nothing",0, FoodItem.FoodOrDrink.FOOD, FoodItem.MealType.LUNCH);
        badLog.addDay(badDay3);

        LocalDate from = LocalDate.of(2020,01,01);
        LocalDate to = LocalDate.of(2020,01,02);

        assertEquals("No calories consumed in range.", badLog.rangeHighestCalDay(from,to));
    }

    @Test
    public void testCheckDate() {
        assertTrue(testLog.checkDayInLog("01/01/2020"));
        assertFalse(testLog.checkDayInLog("01/05/1999"));
    }

    @Test
    public void testGetDatePosition() {
        assertEquals(0,testLog.getDayPosition("01/01/2020"));
        assertEquals(-1,testLog.getDayPosition("01/05/1999"));
    }

    @Test
    public void testGetSize() {
        assertEquals(2,testLog.getLogSize());
    }

    @Test
    public void testGetDay() {
        Day test = new Day(01,01,2020);
        test.addFood("eggs",120, FoodItem.FoodOrDrink.FOOD, FoodItem.MealType.BREAKFAST);

        testLog.addDay(test);

        assertEquals(test,testLog.getDay(2));
    }

    @Test
    public void tesGetLog() {
        assertEquals(2, testLog.getLog().size());

        Day day3 = new Day(12,12,2020);
        testLog.addDay(day3);

        assertEquals(3,testLog.getLog().size());
    }

    @Test
    public void testDateFormat() {
        assertEquals("01/01/2020", testLog.dateFormat("2020-01-01"));
    }

    @Test
    public void testRemoveDay() {
        Day badDay1 = new Day(01,01,2020);
        Day badDay2 = new Day(12,12,3020);

        testLog.removeDay(badDay1);
        testLog.removeDay(badDay2);
        assertEquals(1,testLog.getLogSize());
    }

    @Test
    public void testPieDataAllDays() {
        Day testDay1 = new Day(01,01,2020);
        Day testDay2 = new Day(12,12,2020);

        DietLog newLog = new DietLog();

        testDay1.addFood("eggs",100, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.LUNCH);
        testDay1.addFood("milk",100, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.LUNCH);
        testDay1.addFood("milk",200, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.BREAKFAST);

        testDay2.addFood("eggs",50, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.SNACK);
        testDay2.addFood("milk",100, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.LUNCH);
        testDay2.addFood("milk",75, FoodItem.FoodOrDrink.BEVERAGE, FoodItem.MealType.DINNER);

        newLog.addDay(testDay1);
        newLog.addDay(testDay2);

        Map<String, Integer> testMap = newLog.pieDataAllDays();

        assertEquals(4,testMap.size());

        System.out.println(testMap.get("Breakfast"));
        System.out.println(testMap.get("Lunch"));
        System.out.println(testMap.get("Dinner"));
        System.out.println(testMap.get("Snack"));
    }
}
