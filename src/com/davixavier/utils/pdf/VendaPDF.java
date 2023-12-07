package com.davixavier.utils.pdf;

import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;

public class VendaPDF
{
	private PDFCreator pdfCreator;
	private ProdutoVendaPDFTable table;
	private Document document;
	
	public VendaPDF(String path, ProdutoVendaPDFTable table)
	{
		pdfCreator = new PDFCreator(path);
		pdfCreator.openDocument();
		document = pdfCreator.getDocument();
		
		this.table = table;
		
		placeText("Pedido nº_____", 7, 40, 760);
		placeText("Fornecedor ____________________________________________________________", 7, 100, 760);
		placeText("Contato _____________________________________", 7, 380, 760);
		
		int clienteY = 735;
		placeText("Cliente ________________________________________________________________", 7, 40, clienteY);
		placeText("Tel.1 _______________", 7, 322, clienteY);
		placeText("Tel.2 _______________", 7, 408, clienteY);
		
		int endereçoY = 720;
		placeText("Endereço _____________________________________________________________________________________________________________", 7, 40, endereçoY);
		placeText("Núm. _______", 7, 504, endereçoY);
		
		PDFUtils.roundRectangle(30, 711, 530, 67, 3, BaseColor.BLACK, pdfCreator.getWriter().getDirectContent());
		PDFUtils.placeRectangle(30, 751, 530, 0, BaseColor.BLACK, pdfCreator.getWriter().getDirectContent());
		
		placeTable(table, 40, 700);
	}
	
	public void placeTable(ProdutoVendaPDFTable table, int x, int y)
	{
		PdfContentByte cb = pdfCreator.getWriter().getDirectContent();
		
		table.writeSelectedRows(0, -1, x, y, cb);
	}
	
	public void placeText(String text, int fontSize, int x, int y)
	{
		PdfContentByte cb = pdfCreator.getWriter().getDirectContent();
		
		try 
		{
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			
			cb.saveState();
	        cb.beginText();
	        cb.setColorFill(new GrayColor(0.1f));
	        cb.moveText(x, y);
	        cb.setFontAndSize(bf, fontSize);
	        cb.showText(text);
	        cb.endText();
	        cb.restoreState();
		} 
		catch (DocumentException | IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void closeDocument()
	{
		pdfCreator.closeDocument();
	}

	public PDFCreator getPdfCreator() {
		return pdfCreator;
	}

	public void setPdfCreator(PDFCreator pdfCreator) {
		this.pdfCreator = pdfCreator;
	}

	public ProdutoVendaPDFTable getTable() {
		return table;
	}

	public void setTable(ProdutoVendaPDFTable table) {
		this.table = table;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
}
