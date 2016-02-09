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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import stri.ss_manager.SNMP.smi.OID;

/**
 *
 * @author Lorrain BALBIANI - Farid EL JAMAL - Manavai TEIKITUHAAHAA
 */
public class SNMPAgentMib {
    // attributs
    
    String mibFile = "./mib/SNMPAgent.mib";
    
    // méthodes
    byte[] getOidValue(OID objectId) {
       /* try{ 
            BufferedReader br=new BufferedReader(
                                new InputStreamReader(
                                    new FileInputStream(this.mibFile)));
            String ligne;
            //
            Pattern p = Pattern.compile("(\\w+)");
            //
            while ((ligne=br.readLine())!=null){ // lecture lgine par ligne
                System.out.println(ligne);
            }       
            br.close(); 
        }		
        catch (Exception e){
                System.out.println(e.toString());
        }	*/
        
        
        //
        switch(objectId.toString()){
            default:
                break;
        }
        
        return null;
    }

    byte[] setOidValue(OID objectId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    byte[] getNextOidValue(OID objectId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
