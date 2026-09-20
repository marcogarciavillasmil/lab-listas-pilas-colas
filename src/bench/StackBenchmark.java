package bench;

import stack.ArrayStack;
import stack.MyStack;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class StackBenchmark {

    static final Random RNG = new Random(42);
    static final int[] SIZES = {10, 100, 1_000, 10_000, 100_000, 1_000_000};

    public static void run() throws Exception {
        Map<String, Supplier<MyStack<Integer>>> impls = new LinkedHashMap<>();
        impls.put("ArrayDinamico", ArrayStack::new);

        Csv csv = new Csv("results/stack_benchmark.csv", "implementacion,metodo,n,reps,tiempo_prom_us");

        for (Map.Entry<String, Supplier<MyStack<Integer>>> impl : impls.entrySet()) {
            String nombre = impl.getKey();
            Supplier<MyStack<Integer>> supplier = impl.getValue();

            for (int n : SIZES) {
                System.out.println(nombre + " n=" + n);
                int reps = n <= 1000 ? 300 : n <= 10_000 ? 150 : n <= 100_000 ? 50 : 15;
                int repsDel = Math.max(5, Math.min(100, n / 20));

                {
                    MyStack<Integer> pila = supplier.get();
                    for (int i = 0; i < n; i++) pila.push(i);
                    long total = 0;
                    for (int i = 0; i < reps; i++) {
                        long t0 = System.nanoTime();
                        pila.push(-1);
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                        pila.pop();
                    }
                    csv.row(nombre, "push", n, reps, (total / (double) reps) / 1000.0);
                }

                {
                    MyStack<Integer> pila = supplier.get();
                    for (int i = 0; i < n; i++) pila.push(i);
                    long total = 0;
                    for (int i = 0; i < reps; i++) {
                        pila.push(-1);
                        long t0 = System.nanoTime();
                        pila.pop();
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    csv.row(nombre, "pop", n, reps, (total / (double) reps) / 1000.0);
                }

                {
                    MyStack<Integer> pila = supplier.get();
                    for (int i = 0; i < n; i++) pila.push(i);
                    long total = 0;
                    for (int i = 0; i < reps; i++) {
                        long t0 = System.nanoTime();
                        pila.peek();
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    csv.row(nombre, "peek", n, reps, (total / (double) reps) / 1000.0);
                }

                {
                    MyStack<Integer> pila = supplier.get();
                    for (int i = 0; i < n; i++) pila.push(i);
                    long total = 0;
                    for (int i = 0; i < repsDel; i++) {
                        int target = RNG.nextInt(n);
                        long t0 = System.nanoTime();
                        pila.delete(target);
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    csv.row(nombre, "delete", n, repsDel, (total / (double) repsDel) / 1000.0);
                }
            }
        }
        csv.close();
    }
}
