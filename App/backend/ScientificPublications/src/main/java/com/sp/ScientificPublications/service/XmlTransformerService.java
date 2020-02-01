package com.sp.ScientificPublications.service;

import com.sp.ScientificPublications.dto.DocumentDTO;
import com.sp.ScientificPublications.exception.ApiBadRequestException;
import org.apache.commons.io.FilenameUtils;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

@Service
public class XmlTransformerService {

    @Autowired
    private FopFactory fopFactory; // used for pdf transformations

    @Autowired
    DocumentBuilderFactory docBuilderFactory; // used for html transformations

    @Autowired
    private TransformerFactory transformerFactory;


    private static final String outputDirectory = "src/main/resources/output/";


    /** Transforms .xml file to .pdf, using the .xsl file. Each file type has it's matching .xsl file.
     * @param document - a dto containing the document id and it's content
     * @param xslFilePath - path to the xsl file that fits the document type
     * @return path to the transformed pdf file (for now) */
    public String generatePdfFromXml(DocumentDTO document, String xslFilePath) {

        ByteArrayOutputStream outputStream;

        // transformation using fop and xsl file
        try {
            outputStream = this.pdfFopProcessing(document.getDocumentContent(), xslFilePath);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ApiBadRequestException("Failed to transform the xml file to a pdf file");
        }

        File pdfFile = this.createPdfFile(document.getDocumentId());

        // generating pdf file and it's content to the output folder
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(pdfFile));
            out.write(outputStream.toByteArray());
            out.close();
        } catch(IOException ex) {
            ex.printStackTrace();
            throw new ApiBadRequestException("Failed to generate the pdf file");
        }

        return pdfFile.getPath();
    }


    private ByteArrayOutputStream pdfFopProcessing(String documentContent, String xslFilePath) throws Exception {

        /* Prepping transformation data */
        File xslFile  = new File(xslFilePath);

        StreamSource transformSrc = new StreamSource(xslFile); // creating transformation source
        StreamSource source = new StreamSource(new StringReader(documentContent)); // init transformation subject

        FOUserAgent userAgent = fopFactory.newFOUserAgent(); // init user agent required for transformation
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); // output stream used to store results
        Transformer xslFoTransformer = transformerFactory.newTransformer(transformSrc);

        // fop instance with desired output format
        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, outputStream);
        Result result = new SAXResult(fop.getDefaultHandler()); // resulting sax events

        /* Starting transformation and fop processing */
        xslFoTransformer.transform(source, result);

        return outputStream;

    }

    private File createPdfFile(String documentId) {

        // generating pdf file name based on the input file name
        String outputFilePath = outputDirectory + "pdf/" + documentId + ".pdf";

        File pdfFile = new File(outputFilePath);
        if(!pdfFile.getParentFile().exists()) {
            pdfFile.getParentFile().mkdir();
        }

        return pdfFile;

    }


    /** returns path to html file for now */
    public String generateHtmlFromXml(DocumentDTO document, String xslFilePath) {

        try {

            Transformer transformer = generateTransformer(xslFilePath);

            // transform dom to html
            DOMSource source = new DOMSource(buildDocument(document.getDocumentContent()));
            String outputPath = outputDirectory + "html/" + document.getDocumentId() + ".html";
            createHtmlOutputDirectory(outputPath);
            
            FileOutputStream fileOutputStream = new FileOutputStream(outputPath); 
            StreamResult result = new StreamResult(fileOutputStream);
            transformer.transform(source, result);
            try {
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
            
            return outputPath;

        } catch (FileNotFoundException | TransformerException e) {
            e.printStackTrace();
            throw new ApiBadRequestException("Failed to generate html file");
        }

    }

    private void createHtmlOutputDirectory(String filePath) {

        File htmlFile = new File(filePath);

        if(!htmlFile.getParentFile().exists()) {
            htmlFile.getParentFile().mkdir();
        }

    }

    private Transformer generateTransformer(String xslFilePath) {

        StreamSource transformSrc = new StreamSource(new File(xslFilePath));
        
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer(transformSrc);
            transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xhtml");
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            throw new ApiBadRequestException("Failed to transform document to html file");
        }
        
        
        return transformer;

    }


    private Document buildDocument(String documentContent) {

        Document doc = null;

        try {
        	docBuilderFactory.setValidating(false);
            DocumentBuilder builder = docBuilderFactory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(documentContent)));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        if(doc == null) {
            throw new ApiBadRequestException("Failed to build html");
        }

        return doc;

    }

}
