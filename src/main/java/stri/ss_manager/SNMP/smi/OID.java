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
    private byte[] objectId;
    
    // Constructeurs
    /**
     * Permet de créer un ObjectIdentifier en passant un byte Array.
     * Aucune vérification n'est faite.
     * @param objectId 
     */
    public OID(byte[] objectId){        
        this.objectId    = objectId;
    }
    
    /**
     * Permet de créer un ObjectIdentifier en passant un chaine de caractère au format 1.3.X.X.X     * 
     * @param objectId oid à créer
     */
    public OID(String objectId){ 
       
        this.objectId = new byte[objectId.getBytes().length - ((objectId.getBytes().length/2))]; // 3 Piquets = 2 Barrière et on ne veut que les barrière.
        ByteBuffer temp_ByteBuffer = ByteBuffer.wrap(this.objectId);
        //
        for(byte c : objectId.getBytes()){ // pour chaque octet
            // on regard si c'est un point
            if((char) c != '.'){    // si le caractère extrait n'est pas un point
                temp_ByteBuffer.put((byte) Character.getNumericValue(c));
            }
        }
    }
    // méthodes
    /**
     * Cette fonction permet d'obtenir la chaine de caractère
     * de l'OID au format X.X.X.X.X en prenant en compte l'octet raccourci
     * 1.3 (0x2B).
     * @return OID au format 1.3.X.X.X
     */
    public String getObjectIdStringFormat(){
        String oid = "";
        //
        oid = oid + "1.3";
        //
        for(int index = 1; index < this.objectId.length; index++){
            oid = oid + "." + Byte.toString(this.objectId[index]);
        }
        //
        return oid;
    }
    
    /**
     * Retourn une chaine de caractère en prenant en compte le premier octet primitive 0x2B
     * 
     * @return Chaine de caractère au format 43.X.X.X
     */
    @Override
    public String toString() {
        String oid = "";
        //
        
        oid = oid + Byte.toString(this.objectId[0]);
        //
        for(int index = 1; index < this.objectId.length; index++){
            oid = oid + "." + Byte.toString(this.objectId[index]);
        }   
        return oid;
    }

    /**
     * Retourne l'OID au format TLV
     * @return OID au format TLV
     */
    public byte[] getTLVFormat() {
        
        // VAR
        byte[]      temp_data       = new byte[1 + this.objectId.length]; // 2 octets TL + V
        ByteBuffer  temp_ByteBuffer = ByteBuffer.wrap(temp_data);
        //      
        byte     objectIdLght = (byte) (this.objectId.length - 1);         
        //
        temp_ByteBuffer.put((byte) 0x06);                           // T = ObjectIdentifier 
        temp_ByteBuffer.put(objectIdLght);                          // L
        temp_ByteBuffer.put((byte)0x2B);                            // Raccourci pour 1.3 (iso.org)
        temp_ByteBuffer.put(this.objectId, 2 ,objectIdLght - 1);        // V
        //
        return temp_data;
    }
    
    // getters
    
    /**
     * Retourn l'objet
     * @return tableau d'octet 
     */
    public byte[] getObjectId() {
        return objectId;
    }
    
    
}
