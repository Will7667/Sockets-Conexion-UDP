package Controlador;

import Modelo.Ficha;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;

public class Tablero implements Serializable {

    public static final int FILAS = 7;
    public static final int COLUMNAS = 6;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int[] TAMANIOS_BARCOS = {3, 3, 2, 2, 2};
    public static int turnos = 0;
    public static int suma_total = 0;

    public static int getSumaTotal() {
        return suma_total;
    }

    public static final int PUERTO_SERVIDOR = 5000;
    public static byte[] bufferMensaje = new byte[1024];
    public static DatagramSocket ds;
    public static DatagramPacket envioServidor;
    public static InetAddress ia;
    public static String mensaje;

    public static ArrayList<Integer> numeros = new ArrayList<>();
    public static String numeros1;

    public static ByteArrayOutputStream out;
    public static ObjectOutputStream outputStream;
    public static byte[] listData;

    public Tablero(int jugador) throws Exception {

        ds = new DatagramSocket();
        ia = InetAddress.getLocalHost();

        Ficha[][] tablero = new Ficha[FILAS][COLUMNAS];
        llenarTablero(tablero);
        for (int i = 0; i < TAMANIOS_BARCOS.length; i++) {
            posicionarBarcos(tablero, TAMANIOS_BARCOS[i], (i + 1), Ficha.ESTADO_OCUPADO);
        }

        if (jugador == 1) {
            System.out.println("----------Jugador 1----------");
            mostrarTablero(tablero);
            System.out.println("");

            contarTiros(tablero, 2, 3);
            System.out.println("");
            contarTiros(tablero, 4, 6);
            System.out.println("");
            contarTiros(tablero, 1, 5);
            System.out.println("");
            contarTiros(tablero, 3, 3);
            System.out.println("");
            contarTiros(tablero, 2, 1);

            //DatagramPacket envioArray = new DatagramPacket(numeros1.getBytes(), numeros1.length(), ia, PUERTO_SERVIDOR);
            //ds.send(envioArray);
        } else {
            System.out.println("----------Jugador 2----------");
            mostrarTablero(tablero);
            System.out.println("");

            contarTiros(tablero, 1, 4);
            System.out.println("");
            contarTiros(tablero, 6, 4);
            System.out.println("");
            contarTiros(tablero, 3, 2);
            System.out.println("");
            contarTiros(tablero, 1, 1);
            System.out.println("");
            contarTiros(tablero, 2, 6);

            out = new ByteArrayOutputStream();
            outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(numeros);
            listData = out.toByteArray();

            DatagramPacket envioArray = new DatagramPacket(listData, listData.length, ia, PUERTO_SERVIDOR);
            ds.send(envioArray);
        }

    }

    public static void main(String[] args) throws Exception {
        Tablero jugador1 = new Tablero(1);
        int sumaJugador1 = Tablero.suma_total;
        suma_total = 0;
        turnos = 0;
        Tablero jugador2 = new Tablero(2);
        int sumaJugador2 = Tablero.suma_total;

        if (sumaJugador1 == sumaJugador2) {
            System.out.println("Es un empate!");
        } else if (sumaJugador1 > sumaJugador2) {
            System.out.println("El ganador es el Jugador 1!");
        } else {
            System.out.println("El ganador es el Jugador 2!");
        }

    }

    public static Ficha[][] llenarTablero(Ficha[][] tablero) {
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                tablero[i][j] = new Ficha(i, j, Ficha.ESTADO_VACIO);
            }
        }
        return tablero;
    }

    public static Ficha[][] posicionarBarcos(Ficha[][] tablero, int extension, int identificador, int estado) {
        Random random = new Random();
        int fila_inicial;  //num aleatorio desde el 0 hasta FILAS - 1
        int columna_inicial; //num aleatorio desde el 0 hasta COLUMNAS - 1
        int orientacion; //dependera si sera HORIZONTAL o VERTICAL
        boolean continuar;
        do {
            continuar = false;
            fila_inicial = random.nextInt(FILAS);
            columna_inicial = random.nextInt(COLUMNAS);
            orientacion = random.nextInt(2);

            if ((orientacion == HORIZONTAL) && (columna_inicial + extension) >= COLUMNAS
                    || (orientacion == VERTICAL) && (fila_inicial + extension) >= FILAS) {
                continuar = true;
            } else {
                //HORIZONTAL = 0,  VERTICAL = 1
                if (orientacion == HORIZONTAL) {
                    int columna_final = columna_inicial + extension;

                    for (int columna = columna_inicial; columna < columna_final; columna++) {
                        if (tablero[fila_inicial][columna].getEstado() != Ficha.ESTADO_VACIO) {
                            continuar = true;
                            break;
                        }
                    }
                } else {
                    int fila_final = fila_inicial + extension;

                    for (int fila = fila_inicial; fila < fila_final; fila++) {
                        if (tablero[fila][columna_inicial].getEstado() != Ficha.ESTADO_VACIO) {
                            continuar = true;
                            break;
                        }
                    }
                }
            }

        } while (continuar);

        //HORIZONTAL = 0,  VERTICAL = 1
        if (orientacion == HORIZONTAL) {
            int columna_final = columna_inicial + extension;

            for (int columna = columna_inicial; columna < columna_final; columna++) {
                tablero[fila_inicial][columna].setId_barcos(identificador);
                tablero[fila_inicial][columna].setEstado(estado);
                tablero[fila_inicial][columna].setOrientacion(orientacion);
            }
        } else {
            int fila_final = fila_inicial + extension;

            for (int fila = fila_inicial; fila < fila_final; fila++) {
                tablero[fila][columna_inicial].setId_barcos(identificador);
                tablero[fila][columna_inicial].setEstado(estado);
                tablero[fila][columna_inicial].setOrientacion(orientacion);
            }
        }
        return tablero;
    }

    public static Ficha[][] mostrarTablero(Ficha[][] tablero) {
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                System.out.print(" " + tablero[i][j] + " ");
            }
            System.out.println("");
        }
        return tablero;
    }

    public static void contarTiros(Ficha[][] tablero, int filaDisparo, int columnaDisparo) throws IOException {
        Ficha disparo = tablero[filaDisparo - 1][columnaDisparo - 1];
        numeros.add(filaDisparo);
        numeros.add(columnaDisparo);

        //numeros1 = numeros1 + filaDisparo + ", " + columnaDisparo +"\n";
        if (turnos != 5) {
            if (disparo.getEstado() == Ficha.ESTADO_VACIO || disparo.getEstado() == Ficha.ESTADO_OCUPADO) {
                if (disparo.getEstado() == Ficha.ESTADO_VACIO) {
                    suma_total = suma_total - 10;
                } else if (disparo.getEstado() == Ficha.ESTADO_OCUPADO) {
                    suma_total = suma_total + 10;
                }
                disparo.setEstado(Ficha.ESTADO_GOLPEADO);
                turnos = turnos + 1;
            }
        } else {
            System.out.println("Se acabaron los turnos! ");
        }

        mostrarTablero(tablero);
        System.out.println("La suma total de la " + turnos + " ronda es: " + suma_total);
    }
}