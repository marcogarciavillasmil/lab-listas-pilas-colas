package bench;

import java.time.Duration;
import java.time.Instant;

// mismo patron de medicion que el Main.java que dieron de ejemplo
// (Instant/Duration), pero devuelvo microsegundos en vez de milisegundos
// porque con milisegundos las operaciones O(1) daban 0 casi siempre y no se
// alcanzaba a ver diferencia con las O(n) en listas chiquitas.
public final class Timer {
    private Timer() {}

    public interface Action {
        void run(int i);
    }

    /** Ejecuta action 'times' veces y retorna el tiempo total en microsegundos. */
    public static double timeMicros(int times, Action action) {
        Instant start = Instant.now();
        for (int i = 0; i < times; i++) {
            action.run(i);
        }
        Instant finish = Instant.now();
        long nanos = Duration.between(start, finish).toNanos();
        return nanos / 1000.0;
    }
}
