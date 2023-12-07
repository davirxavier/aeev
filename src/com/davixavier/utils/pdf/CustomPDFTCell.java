package com.davixavier.utils.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

public class CustomPDFTCell extends PdfPCell
{
	private Paragraph paragraph;
	
	public CustomPDFTCell(String text) 
	{
		super(new Paragraph(new Phrase(10f, text, FontFactory.getFont(FontFactory.COURIER, 6f)))); 
		
		setBorderColor(BaseColor.GRAY);
		setBorderWidth(0.1f);
	}
}
