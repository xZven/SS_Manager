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
        ByteBuffer VarBindSNMP = null;
        VarBindSNMP.wrap(varBindValue);
        
        // Extraction de l'OID
        int     objectIdType  = VarBindSNMP.get();
        int     objectIdLght  = VarBindSNMP.get();
        byte[]  objectIdValue = new byte[objectIdLght];
        //
        VarBindSNMP.get(objectIdValue, VarBindSNMP.position(), objectIdLght);
        //
        this.objectId = new OID(objectIdValue);        

        // Extraction de sa valeur (s'il y a lieu)
        int objectValueType = VarBindSNMP.get();
        int objectValueLght = VarBindSNMP.get();
        
        if(objectIdLght == 0){  // s'il n'y a pas de valeur alors
            this.objectId = null;
            
        }else{
            byte[]  objectValueValue = new byte[objectValueLght];
            VarBindSNMP.get(objectValueValue, VarBindSNMP.position(), objectValueLght);
        }
    }
    
}
