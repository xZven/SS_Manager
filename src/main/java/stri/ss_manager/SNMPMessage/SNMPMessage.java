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
package stri.ss_manager.SNMPMessage;

import java.net.DatagramPacket;
import java.net.Inet4Address;
import stri.ss_manager.SNMPMessage.payload.SNMPMessagePayload;
import stri.ss_manager.SNMPMessage.payload.SNMPTrap;

/**
 *
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * @version 1.0
 * 
 * Cette classe défini les messages SNMP échangés. Elle servent entre les échanges
 * entre la couche SNMPMessageHandler et SNMPProtocolHandler.
 * 
 * <p>
 * Attention: lorsqu'un SNMPMessage est créé, il ne peut plus être modifié.
 * (Pas de setteur défini)
 * </p>
 * 
 */
public class SNMPMessage {
    
    // attributs
    private Inet4Address Sender;
    private Inet4Address Receiver;
    
    private final int version;          // numéro de version
    private final byte[] communauty;    // communauté
    
    // Un message SNMP est soit une trap, soit un payload !
    // PAS LES DEUX A LA FOIS !
    SNMPMessagePayload payload; //
    SNMPTrap trap;              //
    
    // Constructeurs
    
    /* 
        On utilisera trois méthodes différents pour créer un SNMPMessage:
        Une première en passant le DatagramPacket liée à la PDU SNMP.
        Une seconde en défissant les valeurs des attributs pour un message
        SNMP normal.
        Une troisième méthodes en définissant les valeurs des attributs pour
        un trap SNMP.
    */
    
    public SNMPMessage(DatagramPacket DG_packet){
        this.version = 0;
        this.communauty = new byte[Byte.decode("none")];
    }
    
}
