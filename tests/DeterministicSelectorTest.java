import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeterministicSelectorTest {

    @Test
    void selectsMinimum() {
        int[] array = {8, 3, 7, 1, 5};

        DeterministicSelector selector = new DeterministicSelector();

        assertEquals(1, selector.select(array, 0));
    }

    @Test
    void selectsMedian() {
        int[] array = {8, 3, 7, 1, 5};

        DeterministicSelector selector = new DeterministicSelector();

        assertEquals(5, selector.select(array, 2));
    }

    @Test
    void selectsMaximum() {
        int[] array = {8, 3, 7, 1, 5};

        DeterministicSelector selector = new DeterministicSelector();

        assertEquals(8, selector.select(array, 4));
    }

    @Test
    void worksWithDuplicates() {
        int[] array = {5, 2, 5, 1, 2, 5, 1};

        int[] sorted = array.clone();
        Arrays.sort(sorted);

        for (int k = 0; k < array.length; k++) {
            int[] copy = array.clone();

            DeterministicSelector selector =
                    new DeterministicSelector();

            assertEquals(sorted[k], selector.select(copy, k));
        }
    }

    @Test
    void passesManyRandomTests() {
        Random random = new Random(42);

        for (int test = 0; test < 100; test++) {
            int size = random.nextInt(100) + 1;

            int[] array = new int[size];

            for (int i = 0; i < size; i++) {
                array[i] = random.nextInt(1000) - 500;
            }

            int[] sorted = array.clone();
            Arrays.sort(sorted);

            int k = random.nextInt(size);

            int[] copy = array.clone();

            DeterministicSelector selector =
                    new DeterministicSelector();

            assertEquals(
                    sorted[k],
                    selector.select(copy, k)
            );
        }
    }

    @Test
    void rejectsInvalidK() {
        int[] array = {1, 2, 3};

        DeterministicSelector selector =
                new DeterministicSelector();

        assertThrows(
                IllegalArgumentException.class,
                () -> selector.select(array, -1)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> selector.select(array, 3)
        );
    }
}