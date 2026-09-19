package bench;

import stack.ArrayStack;
import stack.CircularArrayStack;
import stack.MyStack;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class StackBenchmark {

    static final int[] SIZES = {10, 100, 1_000, 10_000, 100_000, 1_000_000};
    static final Random RNG = new Random(42);

    public static void run() throws Exception {
        Map<String, Supplier<MyStack<Integer>>> impls = new LinkedHashMap<>();
        impls.put("ArrayDinamico", ArrayStack::new);
        impls.put("ArrayCircular", CircularArrayStack::new);

        Csv csv = new Csv("results/stack_benchmark.csv", "implementacion,metodo,n,reps,tiempo_prom_us");

        for (Map.Entry<String, Supplier<MyStack<Integer>>> impl : impls.entrySet()) {
            String nombre = impl.getKey();
            Supplier<MyStack<Integer>> supplier = impl.getValue();

            for (int n : SIZES) {
                System.out.println(nombre + " n=" + n);
                int reps = n <= 1000 ? 300 : n <= 10_000 ? 150 : n <= 100_000 ? 50 : 15;
                int repsDel = Math.max(5, Math.min(100, n / 20));

                // push ------------------------------------------------------
                MyStack<Integer> s1 = supplier.get();
                for (int i = 0; i < n; i++) s1.push(i);
                long total = 0;
                for (int i = 0; i < reps; i++) {
                    long t0 = System.nanoTime();
                    s1.push(-1);
                    long t1 = System.nanoTime();
                    total += (t1 - t0);
                    s1.pop();
                }
                csv.row(nombre, "push", n, reps, (total / (double) reps) / 1000.0);

                // pop ---------------------------------------------------------
                MyStack<Integer> s2 = supplier.get();
                for (int i = 0; i < n; i++) s2.push(i);
                total = 0;
                for (int i = 0; i < reps; i++) {
                    s2.push(-1);
                    long t0 = System.nanoTime();
                    s2.pop();
                    long t1 = System.nanoTime();
                    total += (t1 - t0);
                }
                csv.row(nombre, "pop", n, reps, (total / (double) reps) / 1000.0);

                // peek --------------------------------------------------------
                MyStack<Integer> s3 = supplier.get();
                for (int i = 0; i < n; i++) s3.push(i);
                total = 0;
                for (int i = 0; i < reps; i++) {
                    long t0 = System.nanoTime();
                    s3.peek();
                    long t1 = System.nanoTime();
                    total += (t1 - t0);
                }
                csv.row(nombre, "peek", n, reps, (total / (double) reps) / 1000.0);

                // delete ------------------------------------------------------
                MyStack<Integer> s4 = supplier.get();
                for (int i = 0; i < n; i++) s4.push(i);
                total = 0;
                for (int i = 0; i < repsDel; i++) {
                    int target = RNG.nextInt(n);
                    long t0 = System.nanoTime();
                    s4.delete(target);
                    long t1 = System.nanoTime();
                    total += (t1 - t0);
                }
                csv.row(nombre, "delete", n, repsDel, (total / (double) repsDel) / 1000.0);
            }
        }
        csv.close();
    }
}
