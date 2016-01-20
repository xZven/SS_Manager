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
package stri.ss_manager.SNMPMessage.transport;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Queue;

/**
 *
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * @version 1.0
 * @see Queue
 * @see SNMPMessageHandlerInputStream
 * 
 * Cette classe permet de gérer les PDU SNMP entrants sur le socket UDP.
 * 
 * <p>
 * Ce Thread écoute sur le socket UDP et attent de recevoir des PDU SNMP.
 * Lorsqu'il en reçoit, ce dernier les places dans une file d'attente de type
 * FIFO dans le Thread du SNMPMessageHandlerInputStream.
 * </p>
 */
public class SocketHandlerInputStream extends Thread{
    
    // attributs
    
    private DatagramSocket          socket;                   // Socket UDP
    private Queue<DatagramPacket>   DG_packet_queue_IS;       // File d'attente pour les PDU SNMP pour le flux entrant;
    private boolean                 RUNNING = true;           // Utiliser pour mettre fin au Thread
    
    // méthodes
    
    /**
     * 
     * @param socket Socket UDP où ce Thread écoutera les DGPacket entrants
     * @param DG_packet_queue_IS File d'attente où seront placés les DGPacket entrants.
     */
    public SocketHandlerInputStream(DatagramSocket socket, Queue<DatagramPacket> DG_packet_queue_IS) {
        // Le socket est passé par le programme principale.
        this.socket = socket;
        //
        this.DG_packet_queue_IS = DG_packet_queue_IS;
        //
        System.out.println("[SOCK_HDLR_IS]: Ready...");
    }   
    /**
     * Cette procédure permet d'arrêter le Thread.
     * Tous les PDU SNMP entrantes seront ignorées.
     */
    
    
    
    
    
    public void Stop(){
        System.out.println("[SOCK_HDLR_IS]: Stopping...");
        // arrêt du Thread
        this.RUNNING = false;
    }
    @Override
    public void run(){
        
        DatagramPacket data_packet = null;
        
        System.out.println("[SOCK_HDLR_IS]: Ready...");
        //
        while(socket.isConnected() && RUNNING){ // boucle infinie -- socket connecté && RUN
            try{
                // on écoute sur le socket
                socket.receive(data_packet);
                System.out.println("[SOCK_HDLR_IS]: Datagram Received");
                // On  place le paquet Datagram reçu dans la file
                DG_packet_queue_IS.add(data_packet);
                System.out.println("[SOCK_HDLR_IS]: Datagram transmited to S_MSG_HDLR_IS");
                // 
                data_packet = null;
            }catch(Exception e){
                System.err.println("[SOCK_HDLR_IS]: ERROR -- > "+e.getMessage() );
            }
        }
        
        // on a quitté la booucle while --> FIN du thread
        System.out.println("[SOCK_HDLR_IS]: Stopped");
    }
}
