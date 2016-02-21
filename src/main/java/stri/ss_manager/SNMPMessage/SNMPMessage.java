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
import stri.ss_manager.SNMP.smi.VarBind;
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
 * Un nouveau SNMPMessage devra être créé.
 * </p>
 * 
 */
public class SNMPMessage {
    
    // attributs                    // ces adresses sont extraite ou mis dans les PDU SNMP
    private InetAddress Sender;     // Adresse IPv4 de l'emmeteur du message SNMP
    private int         port;       // numéro de port de l'hôte distant
    private InetAddress Receiver;   // Adresse IPv4 du récepteur du messages SNMP
    //
    private int         version;    // numéro de version SNMP
    private byte[]      communauty; // communauté
    private int         pduType;    // type de la PDU (e.g: Get, GetNext , Set, Getrespons, Trapv1, Trapv2)
   
    
    // Un message SNMP est soit une trap, soit un payload !
    // Une trap de SNMPv2 est de type payload, on pourra donc faire un transtypage.
    SNMPMessagePayload payload;     //
    SNMPTrapV1 trapV1;              //
    
    // Constructeurs
    
    /**
     * Cette fonction permet de construire un message SNMP en lui passant
     * les paramètres suivants.
     * 
     * @param Sender Adresse IP de celui qui envoi le message SNMP
     * @param Receiver Adresse IP de celui qui reçoit le message SNMP
     * @param port Numéro de port distant sur lequel le message SNMP est reçu ou envoyé.
     * @param version Numéro de version.
     * @param communauty Nom de la communauté.
     * @param pduType Type de la PDU (e.g: Get(0xA0), GetNext (0xA1) , Set (0xA3), GetResponse (0xA2) , Trapv1 (0xA4), Trapv2 (0xA7))
     * @param payload Payload du message SNMP
     */
    public SNMPMessage(InetAddress Sender, InetAddress Receiver, int port, int version, byte[] communauty, byte pduType, SNMPMessagePayload payload){
        
        this.Sender     = Sender;
        this.Receiver   = Receiver;
        this.port       = port;
        
        this.version    = version - 1;
        this.communauty = communauty;
        this.pduType    = pduType;
        
        this.payload    = payload;
        this.trapV1      = null;   
    }    
   
    /**
     * Construit un SNMPMessage en lui passant un DatagramPacket
     * issu d'une PDU SNMP. Cette fonction est utilisé
     * par le SocketHandlerInputStream
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
        pduSNMP =  ByteBuffer.wrap(temp_DGPacket.getData());
        //
 
    //  ************************************************************************************************ //
        // Extraction du type et de la taille de la PDU
           int mainType = pduSNMP.get();        // SEQUENCE
           int mainLght = pduSNMP.get();        // TAILLE EN OCTET DE TOUTE LA PDU
           
        // Extraction du numéro de version
           int    versionNumberType  = pduSNMP.get();
           int    versionNumberLght  = pduSNMP.get();
           byte[] versionNumberValue = new byte[versionNumberLght];
           pduSNMP.get(versionNumberValue, 0, versionNumberLght);
          
        // Extraction de la communauté
           
           int    communautyType  = pduSNMP.get();  // OCTET STRING
           int    communautyLght  = pduSNMP.get();  // TAILLE en nombre d'octet de la communté
           byte[] communautyValue = new byte[communautyLght];
           pduSNMP.get(communautyValue, 0, communautyLght);
           
        // attribution des variables à l'objet
           
           this.port       = port;
           this.Sender     = Sender;
           this.Receiver   = Receiver;
           
           this.version = 0;
           // a vérifier
           for(index = 0; index < versionNumberValue.length; index++)
            {   // Somme des octets du tableau transtypé en int
                this.version = this.version * 256 + (int) versionNumberValue[index];
            }
           
           this.communauty = communautyValue;
           
        // Extraction de la PDUType
           
           int pduPayloadType  = pduSNMP.get() & 0xFF;
           int pduPayloadLght  = pduSNMP.get();
           
           this.pduType        = pduPayloadType;
           
        // la transmission de la payload se fera donc à partir de ReqID
           byte[] pduPayloadByteArray = new byte[pduPayloadLght];
           pduSNMP.get(pduPayloadByteArray, 0, pduPayloadLght);
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
    
    @Override
    public String toString(){
        
        if(this.payload == null){ //   trapv1
            return  
                "[SENDER]  "       +   this.Sender                 +
                " [RECEIVER]  "    +   this.Receiver               +
                " [PORT]  "        +   this.port                   +
                " [VERSION]  "     +   this.version                +
                " [COMMUNAUTY]  "  +   new String(this.communauty) +
                " [PDU_TYPE]  "    +   this.pduType                +
                " {PAYLOAD}  "     +   this.trapV1.toString();  
        }else{                    // payload != trapv1
            return  
                "[SENDER]  "       +   this.Sender                 +
                " [RECEIVER]  "    +   this.Receiver               +
                " [PORT]  "        +   this.port                   +
                " [VERSION]  "     +   this.version                +
                " [COMMUNAUTY]  "  +   new String(this.communauty) +
                " [PDU_TYPE]  "    +   this.pduType                +
                " {PAYLOAD}  "     +   this.payload.toString();  
        }
        
        
    }
    
    /**
     * Cette fonction permet d'obtenir un DatagramPacket à partir d'un SNMPMessage
     * formant une PDU SNMP au format BER.
     * @return Le DatagramPacket, ou null si le SNMPMessage est incomplet
     */
    public DatagramPacket getDGPacketPdu() {
        
        // VAR
        
        DatagramPacket temp_DGPacket = null;
        //
        byte    SNMPMessageType = 0x30;                           // SEQUENCE
        byte    SNMPMessageLght = 0x00;
        
        // Extraction du numéro de version
        byte    versionNumberType  = 0x02;                        // INTERGER32
        byte    versionNumberLght  = 0x01;
        byte    versionNumberValue = (byte) (0xFF & (byte) this.version);

          
        // Extraction de la communauté
        byte    communautyType  = 0x04;                           // OCTET STRING
        byte    communautyLght  = (byte) this.communauty.length;  // TAILLE en nombre d'octet de la communté
        byte[]  communautyValue =  this.communauty;
        
        // 
        byte[]  pduPayloadValue;
        byte    pduPayLoadLght;
        byte    pduPayLoadType;
        // Extraction PayLoad
        if(this.pduType == 0xA4){                                // Si TRAPv1;
            pduPayloadValue = this.trapV1.getPduFormat();
            pduPayLoadLght  = (byte) pduPayloadValue.length;
            pduPayLoadType  = (byte) this.pduType;             
        }else{                                                  // AUTRE TYPE
            pduPayloadValue = this.payload.getPduFormat();
            pduPayLoadLght  = (byte) pduPayloadValue.length;
            pduPayLoadType  = (byte) this.pduType; 
        }
        // Constitution du DGPacket
        // Calcul de la taille total de la pdu
        //
        ByteBuffer temp_ByteBuffer = ByteBuffer.allocate(256);
           
        //
        temp_ByteBuffer.put(SNMPMessageType);                   //        |  T
        int SNMPMessageLghtPos = temp_ByteBuffer.position();    //        |  L pos
        temp_ByteBuffer.position(SNMPMessageLghtPos + 1);       // décallage

        //
        temp_ByteBuffer.put(versionNumberType);                 // T      |
        temp_ByteBuffer.put(versionNumberLght);                 // L      |
        temp_ByteBuffer.put(versionNumberValue);                // V      |
        
        //
        temp_ByteBuffer.put(communautyType);                    // T      |
        temp_ByteBuffer.put(communautyLght);                    // L      | V
        temp_ByteBuffer.put(communautyValue);                   // V      |
        
        //
        temp_ByteBuffer.put(pduPayLoadType);                    // T      |
        temp_ByteBuffer.put(pduPayLoadLght);                    // L      |
        temp_ByteBuffer.put(pduPayloadValue);                   // V      |
        // Calcul de la taille total de message SNMP
            SNMPMessageLght = (byte) (6 + versionNumberLght + communautyLght + pduPayLoadLght);
            temp_ByteBuffer.put(SNMPMessageLghtPos, SNMPMessageLght);
        //
        //
        byte[] DgData = new byte[temp_ByteBuffer.position()]; 
        temp_ByteBuffer.position(0);
        temp_ByteBuffer.get(DgData, 0,DgData.length);
        //
        temp_DGPacket = new DatagramPacket(DgData, DgData.length);
        temp_DGPacket.setPort(this.port);                       // Port distant où le paquet sera envoyé
        temp_DGPacket.setAddress(this.Receiver);                // Adresse distant où le paquet sera envoyé
        //
        return temp_DGPacket;
    }
    
    // Getters
    
    /**
     * Retourne l'adresse IP de celui envoi
     * le message SNMP
     * 
     * @return Adresse IP de l'emmteur ou null si le message émane de cette machine.
     */
    public InetAddress getSender() {
        return Sender;
    }
    
    /** 
     * Retourne l'adresse IP de celui qui reçoit
     * le message SNMP.
     * 
     * @return Adresse IP du récepteur obligatoirement
     */
    public InetAddress getReceiver() {
        return Receiver;
    }

    /**
     * Retour le numéro de port distant où le message SNMP
     * est reçu ou envoyé.
     * 
     * @return Numéro de port
     */
    public int getPort() {
        return port;
    }
    
    /**
     * Numéro de version du protocole utilisé par le message SNMP
     * 
     * Version 1
     * Version 2c
     * 
     * @return numéro de version + 1
     */
    public int getVersion() {
        return version + 1;
    }
    
    /**
     * Retour la communauté sous forme d'un tableau d'octets
     * 
     * @return Communauté du message SNMP
     */
    public byte[] getCommunauty() {
        return communauty;
    }
    
    /**
     * Retourne le type du message SNMP.
     * 
     * <p>
     * (e.g: Get(0xA0), GetNext (0xA1) , Set (0xA3), GetResponse (0xA2) , Trapv1 (0xA4), Trapv2 (0xA7))
     * </p>
     * 
     * @return le type du message SNMP
     */
    public int getPduType() {
        return pduType;
    }
    
    /** 
     * Retourne la charge utile du message SNMP
     * 
     * @return Payload de la PDU
     */
    public SNMPMessagePayload getPayload() {
        return payload;
    }
    
    /** 
     * Retourne la charge utile de la PDU SNMP
     * s'il s'agit d'un TRAP SNMP.
     * 
     * @return Trap
     */
    public SNMPTrapV1 getTrapV1() {
        return trapV1;
    }
    
    //Setter
    
    /*
     * Aucun setters n'est défini car un message SNMP n'est utilisable qu'une seule fois.
     * Pour répondre, un nouveau message SNMP doit être créé.
     */
    
    public String getPduTypeString(){

            switch(this.pduType){
               case 0xA0:               // GetReq;
                return "GetReq";
            case 0xA1:                  // GetNextReq;
                return "GetNextReq"; 
            case 0xA2:                  // GetRes
                return "GetRes";
            case 0xA3:                  // SetReq
                return "SetReq";
            case 0xA4:                  // SNMPv1 Trap
                return "SNMPv1 Trap";
            case 0xA5:                  // GetBulkReq
                // not handled
                return "GetBulkReq";
            case 0xA6:                  // InformReq
                // not handled
                return "InformReq";
            case 0xA7:                  // SNMPv2 Trap
                return "SNMPv2 Trap";
            case 0xA8:                  // Report
                // not handled
                return "Report";
            default:
                return "UNKNOWN PDU";  
            }
    }
    public String toStringforIHM() {
        
        // AFFICHAGE
        //
        // FROM 127.0.0.1 
        //      COMMUNAUTY: "public"
        //      VERSION: 1
        //      PDU TYPE: GetReq\n
        // PAYLOAD:
      
        if(this.payload == null){ // payload == trapv1
           return "";            
        }else{                    // payload != trapv1
            String payload;
            String varBind = "";
            //
            for(VarBind vb : this.payload.getVarBindingsList()) {
                varBind += vb.toString() +" ";
            }
            //
            payload = "\tRequestID: "      +this.payload.getRequestId()     + "\n"+
                      "\tErrorStatus: "    +this.payload.getErrorStatus()   + "\n"+
                      "\tErrorIndex: "     +this.payload.getErrorIndex()    + "\n"+
                      "\n:"         +varBind                         + "\n";
            return  
                "FROM "               +   this.Sender                 + "\n"+
                " \tCOMMUNAUTY:  "    +   new String(this.communauty) + "\n"+
                " \tVERSION:  "       +   this.version  + 1           + "\n"+
                " \tPDU_TYPE "        +   this.getPduTypeString()     + "\n"+            
                "PAYLOAD\n"+payload;
        }
    }
}
