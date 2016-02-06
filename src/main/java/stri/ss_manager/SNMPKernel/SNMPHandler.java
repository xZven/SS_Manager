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
 *
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * @version 1
 * Cette classe défini le coeur du programme. C'est ce Thread qui se 
 * charge de répartir les différents messages aux bonnes entités(Agent ou Manager).
 * 
 * <p>
 *  Lorsqu'un message SNMP est reçu:
 *  S'il s'agit d'une requête SNMP, alors elle sera donné au Thread Agent du programme
 *  S'il s'agit d'une réponse SNMP, alors elle sera donné soit à l'IHM, s'il s'agit d'une
 *  réponse à une requête ponctuel; soit aux Thread chargé d'actualiser le Jtree et les 
 *  données liées à chaque agent.
 * </p>
 * 
 * <p> 
 *  Pour cette première version du programme, le coeur ne gèrera que les requête ponctuelles.
 * </p>
 */
public class SNMPHandler extends Thread{
    
    // attributs
    
    // méthodes
    
    // ATTENTION CES METHODES DE MARCHERONT QUE POUR LA VERSION 1 DU PROGRAMME SS_MANAGER
    
    /**
     * Cette méthode permet d'envoyer des GetRequest ponctuels via l'IHM
     * ATTENTION: elle n'est valide que pour la version 1 du programme.
     * Cette méthode est blockante. Si un message SNMP n'est pas reçu, alors
     * NULL est renvoyé.
     * @param S_MSG_TO_SEND
     * @return SNMPMessage reçu de l'agent ou NULL.
     */
    public SNMPMessage sendGetRequest(SNMPMessage S_MSG_TO_SEND){
        SNMPMessage S_MSG_RESPONSE = null;
    // ********************************************************************* //
    
    // ********************************************************************* // 
        return S_MSG_RESPONSE;
    }
    
    /**
     * Cette méthode permet d'envoyer des GeNexttRequest ponctuels via l'IHM.
     * ATTENTION: elle n'est valide que pour la version 1 du programme.
     * Cette méthode est blockante. Si un message SNMP n'est pas reçu, alors
     * NULL est renvoyé.
     * @param S_MSG_TO_SEND
     * @return SNMPMessage reçu de l'agent ou NULL.
     */
    public SNMPMessage sendGetNextRequest(SNMPMessage S_MSG_TO_SEND){
        SNMPMessage S_MSG_RESPONSE = null; 
    // ********************************************************************* //
    
    // ********************************************************************* // 
        
        return S_MSG_RESPONSE;
    }
    
    /**
     * Cette méthode permet d'envoyer des SettRequest ponctuels via l'IHM.
     * Cette méthode est blockante. Si un message SNMP n'est pas reçu, alors
     * NULL est renvoyé.
     * ATTENTION: elle n'est valide que pour la version 1 du programme.
     * @param S_MSG_TO_SEND
     * @return SNMPMessage reçu de l'agent ou NULL.
     */
    public SNMPMessage sendSetRequest(SNMPMessage S_MSG_TO_SEND){
        SNMPMessage S_MSG_RESPONSE = null;
    // ********************************************************************* //
    
    // ********************************************************************* // 
        
        return S_MSG_RESPONSE;
    }
}
