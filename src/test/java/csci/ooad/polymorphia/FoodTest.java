package csci.ooad.polymorphia;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FoodTest {
    ArtifactFactory artifactFactory = new ArtifactFactory();

    @Test
    void testFoodCreation() {
        Food steak = artifactFactory.createFood("Steak");
        assert steak.healthValue() == ArtifactFactory.DEFAULT_FOOD_HEALTH_VALUE;
    }

    @Test
    void testSettingHealthValue() {
        Food steak = artifactFactory.createFood("Steak");
        assert steak.healthValue() == 1;
    }

    @Test
    void testToString() {
        Food steak = artifactFactory.createFood("Steak");
        System.out.println(steak);
        assertTrue(steak.toString().contains("Steak"));
    }

}