package stri.ss_manager.exe;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import stri.ss_manager.SNMP.smi.OID;
import stri.ss_manager.SNMP.smi.VarBind;
import stri.ss_manager.SNMPKernel.SNMPHandler;
import stri.ss_manager.SNMPMessage.SNMPMessage;
import stri.ss_manager.SNMPMessage.handler.*;
import stri.ss_manager.SNMPMessage.payload.SNMPMessagePayload;
import stri.ss_manager.SNMPMessage.transport.SocketHandlerInputStream;
import stri.ss_manager.SNMPMessage.transport.SocketHandlerOutputStream;
import stri.ss_manager.SNMPMessage.transport.SocketHandlerTrapListener;
import stri.ss_manager.SS_Manager_IHM.ManagerIHM;

/*
 * Copyright (C) 2016 Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

/**
 * 
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * 
 *  * Cette classe permet de tester le Manager SNMP en initialisant
 * les Threads nécessaire et en créant des messages de tests.
 * 
 * @version 1
 * 
 */
public class Test {

    /**
     * Programme principale qui sert à tester le Manager SNMP
     * 
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    // Déclaration des variables
        //Sockets
        DatagramSocket                  socket          = null;
        DatagramSocket                  trap_listenner  = null;
        //Queues
        Queue<DatagramPacket>   DG_packet_queue_IS;         // File d'attente pour les PDU SNMP pour le flux entrant;
        Queue<DatagramPacket>   DG_packet_queue_OS;         // File d'attente pour les PDU SNMP pour le flux sortant;

        Queue<SNMPMessage>      S_MSG_queue_IS;             // File d'attente pour les SNMPMessages entrants (transmit au noyau)
        Queue<SNMPMessage>      S_MSG_queue_OS;             // File d'attente pour les SNMPMessages entrants (transmit au noyau)

        //Thread
        SNMPMessageHandlerInputStream  S_MSG_HDLR_IS;       // Thread gérant les SNMPMessages entrants
        SNMPMessageHandlerOutputStream S_MSG_HDLR_OS;       // Thread gérant les SNMPMessages sortants

        SocketHandlerInputStream       SOCK_HDLR_IS;        // Thread gérant les DatagramPacket entrants
        SocketHandlerOutputStream      SOCK_HDLR_OS;        // Thread gérant les DatagramPacket sortants
        SocketHandlerTrapListener      SOCK_HDLR_TRAP_LSTNR;// Thread gérant les DatagramPacket entrants sur le port 162 (TRAPS)

        //Autres

        // Initialisation du Socket
        System.out.println("[MAIN_PROC]: Initializing Sockets...");
        try{
            socket         = new DatagramSocket(10100);         // socket d'envoie/réception des requêtes SNMP
            trap_listenner = new DatagramSocket(162);         // socket de réception des TRAPS
        }catch(Exception e){
            System.err.println("[MAIN_PROC]: ERROR OPENNING SOCKETS --> "+e.getMessage());
            System.err.println("Exiting...");

            System.exit(-1);                                  // FIN RPOG si Erreur de chargement du socket
        }
        System.out.println("[MAIN_PROC]: Sockets initialized...");

        // Initialisation des files d'attentes
        System.out.println("[MAIN_PROC]: Initializing queues...");
        DG_packet_queue_IS = new LinkedList<>();
        DG_packet_queue_OS = new LinkedList<>();

        S_MSG_queue_IS     = new LinkedList<>();
        S_MSG_queue_OS     = new LinkedList<>();

        System.out.println("[MAIN_PROC]: Queues initialized...");
        // Initialisation des Threads
        System.out.println("[MAIN_PROC]: Initializing Threads...");

        // Couche SocketHandler
        SOCK_HDLR_IS         = new SocketHandlerInputStream(socket, DG_packet_queue_IS);
        SOCK_HDLR_TRAP_LSTNR = new SocketHandlerTrapListener(trap_listenner, DG_packet_queue_IS);
        SOCK_HDLR_OS         = new SocketHandlerOutputStream(socket, DG_packet_queue_OS);

        // Couche SNMPMessageHandler
        S_MSG_HDLR_IS = new SNMPMessageHandlerInputStream(DG_packet_queue_IS, S_MSG_queue_IS);
        S_MSG_HDLR_OS = new SNMPMessageHandlerOutputStream(DG_packet_queue_OS, S_MSG_queue_OS);

        // Couche SNMPKernel

        System.out.println("[MAIN_PROC]: Threads initializded...");
        System.out.println("[MAIN_PROC]: Successfull initialized !");
        // Démarrage des Threads

        // Couche SocketHandler
        SOCK_HDLR_IS.start();
        SOCK_HDLR_TRAP_LSTNR.start();
        SOCK_HDLR_OS.start();


        S_MSG_HDLR_IS.start();
        S_MSG_HDLR_OS.start();

        ManagerIHM ihm = new ManagerIHM(new SNMPHandler(S_MSG_queue_IS, S_MSG_queue_OS));
    
        // Initialisation de l'IHM
     /*  java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ManagerIHM().setVisible(true);
            }
        }); */

    
        // Ici le role du programme principale est de vérifier
        // que les threads fonctionnent corecctement.
        // Il les relance en cas d'arrête.

    /*    while(true){
            if(SOCK_HDLR_IS.isAlive() == false){
                SOCK_HDLR_IS.start();
            }
            
            if(SOCK_HDLR_OS.isAlive() == false){
                SOCK_HDLR_OS.start();
            }
            
            if(SOCK_HDLR_TRAP_LSTNR.isAlive() == false){
                SOCK_HDLR_TRAP_LSTNR.start();
            }
            
            if(S_MSG_HDLR_IS.isAlive() == false){
                S_MSG_HDLR_IS.start();
            }
            
            if(S_MSG_HDLR_OS.isAlive() == false){
                S_MSG_HDLR_OS.start();
            }
            
            
            try{
                sleep(1000);
            }catch(Exception e){
                System.err.println("[MAIN_PROC]: "+e.getMessage());
            }
        } */
/*
        // Test d'envoi de message SNMP get
        ArrayList<VarBind> varBindingsList = new ArrayList<>();
        // création d'un oid
        varBindingsList.add(new VarBind(new OID("1.3.6.1.2.1.1.5.0"), null));
        // création de la payload
        SNMPMessagePayload payload      = new SNMPMessagePayload(0X0F000001, 0, 0, varBindingsList);
        try{
            InetAddress Receiver        = InetAddress.getByName("172.16.48.65");            //
            // création du message SNMP
            SNMPMessage SNMPTestMessage = new SNMPMessage(null, Receiver, 161, 2, "public".getBytes(), (byte) 0xA0, payload); 
            // envoi du message
            S_MSG_queue_OS.add(SNMPTestMessage);
        }catch(Exception e){
            System.err.println("ERROR " +e.getMessage());
        }
        
        // Test d'envoi de message SNMP set
        ArrayList<VarBind> varBindingsList1 = new ArrayList<>();
        try{
            Thread.sleep(1000);
        }catch(Exception e){
            
        }
        varBindingsList1.add(new VarBind(new OID("1.3.6.1.2.1.1.5.0"), "debiantest".getBytes()));
        // créeation de la payload
        SNMPMessagePayload payload1      = new SNMPMessagePayload(0X0F000001, 0, 0, varBindingsList1);
        try{
            InetAddress Receiver        = InetAddress.getByName("127.0.0.1");            //
            // création du message SNMP
            SNMPMessage SNMPTestMessage1 = new SNMPMessage(null, Receiver, 161, 2, "public".getBytes(), (byte) 0xA4, payload1); 
            // envoi
            S_MSG_queue_OS.add(SNMPTestMessage1);
            
        }catch(Exception e){
            System.err.println("ERROR " +e.getMessage());
        }
  */
    }

}
