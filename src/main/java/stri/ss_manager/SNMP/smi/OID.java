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
package stri.ss_manager.SNMP.smi;

import java.nio.ByteBuffer;

/**
 *
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * @version 1.0
 * 
 * Cette classe défini les ObjectIdentifier défini dans le protocole SNMP.
 * 
 */
public class OID {
    
    // attributs
    private final byte[] objectId;
    
    // Constructeurs
    /**
     * Permet de créer un ObjectIdentifier en passant un byte Array.
     * 
     * @param objectId 
     */
    public OID(byte[] objectId){        
        this.objectId    = objectId;
    }
    
    // méthodes
    /**
     * Cette fonction permet d'obtenir la chaine de caractère
     * de l'OID au format X.X.X.X.X
     * @return OID.
     */
    public String getObjectIdStringFormat(){
        String oid = "";
        //
        
        oid = oid + "1.3";
        //
        for(int index = 1; index < this.objectId.length; index++){
            oid = oid + "." + Byte.toString(this.objectId[index]);
        }
/*  
        for(byte b: objectId){
            oid = oid + "." + Byte.toString(b);
        }
*/      
        return oid;
    }
    
    /**
     * Retourne l'OID au format TLV
     * @return OID au format TLV
     */
    public byte[] getTLVFormat() {
        
        // VAR
        ByteBuffer temp_ByteBuffer = ByteBuffer.allocate(50);
        byte[] temp_data;
        
        byte     objectIdType;  
        byte     objectIdLght; 
        byte[]   objectIdValue;
        
        //
        objectIdType  = 0x06;
        objectIdLght  = (byte) this.objectId.length;
        objectIdValue = this.objectId;
        //
        temp_ByteBuffer.put(objectIdType);      // T
        temp_ByteBuffer.put(objectIdLght);      // L
        temp_ByteBuffer.put(objectIdValue);     // V
        //
        temp_data = new byte[temp_ByteBuffer.position()];
        temp_ByteBuffer.get(temp_data);
        //
        return temp_data;
    }
}
