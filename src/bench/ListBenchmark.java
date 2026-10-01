package bench;

import list.DoublyLinkedListNoTail;
import list.DoublyLinkedListWithTail;
import list.MyList;
import list.Node;
import list.SinglyLinkedListNoTail;
import list.SinglyLinkedListWithTail;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class ListBenchmark {

    static final Random RNG = new Random(42);
    static final int[] SIZES = {10, 100, 1_000, 10_000, 100_000, 1_000_000};
    static final int LOTE = 1000;
    static final int LOTES = 50;

    static int repsBusqueda(int n) {
        int r = n / 20;
        if (r < 5) r = 5;
        if (r > 100) r = 100;
        return r;
    }

    static int loteLargo(int n) {
        return Math.max(1, Math.min(LOTE, 2_000_000 / n));
    }

    static int sink;
    static volatile int cero = 0;

    public static void run() throws Exception {
        Map<String, Supplier<MyList<Integer>>> impls = new LinkedHashMap<>();
        impls.put("SinglyNoTail", SinglyLinkedListNoTail::new);
        impls.put("SinglyWithTail", SinglyLinkedListWithTail::new);
        impls.put("DoublyNoTail", DoublyLinkedListNoTail::new);
        impls.put("DoublyWithTail", DoublyLinkedListWithTail::new);

        System.out.println("calentando JVM...");
        Path tmp = Files.createTempFile("calentamiento", ".csv");
        Csv descarte = new Csv(tmp.toString(), "descarte");
        medir(impls, descarte, new int[]{10, 100, 1_000, 10_000});
        descarte.close();
        Files.delete(tmp);

        Csv csv = new Csv("results/list_benchmark.csv", "implementacion,metodo,n,reps,tiempo_prom_us");
        medir(impls, csv, SIZES);
        csv.close();
    }

    static void medir(Map<String, Supplier<MyList<Integer>>> impls, Csv csv, int[] tamanos) {
        for (Map.Entry<String, Supplier<MyList<Integer>>> impl : impls.entrySet()) {
            String nombre = impl.getKey();
            Supplier<MyList<Integer>> supplier = impl.getValue();

            for (int n : tamanos) {
                System.out.println(nombre + " n=" + n);
                int largo = loteLargo(n);
                int repsD = repsBusqueda(n);
                System.gc();

                {
                    MyList<Integer> lista = supplier.get();
                    for (int i = 0; i < n; i++) lista.pushFront(i);
                    long total = 0;
                    for (int r = 0; r < LOTES; r++) {
                        long t0 = System.nanoTime();
                        for (int i = 0; i < LOTE; i++) lista.pushFront(-1);
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                        for (int i = 0; i < LOTE; i++) lista.popFront();
                    }
                    int ops = LOTES * LOTE;
                    csv.row(nombre, "pushFront", n, ops, (total / (double) ops) / 1000.0);
                }

                {
                    MyList<Integer> lista = supplier.get();
                    for (int i = 0; i < n; i++) lista.pushFront(i);
                    long total = 0;
                    for (int r = 0; r < LOTES; r++) {
                        for (int i = 0; i < largo; i++) lista.pushBack(-1);
                        long t0 = System.nanoTime();
                        for (int i = 0; i < largo; i++) lista.popBack();
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    int ops = LOTES * largo;
                    csv.row(nombre, "popBack", n, ops, (total / (double) ops) / 1000.0);
                }

                {
                    MyList<Integer> lista = supplier.get();
                    for (int i = 0; i < n; i++) lista.pushFront(i);
                    long total = 0;
                    for (int r = 0; r < LOTES; r++) {
                        long t0 = System.nanoTime();
                        for (int i = 0; i < largo; i++) lista.pushBack(-1);
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                        for (int i = 0; i < largo; i++) lista.popBack();
                    }
                    int ops = LOTES * largo;
                    csv.row(nombre, "pushBack", n, ops, (total / (double) ops) / 1000.0);
                }

                {
                    MyList<Integer> lista = supplier.get();
                    for (int i = 0; i < n; i++) lista.pushFront(i);
                    long total = 0;
                    for (int r = 0; r < LOTES; r++) {
                        for (int i = 0; i < LOTE; i++) lista.pushFront(-1);
                        long t0 = System.nanoTime();
                        for (int i = 0; i < LOTE; i++) lista.popFront();
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    int ops = LOTES * LOTE;
                    csv.row(nombre, "popFront", n, ops, (total / (double) ops) / 1000.0);
                }

                {
                    MyList<Integer> lista = supplier.get();
                    for (int i = 0; i < n; i++) lista.pushFront(i);
                    long total = 0;
                    int suma = 0;
                    for (int r = 0; r < LOTES; r++) {
                        long t0 = System.nanoTime();
                        for (int i = 0; i < LOTE; i++) suma += lista.topFront() + cero;
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    sink += suma;
                    int ops = LOTES * LOTE;
                    csv.row(nombre, "topFront", n, ops, (total / (double) ops) / 1000.0);
                }

                {
                    MyList<Integer> lista = supplier.get();
                    for (int i = 0; i < n; i++) lista.pushFront(i);
                    long total = 0;
                    int suma = 0;
                    for (int r = 0; r < LOTES; r++) {
                        long t0 = System.nanoTime();
                        for (int i = 0; i < largo; i++) suma += lista.topBack() + cero;
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    sink += suma;
                    int ops = LOTES * largo;
                    csv.row(nombre, "topBack", n, ops, (total / (double) ops) / 1000.0);
                }

                {
                    MyList<Integer> lista = supplier.get();
                    for (int i = 0; i < n; i++) lista.pushFront(i);
                    long total = 0;
                    int[] objetivos = new int[largo];
                    for (int r = 0; r < LOTES; r++) {
                        for (int i = 0; i < largo; i++) objetivos[i] = RNG.nextInt(n);
                        long t0 = System.nanoTime();
                        for (int i = 0; i < largo; i++) lista.find(objetivos[i]);
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    int ops = LOTES * largo;
                    csv.row(nombre, "find", n, ops, (total / (double) ops) / 1000.0);
                }

                {
                    MyList<Integer> lista = supplier.get();
                    for (int i = 0; i < n; i++) lista.pushFront(i);
                    long total = 0;
                    int hechas = 0;
                    for (int i = 0; i < repsD; i++) {
                        int target = RNG.nextInt(n);
                        Node<Integer> nodo = lista.find(target);
                        if (nodo == null) continue;
                        long t0 = System.nanoTime();
                        lista.erase(nodo);
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                        hechas++;
                    }
                    csv.row(nombre, "erase", n, hechas, hechas == 0 ? 0.0 : (total / (double) hechas) / 1000.0);
                }

                {
                    MyList<Integer> lista = supplier.get();
                    for (int i = 0; i < n; i++) lista.pushFront(i);
                    long total = 0;
                    int hechas = 0;
                    for (int i = 0; i < repsD; i++) {
                        int target = RNG.nextInt(n);
                        Node<Integer> nodo = lista.find(target);
                        if (nodo == null) continue;
                        long t0 = System.nanoTime();
                        lista.addBefore(nodo, -1);
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                        hechas++;
                    }
                    csv.row(nombre, "addBefore", n, hechas, hechas == 0 ? 0.0 : (total / (double) hechas) / 1000.0);
                }

                {
                    MyList<Integer> lista = supplier.get();
                    for (int i = 0; i < n; i++) lista.pushFront(i);
                    long total = 0;
                    int hechas = 0;
                    for (int i = 0; i < repsD; i++) {
                        int target = RNG.nextInt(n);
                        Node<Integer> nodo = lista.find(target);
                        if (nodo == null) continue;
                        long t0 = System.nanoTime();
                        lista.addAfter(nodo, -1);
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                        hechas++;
                    }
                    csv.row(nombre, "addAfter", n, hechas, hechas == 0 ? 0.0 : (total / (double) hechas) / 1000.0);
                }
            }
        }
    }
}
