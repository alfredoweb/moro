package com.mycompany.teatromoro2;

import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * @author alfil
 */


public class Teatromoro2 {

    private static int totalDescuentosAplicados = 0;
    private static int totalEntradasVendidas = 0;
    private static double totalIngresos = 0;
    private static final String NOMBRE_TEATRO = "Teatro Moro";
    private static final int CAPACIDAD_SALA = 50; 

    private static ArrayList<Entrada> ventas = new ArrayList<>();
    private static ArrayList<Entrada> reservas = new ArrayList<>();
    private static boolean[][] asientosOcupados = new boolean[5][10];
    private static boolean[][] asientosReservados = new boolean[5][10];
    private static int[] preciosPorFila = {50000, 40000, 30000, 20000, 10000};


    static class Entrada {
        int numero;
        String ubicacion;
        int fila;
        int columna;
        String tipoCliente;
        double precioFinal;
        boolean esReserva;

        Entrada(int numero, String ubicacion, int fila, int columna, String tipoCliente, double precioFinal, boolean esReserva) {
            this.numero = numero;
            this.ubicacion = ubicacion;
            this.fila = fila;
            this.columna = columna;
            this.tipoCliente = tipoCliente;
            this.precioFinal = precioFinal;
            this.esReserva = esReserva;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n..::: " + NOMBRE_TEATRO + " :::..");
            System.out.println("1. Reservar entradas");
            System.out.println("2. Comprar entradas");
            System.out.println("3. Modificar una venta");
            System.out.println("4. Imprimir boleta");
            System.out.println("5. Estadisticas");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opcion: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    reservarEntradas(scanner);
                    break;
                case 2:
                    comprarEntradas(scanner);
                    break;
                case 3:
                    modificarVenta(scanner);
                    break;
                case 4:
                    imprimirBoleta(scanner);
                    break;
                case 5:
                    mostrarEstadisticas();
                    break;
                case 6:
                    continuar = false;
                    System.out.println("¡Gracias por preferir " + NOMBRE_TEATRO + "!");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
        scanner.close();
    }

    
    public static void reservarEntradas(Scanner scanner) {
        System.out.println("\n===== RESERVA DE ENTRADAS =====");
        mostrarDisponibilidad();
        System.out.print("¿Cuantas entradas desea reservar? ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < cantidad; i++) {
            
            int fila = -1, columna = -1;
            String tipoCliente = "";
            boolean asientoValido = false;

            while (!asientoValido) {
                System.out.print("Ingrese la fila (A-E): ");
                String filaStr = scanner.nextLine().toUpperCase();
                fila = filaStr.charAt(0) - 'A';
                System.out.print("Ingrese el numero de asiento (1-10): ");
                columna = scanner.nextInt() - 1;
                scanner.nextLine();

                
                System.out.println("// DEBUG: Validando asiento fila " + fila + " columna " + columna);

                if (fila < 0 || fila > 4 || columna < 0 || columna > 9) {
                    System.out.println("Asiento fuera de rango.");
                } else if (asientosOcupados[fila][columna]) {
                    System.out.println("Asiento ya vendido.");
                } else if (asientosReservados[fila][columna]) {
                    System.out.println("Asiento ya reservado.");
                } else {
                    asientoValido = true;
                }
            }

            System.out.println("Tipo de cliente: 1. General 2. Estudiante 3. Tercera edad");
            int tipo = scanner.nextInt();
            scanner.nextLine();
            switch (tipo) {
                case 1: tipoCliente = "General"; break;
                case 2: tipoCliente = "Estudiante"; break;
                case 3: tipoCliente = "Tercera edad"; break;
                default: tipoCliente = "General";
            }

            double precioBase = preciosPorFila[fila];
            double precioFinal = precioBase;
            if (tipoCliente.equals("Estudiante")) {
                precioFinal *= 0.90;
            } else if (tipoCliente.equals("Tercera edad")) {
                precioFinal *= 0.85;
            }

            asientosReservados[fila][columna] = true;
            reservas.add(new Entrada(reservas.size() + 1, "" + (char)('A' + fila) + (columna + 1), fila, columna, tipoCliente, precioFinal, true));
            System.out.println("Reserva realizada para asiento " + (char)('A' + fila) + (columna + 1));
        }
    }

    
    public static void comprarEntradas(Scanner scanner) {
        System.out.println("\n===== COMPRA DE ENTRADAS =====");
        System.out.println("¿Desea comprar una reserva existente? (S/N): ");
        String resp = scanner.nextLine().toUpperCase();
        if (resp.equals("S")) {
            if (reservas.isEmpty()) {
                System.out.println("No hay reservas.");
                return;
            }
            for (int i = 0; i < reservas.size(); i++) {
                Entrada r = reservas.get(i);
                System.out.println((i + 1) + ". Asiento: " + r.ubicacion + " - " + r.tipoCliente + " - $" + (int)r.precioFinal);
            }
            System.out.print("Seleccione el número de reserva a comprar: ");
            int idx = scanner.nextInt() - 1;
            scanner.nextLine();
            if (idx >= 0 && idx < reservas.size()) {
                Entrada r = reservas.remove(idx);
                asientosReservados[r.fila][r.columna] = false;
                asientosOcupados[r.fila][r.columna] = true;
                ventas.add(new Entrada(ventas.size() + 1, r.ubicacion, r.fila, r.columna, r.tipoCliente, r.precioFinal, false));
                totalEntradasVendidas++;
                totalIngresos += r.precioFinal;
                if (!r.tipoCliente.equals("General")) totalDescuentosAplicados++;
                System.out.println("Reserva convertida en compra.");
            } else {
                System.out.println("Selección invalida.");
            }
        } else {
            mostrarDisponibilidad();
            System.out.print("¿Cuantas entradas desea comprar? ");
            int cantidad = scanner.nextInt();
            scanner.nextLine();

            for (int i = 0; i < cantidad; i++) {
                int fila = -1, columna = -1;
                String tipoCliente = "";
                boolean asientoValido = false;

                while (!asientoValido) {
                    System.out.print("Ingrese la fila (A-E): ");
                    String filaStr = scanner.nextLine().toUpperCase();
                    fila = filaStr.charAt(0) - 'A';
                    System.out.print("Ingrese el numero de asiento (1-10): ");
                    columna = scanner.nextInt() - 1;
                    scanner.nextLine();

                    
                    System.out.println("// DEBUG: Validando asiento fila " + fila + " columna " + columna);

                    if (fila < 0 || fila > 4 || columna < 0 || columna > 9) {
                        System.out.println("Asiento fuera de rango.");
                    } else if (asientosOcupados[fila][columna]) {
                        System.out.println("Asiento ya vendido.");
                    } else if (asientosReservados[fila][columna]) {
                        System.out.println("Asiento reservado, debe comprar la reserva.");
                    } else {
                        asientoValido = true;
                    }
                }

                System.out.println("Tipo de cliente: 1. General 2. Estudiante 3. Tercera edad");
                int tipo = scanner.nextInt();
                scanner.nextLine();
                switch (tipo) {
                    case 1: tipoCliente = "General"; break;
                    case 2: tipoCliente = "Estudiante"; break;
                    case 3: tipoCliente = "Tercera edad"; break;
                    default: tipoCliente = "General";
                }

                double precioBase = preciosPorFila[fila];
                double precioFinal = precioBase;
                if (tipoCliente.equals("Estudiante")) {
                    precioFinal *= 0.90;
                } else if (tipoCliente.equals("Tercera edad")) {
                    precioFinal *= 0.85;
                }

                asientosOcupados[fila][columna] = true;
                ventas.add(new Entrada(ventas.size() + 1, "" + (char)('A' + fila) + (columna + 1), fila, columna, tipoCliente, precioFinal, false));
                totalEntradasVendidas++;
                totalIngresos += precioFinal;
                if (!tipoCliente.equals("General")) totalDescuentosAplicados++;
                System.out.println("Compra realizada para asiento " + (char)('A' + fila) + (columna + 1));
            }
        }
    }

    
    public static void modificarVenta(Scanner scanner) {
        System.out.println("\n===== MODIFICAR VENTA =====");
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas para modificar.");
            return;
        }
        for (int i = 0; i < ventas.size(); i++) {
            Entrada v = ventas.get(i);
            System.out.println((i + 1) + ". Asiento: " + v.ubicacion + " - " + v.tipoCliente + " - $" + (int)v.precioFinal);
        }
        System.out.print("Seleccione el número de venta a modificar: ");
        int idx = scanner.nextInt() - 1;
        scanner.nextLine();
        if (idx >= 0 && idx < ventas.size()) {
            Entrada v = ventas.get(idx);
            System.out.print("Ingrese nuevo tipo de cliente (1. General 2. Estudiante 3. Tercera edad): ");
            int tipo = scanner.nextInt();
            scanner.nextLine();
            String tipoCliente = v.tipoCliente;
            double precioBase = preciosPorFila[v.fila];
            double precioFinal = precioBase;
            switch (tipo) {
                case 1: tipoCliente = "General"; break;
                case 2: tipoCliente = "Estudiante"; precioFinal *= 0.90; break;
                case 3: tipoCliente = "Tercera edad"; precioFinal *= 0.85; break;
                default: tipoCliente = "General";
            }
            
            System.out.println("// DEBUG: Modificando venta " + v.ubicacion + " a tipo " + tipoCliente);
            totalIngresos -= v.precioFinal;
            v.tipoCliente = tipoCliente;
            v.precioFinal = precioFinal;
            totalIngresos += precioFinal;
            System.out.println("Venta modificada.");
        } else {
            System.out.println("Selección inválida.");
        }
    }

    
    public static void imprimirBoleta(Scanner scanner) {
        System.out.println("\n===== IMPRIMIR BOLETA =====");
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas para imprimir.");
            return;
        }
        for (int i = 0; i < ventas.size(); i++) {
            Entrada v = ventas.get(i);
            System.out.println((i + 1) + ". Asiento: " + v.ubicacion + " - " + v.tipoCliente + " - $" + (int)v.precioFinal);
        }
        System.out.print("Seleccione el numero de venta para imprimir boleta: ");
        int idx = scanner.nextInt() - 1;
        scanner.nextLine();
        if (idx >= 0 && idx < ventas.size()) {
            Entrada v = ventas.get(idx);
            
            System.out.println("// DEBUG: Generando boleta para venta " + v.ubicacion);
            System.out.println("\n===== BOLETA TEATRO MORO =====");
            System.out.println("Teatro: " + NOMBRE_TEATRO);
            System.out.println("Asiento: " + v.ubicacion);
            System.out.println("Tipo de cliente: " + v.tipoCliente);
            System.out.println("Precio final: $" + (int)v.precioFinal);
            System.out.println("==============================");
        } else {
            System.out.println("Selección invalida.");
        }
    }

    
    public static void mostrarDisponibilidad() {
        System.out.println("\n===== ASIENTOS DISPONIBLES =====");
        System.out.println("     1  2  3  4  5  6  7  8  9  10");
        System.out.println("   -----------------------------");
        for (int i = 0; i < asientosOcupados.length; i++) {
            System.out.print((char)('A' + i) + " | ");
            for (int j = 0; j < asientosOcupados[i].length; j++) {
                if (asientosOcupados[i][j]) {
                    System.out.print("X  ");
                } else if (asientosReservados[i][j]) {
                    System.out.print("R  ");
                } else {
                    System.out.print("O  ");
                }
            }
            System.out.println();
        }
        System.out.println("\nLeyenda: O = Disponible, X = Vendido, R = Reservado");
        System.out.println("Fila A: $50.000 | Fila B: $40.000 | Fila C: $30.000 | Fila D: $20.000 | Fila E: $10.000");
    }

    
    public static void mostrarEstadisticas() {
        System.out.println("\n===== ESTADISTICAS =====");
        System.out.println("Entradas vendidas: " + totalEntradasVendidas);
        System.out.println("Reservas activas: " + reservas.size());
        System.out.println("Total de descuentos aplicados: " + totalDescuentosAplicados);
        System.out.println("Total ingresos: $" + (int)totalIngresos);
        System.out.println("Capacidad total: " + CAPACIDAD_SALA);
        System.out.println("Entradas disponibles: " + (CAPACIDAD_SALA - totalEntradasVendidas - reservas.size()));
    }
}