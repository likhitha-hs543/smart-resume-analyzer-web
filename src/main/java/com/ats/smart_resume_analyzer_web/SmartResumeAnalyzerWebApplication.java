package com.ats.smart_resume_analyzer_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ats")
public class SmartResumeAnalyzerWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartResumeAnalyzerWebApplication.class, args);
	}

}
