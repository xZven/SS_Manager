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
package stri.ss_manager.SNMPKernel;

import stri.ss_manager.SNMPMessage.SNMPMessage;

/**
 * Cette interface sert à déclarer les fonction de gestions d'un Manager distant
 * et d'envoi de TRAP SNMP via un RMI.
 * 
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * @version 4.0
 */
public interface SNMPRemoteControler extends java.rmi.Remote{
    
    public SNMPMessage sendGetRequest(SNMPMessage M_TO_SEND)throws java.rmi.RemoteException;
    
    public SNMPMessage sendGetNextRequest(SNMPMessage M_TO_SEND) throws java.rmi.RemoteException;
    
    public SNMPMessage sendSetRequest(SNMPMessage M_TO_SEND) throws java.rmi.RemoteException;
    
    // fonction de gestion du Manager/Agent distant
    
    public void shutDown() throws java.rmi.RemoteException;;
    
    public void reloadAgentConfiguration() throws java.rmi.RemoteException;;
    
}
