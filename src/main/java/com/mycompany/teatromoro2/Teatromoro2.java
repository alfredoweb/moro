/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.teatromoro2;

import java.util.Scanner;
/**
 *
 * @author alfil
 */
public class Teatromoro2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean[][] asientosOcupados = new boolean[5][10];
        int[] preciosPorFila = {50000, 40000, 30000, 20000, 10000};
        boolean continuarPrograma = true;
        System.out.println("..::: Teatro Moro :::..");
        
        while (continuarPrograma) {
            System.out.println("\n===== MENU =====");
            for (int i = 1; i <= 4; i++) {
                switch (i) {
                    case 1:
                        System.out.println(i + ". Asientos Disponibles");
                        break;
                    case 2:
                        System.out.println(i + ". Comprar entrada");
                        break;
                    case 3:
                        System.out.println(i + ". Caja");
                        break;
                    case 4:
                        System.out.println(i + ". Salir");
                        break;
                }
            }
            
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
    
    public static void comprarEntrada(Scanner scanner, boolean[][] asientosOcupados, int[] preciosPorFila) {
        System.out.println("\n===== COMPRA DE ENTRADAS =====");
        
        mostrarDisponibilidad(asientosOcupados);
        
        int fila = 0;
        int columna = 0;
        int edad = 0;
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
                
                System.out.print("Ingrese la edad del espectador: ");
                edad = scanner.nextInt();
                if (edad <= 0 || edad > 122) {
                    System.out.println("Error: Edad invalida. Debe ser un numero positivo menor a 122.");
                    continue;
                }
                
                datosValidos = true;
                
            } catch (Exception e) {
                System.out.println("Error: Ingreso invalido. Intente nuevamente.");
                scanner.nextLine(); // Limpiar buffer
            }
        } while (!datosValidos);
        
        int precioBase = preciosPorFila[fila];
        
        double precioFinal = precioBase;
        boolean descuentoAplicado = false;
        String tipoDescuento = "Sin descuento";
        
        if (edad < 18) {
            precioFinal = precioBase * 0.7;
            descuentoAplicado = true;
            tipoDescuento = "Descuento para menores (30%)";
        } else if (edad >= 65) {
            precioFinal = precioBase * 0.5;
            descuentoAplicado = true;
            tipoDescuento = "Descuento para adultos mayores (50%)";
        }
        
        asientosOcupados[fila][columna] = true;
        
        System.out.println("\n===== RESUMEN DE LA COMPRA =====");
        System.out.println("Asiento: " + (char)('A' + fila) + (columna + 1));
        System.out.println("Precio base: $" + precioBase);
        if (descuentoAplicado) {
            System.out.println(tipoDescuento);
        }
        System.out.println("Precio final: $" + (int)precioFinal);
        System.out.println("\n¡Compra realizada con éxito! Disfrute la función.");
        
        // Preguntar si desea realizar otra compra
        System.out.print("\n¿Desea realizar otra compra? (S/N): ");
        String respuesta = scanner.next();
        if (respuesta.equalsIgnoreCase("S")) {
            comprarEntrada(scanner, asientosOcupados, preciosPorFila);
        }
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
        System.out.println("Venta total estimada: $" + recaudacionTotal);
    }
}