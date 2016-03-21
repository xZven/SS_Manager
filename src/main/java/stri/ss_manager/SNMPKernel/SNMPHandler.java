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

import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import stri.ss_manager.SNMPMessage.SNMPMessage;

/**
 *
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * @version 4
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
public class SNMPHandler extends UnicastRemoteObject implements SNMPRemoteManagerInterface{
    
    // attributs
    
    // files d'attentes
        Queue<SNMPMessage> S_MSG_queue_IS;
        Queue<SNMPMessage> S_MSG_queue_OS;
    //
        int requestIdIncrementator = 0;
    //
        InetAddress topLevelManager;
        
    // constructeurs
    
    /**
     * Permet de créer un SNMPHandler qui permettra de gérer l'envoi et la réception des requêtes
     * et réponses SNMP.
     * 
     * @param S_MSG_queue_IS File d'attente pour le flux entrant des messages SNMP
     * @param S_MSG_queue_OS File d'attente pour le flux sortant des messages SNMP
     * @throws java.rmi.RemoteException
     */
    public SNMPHandler(Queue<SNMPMessage> S_MSG_queue_IS, Queue<SNMPMessage> S_MSG_queue_OS) throws RemoteException{

        super();
        
        this.S_MSG_queue_IS = S_MSG_queue_IS;
        this.S_MSG_queue_OS = S_MSG_queue_OS;
        
            try {
                this.topLevelManager = InetAddress.getByName("localhost");
            } catch (UnknownHostException ex) {
                Logger.getLogger(SNMPHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }

    // méthodes
    
    // ATTENTION CES METHODES DE MARCHERONT QUE POUR LA VERSION 1 DU PROGRAMME SS_MANAGER
    
    /**
     * Cette méthode permet d'envoyer des GetRequest ponctuels via l'IHM
     * ATTENTION: elle n'est valide que pour la version 1 du programme.
     * Cette méthode est blockante. Si un message SNMP n'est pas reçu, alors
     * NULL est renvoyé.
     * @param S_MSG_TO_SEND Message à envoyer
     * @return SNMPMessage reçu de l'agent ou NULL.
     */
    @Override
    public SNMPMessage sendGetRequest(SNMPMessage S_MSG_TO_SEND){
        SNMPMessage S_MSG_RESPONSE = null;
        //
        int counter = 0;
        // request id
        S_MSG_TO_SEND.getPayload().setRequestId(S_MSG_TO_SEND.getPayload().getRequestId() + this.requestIdIncrementator++);
        
    // ********************************************************************* //
        
        while((S_MSG_RESPONSE = this.S_MSG_queue_IS.poll()) == null && counter <4){ // TANT qu'aucun message n'est reçu
            // on attends et au bout d'un délai on renvoi un message
            // 4 essais MAX
            
            this.S_MSG_queue_OS.add(S_MSG_TO_SEND); counter++;
            
                        
            try{
                sleep(2000);    // 2 s avant retransmission
            }catch(Exception e){
                System.err.println("[SNMPHandler.sendGetRequest()]: Error de sleep --> " + e.getMessage());
            }
            //
        }        
    // ********************************************************************* //
        if(counter < 4 && S_MSG_RESPONSE == null){
            System.out.println("[SNMPHandler.sendGetRequest()]: Aucune réponse reçu...");
            return S_MSG_RESPONSE; // null
        }else{
            return S_MSG_RESPONSE;
        }
        
    }
    
    /**
     * Cette méthode permet d'envoyer des GeNexttRequest ponctuels via l'IHM.
     * ATTENTION: elle n'est valide que pour la version 1 du programme.
     * Cette méthode est blockante. Si un message SNMP n'est pas reçu, alors
     * NULL est renvoyé.
     * @param S_MSG_TO_SEND Message à envoyer
     * @return SNMPMessage reçu de l'agent ou NULL.
     */
    @Override
    public SNMPMessage sendGetNextRequest(SNMPMessage S_MSG_TO_SEND){
           SNMPMessage S_MSG_RESPONSE = null;
        int counter = 0;
        // request id
        S_MSG_TO_SEND.getPayload().setRequestId(S_MSG_TO_SEND.getPayload().getRequestId() + this.requestIdIncrementator++);
    // ********************************************************************* //
        
        while((S_MSG_RESPONSE = this.S_MSG_queue_IS.poll()) == null && counter <4){ // TANT qu'aucun message n'est reçu
            // on attends et au bout d'un délai on renvoi un message
            // 4 essais MAX
            
            this.S_MSG_queue_OS.add(S_MSG_TO_SEND); counter += 1;
            // attente de la réponse
                        
            try{
                sleep(100);
            }catch(Exception e){
                System.err.println("[SNMPHandler.sendGetNextRequest()]: Error de sleep --> " + e.getMessage());
            }
            //
        }        
    // ********************************************************************* //
        if(counter < 4 && S_MSG_RESPONSE == null){
            System.out.println("[SNMPHandler.sendGetRequest()]: Aucune réponse reçu...");
            return S_MSG_RESPONSE; // null
        }else{
            return S_MSG_RESPONSE;
        }
    }
    
    /**
     * Cette méthode permet d'envoyer des SettRequest ponctuels via l'IHM.
     * Cette méthode est blockante. Si un message SNMP n'est pas reçu, alors
     * NULL est renvoyé.
     * ATTENTION: elle n'est valide que pour la version 1 du programme.
     * @param S_MSG_TO_SEND Message à envoyer
     * @return SNMPMessage reçu de l'agent ou NULL.
     */
    @Override
    public SNMPMessage sendSetRequest(SNMPMessage S_MSG_TO_SEND){
               SNMPMessage S_MSG_RESPONSE = null;
        int counter = 0;
        // request id
        S_MSG_TO_SEND.getPayload().setRequestId(S_MSG_TO_SEND.getPayload().getRequestId() + this.requestIdIncrementator++);
    // ********************************************************************* //
        
        while((S_MSG_RESPONSE = this.S_MSG_queue_IS.poll()) == null && counter <4){ // TANT qu'aucun message n'est reçu
            // on attends et au bout d'un délai on renvoi un message
            // 4 essais MAX
            
            this.S_MSG_queue_OS.add(S_MSG_TO_SEND); counter += 1;
            // attente de la réponse
                        
            try{
                sleep(100);
            }catch(Exception e){
                System.err.println("[SNMPHandler.sendGetRequest()]: Error de sleep --> " + e.getMessage());
            }
            //
        }        
    // ********************************************************************* //
        if(counter < 4 && S_MSG_RESPONSE == null){
            System.out.println("[SNMPHandler.sendSetRequest()]: Aucune réponse reçu...");
            return S_MSG_RESPONSE; // null
        }else{
            return S_MSG_RESPONSE;
        }
    }

    public void sendtraopToTopLevelManager(SNMPMessage trap){
       this.S_MSG_queue_OS.add(trap); 
    }
    /** Cette méthode permet d'arrêter le manager distant
     * 
     * @throws RemoteException 
     */
    @Override
    public void shutDown() throws RemoteException {
        System.out.println("[SNMPHandler]: Shutdown from RMI !");
        System.exit(0);
    }

    @Override
    public void reloadAgentConfiguration() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Cette méthode permet d'envoyer un message TRAP.
     * 
     * @param trap_message_v2 Trap à envoyer
     */
    public void sendTrap(SNMPMessage trap_message_v2) {
        this.S_MSG_queue_OS.add(trap_message_v2);
    }
    
    @Override
    public void setIpTopLevelManager(InetAddress ip) throws RemoteException {
        this.topLevelManager = ip;
    }
}
