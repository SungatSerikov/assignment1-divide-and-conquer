public class DeterministicSelector {

    private long comparisons;
    private long recursiveCalls;
    private int maxDepth;

    public int select(int[] array, int k) {
        comparisons = 0;
        recursiveCalls = 0;
        maxDepth = 0;

        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array must not be empty");
        }

        if (k < 0 || k >= array.length) {
            throw new IllegalArgumentException("k is out of range");
        }

        return select(array, 0, array.length - 1, k, 1);
    }

    private int select(int[] array, int left, int right, int k, int depth) {
        recursiveCalls++;

        if (depth > maxDepth) {
            maxDepth = depth;
        }

        if (left == right) {
            return array[left];
        }

        int pivot = medianOfMedians(array, left, right, depth);

        int[] equalRange = partition(array, left, right, pivot);

        if (k < equalRange[0]) {
            return select(array, left, equalRange[0] - 1, k, depth + 1);
        }

        if (k > equalRange[1]) {
            return select(array, equalRange[1] + 1, right, k, depth + 1);
        }

        return array[k];
    }

    private int medianOfMedians(int[] array, int left, int right, int depth) {
        int size = right - left + 1;

        if (size <= 5) {
            insertionSort(array, left, right);
            return array[left + size / 2];
        }

        int medianCount = 0;

        for (int groupStart = left; groupStart <= right; groupStart += 5) {
            int groupEnd = Math.min(groupStart + 4, right);

            insertionSort(array, groupStart, groupEnd);

            int medianIndex =
                    groupStart + (groupEnd - groupStart) / 2;

            swap(array, left + medianCount, medianIndex);

            medianCount++;
        }

        int mediansLeft = left;
        int mediansRight = left + medianCount - 1;
        int medianK = mediansLeft + medianCount / 2;

        return select(array, mediansLeft, mediansRight, medianK, depth + 1
        );
    }

    private int[] partition(int[] array, int left, int right, int pivot
    ) {
        int less = left;
        int current = left;
        int greater = right;

        while (current <= greater) {
            comparisons++;

            if (array[current] < pivot) {
                swap(array, less, current);
                less++;
                current++;
            } else {
                comparisons++;

                if (array[current] > pivot) {
                    swap(array, current, greater);
                    greater--;
                } else {
                    current++;
                }
            }
        }

        return new int[]{less, greater};
    }

    private void insertionSort(int[] array, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int value = array[i];
            int j = i - 1;

            while (j >= left) {
                comparisons++;

                if (array[j] <= value) {
                    break;
                }

                array[j + 1] = array[j];
                j--;
            }

            array[j + 1] = value;
        }
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