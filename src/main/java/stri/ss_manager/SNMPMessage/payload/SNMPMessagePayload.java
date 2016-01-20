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
    private int pduType;
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
    //private vector<OID> varBindings;
    
}
