package bench;

// clase para correr todos los benchmarks y generar los csv en results/.
// las graficas y el analisis final se hacen aparte en python (ver /analisis).
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
