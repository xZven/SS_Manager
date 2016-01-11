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
 * @see SNMPMessageHandlerOutputStream
 * 
 * Cette classe permet de gérer les PDU SNMP sortant sur le socket UDP.
 * 
 * <p>
 * Ce Thread interroge la file d'attente des PDU sortant (de type FIFO)
 * et envoi les PDU sur le socket UDP.
 * </p>
 */
public class SocketHandlerOutputStream extends Thread{
    
     // attributs
    
    private DatagramSocket socket;                          // Socket UDP
    private Queue<DatagramPacket> DG_packet_queue_OS;       // File d'attente pour les PDU SNMP pour le flux sortant;
    
    // méthodes
    
    /**
     * 
     * @param socket Socket UDP où les Datagrammes seront envoyés
     * @param DG_packet_queue_OS    File d'attente des Datagrammes qui devront être envoyés sur le socket UDP.
     */
    public SocketHandlerOutputStream(DatagramSocket socket, Queue<DatagramPacket> DG_packet_queue_OS) {
        //
        this.socket = socket;
        //
        this.DG_packet_queue_OS = DG_packet_queue_OS;
        //
        System.out.println("[SOCK_HDLR_OS]: Ready...");
    }
    
    @Override
    public void run(){
    
        while(socket.isConnected()){
// ****************************************************************************************** //              
            if(DG_packet_queue_OS.isEmpty() == false){ // Si la file n'est pas video
                try {
                    System.out.println("[SOCK_HDLR_OS]: Sending ...");
                    // envoi des PDU snmp sur le socket UDP
                    socket.send(DG_packet_queue_OS.poll());
                    //
                    System.out.println("[SOCK_HDLR_OS]: Sent ...");
                } catch (Exception e) {
                    // affichage du message d'erreur
                    System.err.println("SOCK_HDLR_OS]: ERROR --> "+e.getMessage());
                    
                    if(socket.isClosed()) // Si le socket est fermé
                        break; // on quitte la boucle
                }
            }
// ****************************************************************************************** //
            else{ // pas de PDU à envoyer
                try {
                    // on endort le thread
                    sleep(100); // 100ms
                } catch (Exception e) {
                    // affichage du message d'erreur
                    System.err.println("SOCK_HDLR_OS]: ERROR --> "+e.getMessage());
                }
            }
// ****************************************************************************************** //            
        }    
    }
  
}
