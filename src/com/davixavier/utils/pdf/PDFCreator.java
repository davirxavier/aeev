package com.davixavier.utils.pdf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFCreator 
{
	private Document document;
	private String path;
	private PdfWriter writer;
	
	public PDFCreator(String path) 
	{
		document = new Document(PageSize.A4, 0f, 0f, 30f, 30f);
		
		this.path = path;
		try 
		{
			writer = PdfWriter.getInstance(document, new FileOutputStream(path));
		} 
		catch (FileNotFoundException | DocumentException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void openDocument()
	{
		document.open();
	}
	
	public void closeDocument()
	{
		document.close();
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public PdfWriter getWriter() {
		return writer;
	}
}
