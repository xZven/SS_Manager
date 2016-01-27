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
    
    /*
        Get.req(0),
        Get-Next.req(1),
        Get.res(2),
        Set.req(3),
    */
    //
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
    /*
        Liste
    */
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
           
}
