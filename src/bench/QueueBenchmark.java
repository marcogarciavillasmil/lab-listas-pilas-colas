package bench;

import queue.ArrayQueue;
import queue.MyQueue;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class QueueBenchmark {

    static final Random RNG = new Random(42);
    static final int[] SIZES = {10, 100, 1_000, 10_000, 100_000, 1_000_000};

    public static void run() throws Exception {
        Map<String, Supplier<MyQueue<Integer>>> impls = new LinkedHashMap<>();
        impls.put("ArrayDinamico", ArrayQueue::new);

        Csv csv = new Csv("results/queue_benchmark.csv", "implementacion,metodo,n,reps,tiempo_prom_us");

        for (Map.Entry<String, Supplier<MyQueue<Integer>>> impl : impls.entrySet()) {
            String nombre = impl.getKey();
            Supplier<MyQueue<Integer>> supplier = impl.getValue();

            for (int n : SIZES) {
                System.out.println(nombre + " n=" + n);
                int reps = n <= 1000 ? 300 : n <= 10_000 ? 150 : n <= 100_000 ? 50 : 15;
                int repsDel = Math.max(5, Math.min(100, n / 20));

                {
                    MyQueue<Integer> cola = supplier.get();
                    for (int i = 0; i < n; i++) cola.enqueue(i);
                    long total = 0;
                    for (int i = 0; i < reps; i++) {
                        long t0 = System.nanoTime();
                        cola.enqueue(-1);
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                        cola.delete(-1);
                    }
                    csv.row(nombre, "enqueue", n, reps, (total / (double) reps) / 1000.0);
                }

                {
                    MyQueue<Integer> cola = supplier.get();
                    for (int i = 0; i < n + reps; i++) cola.enqueue(i);
                    long total = 0;
                    for (int i = 0; i < reps; i++) {
                        long t0 = System.nanoTime();
                        cola.dequeue();
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    csv.row(nombre, "dequeue", n, reps, (total / (double) reps) / 1000.0);
                }

                {
                    MyQueue<Integer> cola = supplier.get();
                    for (int i = 0; i < n; i++) cola.enqueue(i);
                    long total = 0;
                    for (int i = 0; i < reps; i++) {
                        long t0 = System.nanoTime();
                        cola.front();
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    csv.row(nombre, "front", n, reps, (total / (double) reps) / 1000.0);
                }

                {
                    MyQueue<Integer> cola = supplier.get();
                    for (int i = 0; i < n; i++) cola.enqueue(i);
                    long total = 0;
                    for (int i = 0; i < repsDel; i++) {
                        int target = RNG.nextInt(n);
                        long t0 = System.nanoTime();
                        cola.delete(target);
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
