package com.davixavier.database;

import java.lang.reflect.Field;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

public enum DBOperationType
{
	INSERT(), DELETE(), SELECT(), UPDATE();
	
	public static DBOperationType fromString(String string)
	{
		for (int i = 0; i < DBOperationType.values().length; i++)
		{
			if (DBOperationType.class.getFields()[i].getName().toLowerCase().equals(string.toLowerCase()))
				return DBOperationType.values()[i];
		}
		
		return SELECT;                                
	}
}
