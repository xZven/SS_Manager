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
import stri.ss_manager.SNMPMessage.handler.SNMPMessageHandlerInputStream;

/**
 *
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * @version 1.0
 * @see SNMPMessageHandlerInputStream
 * 
 * Cette classe permet de gérer les PDU SNMP entrants sur les socket UDP.
 * 
 * <p>
 * Ce Thread écoute sur le socket UDP et attent de recevoir des PDU SNMP.
 * Lorsqu'il en reçoit, ce dernier les places dans une file d'attente de type
 * FIFO dans le Thread du SNMPMessageHandlerInputStream.
 * </p>
 */
public class SocketHandlerInputStream extends Thread{
    
    // attributs
    
    private DatagramSocket socket;                              // Socket UDP
    private SNMPMessageHandlerInputStream S_MSG_HDLR_IS;        // Gestion messages SNMP pour les flux entrants
    
    // méthodes
    
    /**
     * 
     * @param socket Socket UDP où le thread écoutera les PDU SNMP entrantes
     * @param S_MSG_HDLR_IS Gestionnaire pour les messages SNMP entrant.
     */
    public SocketHandlerInputStream(DatagramSocket socket, SNMPMessageHandlerInputStream S_MSG_HDLR_IS) {
        //
        this.socket        = socket;
        //
        this.S_MSG_HDLR_IS = S_MSG_HDLR_IS;
        //
        System.out.println("[SOCK_HDLR_IS]: Ready...");
    }
    /**
     * Cette procédure permet d'arrêter le Thread de manière sécurisé.
     * Tous les PDU SNMP entrantes seront ignorées.
     * 
     */
    public void Stop(){
        System.out.println("[SOCK_HDLR_IS]: Stopping...");
        //
        socket.close();
        //
        System.out.println("[SOCK_HDLR_IS]: Stopped");
    }
    @Override
    public void run(){
        
        DatagramPacket data_packet = null;
        
        System.out.println("[SOCK_HDLR_IS]: Ready...");
        //
        while(socket.isConnected()){ // boucle infinie -- socket connecté
            try{
                // on écoute sur le socket
                socket.receive(data_packet);
                System.out.println("[SOCK_HDLR_IS]: Datagram Received");
                // On transmet place maintenant le paquet Datagram reçu dans la file
                
                System.out.println("[SOCK_HDLR_IS]: Datagram transmited to S_MSG_HDLR_IS");
                // 
                data_packet = null;
            }catch(Exception e){
                System.err.println("[SOCK_HDLR_IS]: ERROR -- > "+e.getMessage() );
            }
        }
    }
}
