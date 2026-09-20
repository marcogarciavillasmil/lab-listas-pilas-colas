package bench;

public class BenchMain {
    public static void main(String[] args) throws Exception {
        System.out.println("== Benchmarks de List ==");
        ListBenchmark.run();

        System.out.println("== Benchmarks de MyStack ==");
        StackBenchmark.run();

        System.out.println("== Benchmarks de MyQueue ==");
        QueueBenchmark.run();

        System.out.println("listo, revisar carpeta results/");
    }
}
