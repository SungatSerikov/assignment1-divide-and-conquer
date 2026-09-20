import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

public class ClosestPairSolver {

    private long distanceCalculations;
    private long recursiveCalls;
    private int maxDepth;

    public double closestDistance(Point[] points) {
        distanceCalculations = 0;
        recursiveCalls = 0;
        maxDepth = 0;

        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("At least two points are required");
        }

        Point[] pointsByX = points.clone();
        Arrays.sort(pointsByX, Comparator.comparingDouble(Point::getX));


        Point[] pointsByY = points.clone();
        Arrays.sort(pointsByY, Comparator.comparingDouble(Point::getY));

        return closest(pointsByX, pointsByY, 1);
    }

    private double closest(Point[] pointsByX, Point[] pointsByY, int depth) {
        recursiveCalls++;

        maxDepth = Math.max(maxDepth, depth);

        int n = pointsByX.length;

        if (n <= 3) {
            return bruteForce(pointsByX);
        }

        int mid = n / 2;

        double midX = pointsByX[mid].getX();

        Point[] leftX = Arrays.copyOfRange(pointsByX, 0, mid);

        Point[] rightX = Arrays.copyOfRange(pointsByX, mid, n);

        HashSet<Point> leftSet = new HashSet<>(Arrays.asList(leftX));

        Point[] leftY = new Point[leftX.length];
        Point[] rightY = new Point[rightX.length];

        int leftIndex = 0;
        int rightIndex = 0;


        for (Point point : pointsByY) {
            if (leftSet.contains(point)) {
                leftY[leftIndex] = point;
                leftIndex++;
            } else {
                rightY[rightIndex] = point;
                rightIndex++;
            }
        }

        double leftDistance =
                closest(leftX, leftY, depth + 1);

        double rightDistance =
                closest(rightX, rightY, depth + 1);

        double d =
                Math.min(leftDistance, rightDistance);

        Point[] strip = new Point[n];
        int stripSize = 0;

        for (Point point : pointsByY) {
            if (Math.abs(point.getX() - midX) < d) {
                strip[stripSize] = point;
                stripSize++;
            }
        }

        for (int i = 0; i < stripSize; i++) {
            for (int j = i + 1; j < stripSize; j++) {

                if (strip[j].getY() - strip[i].getY() >= d) {
                    break;
                }

                double currentDistance = distance(strip[i], strip[j]);

                d = Math.min(d, currentDistance);
            }
        }

        return d;
    }

    private double bruteForce(Point[] points) {
        double minDistance = Double.POSITIVE_INFINITY;

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {

                double currentDistance = distance(points[i], points[j]);

                minDistance = Math.min(minDistance, currentDistance);
            }
        }

        return minDistance;
    }

    private double distance(Point a, Point b) {
        distanceCalculations++;

        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();

        return Math.sqrt(dx * dx + dy * dy);
    }

    public long getDistanceCalculations() {
        return distanceCalculations;
    }

    public long getRecursiveCalls() {
        return recursiveCalls;
    }

    public int getMaxDepth() {
        return maxDepth;
    }
}