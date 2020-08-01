package com.personal.page.service;

import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class PdfServiceImp  {

	private static final String PDF_RESOURCES = "/pdf-resources/";
	private UserAccountService userAccountService;
	private SpringTemplateEngine templateEngine;

	@Autowired
	public PdfServiceImp(UserAccountService userAccountService, SpringTemplateEngine templateEngine) {
		super();
		this.userAccountService = userAccountService;
		this.templateEngine = templateEngine;
	}

    public File generatePdf() throws IOException, DocumentException {
        Context context = getContext();
        String html = loadAndFillTemplate(context);
        return renderPdf(html);
    }

	private File renderPdf(String html) throws IOException, DocumentException {
		    File file = File.createTempFile("userAccounts", ".pdf");
	        OutputStream outputStream = new FileOutputStream(file);
	        ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
	        renderer.setDocumentFromString(html, new ClassPathResource(PDF_RESOURCES).getURL().toExternalForm());
	        renderer.layout();
	        renderer.createPDF(outputStream);
	        outputStream.close();
	        file.deleteOnExit();
	        return file;
	}

	private Context getContext() {
		Context context = new Context();
		context.setVariable("userAccounts", userAccountService.getAllUserAccounts());
		return context;
	}

	private String loadAndFillTemplate(Context context) {
		return templateEngine.process("pdf_userAccounts", context);
	}

}
