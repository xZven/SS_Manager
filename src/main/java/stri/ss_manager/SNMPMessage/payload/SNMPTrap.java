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

import java.net.InetAddress;
import java.security.Timestamp;

/**
 *
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * @version 1.0
 * 
 * Cette classe défini l'information utile dans les PDU SNMP qui sont
 * de type TRAP.
 */
public class SNMPTrap {
    
    
    // attributs
    
    private final int pduType = 4;
    // SNMP SysObjectID
    private String enterprise;
    // Agent adresse
    private InetAddress agentAdress;
    /*
        coldStart(0),
        warmStart(1),
        linkDown(2),
        linkUp(3),
        authenticationFailure(4),
        egpNeighborLoss(5),
        enterpriseSpecific(6)
    */
    private int genTrapType;
    //
    private int specTrapType;
    // Agent sysUpTime
    private Timestamp timeStamp;
    
    //private Vector<OID> varBindings;
}
