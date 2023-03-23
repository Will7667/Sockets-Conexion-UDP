package prototipobarcos;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class ServidorUDP {

    public static void main(String[] args) throws Exception {
        final int PUERTO_SERVIDOR = 5000;
        byte[] b = new byte[1024];
        ByteArrayInputStream baos;
        ObjectInputStream oos;

        System.out.println("Iniciando Servidor!");
        DatagramSocket ds = new DatagramSocket(PUERTO_SERVIDOR);

        DatagramPacket reciboCliente = new DatagramPacket(b, b.length);

        ds.receive(reciboCliente);

        byte[] data = reciboCliente.getData();

        baos = new ByteArrayInputStream(data);
        oos = new ObjectInputStream(baos);

        System.out.println("Leyendo el arreglo de tiros de cada jugador...!");
        ArrayList<Integer> arr = (ArrayList<Integer>) oos.readObject();
        System.out.println("-------------------Jugador 1--------------------");
        int turno;
        turno = 1;
        for (int i = 0; i < (arr.size() - 10); i = i + 2) {
            System.out.println("Turno " + turno + ": {" + arr.get(i) + ", " + arr.get(i + 1) + "}");
            turno = turno + 1;
        }
        
        System.out.println("-------------------Jugador 2--------------------");
        turno = 1;
        for (int i = 10; i < arr.size(); i = i + 2) {
            System.out.println("Turno " + turno + ": {" + arr.get(i) + ", " + arr.get(i + 1) + "}");
            turno = turno + 1;
        }
        System.out.println("------------------------------------------------");
        System.out.println("Arreglo total: " + arr);
    }

}
