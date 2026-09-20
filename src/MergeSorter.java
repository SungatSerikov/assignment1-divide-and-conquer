public class MergeSorter {
    private static final int CUTOFF = 16;

    private long comparisons;
    private long recursiveCalls;
    private int maxDepth;

    public void sort(int[] arr) {
        comparisons = 0;
        recursiveCalls = 0;
        maxDepth = 0;

        if (arr == null || arr.length < 2) {
            return;
        }
        int[] buffer = new int[arr.length];
        mergeSort(arr,buffer,0,arr.length-1,1);
    }

    private void mergeSort(int[] arr, int[] buffer, int left, int right, int depth){
        recursiveCalls++;

        if (depth > maxDepth) {
            maxDepth = depth;
        }

        if(left>=right){
            return;
        }

        if(right-left+1<=CUTOFF){
            insertionSort(arr,left,right);
            return;
        }

        int mid = left + (right - left) / 2;

        mergeSort(arr,buffer,left,mid,depth+1);
        mergeSort(arr,buffer,mid+1,right,depth+1);
        merge(arr,buffer,left,mid,right);
    }

    private void merge(int[] arr, int[] buffer, int left, int mid, int right){
        int i = left;
        int j = mid+1;
        int k = left;

        while(i<=mid && j <=right){
            comparisons++;

            if (arr[i]<=arr[j]){
                buffer[k] = arr[i];
                i++;
            } else {
                buffer[k] = arr[j];
                j++;
            }
            k++;
        }
        while(i<= mid){
            buffer[k] = arr[i];
            i++;
            k++;
        }
        while(j<= right){
            buffer[k] = arr[j];
            j++;
            k++;
        }
        for (int x = left; x <= right; x++) {
            arr[x] = buffer[x];
        }
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
