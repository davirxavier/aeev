package com.davixavier.utils.pdf;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.davixavier.application.Loja;
import com.davixavier.application.Main;
import com.davixavier.application.img.IconsPath;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;

public class EstoquePDF
{
	private PDFCreator pdfCreator;
	private ProdutoPDFTable table;
	private Document document;
	
	public EstoquePDF(String path, ProdutoPDFTable table) 
	{
		pdfCreator = new PDFCreator(path);
		pdfCreator.openDocument();
		document = pdfCreator.getDocument();
		
		this.table = table;
		
		try 
		{
			PDFUtils.placeImg(30, 770, 60, 60,
					Image.getInstance(getClass().getResource(IconsPath.ICON.getPath())), pdfCreator.getWriter().getDirectContent());
		}
		catch (BadElementException | IOException e) 
		{
			e.printStackTrace();
		}
		
		PDFUtils.placeText(Main.TITLE, 10, 100, 810, pdfCreator.getWriter().getDirectContent());
		PDFUtils.placeText("Relatório de estoque", 9, 100, 785, pdfCreator.getWriter().getDirectContent());
		PDFUtils.placeText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/YYYY")), 9, 100, 775, pdfCreator.getWriter().getDirectContent());
		
		PDFUtils.placeTable(table, 40, 750, pdfCreator.getWriter().getDirectContent());
	}
	
	public void closeDocument()
	{
		pdfCreator.closeDocument();
	}
}
