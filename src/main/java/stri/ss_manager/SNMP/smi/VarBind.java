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
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * @version 1.0
 * 
 * Cette classe défini l'ensemble OID plus sa valeur. Le type de sa valeur
 * sera extrait des MIBs.
 */
public class VarBind {
    
    // attributs
    
    private OID    objectId;
    private byte[] objectValue;
    
    // Constructeurs
    public VarBind(OID objectId) {
        this.objectId = objectId;
        this.objectId = null;
    }

    public VarBind(OID objectId, byte[] objectValue) {
        this.objectId    = objectId;
        this.objectValue = objectValue;
    } 
    
    /**
     * 
     * @param varBindValue 
     */
    public VarBind(byte[] varBindValue)
    {
        ByteBuffer VarBindSNMP = ByteBuffer.wrap(varBindValue);
        
        // Extraction de l'OID
        int     objectIdType  = VarBindSNMP.get();
        int     objectIdLght  = VarBindSNMP.get();
        byte[]  objectIdValue = new byte[objectIdLght];
        //
        VarBindSNMP.get(objectIdValue, 0, objectIdLght);
        //
        this.objectId = new OID(objectIdValue);        

        // Extraction de sa valeur (s'il y a lieu)
        int objectValueType = VarBindSNMP.get();
        int objectValueLght = VarBindSNMP.get();
        
        if(objectValueLght == 0){  // s'il n'y a pas de valeur alors
            this.objectValue = null;
        }else{
            byte[]  objectValueValue = new byte[objectValueLght];
            VarBindSNMP.get(objectValueValue, 0, objectValueLght);
        }
    }
    
    @Override
    public String toString() {
        if(this.objectValue == null)
            return objectId.getObjectIdStringFormat() + " = NULL ";
        else{
            return objectId.getObjectIdStringFormat() + " = " + new String(objectValue) +" ";
        }
    }
    //
    private byte[] getobjectIdTLVFormat(){
        return this.objectId.getTLVFormat();
    }
    
    private byte[] getobjectValueTLVFormat(){
        
        // VAR
        ByteBuffer temp_ByteBuffer = ByteBuffer.allocate(50);
        byte[] temp_data;
        
        byte objectValueType;
        byte objectValueLght;
        byte[] objectValueValue;
        
        //
        if(this.objectValue == null){
            objectValueType = 0x05;
            objectValueLght = 0x00;
            //
            temp_ByteBuffer.put(objectValueType);
            temp_ByteBuffer.put(objectValueLght);
            //
            temp_data = new byte[temp_ByteBuffer.position()];
            temp_ByteBuffer.get(temp_data);
            //
            return temp_data;
            //
        }else{
            objectValueType  = 0x00;
            objectValueLght  = (byte) this.objectValue.length;
            objectValueValue = this.objectValue;
            //
            temp_ByteBuffer.put(objectValueType);
            temp_ByteBuffer.put(objectValueLght);
            //
            temp_data = new byte[temp_ByteBuffer.position()];
            temp_ByteBuffer.get(temp_data);
            //
            return temp_data;
        }
    }
    
    public byte[] getTLVFormat() {
        
        // VAR
        ByteBuffer temp_ByteBuffer = ByteBuffer.allocate(50);
        byte[] temp_data;
        
        //
        temp_ByteBuffer.put((byte) 0x30);                // T = SEQUENCE
        int varBindLghtpos = temp_ByteBuffer.position(); // Position de L
        
        // Extraction de l'OID
        temp_ByteBuffer.put(this.getobjectIdTLVFormat());
        // Extraction de sa valeur
        temp_ByteBuffer.put(this.getobjectValueTLVFormat());
        // Calcul de varBindLght
        temp_ByteBuffer.position(varBindLghtpos);
        temp_ByteBuffer.put((byte) (this.getobjectIdTLVFormat().length + 
                                    this.getobjectValueTLVFormat().length));
        //
        temp_data= new byte[temp_ByteBuffer.position()];
        temp_ByteBuffer.get(temp_data);
        //
        return temp_data;
        
    }
    //
}
