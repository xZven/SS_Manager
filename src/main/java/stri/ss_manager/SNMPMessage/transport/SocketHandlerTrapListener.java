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
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * @version 1.0
 * @see Queue
 * @see SNMPMessageHandlerInputStream
 * 
 * Cette classe permet de gérer les PDU SNMP entrants sur le socket UDP port 162 (TRAP).
 * 
 * <p>
 * Ce Thread écoute sur le socket UDP et attent de recevoir des PDU SNMP.
 * Lorsqu'il en reçoit, ce dernier les places dans une file d'attente de type
 * FIFO dans le Thread du SNMPMessageHandlerInputStream.
 * </p>
 * 

 */
public class SocketHandlerTrapListener extends SocketHandlerInputStream{

    /**
     * 
     * @param socket Socket où les TRAPs arriveront
     * @param DG_packet_queue_IS File d'attente où les TRAP seront placé.
     */
    public SocketHandlerTrapListener(DatagramSocket socket, Queue<DatagramPacket> DG_packet_queue_IS) {
        //
        super(socket, DG_packet_queue_IS);
        super.setName("SOCK_HDLR_TRAP_LISTNR");
        
        System.out.println("[SOCK_HDLR_TRAP_LISTNR]: Ready...");
    }

    @Override
    public void Stop(){
        System.out.println("[SOCK_HDLR_TRAP_LISTNR]: Stopping...");
        // arrêt du Thread
        super.RUNNING = false;
    }
    
    @Override
    public void run(){
        System.out.println("[SOCK_HDLR_TRAP_LISTNR]: Started...");
        //
        
        while((socket.isClosed() == false) && RUNNING){ // boucle infinie -- socket connecté && RUN
            DatagramPacket data_packet = new DatagramPacket(new byte[256],256);
            try{
                // on écoute sur le socket
                socket.receive(data_packet);
                System.out.println("[SOCK_HDLR_TRAP_LISTNR]: Datagram Received");
                // On  place le paquet Datagram dans la file d'attente.
                DG_packet_queue_IS.add(data_packet);
                //
                System.out.println("[SOCK_HDLR_TRAP_LISTNR]: Datagram transmited to S_MSG_HDLR_IS");
                
            }catch(Exception e){
                System.err.println("[SOCK_HDLR_TRAP_LISTNR]: ERROR -- > "+e.getMessage() );
            }
        }
        
        // on a quitté la booucle while --> FIN du thread
        System.out.println("[SOCK_HDLR_TRAP_LISTNR]: Stopped");
    }
}
