"""
Lee los csv que genera BenchMain (en ../results) y arma las graficas para
el informe. Todo en escala log-log porque los tamanos van de 10 a 10^6 y los
tiempos de microsegundos a miles de microsegundos, si no se ve todo pegado
en la esquina.
"""
import csv
import os
import matplotlib.pyplot as plt

BASE = os.path.dirname(os.path.abspath(__file__))
RESULTS = os.path.join(BASE, "..", "results")
PLOTS = os.path.join(BASE, "..", "plots")

COLORES = {
    0: "#2a78d6",  # azul
    1: "#eb6834",  # naranja
    2: "#1baf7a",  # aqua
    3: "#eda100",  # amarillo
}

plt.rcParams.update({
    "figure.facecolor": "#fcfcfb",
    "axes.facecolor": "#fcfcfb",
    "axes.edgecolor": "#c3c2b7",
    "axes.labelcolor": "#0b0b0b",
    "text.color": "#0b0b0b",
    "xtick.color": "#52514e",
    "ytick.color": "#52514e",
    "grid.color": "#e1e0d9",
    "font.size": 10,
    "font.family": "sans-serif",
})


def leer_csv(nombre):
    ruta = os.path.join(RESULTS, nombre)
    with open(ruta) as f:
        return list(csv.DictReader(f))


def filas_de(datos, impl, metodo):
    filas = [d for d in datos if d["implementacion"] == impl and d["metodo"] == metodo]
    filas.sort(key=lambda d: int(d["n"]))
    return [int(d["n"]) for d in filas], [float(d["tiempo_prom_us"]) for d in filas]


def graficar_metodo(datos, implementaciones, metodo, titulo, archivo):
    fig, ax = plt.subplots(figsize=(7, 4.5), dpi=150)
    for i, impl in enumerate(implementaciones):
        xs, ys = filas_de(datos, impl, metodo)
        if not xs:
            continue
        ax.plot(xs, ys, marker="o", markersize=4, linewidth=2,
                color=COLORES[i % len(COLORES)], label=impl)
    ax.set_xscale("log")
    ax.set_yscale("log")
    ax.set_xlabel("tamano de la estructura (n)")
    ax.set_ylabel("tiempo promedio (microsegundos, escala log)")
    ax.set_title(titulo)
    ax.grid(True, which="both", linewidth=0.6, alpha=0.6)
    ax.legend(frameon=False)
    fig.tight_layout()
    os.makedirs(os.path.dirname(archivo), exist_ok=True)
    fig.savefig(archivo)
    plt.close(fig)
    print("guardado", archivo)


def graficar_comparativa(series, titulo, archivo):
    # series: lista de (etiqueta, xs, ys)
    fig, ax = plt.subplots(figsize=(7, 4.5), dpi=150)
    for i, (etiqueta, xs, ys) in enumerate(series):
        ax.plot(xs, ys, marker="o", markersize=4, linewidth=2,
                color=COLORES[i % len(COLORES)], label=etiqueta)
    ax.set_xscale("log")
    ax.set_yscale("log")
    ax.set_xlabel("tamano de la estructura (n)")
    ax.set_ylabel("tiempo promedio (microsegundos, escala log)")
    ax.set_title(titulo)
    ax.grid(True, which="both", linewidth=0.6, alpha=0.6)
    ax.legend(frameon=False)
    fig.tight_layout()
    os.makedirs(os.path.dirname(archivo), exist_ok=True)
    fig.savefig(archivo)
    plt.close(fig)
    print("guardado", archivo)


def main():
    lista = leer_csv("list_benchmark.csv")
    pila = leer_csv("stack_benchmark.csv")
    cola = leer_csv("queue_benchmark.csv")

    impls_lista = ["SinglyNoTail", "SinglyWithTail", "DoublyNoTail", "DoublyWithTail"]
    metodos_lista = ["pushFront", "pushBack", "popFront", "popBack", "find", "erase", "addBefore", "addAfter"]
    for m in metodos_lista:
        graficar_metodo(lista, impls_lista, m, f"List - {m}", os.path.join(PLOTS, "list", f"{m}.png"))

    impls_pila = ["ArrayDinamico", "ArrayCircular"]
    for m in ["push", "pop", "peek", "delete"]:
        graficar_metodo(pila, impls_pila, m, f"MyStack - {m}", os.path.join(PLOTS, "stack", f"{m}.png"))

    impls_cola = ["ArrayDinamico", "ArrayCircular"]
    for m in ["enqueue", "dequeue", "front", "delete"]:
        graficar_metodo(cola, impls_cola, m, f"MyQueue - {m}", os.path.join(PLOTS, "queue", f"{m}.png"))

    # comparativas List (DoublyWithTail) vs implementacion circular de Stack/Queue
    def serie_lista(metodo):
        return filas_de(lista, "DoublyWithTail", metodo)

    def serie_pila(metodo):
        return filas_de(pila, "ArrayCircular", metodo)

    def serie_cola(metodo):
        return filas_de(cola, "ArrayCircular", metodo)

    xs, ys = serie_lista("pushFront")
    xs2, ys2 = serie_pila("push")
    graficar_comparativa(
        [("List.pushFront (DoublyWithTail)", xs, ys), ("MyStack.push (circular)", xs2, ys2)],
        "PushFront de List vs push de MyStack",
        os.path.join(PLOTS, "comparativa", "pushfront_vs_push.png"))

    xs, ys = serie_lista("popFront")
    xs2, ys2 = serie_pila("pop")
    graficar_comparativa(
        [("List.popFront (DoublyWithTail)", xs, ys), ("MyStack.pop (circular)", xs2, ys2)],
        "PopFront de List vs pop de MyStack",
        os.path.join(PLOTS, "comparativa", "popfront_vs_pop.png"))

    xs, ys = serie_lista("pushBack")
    xs2, ys2 = serie_cola("enqueue")
    graficar_comparativa(
        [("List.pushBack (DoublyWithTail)", xs, ys), ("MyQueue.enqueue (circular)", xs2, ys2)],
        "PushBack de List vs enqueue de MyQueue",
        os.path.join(PLOTS, "comparativa", "pushback_vs_enqueue.png"))

    xs, ys = serie_lista("popFront")
    xs2, ys2 = serie_cola("dequeue")
    graficar_comparativa(
        [("List.popFront (DoublyWithTail)", xs, ys), ("MyQueue.dequeue (circular)", xs2, ys2)],
        "PopFront de List vs dequeue de MyQueue",
        os.path.join(PLOTS, "comparativa", "popfront_vs_dequeue.png"))

    xs, ys = serie_lista("erase")
    xs2, ys2 = serie_pila("delete")
    xs3, ys3 = serie_cola("delete")
    graficar_comparativa(
        [("List.erase (DoublyWithTail)", xs, ys), ("MyStack.delete (circular)", xs2, ys2),
         ("MyQueue.delete (circular)", xs3, ys3)],
        "Erase de List vs delete de MyStack/MyQueue",
        os.path.join(PLOTS, "comparativa", "erase_vs_delete.png"))


if __name__ == "__main__":
    main()
