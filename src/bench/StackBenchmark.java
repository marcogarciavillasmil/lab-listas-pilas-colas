package bench;

import stack.ArrayStack;
import stack.CircularArrayStack;
import stack.MyStack;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class StackBenchmark {

    static final Random RNG = new Random(42);
    static final int[] SIZES = {10, 100, 1_000, 10_000, 100_000, 1_000_000};
    static final int LOTE = 1000;
    static final int LOTES = 50;
    static int sink;
    static volatile int cero = 0;

    public static void run() throws Exception {
        Map<String, Supplier<MyStack<Integer>>> impls = new LinkedHashMap<>();
        impls.put("ArrayDinamico", ArrayStack::new);
        impls.put("ArrayCircular", CircularArrayStack::new);

        System.out.println("calentando JVM...");
        Path tmp = Files.createTempFile("calentamiento", ".csv");
        Csv descarte = new Csv(tmp.toString(), "descarte");
        medir(impls, descarte, new int[]{10, 100, 1_000, 10_000});
        descarte.close();
        Files.delete(tmp);

        Csv csv = new Csv("results/stack_benchmark.csv", "implementacion,metodo,n,reps,tiempo_prom_us");
        medir(impls, csv, SIZES);
        csv.close();
    }

    static void medir(Map<String, Supplier<MyStack<Integer>>> impls, Csv csv, int[] tamanos) {
        for (Map.Entry<String, Supplier<MyStack<Integer>>> impl : impls.entrySet()) {
            String nombre = impl.getKey();
            Supplier<MyStack<Integer>> supplier = impl.getValue();

            for (int n : tamanos) {
                System.out.println(nombre + " n=" + n);
                int repsDel = Math.max(5, Math.min(100, n / 20));
                int ops = LOTES * LOTE;
                System.gc();

                {
                    MyStack<Integer> pila = supplier.get();
                    for (int i = 0; i < n; i++) pila.push(i);
                    long total = 0;
                    for (int r = 0; r < LOTES; r++) {
                        long t0 = System.nanoTime();
                        for (int i = 0; i < LOTE; i++) pila.push(-1);
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                        for (int i = 0; i < LOTE; i++) pila.pop();
                    }
                    csv.row(nombre, "push", n, ops, (total / (double) ops) / 1000.0);
                }

                {
                    MyStack<Integer> pila = supplier.get();
                    for (int i = 0; i < n; i++) pila.push(i);
                    long total = 0;
                    for (int r = 0; r < LOTES; r++) {
                        for (int i = 0; i < LOTE; i++) pila.push(-1);
                        long t0 = System.nanoTime();
                        for (int i = 0; i < LOTE; i++) pila.pop();
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    csv.row(nombre, "pop", n, ops, (total / (double) ops) / 1000.0);
                }

                {
                    MyStack<Integer> pila = supplier.get();
                    for (int i = 0; i < n; i++) pila.push(i);
                    long total = 0;
                    int suma = 0;
                    for (int r = 0; r < LOTES; r++) {
                        long t0 = System.nanoTime();
                        for (int i = 0; i < LOTE; i++) suma += pila.peek() + cero;
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    sink += suma;
                    csv.row(nombre, "peek", n, ops, (total / (double) ops) / 1000.0);
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

                {
                    int construcciones = n <= 1000 ? 200 : n <= 100_000 ? 20 : 5;
                    long total = 0;
                    for (int c = 0; c < construcciones; c++) {
                        MyStack<Integer> pila = supplier.get();
                        long t0 = System.nanoTime();
                        for (int i = 0; i < n; i++) pila.push(i);
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    long pushes = (long) construcciones * n;
                    csv.row(nombre, "pushCrecimiento", n, pushes, (total / (double) pushes) / 1000.0);
                }
            }
        }
    }
}
