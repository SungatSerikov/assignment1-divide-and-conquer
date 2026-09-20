import java.util.Random;

public class QuickSorter {
    private final Random random = new Random();

    private long comparisons;
    private long recursiveCalls;
    private int maxDepth;

    public void sort(int[] array) {
        comparisons = 0;
        recursiveCalls = 0;
        maxDepth = 0;

        if (array == null || array.length < 2) {
            return;
        }

        quickSort(array, 0, array.length - 1, 1);
    }

    private void quickSort(int[] array, int left, int right, int depth) {
        recursiveCalls++;

        if (depth > maxDepth) {
            maxDepth = depth;
        }

        while (left < right) {
            int pivotIndex = partition(array, left, right);

            int leftSize = pivotIndex - left;
            int rightSize = right - pivotIndex;

            if (leftSize < rightSize) {
                if (left < pivotIndex - 1) {
                     quickSort(array, left, pivotIndex - 1, depth + 1);
                }

                left = pivotIndex + 1;
            } else {
                if (pivotIndex + 1 < right) {
                    quickSort(array, pivotIndex + 1, right, depth + 1);
                }

                right = pivotIndex - 1;
            }
        }
    }

    private int partition(int[] array, int left, int right) {
        int randomIndex = left + random.nextInt(right - left + 1);

        swap(array, randomIndex, right);

        int pivot = array[right];

        int i = left;

        for (int j = left; j < right; j++) {
            comparisons++;

            if (array[j] <= pivot) {
                swap(array, i, j);
                i++;
            }
        }

        swap(array, i, right);

        return i;
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getRecursiveCalls() {
        return recursiveCalls;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

}
