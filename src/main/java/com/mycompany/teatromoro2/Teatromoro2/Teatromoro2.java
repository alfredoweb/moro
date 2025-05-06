package com.mycompany.teatromoro2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author alfil
 */

public class Teatromoro2 {
    
    static final int FILAS = 5;
    static final int COLUMNAS = 10;
    static final int MAX_VENTAS = 100;
    static final int MAX_CLIENTES = 100;

    // Arreglos para ventas, asientos y clientes
    static Venta[] ventas = new Venta[MAX_VENTAS];
    static Cliente[] clientes = new Cliente[MAX_CLIENTES];
    static boolean[][] asientosOcupados = new boolean[FILAS][COLUMNAS];

    // Listas para descuentos/promociones y reservas
    static List<Descuento> descuentos = new ArrayList<>();
    static List<Reserva> reservas = new ArrayList<>();

    // Contadores
    static int ventasRealizadas = 0;
    static int clientesRegistrados = 0;

    // Clases internas para modelar datos
    static class Cliente {
        int id;
        String nombre;
        String tipo; // General, Estudiante, Tercera edad

        Cliente(int id, String nombre, String tipo) {
            this.id = id;
            this.nombre = nombre;
            this.tipo = tipo;
        }
    }

    static class Venta {
        int idVenta;
        int idCliente;
        int fila;
        int columna;
        double precioFinal;

        Venta(int idVenta, int idCliente, int fila, int columna, double precioFinal) {
            this.idVenta = idVenta;
            this.idCliente = idCliente;
            this.fila = fila;
            this.columna = columna;
            this.precioFinal = precioFinal;
        }
    }

    static class Reserva {
        int idReserva;
        int idCliente;
        int fila;
        int columna;

        Reserva(int idReserva, int idCliente, int fila, int columna) {
            this.idReserva = idReserva;
            this.idCliente = idCliente;
            this.fila = fila;
            this.columna = columna;
        }
    }

    static class Descuento {
        String tipoCliente;
        double porcentaje;

        Descuento(String tipoCliente, double porcentaje) {
            this.tipoCliente = tipoCliente;
            this.porcentaje = porcentaje;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        inicializarDescuentos();

        boolean continuar = true;
        while (continuar) {
            System.out.println("\n===== Teatro Moro - Gestión de Entradas =====");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Venta de entrada");
            System.out.println("3. Reservar asiento");
            System.out.println("4. Eliminar venta");
            System.out.println("5. Actualizar cliente");
            System.out.println("6. Mostrar ventas");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1: registrarCliente(scanner); break;
                case 2: venderEntrada(scanner); break;
                case 3: reservarAsiento(scanner); break;
                case 4: eliminarVenta(scanner); break;
                case 5: actualizarCliente(scanner); break;
                case 6: mostrarVentas(); break;
                case 7: continuar = false; break;
                default: System.out.println("Opción inválida.");
            }
        }
        scanner.close();
    }

    
    static void inicializarDescuentos() {
        descuentos.add(new Descuento("Estudiante", 0.10));
        descuentos.add(new Descuento("Tercera edad", 0.15));
    }

    
    static void registrarCliente(Scanner scanner) {
        if (clientesRegistrados >= MAX_CLIENTES) {
            System.out.println("No se pueden registrar más clientes.");
            return;
        }
        System.out.print("Nombre del cliente: ");
        String nombre = scanner.nextLine();
        System.out.print("Tipo (General/Estudiante/Tercera edad): ");
        String tipo = scanner.nextLine();
        if (!tipo.equalsIgnoreCase("General") && !tipo.equalsIgnoreCase("Estudiante") && !tipo.equalsIgnoreCase("Tercera edad")) {
            System.out.println("Tipo de cliente inválido.");
            return;
        }
        clientes[clientesRegistrados] = new Cliente(clientesRegistrados + 1, nombre, tipo);
        System.out.println("Cliente registrado con ID: " + (clientesRegistrados + 1));
        clientesRegistrados++;
    }

    
    static void venderEntrada(Scanner scanner) {
        if (ventasRealizadas >= MAX_VENTAS) {
            System.out.println("No se pueden realizar más ventas.");
            return;
        }
        System.out.print("ID de cliente: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine();
        Cliente cliente = buscarCliente(idCliente);
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        mostrarAsientos();
        System.out.print("Fila (A-E): ");
        char filaChar = scanner.nextLine().toUpperCase().charAt(0);
        int fila = filaChar - 'A';
        System.out.print("Columna (1-10): ");
        int columna = scanner.nextInt() - 1;
        scanner.nextLine();

        
        if (fila < 0 || fila >= FILAS || columna < 0 || columna >= COLUMNAS) {
            System.out.println("Asiento fuera de rango.");
            return;
        }
        if (asientosOcupados[fila][columna]) {
            System.out.println("Asiento ya vendido.");
            return;
        }
        if (reservaExistente(fila, columna)) {
            System.out.println("Asiento reservado, debe comprar la reserva.");
            return;
        }

        double precioBase = obtenerPrecioPorFila(fila);
        double descuento = obtenerDescuento(cliente.tipo);
        double precioFinal = precioBase * (1 - descuento);

        asientosOcupados[fila][columna] = true;
        ventas[ventasRealizadas] = new Venta(ventasRealizadas + 1, idCliente, fila, columna, precioFinal);
        ventasRealizadas++;
        System.out.println("Venta realizada. Precio final: $" + (int)precioFinal);
    }

    
    static void reservarAsiento(Scanner scanner) {
        System.out.print("ID de cliente: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine();
        Cliente cliente = buscarCliente(idCliente);
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        mostrarAsientos();
        System.out.print("Fila (A-E): ");
        char filaChar = scanner.nextLine().toUpperCase().charAt(0);
        int fila = filaChar - 'A';
        System.out.print("Columna (1-10): ");
        int columna = scanner.nextInt() - 1;
        scanner.nextLine();

        if (fila < 0 || fila >= FILAS || columna < 0 || columna >= COLUMNAS) {
            System.out.println("Asiento fuera de rango.");
            return;
        }
        if (asientosOcupados[fila][columna] || reservaExistente(fila, columna)) {
            System.out.println("Asiento no disponible.");
            return;
        }
        reservas.add(new Reserva(reservas.size() + 1, idCliente, fila, columna));
        System.out.println("Reserva realizada para asiento " + (char)('A' + fila) + (columna + 1));
    }

    
    static void eliminarVenta(Scanner scanner) {
        System.out.print("ID de venta a eliminar: ");
        int idVenta = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < ventasRealizadas; i++) {
            if (ventas[i] != null && ventas[i].idVenta == idVenta) {
                asientosOcupados[ventas[i].fila][ventas[i].columna] = false;
                ventas[i] = null;
                System.out.println("Venta eliminada.");
                return;
            }
        }
        System.out.println("Venta no encontrada.");
    }

    
    static void actualizarCliente(Scanner scanner) {
        System.out.print("ID de cliente a actualizar: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine();
        Cliente cliente = buscarCliente(idCliente);
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        System.out.print("Nuevo nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Nuevo tipo (General/Estudiante/Tercera edad): ");
        String tipo = scanner.nextLine();
        if (!tipo.equalsIgnoreCase("General") && !tipo.equalsIgnoreCase("Estudiante") && !tipo.equalsIgnoreCase("Tercera edad")) {
            System.out.println("Tipo de cliente inválido.");
            return;
        }
        cliente.nombre = nombre;
        cliente.tipo = tipo;
        System.out.println("Cliente actualizado.");
    }

    
    static void mostrarVentas() {
        System.out.println("\n===== Ventas Realizadas =====");
        for (int i = 0; i < ventasRealizadas; i++) {
            if (ventas[i] != null) {
                Cliente c = buscarCliente(ventas[i].idCliente);
                System.out.println("Venta ID: " + ventas[i].idVenta +
                        " | Cliente: " + (c != null ? c.nombre : "Desconocido") +
                        " | Asiento: " + (char)('A' + ventas[i].fila) + (ventas[i].columna + 1) +
                        " | Precio: $" + (int)ventas[i].precioFinal);
            }
        }
    }

    // Utilidades
    static Cliente buscarCliente(int idCliente) {
        for (int i = 0; i < clientesRegistrados; i++) {
            if (clientes[i] != null && clientes[i].id == idCliente) {
                return clientes[i];
            }
        }
        return null;
    }

    static boolean reservaExistente(int fila, int columna) {
        for (Reserva r : reservas) {
            if (r.fila == fila && r.columna == columna) return true;
        }
        return false;
    }

    static double obtenerPrecioPorFila(int fila) {
        switch (fila) {
            case 0: return 50000;
            case 1: return 40000;
            case 2: return 30000;
            case 3: return 20000;
            case 4: return 10000;
            default: return 0;
        }
    }

    static double obtenerDescuento(String tipoCliente) {
        for (Descuento d : descuentos) {
            if (d.tipoCliente.equalsIgnoreCase(tipoCliente)) return d.porcentaje;
        }
        return 0;
    }

    static void mostrarAsientos() {
        System.out.println("\nAsientos (O=Libre, X=Vendido, R=Reservado):");
        for (int i = 0; i < FILAS; i++) {
            System.out.print((char)('A' + i) + " | ");
            for (int j = 0; j < COLUMNAS; j++) {
                if (asientosOcupados[i][j]) System.out.print("X ");
                else if (reservaExistente(i, j)) System.out.print("R ");
                else System.out.print("O ");
            }
            System.out.println();
        }
    }
}
