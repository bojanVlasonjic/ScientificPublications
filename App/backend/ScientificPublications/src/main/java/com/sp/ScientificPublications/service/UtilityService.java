package com.sp.ScientificPublications.service;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class UtilityService {
	
	public HttpHeaders getDownloadHeaders() {
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
    	return headers;
    }
	
}
