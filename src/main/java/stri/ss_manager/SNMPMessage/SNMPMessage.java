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

import java.net.InetAddress;
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
    
    // attributs                    // ces adresses sont extraite ou mis dans les PDU SNMP
    private InetAddress Sender;    // Adresse IPv4 de l'emmeteur du message SNMP
    private int          port;      // numéro de port de l'hôte distant
    private InetAddress Receiver;  // Adresse IPv4 du récepteur du messages SNMP
    
    
    private int seqNumber;          // num"ro de séquence du message SNMP
    private int version;            // numéro de version SNMP
    private byte[] communauty;      // communauté
   
    
    // Un message SNMP est soit une trap, soit un payload !
    // PAS LES DEUX A LA FOIS !
    SNMPMessagePayload payload;     //
    SNMPTrap trap;                  //
    
    // Constructeurs
    
    /* 
        Une seconde en défissant les valeurs des attributs pour un message
        SNMP normal.
        Une troisième méthodes en définissant les valeurs des attributs pour
        un trap SNMP.
    */
    
    public SNMPMessage(InetAddress Sender, InetAddress Receiver, int port, int version, byte[] communauty, byte[] payload){
        this.Sender     = Sender;
        this.Receiver   = Receiver;
        this.port       = port;
        this.version    = version;
        this.communauty = communauty;
        
    //  ************************************************************************************************ //
      
        
    //  ************************************************************************************************ //     
    }
}
