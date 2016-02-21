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
package stri.ss_manager.exe;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.LinkedList;
import java.util.Queue;
import stri.ss_manager.SNMPKernel.SNMPAgent.SNMPAgent;
import stri.ss_manager.SNMPMessage.SNMPMessage;
import stri.ss_manager.SNMPMessage.handler.SNMPMessageHandlerInputStream;
import stri.ss_manager.SNMPMessage.handler.SNMPMessageHandlerOutputStream;
import stri.ss_manager.SNMPMessage.transport.SocketHandlerInputStream;
import stri.ss_manager.SNMPMessage.transport.SocketHandlerOutputStream;

/**
 *
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * 
 * Cette classe permet de gérer partiellement l'agent SNMP en intialisant
 * les Thread nécessaires.
 * 
 * @version 1
 */
public class SNMPAgentTest {
    
        public static void main(String[] args) {
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
//      SocketHandlerTrapListener      SOCK_HDLR_TRAP_LSTNR;// Thread gérant les DatagramPacket entrants sur le port 162 (TRAPS)
        
        SNMPAgent                      SNMP_AGENT;

        //Autres

        // Initialisation du Socket
        System.out.println("[MAIN_PROC]: Initializing Sockets...");
        try{
            socket         = new DatagramSocket(161);         // socket d'envoie/réception des requêtes SNMP
            
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
//      SOCK_HDLR_TRAP_LSTNR = new SocketHandlerTrapListener(trap_listenner, DG_packet_queue_IS);
        SOCK_HDLR_OS         = new SocketHandlerOutputStream(socket, DG_packet_queue_OS);

        // Couche SNMPMessageHandler
        S_MSG_HDLR_IS = new SNMPMessageHandlerInputStream(DG_packet_queue_IS, S_MSG_queue_IS);
        S_MSG_HDLR_OS = new SNMPMessageHandlerOutputStream(DG_packet_queue_OS, S_MSG_queue_OS);

        // Couche SNMPKernel
        
        SNMP_AGENT = new SNMPAgent(S_MSG_queue_IS, S_MSG_queue_OS);

        System.out.println("[MAIN_PROC]: Threads initializded...");
        System.out.println("[MAIN_PROC]: Successfull initialized !");
        // Démarrage des Threads

        // Couche SocketHandler
        SOCK_HDLR_IS.start();
//      SOCK_HDLR_TRAP_LSTNR.start();
        SOCK_HDLR_OS.start();

        S_MSG_HDLR_IS.start();
        S_MSG_HDLR_OS.start();
        SNMP_AGENT.start();
        
        
    }
}
