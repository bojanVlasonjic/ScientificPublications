package com.sp.ScientificPublications.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SendEmailDTO {
	private String to;
	private String subject;
	private String message;
	private String pdfGUID;
	private String htmlGUID;
}
