import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Random;

public class Experiment {

    private static final int WARMUP_RUNS = 3;
    private static final int MEASURED_RUNS = 10;

    private static final int[] SIZES = {
            1_000,
            2_000,
            4_000,
            8_000,
            16_000,
            32_000
    };

    private static final String[] INPUT_TYPES = {
            "random", "sorted", "reverse", "duplicates"
    };

    private final Random random = new Random(42);

    public void run() {
        try {
            Files.createDirectories(Path.of("results"));

            try (BufferedWriter writer =
                         Files.newBufferedWriter(Path.of("results/results.csv"))) {

                writer.write(
                        "algorithm,inputType,n,avgTimeNs,"
                                + "avgMaxDepth,avgOperations,operationMetric"
                );
                writer.newLine();

                runArrayAlgorithm(writer, "MergeSort");
                runArrayAlgorithm(writer, "QuickSort");
                runArrayAlgorithm(writer, "DeterministicSelect");
                runClosestPair(writer);
            }

            System.out.println("Experiments finished.");
            System.out.println("Results saved to results/results.csv");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void runArrayAlgorithm(
            BufferedWriter writer,
            String algorithm
    ) throws IOException {

        for (int n : SIZES) {
            for (String type : INPUT_TYPES) {

                int[] original = createArray(n, type);

                // JVM warm-up
                for (int i = 0; i < WARMUP_RUNS; i++) {
                    measureArrayAlgorithm(
                            algorithm,
                            original.clone()
                    );
                }

                long totalTime = 0;
                long totalDepth = 0;
                long totalOperations = 0;

                for (int i = 0; i < MEASURED_RUNS; i++) {

                    long[] result =
                            measureArrayAlgorithm(
                                    algorithm,
                                    original.clone()
                            );

                    totalTime += result[0];
                    totalDepth += result[1];
                    totalOperations += result[2];
                }

                writeResult(
                        writer,
                        algorithm,
                        type,
                        n,
                        totalTime,
                        totalDepth,
                        totalOperations,
                        "comparisons"
                );
            }
        }
    }

    private long[] measureArrayAlgorithm(
            String algorithm,
            int[] array
    ) {

        if (algorithm.equals("MergeSort")) {

            MergeSorter sorter = new MergeSorter();

            long start = System.nanoTime();
            sorter.sort(array);
            long time = System.nanoTime() - start;

            return new long[]{
                    time,
                    sorter.getMaxDepth(),
                    sorter.getComparisons()
            };
        }

        if (algorithm.equals("QuickSort")) {

            QuickSorter sorter = new QuickSorter();

            long start = System.nanoTime();
            sorter.sort(array);
            long time = System.nanoTime() - start;

            return new long[]{
                    time,
                    sorter.getMaxDepth(),
                    sorter.getComparisons()
            };
        }

        DeterministicSelector selector =
                new DeterministicSelector();

        long start = System.nanoTime();
        selector.select(array, array.length / 2);
        long time = System.nanoTime() - start;

        return new long[]{
                time,
                selector.getMaxDepth(),
                selector.getComparisons()
        };
    }

    private void runClosestPair(
            BufferedWriter writer
    ) throws IOException {

        for (int n : SIZES) {

            Point[] points = createPoints(n);

            for (int i = 0; i < WARMUP_RUNS; i++) {
                new ClosestPairSolver()
                        .closestDistance(points);
            }

            long totalTime = 0;
            long totalDepth = 0;
            long totalOperations = 0;

            for (int i = 0; i < MEASURED_RUNS; i++) {

                ClosestPairSolver solver =
                        new ClosestPairSolver();

                long start = System.nanoTime();
                solver.closestDistance(points);
                long time = System.nanoTime() - start;

                totalTime += time;
                totalDepth += solver.getMaxDepth();
                totalOperations +=
                        solver.getDistanceCalculations();
            }

            writeResult(
                    writer,
                    "ClosestPair",
                    "random-points",
                    n,
                    totalTime,
                    totalDepth,
                    totalOperations,
                    "distanceCalculations"
            );
        }
    }

    private int[] createArray(int n, String type) {

        int[] array = new int[n];

        for (int i = 0; i < n; i++) {

            switch (type) {

                case "random":
                    array[i] =
                            random.nextInt(
                                    Math.max(1000, n * 2)
                            );
                    break;

                case "sorted":
                    array[i] = i;
                    break;

                case "reverse":
                    array[i] = n - i;
                    break;

                case "duplicates":
                    array[i] = random.nextInt(10);
                    break;
            }
        }

        return array;
    }

    private Point[] createPoints(int n) {

        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {
            points[i] = new Point(
                    random.nextDouble() * 10000,
                    random.nextDouble() * 10000
            );
        }

        return points;
    }

    private void writeResult(
            BufferedWriter writer,
            String algorithm,
            String type,
            int n,
            long time,
            long depth,
            long operations,
            String metric
    ) throws IOException {

        String line = String.format(
                Locale.US,
                "%s,%s,%d,%.2f,%.2f,%.2f,%s",
                algorithm,
                type,
                n,
                (double) time / MEASURED_RUNS,
                (double) depth / MEASURED_RUNS,
                (double) operations / MEASURED_RUNS,
                metric
        );

        writer.write(line);
        writer.newLine();

        System.out.println(line);
    }
}