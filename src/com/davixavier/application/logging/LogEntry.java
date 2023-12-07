package com.davixavier.application.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

public class LogEntry 
{
	private String className;
	private String methodName;
	private String entry;
	private LocalDateTime dateTime;
	private Level level;
	
	public LogEntry() 
	{
		className = "";
		methodName = "";
		entry = "";
		dateTime = LocalDateTime.MIN;
		level = Level.INFO;
	}
	
	public String toStringNoHeader()
	{
		return level.getName() + ": " + dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS")) + " - " + entry;
	}
	
	public String toString()
	{
		return className + " - " + methodName + "\n"
			+ level.getName() + ": " + dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS")) + " - " + entry;
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getEntry() {
		return entry;
	}
	public void setEntry(String entry) {
		this.entry = entry;
	}
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}
}
