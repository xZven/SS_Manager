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

import java.io.Serializable;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.security.Timestamp;
import java.util.Vector;
import stri.ss_manager.SNMP.smi.VarBind;

/**
 *
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * @version 1.0
 * 
 * Cette classe défini l'information utile dans les PDU SNMP qui sont
 * de type TRAP.
 */
public class SNMPTrapV1 implements Serializable{
    
    
    // attributs

    private byte[]      enterprise;             // SNMP SysObjectID
    private InetAddress agentAdress;            // Agent adresse
    private int         genTrapType;            //
    /*
        coldStart(0),
        warmStart(1),
        linkDown(2),
        linkUp(3),
        authenticationFailure(4),
        egpNeighborLoss(5),
        enterpriseSpecific(6)
    */
    private int             specTrapType;       //
    private Timestamp       timeStamp;          //
    private Vector<VarBind> varBindingsList;    //
    
    //private Vector<OID> varBindings;
    
    // constructeurs

    public SNMPTrapV1(byte[] pduPayload) {
        
        ByteBuffer payloadSNMP = null;
        payloadSNMP.wrap(pduPayload);
        
        int index;
        
        // Extraction enterprise
        int    enterpriseType  = payloadSNMP.get();
        int    enterpriseLght  = payloadSNMP.get();
        byte[] enterpriseValue = new byte[enterpriseLght];
        payloadSNMP.get(enterpriseValue, payloadSNMP.position(), enterpriseLght);
        
        // Extraction de l'adresse de l'agent
        
        int    agentAdressType  = payloadSNMP.get();
        int    agentAdressLght  = payloadSNMP.get();
        byte[] agentAdressValue = new byte[agentAdressLght];
        payloadSNMP.get(agentAdressValue, payloadSNMP.position(), agentAdressLght);
        
        
        // Extraction genTrapType
        
        int    specTrapTypeType  = payloadSNMP.get();
        int    specTrapTypeLght  = payloadSNMP.get();
        byte[] specTrapTypeValue = new byte[specTrapTypeLght];
        payloadSNMP.get(specTrapTypeValue, payloadSNMP.position(), specTrapTypeLght);

        
        
        // Extraction specTrapType
        int    genTrapTypeType  = payloadSNMP.get();
        int    genTrapTypeLght  = payloadSNMP.get();
        byte[] genTrapTypeValue = new byte[genTrapTypeLght];
        payloadSNMP.get(genTrapTypeValue, payloadSNMP.position(), genTrapTypeLght);
        
        
        // Extraction timeStamp        
        int    timeStampType  = payloadSNMP.get();
        int    timeStampLght  = payloadSNMP.get();
        byte[] timeStampValue = new byte[timeStampLght];
        payloadSNMP.get(timeStampValue, payloadSNMP.position(), timeStampLght);
        
        // attributions des valeurs de l'objet
        
        this.enterprise  = enterpriseValue;
        try{
            this.agentAdress.getByAddress(agentAdressValue);
        }catch(Exception e){
            System.out.println("[SNMPTrapV1]: ERREUR inetAdress: "+e.getMessage());
        }
        
        //
        for(index = 0; index < enterpriseValue.length; index++)
        {   // Somme des octets transtypé en int
            this.genTrapType += (int) genTrapTypeValue[index];
        }
        
        //
        for(index = 0; index < enterpriseValue.length; index++)
        {   // Somme des octets transtypé en int
            this.specTrapType += (int) specTrapTypeValue[index];
        }
        
        this.timeStamp = null;
        
        
        // Extraction des VarBinds (s'il y en a)
        while(payloadSNMP.position() < payloadSNMP.capacity()){ 
            
            int varBindType = payloadSNMP.get();
            int varBindLght = payloadSNMP.get();
            
            byte[] varBindValue = new byte[varBindLght];
            payloadSNMP.get(varBindValue, payloadSNMP.position(), varBindLght);
            
            this.varBindingsList.add(new VarBind(varBindValue));
                                
        }
        
        
    }

     public byte[] getPduFormat() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
