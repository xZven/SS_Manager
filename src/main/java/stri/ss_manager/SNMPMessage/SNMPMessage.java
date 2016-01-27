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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import stri.ss_manager.SNMPMessage.payload.SNMPMessagePayload;
import stri.ss_manager.SNMPMessage.payload.SNMPTrapV1;
import stri.ss_manager.SNMPMessage.payload.SNMPTrapV2;

/**
 *
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * @version 1.0
 * 
 * Cette classe défini les messages SNMP échangés pour toute version. 
 * Elle servent entre les échanges
 * entre la couche SNMPMessageHandler et SNMPProtocolHandler.
 * 
 * <p>
 * Attention: lorsqu'un SNMPMessage est créé, il ne peut plus être modifié.
 * (Pas de setteur défini)
 * Aucun accesseur n'est créé
 * </p>
 * 
 */
public class SNMPMessage {
    
    // attributs                    // ces adresses sont extraite ou mis dans les PDU SNMP
    private InetAddress Sender;     // Adresse IPv4 de l'emmeteur du message SNMP
    private int         port;       // numéro de port de l'hôte distant
    private InetAddress Receiver;   // Adresse IPv4 du récepteur du messages SNMP
    
    
    private int         seqNumber;  // numéro de séquence du message SNMP
    private int         version;    // numéro de version SNMP
    private byte[]      communauty; // communauté
    
    private int         pduType;     // type de la PDU (e.g: Get, GetNext , Set, Getrespons, Trapv1, Trapv2)
   
    
    // Un message SNMP est soit une trap, soit un payload !
    // Une trap de SNMPv2 est dans de type payload
    // Seul le type de la pdu changera
    SNMPMessagePayload payload;     //
    SNMPTrapV1 trapV1;              //
    
    // Constructeurs
      
    public SNMPMessage(InetAddress Sender, InetAddress Receiver, int port, int version, byte[] communauty, SNMPMessagePayload payload){
        
        this.Sender     = Sender;
        this.Receiver   = Receiver;
        this.port       = port;
        
        this.version    = version;
        this.communauty = communauty;
        
        this.payload    = payload;
        this.trapV1      = null;   
    }    
    /**
     * Construit un SNMPMessage en lui passant un DatagramPacket
     * issu du réseau pour le protocol SNMP
     * 
     * @param temp_DGPacket DatagramPacket SNMP
     */
    public SNMPMessage(DatagramPacket temp_DGPacket){
        
        // VAR
        int         port   = 0;
        InetAddress Sender = null;
        InetAddress Receiver;
        
        ByteBuffer pduSNMP = null;
        
        int index;
        
        // CODE
        // On récupère les adresses IP sur le Datagramme et le numéro de port distant
        try {
            port     = temp_DGPacket.getPort();         // numéro de port distant
            Sender   = temp_DGPacket.getAddress();      // adresse IP de l'emetteur
            Receiver = InetAddress.getLocalHost();      // adresse IP du récepteur (Manager)

        } catch (UnknownHostException ex) {
            System.out.println("[MSG_HDLR_IS]: ERROR --> Impossible de récuppérer l'adresse local lors de la création d'un SNMPMessage reçu...");
            // SI ON PEUT PAS RCUPERER L'ADRESSE LOCAL
            Receiver = null;
        }

        // Extraction de la partie data du Datagramme --> PDU SNMP
        pduSNMP.wrap(temp_DGPacket.getData());
        //
 
    //  ************************************************************************************************ //
        // Extraction du type et de la taille de la PDU
           int mainType = pduSNMP.get();        // SEQUENCE
           int mainLght = pduSNMP.get();        // TAILLE EN OCTET DE TOUTE LA PDU
           
        // Extraction du numéro de version
           int    versionNumberType  = pduSNMP.get();
           int    versionNumberLght  = pduSNMP.get();
           byte[] versionNumberValue = new byte[versionNumberLght];
           pduSNMP.get(versionNumberValue, pduSNMP.position(), versionNumberLght);

        // Extraction de la communauté
           
           int    communautyType  = pduSNMP.get();  // OCTET STRING
           int    communautyLght  = pduSNMP.get();  // TAILLE en nombre d'octet de la communté
           byte[] communautyValue = new byte[communautyLght];
           pduSNMP.get(communautyValue, pduSNMP.position(), communautyLght);
           
        // attribution des variables à l'objet
           
           this.port       = port;
           this.Sender     = Sender;
           this.Receiver   = Receiver;
           
           for(index = 0; index < versionNumberValue.length; index++)
            {   // Somme des octets du tableau transtypé en int
            this.version += (int) versionNumberValue[index];
            }
           
           this.communauty = communautyValue;
           
        // Extraction de la PDUType
           
           int pduPayloadType  = pduSNMP.get();
           int pduPayloadLght  = pduSNMP.get();
           
           this.pduType        = pduPayloadType;
           
        // la transmission de la payload se fera donc à partir de ReqID
           byte[] pduPayloadByteArray = new byte[pduPayloadLght];
           pduSNMP.get(pduPayloadByteArray, pduSNMP.position(), pduPayloadLght);
        // On choisie le bon constructeur selon la PAYLOAD (Trap ou normal)
           switch(pduPayloadType){      // pduPayload TYPE
            case 0xA0:                  // GetReq;
                this.payload    = new SNMPMessagePayload(pduPayloadByteArray);
                this.trapV1     = null;
                break;
            case 0xA1:                  // GetNextReq;
                this.payload    = new SNMPMessagePayload(pduPayloadByteArray);
                this.trapV1     = null;
                break; 
            case 0xA2:                  // GetRes
                this.payload    = new SNMPMessagePayload(pduPayloadByteArray);
                this.trapV1     = null;
                break;
            case 0xA3:                  // SetReq
                this.payload    = new SNMPMessagePayload(pduPayloadByteArray);
                this.trapV1     = null;
                break;
            case 0xA4:                  // SNMPv1 Trap
                this.trapV1     = new SNMPTrapV1(pduPayloadByteArray);
                this.payload    = null;
                break;
            case 0xA5:                  // GetBulkReq
                // not handled
                this.payload    = null;
                this.trapV1     = null;
                break;
            case 0xA6:                  // InformReq
                // not handled
                this.payload    = null;
                this.trapV1     = null;
                break;
            case 0xA7:                  // SNMPv2 Trap
                this.trapV1     = null;
                this.payload    = (SNMPMessagePayload) new SNMPTrapV2(pduPayloadByteArray);
                break;
            case 0xA8:                  // Report
                // not handled
                this.payload    = null;
                this.trapV1     = null;
                break;
            default:
                // on détruit ce SNMP message.
                break;  
        }    
    //  ************************************************************************************************ //   
    }
}
