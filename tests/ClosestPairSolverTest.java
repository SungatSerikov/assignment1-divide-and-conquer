import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClosestPairSolverTest {

    @Test
    void findsClosestPair() {
        Point[] points = {
                new Point(0, 0),
                new Point(5, 5),
                new Point(1, 1),
                new Point(10, 10)
        };

        ClosestPairSolver solver = new ClosestPairSolver();

        assertEquals(
                Math.sqrt(2),
                solver.closestDistance(points),
                1e-9
        );
    }

    @Test
    void worksWithTwoPoints() {
        Point[] points = {
                new Point(0, 0),
                new Point(3, 4)
        };

        ClosestPairSolver solver = new ClosestPairSolver();

        assertEquals(
                5.0,
                solver.closestDistance(points),
                1e-9
        );
    }

    @Test
    void worksWithDuplicatePoints() {
        Point[] points = {
                new Point(1, 1),
                new Point(5, 5),
                new Point(1, 1)
        };

        ClosestPairSolver solver = new ClosestPairSolver();

        assertEquals(
                0.0,
                solver.closestDistance(points),
                1e-9
        );
    }

    @Test
    void matchesBruteForceOnRandomInputs() {
        Random random = new Random(42);

        for (int test = 0; test < 50; test++) {

            int size = random.nextInt(100) + 2;

            Point[] points = new Point[size];

            for (int i = 0; i < size; i++) {
                points[i] = new Point(
                        random.nextDouble() * 1000,
                        random.nextDouble() * 1000
                );
            }

            double expected = bruteForce(points);

            ClosestPairSolver solver =
                    new ClosestPairSolver();

            double actual =
                    solver.closestDistance(points);

            assertEquals(expected, actual, 1e-9);
        }
    }

    @Test
    void matchesBruteForceWith2000Points() {
        Random random = new Random(123);

        Point[] points = new Point[2000];

        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(
                    random.nextDouble() * 10000,
                    random.nextDouble() * 10000
            );
        }

        double expected = bruteForce(points);

        ClosestPairSolver solver =
                new ClosestPairSolver();

        double actual =
                solver.closestDistance(points);

        assertEquals(expected, actual, 1e-9);
    }

    @Test
    void rejectsTooFewPoints() {
        ClosestPairSolver solver =
                new ClosestPairSolver();

        assertThrows(
                IllegalArgumentException.class,
                () -> solver.closestDistance(
                        new Point[]{}
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> solver.closestDistance(
                        new Point[]{
                                new Point(1, 1)
                        }
                )
        );
    }

    private double bruteForce(Point[] points) {
        double minDistance =
                Double.POSITIVE_INFINITY;

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {

                double dx =
                        points[i].getX()
                                - points[j].getX();

                double dy =
                        points[i].getY()
                                - points[j].getY();

                double distance =
                        Math.sqrt(dx * dx + dy * dy);

                minDistance =
                        Math.min(minDistance, distance);
            }
        }

        return minDistance;
    }
}