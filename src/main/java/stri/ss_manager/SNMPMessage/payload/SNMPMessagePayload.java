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
package stri.ss_manager.SNMPMessage.payload;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import stri.ss_manager.SNMP.smi.VarBind;

/**
 *
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * @version 1.0
 * 
 * Cette classe défini les informations utiles contenu dans les PDU SNMP
 * de type différent que les traps SNMP.
 * 
 * <p>
 * Attention: Une fois créé le contenu ne peut plus être modifié.
 * (Pas de setter défini).
 * </p>
 */
public class SNMPMessagePayload {
    
    // attributs
    private int requestId;
    /*
        noError(0),
        tooBig(1),
        noSuchName(2),
        badValue(3),
        readOnly(4),
        genErr(5),

        Toujours à 0 dans une requête !
        Positionné par l'agent SNMP
    */
    private int errorStatus;
    /*
        Indique la variable qui a provoqué l'erreur
        dans le cas de "noSuchName(2), badValue(3) et
        readOnly(4).
    */
    private int errorIndex;
    private ArrayList<VarBind> varBindingsList;
 
    // constructeurs
    
    /**
     * Ce constructeur permet de construire un payload à partir
     * d'une suite d'octet ici des PDU au format TLV.
     * 
     * @param pduPayload Payload à extraire
     */
    public SNMPMessagePayload(byte[] pduPayload) {
       
        ByteBuffer payloadSNMP = ByteBuffer.wrap(pduPayload);
        
        int index;
        
        // Extraction RequestId
        int    requestIdType = payloadSNMP.get();
        int    requestIdLght = payloadSNMP.get();
        byte[] requesIdValue = new byte[requestIdLght];
        payloadSNMP.get(requesIdValue, 0, requestIdLght);
        
        for(index = 0; index < requesIdValue.length; index++)
        {   // Somme des octets du tableau transtypé en int
            this.requestId = (this.requestId * 256 + (int) (requesIdValue[index] & 0xFF));
        }
        // Extraction ErrorStatus
        int    errorStatusType  = payloadSNMP.get();
        int    errorStatusLght  = payloadSNMP.get();
        byte[] errorStatusValue = new byte[errorStatusLght];
        payloadSNMP.get(errorStatusValue, 0, errorStatusLght);  
        
        for(index = 0; index < errorStatusValue.length; index++)
        {   // Somme des octets du tableau transtypé en int
            this.errorStatus = this.errorStatus * 256 + (int) (errorStatusValue[index] & 0xFF);
        }
        // Extraction ErrorIndex
        
        int    errorIndexType  = payloadSNMP.get();
        int    errorIndexLght  = payloadSNMP.get();
        byte[] errorIndexValue = new byte[ errorIndexLght];
        payloadSNMP.get(errorIndexValue, 0, errorIndexLght); 
        
        for(index = 0; index < errorIndexValue.length; index++)
        {
            // Somme des octets du tableau transtypé en int
            this.errorIndex = this.errorIndex * 256 + (int) (errorIndexValue[index] & 0xFF);
            
        }
        
        // Extraction de la liste des variables
        
        int varBindListType = payloadSNMP.get();        // type séquence
        int varBindListLght = payloadSNMP.get();        //
                

        int varBindType ;                               
        int varBindLght ;
        byte[] varBindValue;
        
        this.varBindingsList = new ArrayList<>(5);
        // TANT QU'ON a pas atteint la fin
        
        while(payloadSNMP.position() < payloadSNMP.capacity()){ 
            //
            varBindType = payloadSNMP.get();            // type ObjectIdentifier
            varBindLght = payloadSNMP.get();
            //
            varBindValue = new byte[varBindLght];
            payloadSNMP.get(varBindValue, 0, varBindLght);
            //
            this.varBindingsList.add(new VarBind(varBindValue));                       
        }
    } 

    /**
     *  Ce constructeur permet de créer une payload on y insérant directement
     *  les valeurs.
     * 
     * @param requestId Numéro d'identification de la requête.
     * <p>
     *  Pour une requête ponctuel, cette valeur doit être de la forme 0xFFXXXXXXXX
     *  avec X une valeur décimal quelconque( Valide que pour la version 1).
     *  Cette valeur est défini sur 4 octets dans une PDU SNMP.
     * </p>
     * @param errorStatus Status de l'erreur 
     * <p> noError(0), tooBig(1), noSuchName(2), badValue(3), readOnly(4), genErr(5)</p>
     * @param errorIndex Variable qui a provoqué l'erreur dans les cas suivants:
     * <p>noSuchName(2), badValue(3) et readOnly(4)</p>
     * @param varBindingsList Liste de Varbind
     */
    public SNMPMessagePayload(int requestId, int errorStatus, int errorIndex, ArrayList<VarBind> varBindingsList) {
        this.requestId       = requestId;
        this.errorStatus     = errorStatus;
        this.errorIndex      = errorIndex;
        this.varBindingsList = varBindingsList;
    }
    
    
    // méthodes
    /**
     * Cette fonction renvoi un chaine de caractère représentant
     * la charge utile d'un message SNMP
     * @return Chaine de caractère
     */
    @Override
    public String toString(){
        String VB_String = "";
        
        for(VarBind VB : this.varBindingsList)
        {
             VB_String = VB_String +  VB.toString();
        }
        return
                "[REQUES_ID]  "       +  this.requestId       +
                "[ERROR_STATUS]  "    +  this.errorStatus     +
                "[ERROR_INDEX]  "     +  this.errorIndex      +
                "{VAR_BIND]}  "       +  VB_String;
    }
    //
    /** 
     * 
     * @return tableau d'octet du numéro de requête au format TLV
     */
    private byte[] getRequestIdTLVFormat(){
        
        // VAR
        ByteBuffer temp_ByteBuffer;
        byte[] temp_data = new byte[2+Integer.BYTES]; 
        temp_ByteBuffer = ByteBuffer.wrap(temp_data);
        //
        byte    requestIdType    = 0x02;
        byte    requestIdLght    = (byte) Integer.BYTES;
        byte[]  requestIdValue   = ByteBuffer.allocate(Integer.BYTES).putInt(this.requestId).array();
        //
        temp_ByteBuffer.put(requestIdType);         // T
        temp_ByteBuffer.put(requestIdLght);         // L
        temp_ByteBuffer.put(requestIdValue);        // V
        //
        return temp_data;
    }
    
    /**
     * 
     * @return tableau d'octet l'errorStatus au format TLV
     */
    private byte[] getErrorStatusTLVFormat(){
        
         // VAR
        ByteBuffer temp_ByteBuffer;
        byte[] temp_data = new byte[3]; 
        temp_ByteBuffer = ByteBuffer.wrap(temp_data);
        //
        byte    errorStatusType  = 0x02;
        byte    errorStatusLght  = 0x01;
        byte    errorStatusValue = (byte) (0xFF & (byte) this.errorStatus);
        //
        temp_ByteBuffer.put(errorStatusType);       // T
        temp_ByteBuffer.put(errorStatusLght);       // L
        temp_ByteBuffer.put(errorStatusValue);      // V
        //
        return temp_data;
    }
    
    /**
     * 
     * @return tableau d'octet du errorIndex au format TLV
     */
    private byte[] getErrorIndexTLVFormat(){
         // VAR
        ByteBuffer temp_ByteBuffer;
        byte[] temp_data = new byte[3]; 
        temp_ByteBuffer = ByteBuffer.wrap(temp_data);
        //
        byte    errorIndexType   = 0x02;
        byte    errorIndexLght   = 0x01;
        byte    errorIndexValue  = (byte) (0xFF & (byte) this.errorIndex);
        //
        temp_ByteBuffer.put(errorIndexType);        // T
        temp_ByteBuffer.put(errorIndexLght);        // L
        temp_ByteBuffer.put(errorIndexValue);       // V
        //
        return temp_data;
    }
    
    /**
     * 
     * @return tableau d'octet des Varaibles Bindings au format TLV
     */
    private byte[] getVarBindListTLVFormat(){
        
        // VAR
        ByteBuffer temp_ByteBuffer = ByteBuffer.allocate(100);
        byte[] temp_data;
        int varBindListLght = 0;
        //
        temp_ByteBuffer.put((byte) 0x30);       // VarBindListType = SEQUENCE
        // enregistrement de la position pour L
        int varBindListLghtpos = temp_ByteBuffer.position();
        // décallage de 1 pour insérer les données
        temp_ByteBuffer.position(varBindListLghtpos + 1);
        // Pour Chaque varBind dans la liste
        for(VarBind temp_vb : this.varBindingsList){
            varBindListLght += temp_vb.getTLVFormat().length;
            temp_ByteBuffer.put(temp_vb.getTLVFormat());   
        }
        temp_data = new byte[temp_ByteBuffer.position()];
        // Calcule de la taille
        temp_ByteBuffer.put(varBindListLghtpos, (byte) varBindListLght);
        // return
        temp_ByteBuffer.position(0);
        temp_ByteBuffer.get(temp_data, 0,temp_data.length);
        //
        return temp_data;
    }
    
    /**
     * 
     * @return tableau d'octet du type de la pdu au format TLV
     */
    public byte[] getPduFormat() {
        // VAR
        ByteBuffer temp_ByteBuffer = ByteBuffer.allocate(256);
        byte[] temp_data;
        // Les données ci-dessous suit le PDU TYPE défini dans le SNMPMessage
        
        // Extraction RequestId
           temp_ByteBuffer.put(this.getRequestIdTLVFormat());
        // Extraction ErrorStatus
           temp_ByteBuffer.put(this.getErrorStatusTLVFormat());
        // Extraction ErrorIndex
           temp_ByteBuffer.put(this.getErrorIndexTLVFormat());
        // Extraction du VarBindList
           temp_ByteBuffer.put(this.getVarBindListTLVFormat());        
        // Assemblage
           temp_data = new byte[temp_ByteBuffer.position()];
           temp_ByteBuffer.position(0);
           temp_ByteBuffer.get(temp_data, 0, temp_data.length);
        //
        return temp_data;
    }
     
    
    // getters

    /**
     * 
     * @return Le numéro du message SNMP
     */
    public int getRequestId() {
        return requestId;
    }
    
    /**
     * 
     * @return l'erreur status
     */
    public int getErrorStatus() {
        return errorStatus;
    }

    
    /**
     * 
     * @return l'erreur index
     */
    public int getErrorIndex() {
        return errorIndex;
    }
    
    /**
     * 
     * @return arraylist de Variable Binding
     */
    public ArrayList<VarBind> getVarBindingsList() {
        return varBindingsList;
    }
    
    // setters
    
    /**
     * noError(0), tooBig(1), noSuchName(2), badValue(3), readOnly(4), genErr(5)
     * 
     * @param errorStatus 
     */
    public void setErrorStatus(int errorStatus) {
        this.errorStatus = errorStatus;
    }

    /**
     * Variable qui a provoqué l'erreur dans les cas suivants:
     * noSuchName(2), badValue(3) et readOnly(4)
     * 
     * @param errorIndex 
     */
    public void setErrorIndex(int errorIndex) {
        this.errorIndex = errorIndex;
    }

    public String toStringforIHM() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
