import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
public class MergeSorterTest {

    @Test
    void sortsRandomArray() {
        int[] actual = {7, 2, 5, 1, 9, 3, 8};

        assertSameAsArraysSort(actual);
    }

    @Test
    void sortsAlreadySortedArray() {
        int[] actual = {1, 2, 3, 4, 5, 6};

        assertSameAsArraysSort(actual);
    }

    @Test
    void sortsReverseSortedArray() {
        int[] actual = {6, 5, 4, 3, 2, 1};

        assertSameAsArraysSort(actual);
    }

    @Test
    void sortsArrayWithDuplicates() {
        int[] actual = {5, 2, 5, 1, 2, 5, 1};

        assertSameAsArraysSort(actual);
    }

    @Test
    void sortsEmptyArray() {
        int[] actual = {};

        assertSameAsArraysSort(actual);
    }

    @Test
    void sortsSingleElementArray() {
        int[] actual = {42};

        assertSameAsArraysSort(actual);
    }

    @Test
    void sortsManyRandomArrays() {
        Random random = new Random(42);

        for (int test = 0; test < 100; test++) {
            int size = random.nextInt(200);

            int[] actual = new int[size];

            for (int i = 0; i < size; i++) {
                actual[i] = random.nextInt(1000) - 500;
            }

            assertSameAsArraysSort(actual);
        }
    }

    private void assertSameAsArraysSort(int[] actual) {
        int[] expected = actual.clone();

        Arrays.sort(expected);

        MergeSorter sorter = new MergeSorter();
        sorter.sort(actual);

        assertArrayEquals(expected, actual);
    }
}