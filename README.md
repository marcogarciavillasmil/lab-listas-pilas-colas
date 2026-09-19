# Laboratorio - Listas, Pilas y Colas (Estructuras de Datos 2026-2)

Implementacion y analisis de complejidad de `List` (4 variantes de lista enlazada),
`MyStack` y `MyQueue` (arreglo dinamico y arreglo circular), para el taller de
Estructuras de Datos.

El informe completo (objetivo, explicacion, analisis teorico vs empirico, graficas
y conclusiones) esta en [`informe/Stack-Queue-Java-ED-1094050756.pdf`](informe/Stack-Queue-Java-ED-1094050756.pdf).

## Estructura del proyecto

```
src/
  list/    -> MyList<T> y las 4 implementaciones (Singly/Doubly x NoTail/WithTail)
  stack/   -> MyStack<T>, ArrayStack (arreglo dinamico), CircularArrayStack
  queue/   -> MyQueue<T>, ArrayQueue (arreglo dinamico), CircularArrayQueue
  bench/   -> clases que miden los tiempos y escriben los csv en results/
results/   -> csv con los tiempos medidos (uno por List/Stack/Queue)
plots/     -> graficas (png) generadas a partir de esos csv
analisis/  -> script de python que arma las graficas
informe/   -> script que arma el pdf final + el pdf ya generado
```

## Como correr los benchmarks

Compilar y correr (Java 17+):

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out bench.BenchMain
```

Esto deja los csv actualizados en `results/`. Para regenerar las graficas:

```bash
pip install matplotlib
python3 analisis/graficar.py
```

Y para regenerar el informe en pdf:

```bash
pip install reportlab pillow
python3 informe/build_informe.py
```

## Notas rapidas

- `find()` en `MyList` devuelve el `Node<T>` (no un booleano) para poder pasarlo
  despues a `erase`/`addBefore`/`addAfter` sin tener que buscar dos veces.
- Los tamanos de prueba van de 10 a 1 000 000. El enunciado sugiere hasta 10^8 pero
  para los metodos O(n) eso ya tarda minutos por corrida, asi que se justifica en el
  informe por que se limito el rango (la tendencia ya se ve clara igual).
- `ArrayStack`/`ArrayQueue` usan arreglo dinamico que se duplica al llenarse.
  `CircularArrayStack`/`CircularArrayQueue` usan la misma idea pero con indices
  modulo capacidad (buffer circular), que es lo que hace que `dequeue` en la cola
  quede en O(1) en vez de O(n).
