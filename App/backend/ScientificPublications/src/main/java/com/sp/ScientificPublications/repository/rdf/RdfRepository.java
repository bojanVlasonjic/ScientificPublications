package com.sp.ScientificPublications.repository.rdf;

import com.sp.ScientificPublications.models.MetadataStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Repository
public class RdfRepository {

    @Autowired
    private MetadataExtractor metadataExtractor;

    public void saveMetadataFromXml(String xmlContent) {
        try {
            InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            metadataExtractor.extractMetadata(inputStream, outputStream);
            System.out.println(outputStream.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
