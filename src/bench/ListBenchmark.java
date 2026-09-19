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

// corre los 8 metodos de List (pushfront, pushback, popfront, popback, find,
// erase, addbefore, addafter) en las 4 implementaciones y distintos tamanos,
// y deja todo en results/list_benchmark.csv
public class ListBenchmark {

    static final int[] SIZES = {10, 100, 1_000, 10_000, 100_000, 1_000_000};
    static final Random RNG = new Random(42);

    static int repsExtremos(int n) {
        if (n <= 1000) return 300;
        if (n <= 10_000) return 150;
        if (n <= 100_000) return 50;
        return 15;
    }

    static int repsBusqueda(int n) {
        // para erase/addBefore/addAfter no restauro el elemento borrado/insertado,
        // asi que uso pocas repeticiones para no vaciar/inflar demasiado la lista
        int r = n / 20;
        if (r < 5) r = 5;
        if (r > 100) r = 100;
        return r;
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

                // pushFront -------------------------------------------------
                MyList<Integer> l1 = supplier.get();
                for (int i = 0; i < n; i++) l1.pushFront(i);
                int repsE = repsExtremos(n);
                long total = 0;
                for (int i = 0; i < repsE; i++) {
                    long t0 = System.nanoTime();
                    l1.pushFront(-1);
                    long t1 = System.nanoTime();
                    total += (t1 - t0);
                    l1.popFront(); // deshago para no seguir creciendo
                }
                csv.row(nombre, "pushFront", n, repsE, (total / (double) repsE) / 1000.0);

                // pushBack --------------------------------------------------
                MyList<Integer> l2 = supplier.get();
                for (int i = 0; i < n; i++) l2.pushFront(i);
                total = 0;
                for (int i = 0; i < repsE; i++) {
                    long t0 = System.nanoTime();
                    l2.pushBack(-1);
                    long t1 = System.nanoTime();
                    total += (t1 - t0);
                    l2.popBack();
                }
                csv.row(nombre, "pushBack", n, repsE, (total / (double) repsE) / 1000.0);

                // popFront ----------------------------------------------------
                MyList<Integer> l3 = supplier.get();
                for (int i = 0; i < n; i++) l3.pushFront(i);
                total = 0;
                for (int i = 0; i < repsE; i++) {
                    l3.pushFront(-1); // sin medir
                    long t0 = System.nanoTime();
                    l3.popFront();
                    long t1 = System.nanoTime();
                    total += (t1 - t0);
                }
                csv.row(nombre, "popFront", n, repsE, (total / (double) repsE) / 1000.0);

                // popBack -----------------------------------------------------
                MyList<Integer> l4 = supplier.get();
                for (int i = 0; i < n; i++) l4.pushFront(i);
                total = 0;
                for (int i = 0; i < repsE; i++) {
                    l4.pushBack(-1);
                    long t0 = System.nanoTime();
                    l4.popBack();
                    long t1 = System.nanoTime();
                    total += (t1 - t0);
                }
                csv.row(nombre, "popBack", n, repsE, (total / (double) repsE) / 1000.0);

                // find ----------------------------------------------------------
                MyList<Integer> l5 = supplier.get();
                for (int i = 0; i < n; i++) l5.pushFront(i);
                total = 0;
                for (int i = 0; i < repsE; i++) {
                    int target = RNG.nextInt(n);
                    long t0 = System.nanoTime();
                    l5.find(target);
                    long t1 = System.nanoTime();
                    total += (t1 - t0);
                }
                csv.row(nombre, "find", n, repsE, (total / (double) repsE) / 1000.0);

                // erase -----------------------------------------------------------
                MyList<Integer> l6 = supplier.get();
                for (int i = 0; i < n; i++) l6.pushFront(i);
                int repsD = repsBusqueda(n);
                total = 0;
                for (int i = 0; i < repsD; i++) {
                    // elijo un valor random que todavia deberia existir
                    int target = RNG.nextInt(n);
                    Node<Integer> nodo = l6.find(target); // no se mide, es la busqueda previa
                    if (nodo == null) continue;
                    long t0 = System.nanoTime();
                    l6.erase(nodo);
                    long t1 = System.nanoTime();
                    total += (t1 - t0);
                }
                csv.row(nombre, "erase", n, repsD, (total / (double) repsD) / 1000.0);

                // addBefore ---------------------------------------------------
                MyList<Integer> l7 = supplier.get();
                for (int i = 0; i < n; i++) l7.pushFront(i);
                total = 0;
                for (int i = 0; i < repsD; i++) {
                    int target = RNG.nextInt(n);
                    Node<Integer> nodo = l7.find(target);
                    if (nodo == null) continue;
                    long t0 = System.nanoTime();
                    l7.addBefore(nodo, -1);
                    long t1 = System.nanoTime();
                    total += (t1 - t0);
                }
                csv.row(nombre, "addBefore", n, repsD, (total / (double) repsD) / 1000.0);

                // addAfter ----------------------------------------------------
                MyList<Integer> l8 = supplier.get();
                for (int i = 0; i < n; i++) l8.pushFront(i);
                total = 0;
                for (int i = 0; i < repsD; i++) {
                    int target = RNG.nextInt(n);
                    Node<Integer> nodo = l8.find(target);
                    if (nodo == null) continue;
                    long t0 = System.nanoTime();
                    l8.addAfter(nodo, -1);
                    long t1 = System.nanoTime();
                    total += (t1 - t0);
                }
                csv.row(nombre, "addAfter", n, repsD, (total / (double) repsD) / 1000.0);
            }
        }
        csv.close();
    }
}
