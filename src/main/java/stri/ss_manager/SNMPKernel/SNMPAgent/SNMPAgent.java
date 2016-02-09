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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import stri.ss_manager.SNMP.smi.VarBind;
import stri.ss_manager.SNMPMessage.SNMPMessage;
import stri.ss_manager.SNMPMessage.handler.SNMPMessageHandlerInputStream;
import stri.ss_manager.SNMPMessage.handler.SNMPMessageHandlerOutputStream;
import stri.ss_manager.SNMPMessage.payload.SNMPMessagePayload;
import stri.ss_manager.SNMPMessage.transport.SocketHandlerInputStream;
import stri.ss_manager.SNMPMessage.transport.SocketHandlerOutputStream;
import stri.ss_manager.SNMPMessage.transport.SocketHandlerTrapListener;

/**
 *
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 */
public class SNMPAgent extends Thread {
    
    //attributs
    
    private byte[]             communauty = "public".getBytes();                // nom de communauté par défaut: public
    private InetAddress        addrManager;                                     // Adresse IP du manager où les TRAPs seront envoyées
    
    private Queue<SNMPMessage> S_MSG_queue_IS;                                  // file d'attente pour les reqûetes entrants
    private Queue<SNMPMessage> S_MSG_queue_OS;                                  // file d'atten pour les messages sortant
    private SNMPAgentMib       agent_mib;                                       // 
    private boolean            RUNNING = true;                                  // sert à arrêter le thread   
    private String             configurationFile = "./conf/SNMPAgent.conf";     // ficjier de configuration de l'agent
    
    // constructeurs
    
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
                
                // vérification du nom de communauté
                
                switch(temp_SNMPMessage.getPduType()){
                    case 0xA0:                          // Get.req
                        // vérification de la communauté
                        if(this.communauty == temp_SNMPMessage.getCommunauty()){
                            // Traitement du message reçu
                            for(VarBind vb: temp_SNMPMessage.getPayload().getVarBindingsList())
                            {
                                // Pour chaque varbind, on lui attribut sa valeur
                                vb.setObjectValue(agent_mib.getOidValue(vb.getObjectId()));  
                            }
                            // Construction du message de réponse.
                            res_SNMPMessage = new SNMPMessage(temp_SNMPMessage.getReceiver()    // celui qui a reçu (l'agent) devient l'emmeteur
                                                         ,temp_SNMPMessage.getSender()          // celui qui à envoyé devient le récepteur
                                                         ,temp_SNMPMessage.getPort()            // port distant
                                                         ,temp_SNMPMessage.getVersion()         // numéro de version identique
                                                         ,temp_SNMPMessage.getCommunauty()      // communauté identique
                                                         ,(byte) 0xA2                           // Get.res
                                                         ,temp_SNMPMessage.getPayload());       // payload du message réponse.
                             this.S_MSG_queue_OS.add(res_SNMPMessage);                           // renvoi de la réponse
                        }else{  // ne fais pas partie de la bonne communauté
                            
                           // un trap sera envoyé à son Manager
                        }
                       
                        
                        break;
                    case 0xA1:                          // GetNext.req
                         // vérification de la communauté
                        if(this.communauty == temp_SNMPMessage.getCommunauty()){
                            // Traitement du message reçu
                            for(VarBind vb: temp_SNMPMessage.getPayload().getVarBindingsList())
                            {
                                // Pour chaque varbind, on lui attribut sa valeur
                                vb.setObjectValue(agent_mib.getNextOidValue(vb.getObjectId()));  
                            }
                            // Construction du message de réponse.
                            res_SNMPMessage = new SNMPMessage(temp_SNMPMessage.getReceiver()    // celui qui a reçu (l'agent) devient l'emmeteur
                                                         ,temp_SNMPMessage.getSender()          // celui qui à envoyé devient le récepteur
                                                         ,temp_SNMPMessage.getPort()            // port distant
                                                         ,temp_SNMPMessage.getVersion()         // numéro de version identique
                                                         ,temp_SNMPMessage.getCommunauty()      // communauté identique
                                                         ,(byte) 0xA2                           // Get.res
                                                         ,temp_SNMPMessage.getPayload());       // payload du message réponse.
                            
                            this.S_MSG_queue_OS.add(res_SNMPMessage);                           // renvoi de la réponse
                        }else{  // ne fais pas partie de la bonne communauté
                           // un trap sera envoyé à son Manager
                        }
                        
                        break;
                    case 0xA3:                          // SetReq
                        // vérification de la communauté
                        if(this.communauty == temp_SNMPMessage.getCommunauty()){
                            // Traitement du message reçu
                            for(VarBind vb: temp_SNMPMessage.getPayload().getVarBindingsList())
                            {
                                // Pour chaque varbind, on lui attribut sa valeur
                                vb.setObjectValue(agent_mib.setOidValue(vb.getObjectId()));  
                            }
                            // Construction du message de réponse.
                            res_SNMPMessage = new SNMPMessage(temp_SNMPMessage.getReceiver()    // celui qui a reçu (l'agent) devient l'emmeteur
                                                         ,temp_SNMPMessage.getSender()          // celui qui à envoyé devient le récepteur
                                                         ,temp_SNMPMessage.getPort()            // port distant
                                                         ,temp_SNMPMessage.getVersion()         // numéro de version identique
                                                         ,temp_SNMPMessage.getCommunauty()      // communauté identique
                                                         ,(byte) 0xA2                           // Get.res
                                                         ,temp_SNMPMessage.getPayload());       // payload du message réponse.
                            
                            this.S_MSG_queue_OS.add(res_SNMPMessage);                           // renvoi de la réponse
                        }else{  // ne fais pas partie de la bonne communauté
                            
                           // un trap sera envoyé à son Manager
                        }
                        
                        break;
                        
                    default:                            // Autres type de paquets = non-géré.
                        // un trap peut être envoyé à son manager
                        break;    
                }
                //
            } else { // SI la file d'attent est vide, on endors le thread pour 100ms
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
    
    public static void main(String[] args) {
        DatagramSocket                  socket          = null;
        DatagramSocket                  trap_listenner  = null;
        //Queues
        Queue<DatagramPacket>   DG_packet_queue_IS;         // File d'attente pour les PDU SNMP pour le flux entrant;
        Queue<DatagramPacket>   DG_packet_queue_OS;         // File d'attente pour les PDU SNMP pour le flux sortant;

        Queue<SNMPMessage>      S_MSG_queue_IS;             // File d'attente pour les SNMPMessages entrants (transmit au noyau)
        Queue<SNMPMessage>      S_MSG_queue_OS;             // File d'attente pour les SNMPMessages entrants (transmit au noyau)

        //Thread
        SNMPMessageHandlerInputStream  S_MSG_HDLR_IS;       // Thread gérant les SNMPMessages entrants
        SNMPMessageHandlerOutputStream S_MSG_HDLR_OS;       // Thread gérant les SNMPMessages sortants

        SocketHandlerInputStream       SOCK_HDLR_IS;        // Thread gérant les DatagramPacket entrants
        SocketHandlerOutputStream      SOCK_HDLR_OS;        // Thread gérant les DatagramPacket sortants
//      SocketHandlerTrapListener      SOCK_HDLR_TRAP_LSTNR;// Thread gérant les DatagramPacket entrants sur le port 162 (TRAPS)
        
        SNMPAgent                      SNMP_AGENT;

        //Autres

        // Initialisation du Socket
        System.out.println("[MAIN_PROC]: Initializing Sockets...");
        try{
            socket         = new DatagramSocket(161);         // socket d'envoie/réception des requêtes SNMP
            trap_listenner = new DatagramSocket(162);         // socket de réception des TRAPS
        }catch(Exception e){
            System.err.println("[MAIN_PROC]: ERROR OPENNING SOCKETS --> "+e.getMessage());
            System.err.println("Exiting...");

            System.exit(-1);                                  // FIN RPOG si Erreur de chargement du socket
        }
        System.out.println("[MAIN_PROC]: Sockets initialized...");

        // Initialisation des files d'attentes
        System.out.println("[MAIN_PROC]: Initializing queues...");
        DG_packet_queue_IS = new LinkedList<>();
        DG_packet_queue_OS = new LinkedList<>();

        S_MSG_queue_IS     = new LinkedList<>();
        S_MSG_queue_OS     = new LinkedList<>();

        System.out.println("[MAIN_PROC]: Queues initialized...");
        // Initialisation des Threads
        System.out.println("[MAIN_PROC]: Initializing Threads...");

        // Couche SocketHandler
        SOCK_HDLR_IS         = new SocketHandlerInputStream(socket, DG_packet_queue_IS);
//      SOCK_HDLR_TRAP_LSTNR = new SocketHandlerTrapListener(trap_listenner, DG_packet_queue_IS);
        SOCK_HDLR_OS         = new SocketHandlerOutputStream(socket, DG_packet_queue_OS);

        // Couche SNMPMessageHandler
        S_MSG_HDLR_IS = new SNMPMessageHandlerInputStream(DG_packet_queue_IS, S_MSG_queue_IS);
        S_MSG_HDLR_OS = new SNMPMessageHandlerOutputStream(DG_packet_queue_OS, S_MSG_queue_OS);

        // Couche SNMPKernel
        
        SNMP_AGENT = new SNMPAgent(S_MSG_queue_IS, S_MSG_queue_OS);

        System.out.println("[MAIN_PROC]: Threads initializded...");
        System.out.println("[MAIN_PROC]: Successfull initialized !");
        // Démarrage des Threads

        // Couche SocketHandler
        SOCK_HDLR_IS.start();
//      SOCK_HDLR_TRAP_LSTNR.start();
        SOCK_HDLR_OS.start();

        S_MSG_HDLR_IS.start();
        S_MSG_HDLR_OS.start();
        
        
    }
    
}

