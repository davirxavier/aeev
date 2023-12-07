package com.davixavier.utils.pdf;

import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPRow;

public class PDFUtils 
{
	public static void placeImg(int x, int y, int width, int height, Image image, PdfContentByte cb)
	{
		cb.saveState();
		image.setAbsolutePosition(x, y);
		image.scaleAbsolute(width, height);
		try 
		{
			cb.addImage(image);
		} 
		catch (DocumentException e) {
			e.printStackTrace();
		}
		cb.restoreState();
	}
	
	public static void placeRectangle(int x, int y, int width, int height, BaseColor color, PdfContentByte cb)
	{
		cb.saveState();
		cb.setColorStroke(color);
		cb.setLineWidth(1f);
		cb.rectangle(x, y, width, height);
		cb.stroke();
		cb.restoreState();
	}
	
	public static void roundRectangle(int x, int y, int width, int height, Integer radius, BaseColor color, PdfContentByte cb)
	{
		cb.saveState();
		cb.setColorStroke(color);
		cb.setLineWidth(1f);
		cb.roundRectangle(x, y, width, height, radius);
		cb.stroke();
		cb.restoreState();
	}
	
	public static <T extends PDFRow<S>, S> void placeTable(PDFTable<T, S> table, int x, int y, PdfContentByte cb)
	{
		boolean bigger = false;
		
		float hdiff = cb.getPdfDocument().getPageSize().getHeight() - y;
		float pageheight = cb.getPdfDocument().getPageSize().getHeight();
		
		int rowcount = 0;
		if (table.calculateHeights() > pageheight - hdiff)
		{
			bigger = true;
			
			float rowh = hdiff;
			
			for(PdfPRow row : table.getRows())
			{
				rowh += row.getMaxHeights();
				rowcount++;
				
				if (rowh >= pageheight - 30)
				{
					rowcount--;
					break;
				}
			}
		}
		
		if (bigger)
		{
			table.writeSelectedRows(0, rowcount, x, y, cb);
			
			cb.getPdfDocument().newPage();
			
			table.writeSelectedRows(rowcount, -1, x, pageheight - 30, cb);
		}
		else 
		{
			table.writeSelectedRows(0, -1, x, y, cb);
		}
	}
	
	public static void placeText(String text, int fontSize, int x, int y, PdfContentByte cb)
	{
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
}
