package bench;

import queue.ArrayQueue;
import queue.CircularArrayQueue;
import queue.MyQueue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class QueueBenchmark {

    static final Random RNG = new Random(42);
    static final int[] SIZES = {10, 100, 1_000, 10_000, 100_000, 1_000_000};
    static final int LOTE = 1000;
    static final int LOTES = 50;
    static int sink;
    static volatile int cero = 0;

    public static void run() throws Exception {
        Map<String, Supplier<MyQueue<Integer>>> impls = new LinkedHashMap<>();
        impls.put("ArrayDinamico", ArrayQueue::new);
        impls.put("ArrayCircular", CircularArrayQueue::new);

        System.out.println("calentando JVM...");
        Path tmp = Files.createTempFile("calentamiento", ".csv");
        Csv descarte = new Csv(tmp.toString(), "descarte");
        medir(impls, descarte, new int[]{10, 100, 1_000, 10_000});
        descarte.close();
        Files.delete(tmp);

        Csv csv = new Csv("results/queue_benchmark.csv", "implementacion,metodo,n,reps,tiempo_prom_us");
        medir(impls, csv, SIZES);
        csv.close();
    }

    static void medir(Map<String, Supplier<MyQueue<Integer>>> impls, Csv csv, int[] tamanos) {
        for (Map.Entry<String, Supplier<MyQueue<Integer>>> impl : impls.entrySet()) {
            String nombre = impl.getKey();
            Supplier<MyQueue<Integer>> supplier = impl.getValue();

            for (int n : tamanos) {
                System.out.println(nombre + " n=" + n);
                int repsDel = Math.max(5, Math.min(100, n / 20));
                int ops = LOTES * LOTE;
                int loteDeq = nombre.equals("ArrayDinamico") ? Math.max(1, Math.min(LOTE, 2_000_000 / n)) : LOTE;
                int opsDeq = LOTES * loteDeq;
                System.gc();

                {
                    long total = 0;
                    for (int r = 0; r < LOTES; r++) {
                        MyQueue<Integer> cola = supplier.get();
                        for (int i = 0; i < n; i++) cola.enqueue(i);
                        long t0 = System.nanoTime();
                        for (int i = 0; i < LOTE; i++) cola.enqueue(-1);
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    csv.row(nombre, "enqueue", n, ops, (total / (double) ops) / 1000.0);
                }

                {
                    MyQueue<Integer> cola = supplier.get();
                    for (int i = 0; i < n; i++) cola.enqueue(i);
                    long total = 0;
                    for (int r = 0; r < LOTES; r++) {
                        for (int i = 0; i < loteDeq; i++) cola.enqueue(-1);
                        long t0 = System.nanoTime();
                        for (int i = 0; i < loteDeq; i++) cola.dequeue();
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    csv.row(nombre, "dequeue", n, opsDeq, (total / (double) opsDeq) / 1000.0);
                }

                {
                    MyQueue<Integer> cola = supplier.get();
                    for (int i = 0; i < n; i++) cola.enqueue(i);
                    long total = 0;
                    int suma = 0;
                    for (int r = 0; r < LOTES; r++) {
                        long t0 = System.nanoTime();
                        for (int i = 0; i < LOTE; i++) suma += cola.front() + cero;
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    sink += suma;
                    csv.row(nombre, "front", n, ops, (total / (double) ops) / 1000.0);
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
    }
}
