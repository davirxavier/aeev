package com.davixavier.utils.pdf;

import java.util.List;

import com.davixavier.entidades.compras.ProdutoVenda;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class ProdutoVendaPDFTable extends PDFTable<ProdutoVendaPDFTRow, ProdutoVenda>
{	
	public ProdutoVendaPDFTable() 
	{
		super(6);

		setTotalWidth(515);
		
		CustomPDFTCell nomeCell = new CustomPDFTCell("Descri��o");
		CustomPDFTCell idCell = new CustomPDFTCell("C�digo");
		CustomPDFTCell pre�oCell = new CustomPDFTCell("Unit�rio(R$)");
		CustomPDFTCell quantidadeCell = new CustomPDFTCell("Quantidade");
		CustomPDFTCell itemnumeroCell = new CustomPDFTCell("Item");
		CustomPDFTCell totalCell = new CustomPDFTCell("Total");
		
		idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		nomeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pre�oCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		itemnumeroCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		quantidadeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		nomeCell.setVerticalAlignment(Element.ALIGN_CENTER);
		idCell.setVerticalAlignment(Element.ALIGN_CENTER);
		pre�oCell.setVerticalAlignment(Element.ALIGN_CENTER);
		itemnumeroCell.setVerticalAlignment(Element.ALIGN_CENTER);
		quantidadeCell.setVerticalAlignment(Element.ALIGN_CENTER);
		
		nomeCell.setBackgroundColor(new GrayColor(0.85f));
		idCell.setBackgroundColor(new GrayColor(0.85f));
		pre�oCell.setBackgroundColor(new GrayColor(0.85f));
		itemnumeroCell.setBackgroundColor(new GrayColor(0.85f));
		quantidadeCell.setBackgroundColor(new GrayColor(0.85f));
		totalCell.setBackgroundColor(new GrayColor(0.85f));
		
		addCell(itemnumeroCell);
		addCell(quantidadeCell);
		addCell(idCell);
		addCell(nomeCell);
		addCell(pre�oCell);
		addCell(totalCell);
		
		float[] widths = {0.5f, 1f, 1f, 4f, 1f, 1f};
		try 
		{
			setWidths(widths);
		} catch (DocumentException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void addRow(ProdutoVendaPDFTRow row)
	{
		addCell(new CustomPDFTCell("" + getRows().size()));
		addCell(row.getQuantidadeCell());
		addCell(row.getIdCell());
		addCell(row.getNomeCell());
		addCell(row.getPre�oCell());
		addCell(row.getTotalCell());
	}
	
	@Override
	public void addRows(List<ProdutoVenda> produtoVendas) 
	{
		produtoVendas.forEach(p ->
		{
			ProdutoVendaPDFTRow row = new ProdutoVendaPDFTRow(p);
			addRow(row);
		});
	}
}	
