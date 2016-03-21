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

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * @version 1.0
 * 
 * Cette classe défini l'ensemble OID plus sa valeur. Le type de sa valeur
 * sera extrait des MIBs.
 */
public class VarBind implements Serializable{
    
    // attributs
    
    private OID    objectId;
    private byte[] objectValue;
    
    // Constructeurs
        
    /**
     * Cette fonction permet de créer un varBind complet.
     * 
     * @param objectId OID de l'objet.
     * @param objectValue Valeur de l'object
     */
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
            this.objectValue = objectValueValue;
        }
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString() {
        if(this.objectValue == null)
            return this.objectId.getObjectIdStringFormat() + " = NULL ";
        else{
            return this.objectId.getObjectIdStringFormat() + " = " + new String(this.objectValue) +" ";
        }
    }
    //
    private byte[] getobjectIdTLVFormat(){
        return this.objectId.getTLVFormat();
    }
    
    private byte[] getobjectValueTLVFormat(){
        
        // VAR
        
        byte objectValueType;
        byte objectValueLght;
        byte[] objectValueValue;
        
        //
        if(this.objectValue == null){
            byte[]     temp_data       = new byte[2];
            ByteBuffer temp_ByteBuffer = ByteBuffer.wrap(temp_data);
            //
            objectValueType = 0x05;
            objectValueLght = 0x00;
            //
            temp_ByteBuffer.put(objectValueType);
            temp_ByteBuffer.put(objectValueLght);
            //
            return temp_data;
            //
        }else{  // si l'objet a une valeur
            byte[]     temp_data       = new byte[2 + objectValue.length];
            ByteBuffer temp_ByteBuffer = ByteBuffer.wrap(temp_data);
            //
            
            objectValueType  = 0x04; // à modifier plus tard lors de la mise en place des mibs
            objectValueLght  = (byte) this.objectValue.length;
            objectValueValue = this.objectValue;
            //
            temp_ByteBuffer.put(objectValueType);           // T
            temp_ByteBuffer.put(objectValueLght);           // L
            temp_ByteBuffer.put(objectValueValue);          // V
            //
            return temp_data;
        }
    }
    
    public byte[]  getTLVFormat() {
        
        // VAR
        ByteBuffer temp_ByteBuffer = ByteBuffer.allocate(50);
        byte[] temp_data;
        
        //
        temp_ByteBuffer.put((byte) 0x30);                // T = SEQUENCE
        int varBindLghtpos = temp_ByteBuffer.position(); // Position de L
        temp_ByteBuffer.position(temp_ByteBuffer.position() + 1);
        
        // Extraction de l'OID
        temp_ByteBuffer.put(this.getobjectIdTLVFormat());
        // Extraction de sa valeur
        temp_ByteBuffer.put(this.getobjectValueTLVFormat());
        // Allocation
        temp_data= new byte[temp_ByteBuffer.position()];
        // Calcul de varBindLght       
        temp_ByteBuffer.position(varBindLghtpos);
        temp_ByteBuffer.put((byte) (this.getobjectIdTLVFormat().length + 
                                    this.getobjectValueTLVFormat().length));
        //
        temp_ByteBuffer.position(0);
        temp_ByteBuffer.get(temp_data,0, temp_data.length);
        //
        return temp_data;
        
    }
    // getters

    public OID getObjectId() {
        return objectId;
    }

    public byte[] getObjectValue() {
        return objectValue;
    }
    
    // setters

    public void setObjectId(OID objectId) {
        this.objectId = objectId;
    }

    public void setObjectValue(byte[] objectValue) {
        this.objectValue = objectValue;
    }
    
    
    
}
