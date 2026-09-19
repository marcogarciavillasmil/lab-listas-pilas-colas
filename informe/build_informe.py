"""
Arma el PDF final del taller (Stack-Queue-Java-ED-<codigo>.pdf) juntando el
texto del analisis con las tablas de resultados y las graficas que genera
analisis/graficar.py.
"""
import csv
import os

from reportlab.lib import colors
from reportlab.lib.pagesizes import LETTER
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import inch
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Image, Table, TableStyle,
    PageBreak, ListFlowable, ListItem, HRFlowable
)

BASE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.join(BASE, "..")
RESULTS = os.path.join(ROOT, "results")
PLOTS = os.path.join(ROOT, "plots")

CODIGO_ESTUDIANTE = "1094050756"
NOMBRE_ESTUDIANTE = "Marco Antonio Garcia Villasmil"
URL_REPO = "https://github.com/marcogarciavillasmil/lab-listas-pilas-colas"

OUT_PDF = os.path.join(BASE, f"Stack-Queue-Java-ED-{CODIGO_ESTUDIANTE}.pdf")

styles = getSampleStyleSheet()
styles.add(ParagraphStyle(name="Titulo", parent=styles["Title"], fontSize=18, spaceAfter=4))
styles.add(ParagraphStyle(name="Sub", parent=styles["Normal"], fontSize=11, textColor=colors.HexColor("#52514e")))
styles.add(ParagraphStyle(name="H1", parent=styles["Heading1"], fontSize=15, spaceBefore=18, spaceAfter=8))
styles.add(ParagraphStyle(name="H2", parent=styles["Heading2"], fontSize=12.5, spaceBefore=12, spaceAfter=6))
styles.add(ParagraphStyle(name="Cuerpo", parent=styles["Normal"], fontSize=10.2, leading=14.5, spaceAfter=8))
styles.add(ParagraphStyle(name="Caption", parent=styles["Normal"], fontSize=8.7, textColor=colors.HexColor("#52514e"),
                           spaceAfter=14, leading=12))

story = []


def h1(txt):
    story.append(Paragraph(txt, styles["H1"]))


def h2(txt):
    story.append(Paragraph(txt, styles["H2"]))


def p(txt):
    story.append(Paragraph(txt, styles["Cuerpo"]))


def img(ruta, ancho=6.3, caption=None):
    from PIL import Image as PILImage
    w, h = PILImage.open(ruta).size
    alto = ancho * h / w
    story.append(Image(ruta, width=ancho * inch, height=alto * inch))
    if caption:
        story.append(Paragraph(caption, styles["Caption"]))
    else:
        story.append(Spacer(1, 10))


def leer_csv(nombre):
    with open(os.path.join(RESULTS, nombre)) as f:
        return list(csv.DictReader(f))


def tabla_resultados(datos, impl, metodos, encabezado="n"):
    ns = sorted(set(int(d["n"]) for d in datos if d["implementacion"] == impl))
    filas = [[encabezado] + [str(n) for n in ns]]
    for m in metodos:
        fila = [m]
        for n in ns:
            match = [d for d in datos if d["implementacion"] == impl and d["metodo"] == m and int(d["n"]) == n]
            fila.append(f"{float(match[0]['tiempo_prom_us']):.2f}" if match else "-")
        filas.append(fila)
    t = Table(filas, hAlign="LEFT")
    t.setStyle(TableStyle([
        ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#2a78d6")),
        ("TEXTCOLOR", (0, 0), (-1, 0), colors.white),
        ("FONTSIZE", (0, 0), (-1, -1), 8),
        ("FONTNAME", (0, 0), (-1, 0), "Helvetica-Bold"),
        ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.white, colors.HexColor("#f2f1ec")]),
        ("GRID", (0, 0), (-1, -1), 0.5, colors.HexColor("#c3c2b7")),
        ("ALIGN", (1, 0), (-1, -1), "CENTER"),
        ("TOPPADDING", (0, 0), (-1, -1), 3),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 3),
    ]))
    story.append(t)
    story.append(Spacer(1, 10))


def tabla_teorica(filas, encabezados):
    data = [encabezados] + filas
    t = Table(data, hAlign="LEFT")
    t.setStyle(TableStyle([
        ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#0b0b0b")),
        ("TEXTCOLOR", (0, 0), (-1, 0), colors.white),
        ("FONTSIZE", (0, 0), (-1, -1), 8.5),
        ("FONTNAME", (0, 0), (-1, 0), "Helvetica-Bold"),
        ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.white, colors.HexColor("#f2f1ec")]),
        ("GRID", (0, 0), (-1, -1), 0.5, colors.HexColor("#c3c2b7")),
        ("ALIGN", (1, 0), (-1, -1), "CENTER"),
        ("TOPPADDING", (0, 0), (-1, -1), 3),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 3),
    ]))
    story.append(t)
    story.append(Spacer(1, 10))


# portada
story.append(Spacer(1, 40))
story.append(Paragraph("ESTRUCTURAS DE DATOS - 2026-2", styles["Sub"]))
story.append(Paragraph("Implementacion y Analisis de Complejidad de Listas, Pilas y Colas en Java", styles["Titulo"]))
story.append(Spacer(1, 10))
story.append(HRFlowable(width="100%", color=colors.HexColor("#c3c2b7"), thickness=1))
story.append(Spacer(1, 10))
story.append(Paragraph(f"<b>Estudiante:</b> {NOMBRE_ESTUDIANTE}", styles["Cuerpo"]))
story.append(Paragraph(f"<b>Codigo:</b> {CODIGO_ESTUDIANTE}", styles["Cuerpo"]))
story.append(Paragraph("<b>Profesor:</b> David Herrera &nbsp;&nbsp; <b>Monitora:</b> Angela Camila Siabato Londono", styles["Cuerpo"]))
story.append(Paragraph(f'<b>Repositorio:</b> <link href="{URL_REPO}">{URL_REPO}</link>', styles["Cuerpo"]))
story.append(Spacer(1, 20))

h1("1. Objetivo del taller")
p("""El objetivo de este taller es implementar en Java la estructura List usando listas
enlazadas en sus cuatro variantes (simple sin cola, simple con cola, doble sin cola y
doble con cola), y las estructuras Stack y Queue usando arreglo dinamico y arreglo
circular. Con las 8 implementaciones ya funcionando, el trabajo real del taller es medir
el tiempo de ejecucion de cada metodo para distintos tamanos de entrada, comparar esos
tiempos contra la complejidad teorica en notacion Big-O, y sacar conclusiones sobre
quien conviene usar en cada caso.""")

h1("2. Explicacion de la implementacion")

h2("2.1 List (listas enlazadas)")
p("""Se implemento una interfaz comun <font face="Courier">MyList&lt;T&gt;</font> con los
8 metodos que pide el enunciado (pushFront, pushBack, popFront, popBack, find, erase,
addBefore, addAfter), y 4 clases que la implementan:""")
story.append(ListFlowable([
    ListItem(Paragraph("<b>SinglyLinkedListNoTail:</b> solo guarda head. Todo lo que necesite llegar al final "
                        "(pushBack, popBack, topBack) o encontrar el nodo anterior a otro (erase, addBefore) "
                        "tiene que recorrer la lista completa.", styles["Cuerpo"])),
    ListItem(Paragraph("<b>SinglyLinkedListWithTail:</b> agrega un puntero tail. pushBack pasa a ser O(1), pero "
                        "popBack sigue siendo O(n) porque los nodos no tienen puntero al anterior (es simple, "
                        "no doble) y hay que recorrer para encontrar quien queda como nuevo tail.", styles["Cuerpo"])),
    ListItem(Paragraph("<b>DoublyLinkedListNoTail:</b> los nodos ya tienen prev y next, asi que dado un nodo, "
                        "erase/addBefore/addAfter son O(1). El problema es que sin tail, llegar al ultimo nodo "
                        "sigue siendo O(n).", styles["Cuerpo"])),
    ListItem(Paragraph("<b>DoublyLinkedListWithTail:</b> head + tail + prev/next en cada nodo. Es la version "
                        "completa: todo queda en O(1) menos find, que al no haber indexacion siempre es O(n).",
                        styles["Cuerpo"])),
], bulletType="bullet", leftIndent=14))
p("""find() devuelve la referencia al Node (no un booleano ni un indice) para poder pasarla
despues a erase, addBefore o addAfter sin tener que volver a buscar el elemento.""")

h2("2.2 MyStack y MyQueue")
p("""Para MyStack y MyQueue se implemento cada una con dos estrategias de arreglo:""")
story.append(ListFlowable([
    ListItem(Paragraph("<b>Arreglo dinamico (ArrayStack / ArrayQueue):</b> arreglo interno que se duplica cuando "
                        "se llena, igual que ArrayList. En el Stack esto alcanza para que push/pop sean O(1) "
                        "amortizado porque ambos operan sobre el mismo extremo (el tope). En el Queue, en cambio, "
                        "enqueue mete al final pero dequeue tiene que sacar la posicion 0 y correr todos los "
                        "elementos restantes una posicion, lo que deja dequeue en O(n).", styles["Cuerpo"])),
    ListItem(Paragraph("<b>Arreglo circular (CircularArrayStack / CircularArrayQueue):</b> en vez de un arreglo "
                        "plano, se maneja un indice front y las posiciones se calculan modulo la capacidad. "
                        "Para el Stack esto no cambia nada frente a la version dinamica (sigue operando en un "
                        "solo extremo), pero para el Queue es la diferencia clave: dequeue deja de desplazar "
                        "elementos, solo avanza el indice front, quedando en O(1).", styles["Cuerpo"])),
], bulletType="bullet", leftIndent=14))
p("""El crecimiento del arreglo (cuando se llena) se hace duplicando la capacidad
(capacidad *= 2), la estrategia estandar de amortizacion: el costo total de n inserciones
sigue siendo O(n), aunque una insercion puntual pueda costar O(n) cuando toca redimensionar.""")

h2("2.3 Medicion de tiempos")
p("""La medicion sigue el mismo patron del Main.java de referencia (Instant/Duration), pero
usando <font face="Courier">System.nanoTime()</font> y reportando en microsegundos: en
listas chicas, milisegundos redondeaba todo a 0 y no se alcanzaba a notar la diferencia
entre un metodo O(1) y uno O(n). Para cada tamano n se arma primero la estructura con n
elementos (usando siempre un metodo O(1) para no distorsionar el tiempo de armado), y
despues se repite el metodo que se quiere medir varias veces seguidas, promediando. En
push/pop se deshace cada operacion inmediatamente despues de medirla para que el tamano
de la estructura se mantenga estable durante toda la medicion. En metodos que buscan un
valor (find, erase, addBefore, addAfter, delete) el objetivo se elige al azar (semilla
fija = 42, para que sea reproducible) entre los valores existentes.""")
p("""Los tamanos usados fueron n = 10, 100, 1000, 10 000, 100 000 y 1 000 000. El enunciado
sugiere llegar hasta 10^8, pero para los metodos O(n) eso significa que una sola corrida
del benchmark tarda minutos, asi que se decidio limitar a 10^6: en las graficas se ve con
claridad la tendencia (recta ascendente en escala log-log para O(n), plana para O(1)), y
extrapolar dos ordenes de magnitud mas no cambia la conclusion.""")

story.append(PageBreak())

h1("3. Analisis de complejidad")

h2("3.1 List: complejidad teorica")
tabla_teorica(
    [
        ["pushFront", "O(1)", "O(1)", "O(1)", "O(1)"],
        ["pushBack", "O(n)", "O(1)", "O(n)", "O(1)"],
        ["popFront", "O(1)", "O(1)", "O(1)", "O(1)"],
        ["popBack", "O(n)", "O(n)", "O(n)", "O(1)"],
        ["find", "O(n)", "O(n)", "O(n)", "O(n)"],
        ["erase", "O(n)", "O(n)", "O(1)", "O(1)"],
        ["addBefore", "O(n)", "O(n)", "O(1)", "O(1)"],
        ["addAfter", "O(1)", "O(1)", "O(1)", "O(1)"],
    ],
    ["metodo", "SinglyNoTail", "SinglyWithTail", "DoublyNoTail", "DoublyWithTail"],
)
p("""popBack en SinglyWithTail queda en O(n) a pesar de tener tail porque, al ser simple,
no hay forma de llegar al penultimo nodo sin recorrer desde el head. erase y addBefore
necesitan el nodo anterior al indicado: en las listas simples eso obliga a recorrer, en
las dobles no porque cada nodo ya conoce a su vecino.""")

h2("3.2 List: resultados empiricos por metodo")
p("Tiempos promedio en microsegundos, uno por metodo (las 4 implementaciones juntas para comparar):")
metodos_lista = ["pushFront", "pushBack", "popFront", "popBack", "find", "erase", "addBefore", "addAfter"]
lista_datos = leer_csv("list_benchmark.csv")
for m in metodos_lista:
    img(os.path.join(PLOTS, "list", f"{m}.png"), ancho=6.0)

story.append(PageBreak())

h2("3.3 Tabla de resultados - SinglyLinkedListNoTail")
tabla_resultados(lista_datos, "SinglyNoTail", metodos_lista)
h2("Tabla de resultados - SinglyLinkedListWithTail")
tabla_resultados(lista_datos, "SinglyWithTail", metodos_lista)
h2("Tabla de resultados - DoublyLinkedListNoTail")
tabla_resultados(lista_datos, "DoublyNoTail", metodos_lista)
h2("Tabla de resultados - DoublyLinkedListWithTail")
tabla_resultados(lista_datos, "DoublyWithTail", metodos_lista)
p("""Los resultados coinciden con lo teorico: pushBack y popBack se disparan en las
implementaciones sin tail (o sin tail util, caso popBack en SinglyWithTail), find crece
en las 4 por igual, y erase/addBefore se mantienen practicamente planos solo en las
listas dobles. addAfter es O(1) en las 4, y eso tambien se ve reflejado.""")

story.append(PageBreak())

h2("3.4 MyStack y MyQueue: complejidad teorica")
tabla_teorica(
    [
        ["push / enqueue", "O(1) am.", "O(1) am."],
        ["pop / dequeue (Stack)", "O(1)", "O(1)"],
        ["dequeue (Queue)", "O(n)", "O(1)"],
        ["peek / front", "O(1)", "O(1)"],
        ["delete(valor)", "O(n)", "O(n)"],
    ],
    ["operacion", "Arreglo dinamico", "Arreglo circular"],
)
p("""Para el Stack, push y pop operan siempre sobre el mismo extremo (el tope), asi que el
arreglo circular no aporta nada frente al dinamico: ambos quedan O(1). La diferencia real
aparece en el Queue: con arreglo dinamico, dequeue saca el elemento 0 y desplaza todo el
resto (O(n)); con arreglo circular solo se mueve el indice front (O(1)).""")

h2("3.5 MyStack: resultados empiricos")
pila_datos = leer_csv("stack_benchmark.csv")
for m in ["push", "pop", "peek", "delete"]:
    img(os.path.join(PLOTS, "stack", f"{m}.png"), ancho=6.0)
tabla_resultados(pila_datos, "ArrayDinamico", ["push", "pop", "peek", "delete"])
tabla_resultados(pila_datos, "ArrayCircular", ["push", "pop", "peek", "delete"])
p("""Tal como se esperaba, push/pop/peek quedan practicamente iguales entre las dos
implementaciones del Stack -- las dos curvas se superponen en las graficas. delete crece
igual en ambas porque en las dos hay que recorrer buscando el valor.""")

story.append(PageBreak())

h2("3.6 MyQueue: resultados empiricos")
cola_datos = leer_csv("queue_benchmark.csv")
for m in ["enqueue", "dequeue", "front", "delete"]:
    img(os.path.join(PLOTS, "queue", f"{m}.png"), ancho=6.0)
tabla_resultados(cola_datos, "ArrayDinamico", ["enqueue", "dequeue", "front", "delete"])
tabla_resultados(cola_datos, "ArrayCircular", ["enqueue", "dequeue", "front", "delete"])
p("""Aqui si se nota la diferencia: dequeue en ArrayDinamico crece de forma clara con n
(llega a mas de 8500 microsegundos en n=1 000 000), mientras que en ArrayCircular se
mantiene practicamente plano en todo el rango. Es la mejor evidencia empirica de todo el
taller de que el arreglo circular vale la pena quando se necesita sacar elementos por el
frente.""")

story.append(PageBreak())

h2("3.7 Comparativa de metodos equivalentes (List vs MyStack/MyQueue)")
p("""Para esta comparacion se uso siempre <b>DoublyLinkedListWithTail</b> como representante
de List, porque es la unica de las 4 que queda en O(1) en los metodos que entran aca
(pushFront, popFront, pushBack, erase) -- comparar contra alguna de las otras 3 hubiera sido
un poco injusto, ya que todas tienen al menos un metodo en O(n). Del lado de MyStack/MyQueue
se usa la version de <b>arreglo circular</b>, que tambien es la mejor de las dos.""")

img(os.path.join(PLOTS, "comparativa", "pushfront_vs_push.png"), ancho=6.0,
    caption="PushFront de List (uso tipico como pila) vs push de MyStack.")
img(os.path.join(PLOTS, "comparativa", "popfront_vs_pop.png"), ancho=6.0,
    caption="PopFront de List vs pop de MyStack.")
img(os.path.join(PLOTS, "comparativa", "pushback_vs_enqueue.png"), ancho=6.0,
    caption="PushBack de List (uso tipico como cola) vs enqueue de MyQueue.")
img(os.path.join(PLOTS, "comparativa", "popfront_vs_dequeue.png"), ancho=6.0,
    caption="PopFront de List vs dequeue de MyQueue.")
img(os.path.join(PLOTS, "comparativa", "erase_vs_delete.png"), ancho=6.0,
    caption="Erase de List vs delete de MyStack y MyQueue (busqueda + eliminacion).")
p("""En push/pop puros las tres estructuras quedan practicamente empatadas -- las dos son
O(1), la diferencia esta solo en la constante (la lista paga malloc de un nodo por cada
insercion; el arreglo circular a veces paga un resize). En delete, en cambio, List sale
ganando frente a Stack/Queue con arreglo: aunque las tres son O(n) porque hay que buscar
el valor, en la lista el borrado en si mismo es O(1) una vez encontrado (no hay que
desplazar nada), mientras que en el arreglo, ademas de buscar, hay que correr los
elementos para cerrar el hueco.""")

story.append(PageBreak())

h1("4. Conclusiones")
story.append(ListFlowable([
    ListItem(Paragraph("Ninguna de las 4 implementaciones de List gana en todo. SinglyLinkedListNoTail es la "
                        "mas facil de programar pero tambien la que peor escala (pushBack, popBack, erase y "
                        "addBefore quedan en O(n)); si solo se va a trabajar sobre el frente, como una pila, ya "
                        "alcanza. Para insertar y eliminar por los dos extremos sin pagar O(n), la unica que de "
                        "verdad cumple es DoublyLinkedListWithTail.", styles["Cuerpo"])),
    ListItem(Paragraph("Algo que no era tan obvio antes de correr los benchmarks: el puntero a tail por si solo "
                        "no resuelve todo. <b>SinglyLinkedListWithTail</b> arregla pushBack pero no popBack, porque "
                        "sin puntero al nodo anterior no hay como actualizar tail sin recorrer la lista completa. "
                        "Ahi se ve para que sirven realmente las listas dobles: el prev extra en cada nodo es lo "
                        "que deja ambos extremos en O(1) a la vez.", styles["Cuerpo"])),
    ListItem(Paragraph("El arreglo circular no mejora todo por igual, mejora colas. En el Stack push y pop "
                        "siempre trabajan sobre el mismo extremo, asi que circular y dinamico terminan rindiendo "
                        "igual (las curvas de 3.5 quedan encimadas). En el Queue si hay una diferencia real: con "
                        "arreglo dinamico cada dequeue desplaza el arreglo entero (O(n)), con circular solo se "
                        "mueve un indice (O(1)). Para mi fue el resultado mas claro de todo el laboratorio.",
                        styles["Cuerpo"])),
    ListItem(Paragraph("<b>Arreglos vs listas enlazadas, en general:</b> el arreglo gana en localidad de memoria "
                        "y en operaciones sobre un extremo fijo (menos overhead que asignar un nodo por elemento), "
                        "pero pierde feo apenas hay que insertar o eliminar en el medio o en el extremo que no "
                        "optimiza. La lista enlazada no tiene ese problema porque nunca desplaza datos, solo mueve "
                        "punteros, aunque paga con un nodo extra por elemento y peor localidad de cache.",
                        styles["Cuerpo"])),
    ListItem(Paragraph("En la practica, un Stack con arreglo dinamico ya es suficiente para casi cualquier caso "
                        "real (push/pop en un extremo son O(1) de cualquier forma). Un Queue en cambio deberia ir "
                        "siempre con arreglo circular o con una lista doblemente enlazada con tail, nunca con "
                        "arreglo dinamico simple -- el resultado de 3.6 deja bastante claro que esa version "
                        "ingenua se vuelve impractica con colecciones grandes.", styles["Cuerpo"])),
], bulletType="bullet", leftIndent=14))

doc = SimpleDocTemplate(
    OUT_PDF, pagesize=LETTER,
    topMargin=0.7 * inch, bottomMargin=0.7 * inch,
    leftMargin=0.75 * inch, rightMargin=0.75 * inch,
    title="Implementacion y Analisis de Complejidad de Listas, Pilas y Colas en Java",
    author=NOMBRE_ESTUDIANTE,
)
doc.build(story)
print("PDF generado en", OUT_PDF)
