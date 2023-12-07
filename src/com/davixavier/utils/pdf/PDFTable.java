package com.davixavier.utils.pdf;

import java.util.List;

import com.itextpdf.text.pdf.PdfPTable;

public abstract class PDFTable<T extends PDFRow<S>, S> extends PdfPTable
{
	public PDFTable(int numrows)
	{
		super(numrows);
	}
	
	public abstract void addRow(T row);
	public abstract void addRows(List<S> list);
}
