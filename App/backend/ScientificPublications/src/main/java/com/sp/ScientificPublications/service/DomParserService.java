package com.sp.ScientificPublications.service;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.exception.ApiNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;


@Service
public class DomParserService {

    @Autowired
    DocumentBuilderFactory dbFactory;

    @Autowired
    SchemaFactory schemaFactory;

    // this we probably won't need
    /*
    public boolean parseXmlDocument(DocumentDTO documentDTO) {

        try {

            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();

            InputSource is = new InputSource(new StringReader(documentDTO.getDocumentContent()));
            Document document = docBuilder.parse(is);

            document.getDocumentElement().normalize();
            System.out.println("Root element :" + document.getDocumentElement().getNodeName());

            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;

    }*/


    public boolean validateXmlDocument(String fileContent, String schemaPath) {

        if(fileContent == null || schemaPath == null) {
            return false;
        }

        File schemaFile = new File(schemaPath);
        Source xmlFile = new StreamSource(new StringReader(fileContent));

        try {
            Schema schema = schemaFactory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            validator.validate(xmlFile);
            System.out.println("Document is valid");

            return true;

        } catch (SAXException e) {
            System.out.println("Document not valid: " + e);
            throw new ApiNotValidException(e.getMessage(), ((SAXParseException)e).getLineNumber(),
                    ((SAXParseException)e).getColumnNumber());
        } catch (IOException e) {
           System.out.println("Failed to find the xml document");
        }

        return false;
    }
    
    public String readMultipartXMLFile(MultipartFile file) {
    	String data = "";
    	if (!file.isEmpty()) {
    		byte[] bytes;
			try {
				bytes = file.getBytes();
				data = new String(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
    		
    	}
    	return data;
    }

    public String readXmlFile(String filePath) throws IOException {

        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        return new String(bytes);
    }

}
