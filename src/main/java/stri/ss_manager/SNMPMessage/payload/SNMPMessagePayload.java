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
    
    // méthodes
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
    private byte[] getRequestIdTLVFormat(){
        
        // VAR
        ByteBuffer temp_ByteBuffer = ByteBuffer.allocate(3);
        byte[] temp_data;
        //
        byte    requestIdType    = 0x02;
        byte    requestIdLght    = (byte) Integer.BYTES;
        byte[]  requestIdValue   = ByteBuffer.allocate(Integer.BYTES).putInt(this.requestId).array();
        //
        temp_ByteBuffer.put(requestIdType);         // T
        temp_ByteBuffer.put(requestIdLght);         // L
        temp_ByteBuffer.put(requestIdValue);        // V
        //
        temp_data = new byte[temp_ByteBuffer.position()];
        temp_ByteBuffer.get(temp_data);
        //
        return temp_data;
    }
    
    private byte[] getErrorStatusTLVFormat(){
        
         // VAR
        ByteBuffer temp_ByteBuffer = ByteBuffer.allocate(3);
        byte[] temp_data;
        //
        byte    errorStatusType  = 0x02;
        byte    errorStatusLght  = 0x01;
        byte    errorStatusValue = (byte) (0xFF & (byte) this.errorStatus);
        //
        temp_ByteBuffer.put(errorStatusType);       // T
        temp_ByteBuffer.put(errorStatusLght);       // L
        temp_ByteBuffer.put(errorStatusValue);      // V
        //
        temp_data = new byte[temp_ByteBuffer.position()];
        temp_ByteBuffer.get(temp_data);
        //
        return temp_data;
    }
    
    private byte[] getErrorIndexTLVFormat(){
         // VAR
        ByteBuffer temp_ByteBuffer = ByteBuffer.allocate(3);
        byte[] temp_data;
        //
        byte    errorIndexType   = 0x02;
        byte    errorIndexLght   = 0x01;
        byte    errorIndexValue  = (byte) (0xFF & (byte) this.errorIndex);
        //
        temp_ByteBuffer.put(errorIndexType);        // T
        temp_ByteBuffer.put(errorIndexLght);        // L
        temp_ByteBuffer.put(errorIndexValue);       // V
        //
        temp_data = new byte[temp_ByteBuffer.position()];
        temp_ByteBuffer.get(temp_data);
        //
        return temp_data;
    }
    
    private byte[] getVarBindListTLVFormat(){
        
        // VAR
        ByteBuffer temp_ByteBuffer = ByteBuffer.allocate(50);
        byte[] temp_data;
        int varBindListLght = 0;
        //
        temp_ByteBuffer.put((byte) 0x30);       // VarBindListType = SEQUENCE
        // enregistrement de la position pour L
        int varBindListLghtpos = temp_ByteBuffer.position();
        // décallage de 1 pour insérer les données
        temp_ByteBuffer.position(varBindListLghtpos + 1);
        //
        for(VarBind temp_vb : this.varBindingsList){
            varBindListLght += temp_vb.getTLVFormat().length;
            temp_ByteBuffer.put(temp_vb.getTLVFormat());   
        }
        // Calcule de la taille
        temp_ByteBuffer.put(varBindListLghtpos, (byte) varBindListLght);
        // return
        
        temp_data = new byte[temp_ByteBuffer.position()];
        temp_ByteBuffer.get(temp_data);
        //
        return temp_data;
    }
    
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
        //
        temp_data = new byte[temp_ByteBuffer.position()];
        temp_ByteBuffer.get(temp_data);
        //
        return temp_data;
    }
           
}
