package com.sp.ScientificPublications.models;

import java.util.Properties;

public class FusekiConnectionProperties {
	public String endpoint;
	public String dataset;
	public String datasetType;
	
	public String queryEndpoint;
	public String updateEndpoint;
	public String dataEndpoint;

	public FusekiConnectionProperties(Properties props) {
		super();
		dataset = props.getProperty("conn.dataset").trim();
		endpoint = props.getProperty("conn.endpoint").trim();
		datasetType = props.getProperty("conn.datasetType").trim();
		
		queryEndpoint = String.join("/", endpoint, dataset, props.getProperty("conn.query").trim());
		updateEndpoint = String.join("/", endpoint, dataset, props.getProperty("conn.update").trim());
		dataEndpoint = String.join("/", endpoint, dataset, props.getProperty("conn.data").trim());
		
		System.out.println("[INFO] Parsing connection properties:");
		System.out.println("[INFO] Query endpoint: " + queryEndpoint);
		System.out.println("[INFO] Update endpoint: " + updateEndpoint);
		System.out.println("[INFO] Graph store endpoint: " + dataEndpoint);
	}
}
