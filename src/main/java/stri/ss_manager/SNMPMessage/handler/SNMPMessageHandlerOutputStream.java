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
import java.util.Queue;
import stri.ss_manager.SNMPMessage.SNMPMessage;

/**
 *
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * @version 1.0
 * @see SocketHandlerOutputStream
 * @see SNMPMessage
 *
 * Cette classe défini le Thread du gestionnaire des messages SNMP pour les flux
 * sortants. Elle fait parti de la couche SNMPMessageHandler.
 *
 * <p>
 * Lorsqu'une PDU SNMP est plancé dans la file d'attente (FIFO) par le
 * SNMPKernel, ce dernier est converti en DatagramPacket.
 * </p>
 *
 * <p>
 * Une fois que le message converti, il est placé dans la file d'attente
 * du SocketHandlerOutputStream pour être envoyé sur le socket.
 * </p>
 * 
 *  <p>
 * Dans un prochaine version, on introduira un fichier de log qui enregistreara tous les messages
 * SNMP envoyés.
 * </p>
 */
public class SNMPMessageHandlerOutputStream extends Thread {
    
    // attributs
    
    private Queue<DatagramPacket> DG_packet_queue_OS;     // File d'attente pour les DGPacket sortants
    private Queue<SNMPMessage>    S_MSG_queue_OS;         // File d'attente pour les SNMPMessages sortant (transmit par le noyau)
    private boolean               RUNNING = true;         // Variable utilisé pour arrêter le Thread
    
    
    // Constructeurs
    
    public SNMPMessageHandlerOutputStream(Queue<DatagramPacket> DG_packet_queue_OS, Queue<SNMPMessage> S_MSG_queue_OS) {
        // Attribution d'un nom au thread
        this.setName("S_MSG_HDLR_OS");
        // On construit la file d'attente entre SOCK_HDLR_IS et MSG_HDLR_IS
        this.DG_packet_queue_OS = DG_packet_queue_OS;
        // On lie la file d'attente  DU S_Proto_HDLR et MSG_HDLR_IS
        this.S_MSG_queue_OS     = S_MSG_queue_OS;
        //
        //
        System.out.println("[MSG_HDLR_OS]: Ready...");
        
    }
    
    // méthodes
    /**
     * Cette méthode permet d'arrêter le Thread en cours.
     */
    public void Stop(){
        System.out.println("[MSG_HDLR_OS]: Stopping...");
        this.RUNNING = false;
    }

    @Override
    public void run(){
        // VAR
        DatagramPacket  temp_DGPacket;
        SNMPMessage     temp_SNMPMessage;
        //
        System.out.println("[MSG_HDLR_IS]: Started...");

        while (RUNNING) {

            if (S_MSG_queue_OS.isEmpty()== false) { //FILE d'attente n'est pas vide
                // on extrait un DG_packet de la file d'attente
                temp_SNMPMessage    =  S_MSG_queue_OS.poll();
                // Conversion
                temp_DGPacket = temp_SNMPMessage.getDGPacketPdu();
                // affichage
                System.out.println("[MSG_HDLR_OS]: SENDING MSG --> " + temp_SNMPMessage.toString());
                // on le transmet à la file d'attente
                DG_packet_queue_OS.add(temp_DGPacket);
                // on endors le thread pour 10ms
                //
                try {
                    sleep(10); // 10 ms
                } catch (Exception e) {
                    System.err.println("[MSG_HDLR_OS]: ERROR --> " + e.getMessage());
                }
                //
            } else { // SI la file d'attent est vide, on endors le thread pour 100ms
                try {
                    sleep(100); // 100 ms
                } catch (Exception e) {
                    System.err.println("[MSG_HDLR_OS]: ERROR --> " + e.getMessage());
                }
            }
        }
        // on a quitté la booucle while --> FIN du thread
        System.out.println("[MSG_HDLR_OS]: Stopped");
    }
}
