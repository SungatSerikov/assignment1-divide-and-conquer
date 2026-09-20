public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("Starting experiments...");

        Experiment experiment = new Experiment();
        experiment.run();

        System.out.println("Experiments finished.");
        System.out.println("Results saved to results/results.csv");
    }
}