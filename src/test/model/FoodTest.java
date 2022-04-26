package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.FoodItem.MealType.BREAKFAST;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FoodTest {
    private FoodItem item;

    @BeforeEach
    public void setup() {
        item = new FoodItem("eggs",120, FoodItem.FoodOrDrink.FOOD, FoodItem.MealType.BREAKFAST);
    }

    @Test
    public void testGetCal() {
        assertEquals(120, item.getCalories());
    }

    @Test
    public void testGetMealTime() {
        assertEquals(BREAKFAST, item.getMealTime());
    }
}
