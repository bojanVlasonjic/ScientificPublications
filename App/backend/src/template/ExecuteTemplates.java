package template;

import java.util.HashMap;

import util.AuthenticationUtilities;
import util.AuthenticationUtilities.ConnectionProperties;

public class ExecuteTemplates {
	
	private static ConnectionProperties conn;

	public static void main(String[] args) throws Exception {
		
		conn = AuthenticationUtilities.loadProperties();
		HashMap<String, String> fileMap = new HashMap<String, String>();
		
		fileMap.put("document-id", "cover-letter");
    	fileMap.put("file-path", "data/instances/cover-letter-instance1.xml");
    	StoreSampleFile.run(conn, fileMap);
    	
    	
    	fileMap.put("document-id", "scientific-paper");
    	fileMap.put("file-path", "data/instances/scientific-paper-instance1.xml");
    	StoreSampleFile.run(conn, fileMap);
	}
	
}
