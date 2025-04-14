/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.teatromoro2;

import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * @author alfil
 */

public class Teatromoro2 {
    // Variables estáticas para estadísticas
    private static int totalDescuentosAplicados = 0;
    private static ArrayList<String> registroVentas = new ArrayList<>();
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean[][] asientosOcupados = new boolean[5][10];
        int[] preciosPorFila = {50000, 40000, 30000, 20000, 10000};
        boolean continuarPrograma = true;
        System.out.println("..::: Teatro Moro :::..");
        
        while (continuarPrograma) {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Asientos Disponibles");
            System.out.println("2. Comprar entrada");
            System.out.println("3. Caja");
            System.out.println("4. Buscar entrada");
            System.out.println("5. Promociones");
            System.out.println("6. Eliminar entrada");
            System.out.println("7. Salir");
            
            System.out.print("\nSeleccione una opcion: ");
            int opcion = 0;
            
            try {
                opcion = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Error: Ingrese un numero valido.");
                scanner.nextLine();
                continue;
            }
            
            switch (opcion) {
                case 1:
                    mostrarDisponibilidad(asientosOcupados);
                    break;
                case 2:
                    comprarEntrada(scanner, asientosOcupados, preciosPorFila);
                    break;
                case 3:
                    mostrarEstadisticas(asientosOcupados, preciosPorFila);
                    break;
                case 4:
                    buscarEntrada(scanner);
                    break;
                case 5:
                    mostrarPromociones();
                    break;
                case 6:
                    eliminarEntrada(scanner, asientosOcupados);
                    break;
                case 7:
                    System.out.println("Gracias por preferir Teatro Moro.");
                    continuarPrograma = false;
                    break;
                default:
                    System.out.println("Opcion invalida. Por favor, escriba una opcion del menu.");
            }
        }
        
        scanner.close();
    }

    public static void mostrarDisponibilidad(boolean[][] asientosOcupados) {
        System.out.println("\n===== TEATRO MORO =====");
        System.out.println("\n===== ASIENTOS DISPONIBLES =====");
        System.out.println("     1  2  3  4  5  6  7  8  9  10");
        System.out.println("   -----------------------------");
        
        for (int i = 0; i < asientosOcupados.length; i++) {
            System.out.print((char)('A' + i) + " | ");
            for (int j = 0; j < asientosOcupados[i].length; j++) {
                if (asientosOcupados[i][j]) {
                    System.out.print("X  ");
                } else {
                    System.out.print("O  ");
                }
            }
            System.out.println();
        }
        
        System.out.println("\nLeyenda: O = Disponible, X = Ocupado");
        System.out.println("\nPrecios por fila:");
        System.out.println("Fila A: $50.000");
        System.out.println("Fila B: $40.000");
        System.out.println("Fila C: $30.000");
        System.out.println("Fila D: $20.000");
        System.out.println("Fila E: $10.000");
    }

    public static void mostrarEstadisticas(boolean[][] asientosOcupados, int[] preciosPorFila) {
        System.out.println("\n===== ESTADISTICAS DE VENTAS =====");
        
        int totalAsientos = asientosOcupados.length * asientosOcupados[0].length;
        int asientosVendidos = 0;
        int recaudacionTotal = 0;
        
        for (int i = 0; i < asientosOcupados.length; i++) {
            int vendidosFila = 0;
            for (int j = 0; j < asientosOcupados[i].length; j++) {
                if (asientosOcupados[i][j]) {
                    asientosVendidos++;
                    vendidosFila++;
                    recaudacionTotal += preciosPorFila[i];
                }
            }
            System.out.println("Fila " + (char)('A' + i) + ": " + vendidosFila + " asientos vendidos");
        }
        
        double porcentajeOcupacion = (double) asientosVendidos / totalAsientos * 100;
        
        System.out.println("\nTotal de asientos: " + totalAsientos);
        System.out.println("Asientos vendidos: " + asientosVendidos);
        System.out.println("Asientos disponibles: " + (totalAsientos - asientosVendidos));
        System.out.println("Porcentaje de ocupacion: " + String.format("%.2f", porcentajeOcupacion) + "%");
        System.out.println("Recaudación total: $" + recaudacionTotal);
        System.out.println("Total de descuentos aplicados: " + totalDescuentosAplicados);
    }
    
    public static void comprarEntrada(Scanner scanner, boolean[][] asientosOcupados, int[] preciosPorFila) {
        System.out.println("\n===== COMPRA DE ENTRADAS =====");
        
        mostrarDisponibilidad(asientosOcupados);
        
        int fila = 0;
        int columna = 0;
        String tipoCliente = "";
        boolean datosValidos = false;
        
        do {
            try {
                System.out.print("\nIngrese la fila (A-E): ");
                String filaStr = scanner.next().toUpperCase();
                if (filaStr.length() != 1 || filaStr.charAt(0) < 'A' || filaStr.charAt(0) > 'E') {
                    System.out.println("Error: Fila invalida. Debe ser una letra entre A y E.");
                    continue;
                }
                fila = filaStr.charAt(0) - 'A';
                
                System.out.print("Ingrese el numero de asiento (1-10): ");
                columna = scanner.nextInt() - 1;
                if (columna < 0 || columna > 9) {
                    System.out.println("Error: Numero de asiento invalido. Debe ser un número entre 1 y 10.");
                    continue;
                }
                
                if (asientosOcupados[fila][columna]) {
                    System.out.println("Error: El asiento seleccionado ya está ocupado. Por favor, elija otro.");
                    continue;
                }
                
                System.out.println("\nTipo de cliente:");
                System.out.println("1. General");
                System.out.println("2. Estudiante (10% descuento)");
                System.out.println("3. Tercera edad (15% descuento)");
                System.out.print("Seleccione tipo de cliente: ");
                int tipoClienteOp = scanner.nextInt();
                
                switch(tipoClienteOp) {
                    case 1: tipoCliente = "General"; break;
                    case 2: tipoCliente = "Estudiante"; break;
                    case 3: tipoCliente = "Tercera edad"; break;
                    default:
                        System.out.println("Tipo de cliente no válido");
                        continue;
                }
                
                datosValidos = true;
                
            } catch (Exception e) {
                System.out.println("Error: Ingreso invalido. Intente nuevamente.");
                scanner.nextLine();
            }
        } while (!datosValidos);
        
        int precioBase = preciosPorFila[fila];
        double precioFinal = precioBase;
        String tipoDescuento = "Sin descuento";
        
        // Aplicar descuentos según tipo de cliente
        if (tipoCliente.equals("Estudiante")) {
            precioFinal = precioBase * 0.90; // 10% descuento
            tipoDescuento = "Descuento estudiante (10%)";
            totalDescuentosAplicados++;
        } else if (tipoCliente.equals("Tercera edad")) {
            precioFinal = precioBase * 0.85; // 15% descuento
            tipoDescuento = "Descuento tercera edad (15%)";
            totalDescuentosAplicados++;
        }
        
        asientosOcupados[fila][columna] = true;
        
        // Registrar la venta
        String registroVenta = String.format("Asiento: %s%d, Tipo: %s, Precio: $%d", 
            (char)('A' + fila), (columna + 1), tipoCliente, (int)precioFinal);
        registroVentas.add(registroVenta);
        
        System.out.println("\n===== RESUMEN DE LA COMPRA =====");
        System.out.println("Asiento: " + (char)('A' + fila) + (columna + 1));
        System.out.println("Tipo de cliente: " + tipoCliente);
        System.out.println("Precio base: $" + precioBase);
        System.out.println(tipoDescuento);
        System.out.println("Precio final: $" + (int)precioFinal);
        System.out.println("\n¡Compra realizada con éxito! Disfrute la función.");
    }
    
    public static void buscarEntrada(Scanner scanner) {
        if (registroVentas.isEmpty()) {
            System.out.println("No hay entradas registradas.");
            return;
        }
        
        System.out.println("\n===== BÚSQUEDA DE ENTRADAS =====");
        System.out.println("1. Buscar por número de asiento");
        System.out.println("2. Buscar por tipo de cliente");
        System.out.print("Seleccione una opción: ");
        
        try {
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            
            switch (opcion) {
                case 1:
                    System.out.print("Ingrese fila (A-E): ");
                    String fila = scanner.nextLine().toUpperCase();
                    System.out.print("Ingrese número (1-10): ");
                    int numero = scanner.nextInt();
                    String asientoBuscado = fila + numero;
                    
                    boolean encontrado = false;
                    for (String venta : registroVentas) {
                        if (venta.contains("Asiento: " + asientoBuscado)) {
                            System.out.println("\nEntrada encontrada:");
                            System.out.println(venta);
                            encontrado = true;
                        }
                    }
                    if (!encontrado) {
                        System.out.println("No se encontró la entrada.");
                    }
                    break;
                    
                case 2:
                    System.out.println("Tipos de cliente disponibles:");
                    System.out.println("1. General");
                    System.out.println("2. Estudiante");
                    System.out.println("3. Tercera edad");
                    System.out.print("Seleccione tipo de cliente: ");
                    int tipoCliente = scanner.nextInt();
                    
                    String tipoBuscado = "";
                    switch (tipoCliente) {
                        case 1: tipoBuscado = "General"; break;
                        case 2: tipoBuscado = "Estudiante"; break;
                        case 3: tipoBuscado = "Tercera edad"; break;
                        default: 
                            System.out.println("Tipo de cliente no válido");
                            return;
                    }
                    
                    System.out.println("\nEntradas encontradas:");
                    boolean hayEntradas = false;
                    for (String venta : registroVentas) {
                        if (venta.contains("Tipo: " + tipoBuscado)) {
                            System.out.println(venta);
                            hayEntradas = true;
                        }
                    }
                    if (!hayEntradas) {
                        System.out.println("No se encontraron entradas para ese tipo de cliente.");
                    }
                    break;
                    
                default:
                    System.out.println("Opción no válida.");
            }
        } catch (Exception e) {
            System.out.println("Error en la entrada de datos.");
            scanner.nextLine();
        }
    }
    
    public static void mostrarPromociones() {
        System.out.println("\n===== PROMOCIONES DISPONIBLES =====");
        System.out.println("1. Estudiantes: 10% de descuento en todas las ubicaciones");
        System.out.println("2. Tercera edad: 15% de descuento en todas las ubicaciones");
        System.out.println("3. Grupos (más de 5 personas): 5% de descuento adicional");
        System.out.println("\nNotas:");
        System.out.println("- Los descuentos no son acumulables");
        System.out.println("- Se requiere presentar documentación correspondiente");
    }
    
    public static void eliminarEntrada(Scanner scanner, boolean[][] asientosOcupados) {
        System.out.println("\n===== ELIMINAR ENTRADA =====");
        
        try {
            System.out.print("Ingrese fila (A-E): ");
            String filaStr = scanner.next().toUpperCase();
            System.out.print("Ingrese número de asiento (1-10): ");
            int columna = scanner.nextInt() - 1;
            
            int fila = filaStr.charAt(0) - 'A';
            
            if (fila < 0 || fila >= 5 || columna < 0 || columna >= 10) {
                System.out.println("Asiento no válido.");
                return;
            }
            
            if (!asientosOcupados[fila][columna]) {
                System.out.println("El asiento no está ocupado.");
                return;
            }
            
            asientosOcupados[fila][columna] = false;
            
            // Eliminar del registro de ventas
            String asientoBuscado = filaStr + (columna + 1);
            registroVentas.removeIf(venta -> venta.contains("Asiento: " + asientoBuscado));
            
            System.out.println("Entrada eliminada exitosamente.");
            
        } catch (Exception e) {
            System.out.println("Error en la entrada de datos.");
            scanner.nextLine();
        }
    }
}