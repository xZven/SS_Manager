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
package stri.ss_manager.SNMPKernel.SNMPAgent;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Queue;
import java.util.Scanner;
import stri.ss_manager.SNMPMessage.SNMPMessage;
import stri.ss_manager.SNMPMessage.payload.SNMPMessagePayload;
import stri.ss_manager.SNMPMessage.payload.SNMPTrapV2;

/**
 *
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 * 
 * Cette classe défini le Thread de l'agent SNMp qui permet de gérer
 * les requêtes SNMP à destination de cette machine.
 */
public class SNMPAgent extends Thread {
    
    //attributs
    
    private byte[]             communauty = "public".getBytes();                // nom de communauté par défaut: public
    private InetAddress        addrManager;                                     // Adresse IP du manager où les TRAPs seront envoyées
    
    private Queue<SNMPMessage> S_MSG_queue_IS;                                  // file d'attente pour les reqûetes entrants
    private Queue<SNMPMessage> S_MSG_queue_OS;                                  // file d'atten pour les messages sortant
    private SNMPAgentMib       agent_mib = new SNMPAgentMib();                                       // 
    private boolean            RUNNING = true;                                  // sert à arrêter le thread   
    private String             configurationFile = "./conf/SNMPAgent.conf";     // ficjier de configuration de l'agent
    
    // constructeurs
    
    /**
     * Créer un Agent SNMP
     * 
     * @param S_MSG_queue_IS File d'attente pour le flux entrant des messages SNMP
     * @param S_MSG_queue_OS File d'attente pour le flux sortant des messages SNMP
     */
    public SNMPAgent(Queue<SNMPMessage> S_MSG_queue_IS,Queue<SNMPMessage> S_MSG_queue_OS){
        // lecture du fichier de configuration
        try{ 
            BufferedReader br=new BufferedReader(
                                new InputStreamReader(
                                    new FileInputStream(this.configurationFile)));
            String ligne;
            //
            //
            while ((ligne=br.readLine())!=null){ // lecture lgine par ligne
                System.out.println(ligne);
                if(ligne.charAt(0) == '#'){      //ligne de commentaire
                    // Ignoré
                }else{                           // paramètre de configuration
                    //
                    Scanner scanner = new Scanner(ligne);
                    //
                    String param = scanner.next();
                    //
                    switch(param){
                        case "communauty":
                            scanner.next("=");
                            this.communauty = scanner.next().getBytes();
                            break;                                
                        default: // paramètre inconnu donc ignoré
                            System.err.println("[SNMP_AGENT]: Paramètre inconnu: "+param);
                            break;
                    }
                    scanner.close();
                }
            }       
            br.close(); 
        }		
        catch (Exception e){
                System.out.println(e.toString());
        }	
        //
        this.S_MSG_queue_OS = S_MSG_queue_OS;
        //
        this.S_MSG_queue_IS = S_MSG_queue_IS;
        //
        this.setName("SNMP_AGENT");
        //
        System.out.println("[SNMP_AGENT]: Ready...");       
    }

    // méthodes
    @Override
    public void run() {
        //VAR
        SNMPMessage temp_SNMPMessage, res_SNMPMessage;
        SNMPMessagePayload res_payload;
        //
        System.out.println("[SNMP_AGENT]: Started...");
        //
        while (RUNNING) {
            //
            temp_SNMPMessage = null; // déréférençage
            if (this.S_MSG_queue_IS.isEmpty()== false) { //FILE d'attente n'est pas vide
                // on extrait le message de la file d'attente
                
                temp_SNMPMessage = this.S_MSG_queue_IS.poll();
                    //
                switch(temp_SNMPMessage.getPduType()){
                case 0xA0: // ******************************************* //Get.req
                    System.out.println("[SNMP_AGENT] GET.req Received...");
                    // vérification de la communauté
                    if(new String(temp_SNMPMessage.getCommunauty()).matches(new String(this.communauty))){
                        // Traitement du message reçu
                        System.out.println("[SNMP_AGENT] communauté égale");
                        res_payload = this.agent_mib.getOidValue(temp_SNMPMessage.getPayload());

                        // Construction du message de réponse.
                        res_SNMPMessage = new SNMPMessage(temp_SNMPMessage.getReceiver()    // celui qui a reçu (l'agent) devient l'emmeteur
                                                     ,temp_SNMPMessage.getSender()          // celui qui à envoyé devient le récepteur
                                                     ,temp_SNMPMessage.getPort()            // port distant
                                                     ,temp_SNMPMessage.getVersion()     // numéro de version identique
                                                     ,temp_SNMPMessage.getCommunauty()      // communauté identique
                                                     ,(byte) 0xA2                           // Get.res
                                                     ,res_payload);                         // payload du message réponse.
                        // renvoi de la réponse
                        this.S_MSG_queue_OS.add(res_SNMPMessage);                          // placement en file d'attente de sorti
                    }else{  // ne fais pas partie de la bonne communauté
                       //
                       System.out.println("[SNMP_AGENT]: Bad communauty ! (Sending Trap to manager...)");
                       // un trap sera envoyé à son Manager
                       SNMPTrapV2 trapPdu = null;
                       // envoi du Trap
                       sendTrapToManager(trapPdu);
                       
                    }
                    break;
                case 0xA1:  // ******************************************* // GetNext.req
                    System.out.println("[SNMP_AGENT] GETNext.req Received...");
                   // vérification de la communauté
                   if(new String(temp_SNMPMessage.getCommunauty()).matches(new String(this.communauty))){ //Si communauté est bonne
                        // Traitement du message reçu
                        res_payload = this.agent_mib.getNextOidValue(temp_SNMPMessage.getPayload());
                        // Construction du message de réponse.
                        res_SNMPMessage = new SNMPMessage(temp_SNMPMessage.getReceiver()    // celui qui a reçu (l'agent) devient l'emmeteur
                                                     ,temp_SNMPMessage.getSender()          // celui qui à envoyé devient le récepteur
                                                     ,temp_SNMPMessage.getPort()            // port distant
                                                     ,temp_SNMPMessage.getVersion()         // numéro de version identique
                                                     ,temp_SNMPMessage.getCommunauty()      // communauté identique
                                                     ,(byte) 0xA2                           // Get.res
                                                     ,res_payload);                         // payload du message réponse.

                        this.S_MSG_queue_OS.add(res_SNMPMessage);                           // renvoi de la réponse
                    }else{  // ne fais pas partie de la bonne communauté
                       System.out.println("[SNMP_AGENT]: Bad communauty ! (Sending Trap to manager...)");
                       // un trap sera envoyé à son Manager
                    }

                    break;
                case 0xA3:   // ******************************************* // SetReq
                    System.out.println("[SNMP_AGENT] SET.req Received...");
                    // vérification de la communauté
                    if(new String(temp_SNMPMessage.getCommunauty()).matches(new String(this.communauty))){
                        // Traitement du message reçu

                        res_payload = this.agent_mib.setOidValue(temp_SNMPMessage.getPayload());
                        // Construction du message de réponse.
                        res_SNMPMessage = new SNMPMessage(temp_SNMPMessage.getReceiver()    // celui qui a reçu (l'agent) devient l'emmeteur
                                                     ,temp_SNMPMessage.getSender()          // celui qui à envoyé devient le récepteur
                                                     ,temp_SNMPMessage.getPort()            // port distant
                                                     ,temp_SNMPMessage.getVersion()         // numéro de version identique
                                                     ,temp_SNMPMessage.getCommunauty()      // communauté identique
                                                     ,(byte) 0xA2                           // Get.res
                                                     ,res_payload);                         // payload du message réponse.
                        // renvoi de la réponse
                        this.S_MSG_queue_OS.add(res_SNMPMessage);                           // placement en file d'attente
                    }else{  // ne fais pas partie de la bonne communauté
                        System.out.println("[SNMP_AGENT]: Bad communauty ! (Sending Trap to manager...)");
                       // un trap sera envoyé à son Manager 

                    }
                    break;

                default:                            // Autres type de paquets = non-géré.
                    // un trap peut être envoyé à son manager
                    break;    
                }
            }else{ // file d'attente vide
                
                //
                try {
                    sleep(100); // 100 ms
                } catch (Exception e) {
                    System.err.println("[SNMP_AGENT]: ERROR --> " + e.getMessage());
                }
            }
            
        }
        
        System.out.println("[SNMP_AGENT]: Stopped");
    }
   
    /**
     * Cette fonction permet de recharger la configuration de l'agent SNMP
     * sans arrêter le processus.
     */
    public void reloadConfig(){
        // Arrêt du Thread
        this.RUNNING = false;
        // Rechargement de la configuration
        try{ 
            BufferedReader br=new BufferedReader(
                                new InputStreamReader(
                                    new FileInputStream(this.configurationFile)));
            String ligne;
            //
            //
            while ((ligne=br.readLine())!=null){ // lecture lgine par ligne
                System.out.println(ligne);
                if(ligne.charAt(0) == '#'){      //ligne de commentaire
                    // Ignoré
                }else{                           // paramètre de configuration
                    //
                    Scanner scanner = new Scanner(ligne);
                    //
                    String param = scanner.next();
                    //
                    switch(param){
                        case "communauty":
                            scanner.next("=");
                            this.communauty = scanner.next().getBytes();
                            break;                                
                        default: // paramètre inconnu donc ignoré
                            System.err.println("[SNMP_AGENT]: Paramètre inconnu: "+param);
                            break;
                    }
                    scanner.close();
                }
            }       
            br.close(); 
        }		
        catch (Exception e){
                System.out.println(e.toString());
        }	
        //
        // Redémarrage auto du thread.
        this.start();
    }
   
    /**
     * Cette fonction permet d'envoyer un TRAP SNMP version 2 au Manager
     * de cette agent, sur le port 162.
     * 
     * @param trapPdu Contenu du message trap 
     */
    public void sendTrapToManager(SNMPTrapV2 trapPdu){

        // conception du message SNMP
        SNMPMessage trap_message_v2 = new SNMPMessage(null
                , this.addrManager, 
                162, 
                1,
                this.communauty, 
                (byte) 0xA7, 
                trapPdu);

        this.S_MSG_queue_OS.add(trap_message_v2);
    }
}

