package bench;

import list.DoublyLinkedListNoTail;
import list.DoublyLinkedListWithTail;
import list.MyList;
import list.Node;
import list.SinglyLinkedListNoTail;
import list.SinglyLinkedListWithTail;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class ListBenchmark {

    static final Random RNG = new Random(42);
    static final int[] SIZES = {10, 100, 1_000, 10_000, 100_000, 1_000_000};

    static int repsBusqueda(int n) {
        int r = n / 20;
        if (r < 5) r = 5;
        if (r > 100) r = 100;
        return r;
    }

    static int repsExtremos(int n)
    {
        if (n <= 1000) return 300;
        if (n <= 10_000) return 150;
        if (n <= 100_000) return 50;
        return 15;
    }

    public static void run() throws Exception {
        Map<String, Supplier<MyList<Integer>>> impls = new LinkedHashMap<>();
        impls.put("SinglyNoTail", SinglyLinkedListNoTail::new);
        impls.put("SinglyWithTail", SinglyLinkedListWithTail::new);
        impls.put("DoublyNoTail", DoublyLinkedListNoTail::new);
        impls.put("DoublyWithTail", DoublyLinkedListWithTail::new);

        Csv csv = new Csv("results/list_benchmark.csv", "implementacion,metodo,n,reps,tiempo_prom_us");

        for (Map.Entry<String, Supplier<MyList<Integer>>> impl : impls.entrySet()) {
            String nombre = impl.getKey();
            Supplier<MyList<Integer>> supplier = impl.getValue();

            for (int n : SIZES) {
                System.out.println(nombre + " n=" + n);
                int repsE = repsExtremos(n);
                int repsD = repsBusqueda(n);

                {
                    MyList<Integer> lista = supplier.get();
                    for (int i = 0; i < n; i++) lista.pushFront(i);
                    long total = 0;
                    for (int i = 0; i < repsE; i++) {
                        long t0 = System.nanoTime();
                        lista.pushFront(-1);
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                        lista.popFront();
                    }
                    csv.row(nombre, "pushFront", n, repsE, (total / (double) repsE) / 1000.0);
                }

                {
                    MyList<Integer> lista = supplier.get();
                    for (int i = 0; i < n; i++) lista.pushFront(i);
                    long total = 0;
                    for (int i = 0; i < repsE; i++) {
                        long t0 = System.nanoTime();
                        lista.pushBack(-1);
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                        lista.popBack();
                    }
                    csv.row(nombre, "pushBack", n, repsE, (total / (double) repsE) / 1000.0);
                }

                {
                    MyList<Integer> lista = supplier.get();
                    for (int i = 0; i < n; i++) lista.pushFront(i);
                    long total = 0;
                    for (int i = 0; i < repsE; i++) {
                        lista.pushFront(-1);
                        long t0 = System.nanoTime();
                        lista.popFront();
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    csv.row(nombre, "popFront", n, repsE, (total / (double) repsE) / 1000.0);
                }

                {
                    MyList<Integer> lista = supplier.get();
                    for (int i = 0; i < n; i++) lista.pushFront(i);
                    long total = 0;
                    for (int i = 0; i < repsE; i++) {
                        lista.pushBack(-1);
                        long t0 = System.nanoTime();
                        lista.popBack();
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    csv.row(nombre, "popBack", n, repsE, (total / (double) repsE) / 1000.0);
                }

                {
                    MyList<Integer> lista = supplier.get();
                    for (int i = 0; i < n; i++) lista.pushFront(i);
                    long total = 0;
                    for (int i = 0; i < repsE; i++) {
                        int target = RNG.nextInt(n);
                        long t0 = System.nanoTime();
                        lista.find(target);
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    csv.row(nombre, "find", n, repsE, (total / (double) repsE) / 1000.0);
                }

                {
                    MyList<Integer> lista = supplier.get();
                    for (int i = 0; i < n; i++) lista.pushFront(i);
                    long total = 0;
                    for (int i = 0; i < repsD; i++) {
                        int target = RNG.nextInt(n);
                        Node<Integer> nodo = lista.find(target);
                        if (nodo == null) continue;
                        long t0 = System.nanoTime();
                        lista.erase(nodo);
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    csv.row(nombre, "erase", n, repsD, (total / (double) repsD) / 1000.0);
                }

                {
                    MyList<Integer> lista = supplier.get();
                    for (int i = 0; i < n; i++) lista.pushFront(i);
                    long total = 0;
                    for (int i = 0; i < repsD; i++) {
                        int target = RNG.nextInt(n);
                        Node<Integer> nodo = lista.find(target);
                        if (nodo == null) continue;
                        long t0 = System.nanoTime();
                        lista.addBefore(nodo, -1);
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    csv.row(nombre, "addBefore", n, repsD, (total / (double) repsD) / 1000.0);
                }

                {
                    MyList<Integer> lista = supplier.get();
                    for (int i = 0; i < n; i++) lista.pushFront(i);
                    long total = 0;
                    for (int i = 0; i < repsD; i++) {
                        int target = RNG.nextInt(n);
                        Node<Integer> nodo = lista.find(target);
                        if (nodo == null) continue;
                        long t0 = System.nanoTime();
                        lista.addAfter(nodo, -1);
                        long t1 = System.nanoTime();
                        total += (t1 - t0);
                    }
                    csv.row(nombre, "addAfter", n, repsD, (total / (double) repsD) / 1000.0);
                }
            }
        }
        csv.close();
    }
}
