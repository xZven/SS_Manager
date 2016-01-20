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
package stri.ss_manager.SNMPMessage.handler;

import java.net.DatagramPacket;
import java.util.LinkedList;
import java.util.Queue;
import stri.ss_manager.SNMPMessage.SNMPMessage;

/**
 *
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * @version 1.0
 * @see SocketHandlerInputStream
 * @see SNMPMessage
 * 
 * Cette classe défini le Thread du gestionnaire des messages SNMP pour les flux entrants.
 * Elle fait parti de la couche SNMPMessageHandler.
 * 
 * <p>
 * Lorsqu'une PDU SNMP est plancé dans la file d'attente (FIFO) par le SocketHandlerInputSTream,
 * Ce dernier est décortiqué pour être "converti" en SNMPMessage par cette classe.
 * </p>
 * 
 * <p>
 * Une fois que le message est converti, elle est placé dans la file d'attente du SNMPProtocolHandler
 * pour y être traité.
 * </p>
 */
public class SNMPMessageHandlerInputStream extends Thread {
    
    // attributs

    private Queue<DatagramPacket>   DG_packet_queue_IS;     // File d'attente pour les DGPacket entrants
    private Queue<SNMPMessage>      S_MSG_queue_IS;         // File d'attente pour les SNMPMessages entrants (transmit au noyau)
    private boolean                 RUNNING = true;         // Variable utilisé pour arrêter le Thread
    
    // méthodes
    
    /**
     * Ce constructeur sert à initialiser le SNMPMessageHandler pour les flux entrants
     * @param S_MSG_queue_IS - FIle d'attente pour les message SNMP entrants
     */
    public SNMPMessageHandlerInputStream(Queue<SNMPMessage> S_MSG_queue_IS) {
        // On construit la file d'attente entre SOCK_HDLR_IS et MSG_HDLR_IS
        this.DG_packet_queue_IS = new LinkedList<>();
        // On lie la file d'attente  DU S_Proto_HDLR et MSG_HDLR_IS
        this.S_MSG_queue_IS = S_MSG_queue_IS;
        //
        System.out.println("[MSG_HDLR_IS]: Ready...");
    }
    
    /**
     * Permet d'obtenir la file d'attente pour les DatagramPacket.
     * Cette fonction sera utiliser lors de l'apelle du constructeur
     * du SocketHandlerInputStream.
     * 
     * @return La file d'attente entre le SOCK_HDLR_IS et MSG_HDLR_IS.
     */
    public Queue<DatagramPacket> getDG_packet_queue_IS() {
        return DG_packet_queue_IS;
    }
    /**
     * Cette fonction est utilisé pour arrêter le Thd
     */
    public void Stop(){
         System.out.println("[MSG_HDLR_IS]: Stopping...");
         this.RUNNING = false;
    }
    @Override
    public void run(){
        // VAR
        DatagramPacket temp_DGPacket;
        SNMPMessage    temp_SNMPMessage = null;
        //
        System.out.println("[MSG_HDLR_IS]: Started...");
         
        while(RUNNING){

            if(DG_packet_queue_IS.isEmpty() == false){ //FILE d'attente n'est pas vide
                // on extrait un DG_packet de la file d'attente
                temp_DGPacket = DG_packet_queue_IS.poll();
                // CONVERSION en SNMPMessage
//  ************************************************************************************************ //
                
//  ************************************************************************************************ //
                // on le transmet à la file d'attente
                S_MSG_queue_IS.add(temp_SNMPMessage);
                
                // on endors maintenant le thread pour 10ms
                    try {
                       sleep(10); // 100 ms
                } catch (Exception e) {
                       System.err.println("[MSG_HDLR_IS]: ERROR --> "+e.getMessage());
                }
                //
            }else{ // SI la file d'attent est vide, on endors le thread pour 100ms
                   try {
                       sleep(100); // 100 ms
                } catch (Exception e) {
                       System.err.println("[MSG_HDLR_IS]: ERROR --> "+e.getMessage());
                }
            }
        }
        
        // on a quitté la booucle while --> FIN du thread
        System.out.println("[MSG_HDLR_IS]: Stopped");
    }
    
}
