package ch.heigvd.res.ldap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.Normalizer;
import java.util.regex.Pattern;
import java.util.LinkedList;

/** This program is meant to convert a csv file containing personal informations
 ** about People in a organization to a ldif file, which is easier to use in the 
 ** context of LDAP.
 ** Writtent By Bignens Julien & Brito Carvalho Bruno
 */

public class CSVtoLDIF {

	final static int NUMBEROFFIELDS = 8;
	final static File csvFile = new File("users.csv");
	
	private static void convertFile(File f) {
		
		BufferedReader br = null;
		PrintWriter pw = null;
		LinkedList<String> listOfDepartments = new LinkedList<String>();
		String[] infoPeople = new String[NUMBEROFFIELDS];
		
		try {
			br = new BufferedReader(new FileReader("users.csv"));
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("users.ldif"), "UTF-8"));
			
			pw.println("dn: dc=heigvd,dc=ch\ndc: heigvd\nobjectClass: domain\nobjectClass: top\n");
            
			pw.println("dn: ou=Departments,dc=heigvd,dc=ch\nobjectClass: organizationalunit\nobjectClass: top\nou: Departments\n"); 
			pw.println("dn: ou=People,dc=heigvd,dc=ch\nobjectClass: organizationalunit\nobjectClass: top\nou: People\n");
			
			String line;
			
			while((line = br.readLine()) != null) {
				
				infoPeople = line.split(", ");
				
				if(!testDepartment(infoPeople[6], listOfDepartments))
					listOfDepartments.add(infoPeople[6]);
				
                pw.println("dn: uid=" + infoPeople[0] + ",ou=people,dc=heigvd,dc=ch");
                pw.println("objectClass: top\nobjectClass: inetOrgPerson");
                pw.println("objectClass: top\nobjectClass: heigPeople");
                pw.println("uid: " + infoPeople[0]);
                pw.println("givenName: " + infoPeople[2]);
                pw.println("sn: " + infoPeople[1]);
                pw.println("cn: " + infoPeople[2] + "." + infoPeople[1]);
                pw.println("sexType: " + infoPeople[5]);
                pw.println("employeeType: " + infoPeople[7]);
                pw.println("departmentNumber: " + infoPeople[6]);
                pw.println("telephoneNumber: " + infoPeople[3]);
                pw.println("mail: " + deAccent(infoPeople[4]) + "\n");
			}
				
            // creation of department
            for (String s : listOfDepartments) {
                pw.println("dn: ou="+ s +", ou=Departments, dc=heigvd,dc=ch");
                pw.println("objectClass: organizationalunit\nobjectclass: top");
                pw.println("ou:" + s + "\n");
            }
			
			
    	} 
		catch (IOException ex) {
	        System.out.println("erreur 1");
		} finally {
			try {
				pw.close();
				br.close();
			} catch (IOException ex) {
				System.out.println("error while closing");
			}
		}
	}
	
	public static void main(String[] args){
		
		convertFile(csvFile);
	}
	
    private static boolean testDepartment (String s, LinkedList<String> l) {
        
        for (String str : l) {
            if(str.equals(s))
                return true;
        }
        
        return false;
        
    }
    
    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }
	
}
