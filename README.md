# Sistema de Ventas e Inventario

Aplicación de escritorio en Java (Swing) hecha con Programación Orientada a Objetos.
Los datos se guardan en archivos de texto, así que no necesita base de datos.

## Qué hace

- Registra productos, clientes, empleados, bodegas, inventario y órdenes de compra.
- Una orden necesita al menos un producto.
- Las órdenes nacen como **PENDIENTE**. Al **completarlas** se resta el stock del inventario;
  si alguna bodega no alcanza, la venta no se completa y no se toca nada.
- Una orden pendiente también se puede **cancelar** (no mueve el stock).
- Cada bodega valida su capacidad: no deja guardar más unidades de las que caben.

## Estructura

```
src/tienda/
├── Main.java
├── modelo/         Producto, Cliente, Empleado, Bodega, Inventario,
│                   ItemInventario, OrdenCompra, DetalleOrden, enums
├── persistencia/   Almacen (lee y escribe los archivos)
├── servicio/       Tienda (validaciones y reglas del negocio)
└── vista/          Ventana principal y un panel por cada módulo
```

Relaciones principales:

- `Bodega` tiene un `Empleado` encargado.
- `Inventario` pertenece a una `Bodega` y guarda una lista de `ItemInventario` (producto + stock).
- `OrdenCompra` tiene un `Cliente` y una lista de `DetalleOrden` (producto, cantidad, precio al momento de la compra).

## Archivos de datos

Se crean solos en la carpeta `datos/`. Un registro por línea, campos separados con `|`:

| Archivo | Formato |
|---|---|
| productos.txt | codigo\|nombre\|descripcion\|marca\|precio |
| clientes.txt | dpi\|nombre\|direccion\|fechaNacimiento\|tipo |
| empleados.txt | id\|nombre\|dpi\|direccion\|puesto\|salario |
| bodegas.txt | id\|idEmpleado\|capacidad\|direccion |
| inventarios.txt | id\|idBodega\|codigo:cantidad;codigo:cantidad |
| ordenes.txt | numero\|dpiCliente\|fecha\|total\|estado\|codigo:cantidad:precio;... |

## Cómo ejecutarlo

Requiere JDK 11 o superior.

```bash
mkdir out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out tienda.Main
```

En Windows (PowerShell):

```powershell
mkdir out
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out tienda.Main
```

También se puede abrir la carpeta en IntelliJ, Eclipse o NetBeans y correr `Main`.

## Orden recomendado para probarlo

1. Registrar un empleado.
2. Registrar una bodega (usa ese empleado).
3. Registrar productos.
4. Agregar stock en la pestaña Inventario.
5. Registrar un cliente.
6. Crear una orden y completarla; revisar que el stock bajó.

## Versionamiento

```bash
git init
git add .
git commit -m "Estructura inicial del proyecto"
git branch -M main
git remote add origin https://github.com/USUARIO/sistema-ventas.git
git push -u origin main
```
