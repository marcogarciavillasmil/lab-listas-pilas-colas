# Laboratorio - Listas, Pilas y Colas (Estructuras de Datos 2026-2)

Taller de ED. Implemente las 4 variantes de `List` que pide el enunciado
(simple/doble, con y sin puntero a cola) y `MyStack`/`MyQueue` con arreglo
dinamico y con arreglo circular, y les mido el tiempo a los metodos para
comparar. El analisis con las graficas esta completo en el pdf:
[`informe/Stack-Queue-Java-ED-1094050756.pdf`](informe/Stack-Queue-Java-ED-1094050756.pdf),
aca abajo dejo nomas como correr todo.

## Estructura

- `src/list` -> `MyList<T>` y las 4 implementaciones (Singly/Doubly x NoTail/WithTail)
- `src/stack` -> `MyStack<T>`, `ArrayStack` y `CircularArrayStack`
- `src/queue` -> igual pero para cola
- `src/bench` -> mide los tiempos, deja los csv en `results/`
- `analisis/` y `informe/` -> los scripts de python que arman las graficas y el pdf

## Como correrlo

Con Java 17:

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out bench.BenchMain
```

Con eso quedan los csv actualizados en `results/`. Si quieren las graficas de
nuevo (`pip install matplotlib`):

```bash
python3 analisis/graficar.py
```

Y el informe en pdf (`pip install reportlab pillow`):

```bash
python3 informe/build_informe.py
```

## Notas

`find()` devuelve el `Node<T>` en vez de un booleano porque despues me sirve
para pasarlo directo a `erase`/`addBefore`/`addAfter` sin buscar dos veces.

Los tamanos de prueba van hasta 1,000,000 y no hasta 10^8 como sugiere el
enunciado: en los metodos O(n) eso ya se demora minutos por corrida y la
tendencia se nota igual de clara mucho antes, asi que no valia la pena
(queda justificado en el pdf).

Y sobre `CircularArrayStack`: en la pila el circular casi no cambia nada
frente a `ArrayStack`, porque push/pop ya eran O(1) con el arreglo normal
(el tope siempre queda al final). Lo implemente sobre todo para comparar
con la cola, que es donde si importa -- ahi `dequeue` pasa de O(n) a O(1).
