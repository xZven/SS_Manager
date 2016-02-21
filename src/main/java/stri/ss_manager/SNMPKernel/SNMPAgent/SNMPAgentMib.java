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
import java.util.Scanner;
import stri.ss_manager.SNMP.smi.OID;
import stri.ss_manager.SNMP.smi.VarBind;
import stri.ss_manager.SNMPMessage.payload.SNMPMessagePayload;

/**
 *
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 */
public class SNMPAgentMib {
    // attributs
    
    String mibFile = "./mib/SNMPAgent.mib";
    
    // méthodes
    
    /**
     * Cette fonction permet d'obtenir la valeur des oid
     * présente dans la payload des messages SNMP.
     * @param payload
     * @return payload avec les valeurs attribuer
     */
    public SNMPMessagePayload getOidValue(SNMPMessagePayload payload) {
        //
        try{ 
            // ouverture du fichier mib
            BufferedReader br=new BufferedReader(
                                new InputStreamReader(
                                    new FileInputStream(this.mibFile)));
            String ligne;
            //
            // FORMAT D'UNE LIGNE DANS UN FICHIER MIB
            // X.X.X.X.X.X.X.X VALUE VALUE VALUE
            int index       = 0;                 // variable utilisé pour les erreurs de mathcing
            while ((ligne=br.readLine())!=null){ // lecture lgine par ligne
                System.out.println(ligne);
                if(ligne.charAt(0) == '#'){      //ligne de commentaire
                    // Ignoré
                }else{                           // paramètre de configuration
                    //
                    Scanner scanner = new Scanner(ligne);
                    //
                    String oid      = scanner.next();
                    String oidValue = "";
                    
                    
                    // on concatène tous ce qui suit l'oid dans le fichier
                    while(scanner.hasNext()) oidValue += " " + scanner.next();
                    
                    // new String(temp_SNMPMessage.getCommunauty()).matches(new String(this.communauty)))
                    for(VarBind vb: payload.getVarBindingsList())              // comparaison avec chaque VarBind
                    {
                        // si les oid correspondent
                        if(vb.getObjectId().getObjectIdStringFormat().matches(oid)){
                            // on attribut sa valeur
                            vb.setObjectValue(oidValue.getBytes());
                            // matching réussi
                            index++;
                        }
                    }
                    // si le nombre de match est inférieure au nombre de VarBind
 /*                   if(index < payload.getVarBindingsList().size())
                    {   // petit problèmes à ce niveau: vérifier comment marche le errorindex
                        payload.setErrorStatus(2);
                        payload.setErrorIndex(index+1); // variable suivant le dernier matching
                    } */
                    // fermeture scanner
                    scanner.close();
                    // s'il n'y a aucun match, on générera une erreur.
                }
            }       
            br.close(); 
        }		
        catch (Exception e){
            // Erreur renvoyé dans le SNMP Message
                payload.setErrorStatus(5);   // erreur non générique
            //
            System.err.println("[AgentMIB]: ERROR --> "+e.toString());
        }
        return payload;
    }

    /**
     * Cette fonction  permet de définir la valeur des oid
     * présents dans les messages SNMP, dans la MIB de l'agent.
     * 
     * @param payload payload contenant les oid à set ainsi que leur valeur
     * @return la payload qui pourra être utilisé dans un message GET.RES
     */
    public SNMPMessagePayload setOidValue(SNMPMessagePayload payload) {
        /*
        try{ 
            // ouverture du fichier mib en écriture et en lecture
            BufferedReader br      = new BufferedReader(new InputStreamReader(new FileInputStream(this.mibFile)));
            BufferedWriter bw      = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.mibFile)));
            
            String ligne;
            //
            // FORMAT D'UNE LIGNE DANS UN FICHIER MIB
            // X.X.X.X.X.X.X.X VALUE
            while ((ligne=br.readLine())!=null){ // lecture lgine par ligne
                System.out.println(ligne);
                if(ligne.charAt(0) == '#'){      // si ligne de commentaire
                    // Ignoré
                    // on réécrit la ligne dans le fichier
                    bw.write(ligne);
                }else{                           // sinon on analyse la ligne
                    // 
                    Scanner scanner = new Scanner(ligne);
                    //
                    String oid = scanner.next();
                    // on concatène tous ce qui suit l'oid dans le fichier
                    String oidValue = "";
                    while(scanner.hasNext()) oidValue += " " + scanner.next();
                    // On vérifie la présence des varBind dans le fichier.
                    for(VarBind vb: payload.getVarBindingsList())              // comparaison avec chaque VarBind
                    {
                        // attention risque de duplication.
                        // si les oid correspondent
                        if(vb.getObjectId().getObjectIdStringFormat().matches(oid)){
                            // on écrit sa nouvelle valeur sur la ligne
                            bw.write(oid +" "+vb.getObjectId().getObjectIdStringFormat());
                        }else{ // si les oid ne correspondent pasangamma
                            // on réécrit la ligne sans modification
                            bw.write(ligne);
                        }
                    }
                    // fermeture scanner
                    scanner.close();
                }
            } 
            
            // fermeture du fichiers (enregistrement)
            br.close(); bw.flush(); bw.close();
        }		
        catch (Exception e){
            // Erreur renvoyé dans le SNMP Message
            payload.setErrorStatus(5);   // erreur non générique
            //
            System.err.println("[AgentMIB]: ERROR --> "+e.toString());
        } */
        payload.setErrorStatus(4);
        payload.setErrorIndex(1);
        return payload;
    }

    /**
     * Cette fonction permet d'obtenir la valeur suivante l'oid
     * présent dans la payload d'un message SNMP.
     * 
     * @param payload payload contenant la requête getNext
     * @return payload contenant les oid et leur valeurs
     */
    public SNMPMessagePayload getNextOidValue(SNMPMessagePayload payload) {
       // on fait un get de la valeur suivant
       
       // Pour chaque Varbind dans la payload, on incrémente la valeur de l'oid
       // exemple: 1.3.6.1.2.1.1.6.0 devient  1.3.6.1.2.1.1.7.0
       byte[] oid;

       for(VarBind vb:payload.getVarBindingsList()){
           //
           oid = vb.getObjectId().getObjectId();
           // On incrémente l'avant dernière valeur
           oid[oid.length - 2] += 1;
           // on reattribut l'oid à la payload
           vb.setObjectId(new OID(oid));
       }
       // get des valeurs
       return this.getOidValue(payload);
       
    }
    
}
