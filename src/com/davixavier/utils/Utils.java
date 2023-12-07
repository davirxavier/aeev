package com.davixavier.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.davixavier.entidades.estoque.Produto;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Window;
import javafx.util.Callback;

public class Utils
{
	public static String getStackTraceString(Exception ex)
	{
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
	
	public static String getCallerMethodName(int level)
	{
		return Thread.currentThread().getStackTrace()[level].getMethodName();
	}
	
	public static String getCallerCallerClassName() 
	{ 
	    StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
	    String callerClassName = null;
	    for (int i=1; i<stElements.length; i++)
	    {
	        StackTraceElement ste = stElements[i];
	        if (!ste.getClassName().equals(Utils.class.getName())&& ste.getClassName().indexOf("java.lang.Thread")!=0) 
	        {
	            if (callerClassName==null)
	            {
	                callerClassName = ste.getClassName();
	            } 
	            else if (!callerClassName.equals(ste.getClassName()))
	            {
	                return ste.getClassName();
	            }
	        }
	    }
	    return null;
	 }
	
	public static String removerAcentos(String string)
	{
		string = Normalizer.normalize(string, Normalizer.Form.NFKD);
		string = string.replaceAll("\\p{M}", "");
		
		return string;
	}
	
	public static boolean checkConnection()
	{
		Socket socket = new Socket();
		try
		{
			socket.connect(new InetSocketAddress("www.dropbox.com", 80), 100);
			socket.close();
			
			return true;
		} 
		catch (IOException e)
		{
			//e.printStackTrace();
			return false;
		}
	}
	
	public static void applyPhoneFormatter(TextField field)
	{
		TextFormatter<String> formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change ->
		{
		    if (!change.isContentChange() &&
		        !change.getControlNewText().isEmpty())
		    {

		        return change;
		    }

		    String oldText = change.getControlText();
		    String text = change.getControlNewText();
		    int start = change.getRangeStart();
		    int end = change.getRangeEnd();

		    int anchor = change.getAnchor();
		    int caret = change.getCaretPosition();
		    
		    for (int i = 0; i < text.length(); i++)
		    {
		    	if (Character.toString(text.charAt(i)).matches("[^\\d\\-\\ \\(\\)]"))
		    	{
		    		text = oldText;
		    		break;
		    	}
		    }
		    
		    StringBuilder newText = new StringBuilder(text);
		    
		    int dash;
		    while ((dash = newText.lastIndexOf("-")) >= start || (dash = newText.lastIndexOf("(")) >= start 
		    		|| (dash = newText.lastIndexOf(")")) >= start) 
		    {
		        newText.deleteCharAt(dash);
		        if (caret > dash) 
		        {
		            caret--;
		        }
		        if (anchor > dash) 
		        {
		            anchor--;
		        }
		    }
		    
		    if (newText.length() == 0 || newText.charAt(0) != '(')
		    {
		    	newText.insert(0, "(");
		    	
		    	if (caret > 0 || (caret == 0 && end <= 0 && change.isDeleted())) 
		    	{
		            caret++;
		        }
		        if (anchor > 0 || (anchor == 0 && end <= 0 && change.isDeleted())) 
		        {
		            anchor++;
		        }
		    }
		    
		    while (newText.length() < 3)
		    {
		    	newText.append(" ");
		    }
		    
		    if (newText.length() == 3 || newText.charAt(3) != ')')
		    {
		    	newText.insert(3, ")");
		    	
		    	if (caret > 3 || (caret == 3 && end <= 3 && change.isDeleted())) 
		    	{
		            caret++;
		        }
		        if (anchor > 3 || (anchor == 3 && end <= 3 && change.isDeleted())) 
		        {
		            anchor++;
		        }
		    }
		    
		    while (newText.length() < 9)
		    {
		    	newText.append(" ");
		    }
		    
		    if (newText.length() == 9 || newText.charAt(9) != '-')
		    {
		    	newText.insert(9, "-");
		    	
		    	if (caret > 9 || (caret == 9 && end <= 9 && change.isDeleted()))
		    	{
		            caret++;
		        }
		        if (anchor > 9 || (anchor == 9 && end <= 9 && change.isDeleted()))
		        {
		            anchor++;
		        }
		    }
		    
//		    while (newText.length() < 14)
//		    {
//		    	newText.append(" ");
//		    }

		    if (newText.length() > 14)
		    {
		        newText.delete(14, newText.length());
		    }
		    
		    int lastNumber = text.indexOf(" ");
		    int lastNumberOld = oldText.indexOf(" ");
		    int newPos = 0;
		    if (caret != lastNumber && caret > lastNumber)
		    {
		    	newPos = lastNumber;
		    }
		    else if (caret != lastNumber && caret < lastNumber)
		    {
		    	newPos = caret;
		    }
		    else if (caret == lastNumber)
		    {
		    	newPos = caret;
		    }
		    
		    int lastDigit = Utils.lastIndexOf(newText.toString(), "\\d");
		    lastDigit = (lastDigit < 0) ? 0 : lastDigit;
		    
		    if (lastDigit > 0)
		    {
		    	if (newText.charAt(lastDigit-1) == ' ' 
		    			|| (lastDigit > 1 
		    					&& (newText.charAt(lastDigit-1) == '-' || newText.charAt(lastDigit-1) == '(' || newText.charAt(lastDigit-1) == ')') 
		    					&&  newText.charAt(lastDigit-2) == ' '))
			    {
			    	newText.replace(newText.indexOf(" "), newText.indexOf(" ")+1, newText.charAt(lastDigit) + "");
			    	newText.replace(lastDigit, lastDigit+1, " ");
			    	
			    	lastDigit = Utils.lastIndexOf(newText.toString(), "\\d");
			    	newPos = lastDigit+1;
			    }
		    }
		    
		    if (lastNumber < 0)
		    	newPos = caret;
		    
		    if (newPos < 0)
		    	newPos = 0;
		    
		    text = newText.toString();
		    anchor = Math.min(newPos, 14);
		    caret = Math.min(newPos, 14);

		    if (anchor > text.length())
		    	anchor = text.length()-1;
		    
		    if (caret > text.length())
		    		caret = text.length()-1;
		    
		    change.setText(text);
		    
		    change.setAnchor(anchor);
		    change.setCaretPosition(caret);
		    
		    change.setRange(0, change.getControlText().length());

		    return change;
		});
		
		field.setTextFormatter(formatter);
	}
	
	public static void cpfFormatter(TextField field)
	{
		TextFormatter<String> formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change ->
		{
		    if (!change.isContentChange() &&
		        !change.getControlNewText().isEmpty())
		    {

		        return change;
		    }

		    String oldText = change.getControlText();
		    String text = change.getControlNewText();
		    int start = change.getRangeStart();
		    int end = change.getRangeEnd();

		    int anchor = change.getAnchor();
		    int caret = change.getCaretPosition();
		    
		    for (int i = 0; i < text.length(); i++)
		    {
		    	if (Character.toString(text.charAt(i)).matches("[^\\d\\-\\.\\ ]"))
		    	{
		    		text = oldText;
		    		break;
		    	}
		    }
		    
		    StringBuilder newText = new StringBuilder(text);
		    
		    int dash;
		    while ((dash = newText.lastIndexOf("-")) >= start || (dash = newText.lastIndexOf(".")) >= start) 
		    {
		        newText.deleteCharAt(dash);
		        if (caret > dash) 
		        {
		            caret--;
		        }
		        if (anchor > dash) 
		        {
		            anchor--;
		        }
		    }
		    
		    while (newText.length() < 3)
		    {
		    	newText.append(" ");
		    }
		    
		    if (newText.length() == 3 || newText.charAt(3) != '.')
		    {
		    	newText.insert(3, ".");
		    	
		    	if (caret > 3 || (caret == 3 && end <= 3 && change.isDeleted())) 
		    	{
		            caret++;
		        }
		        if (anchor > 3 || (anchor == 3 && end <= 3 && change.isDeleted())) 
		        {
		            anchor++;
		        }
		    }
		    
		    while (newText.length() < 7)
		    {
		    	newText.append(" ");
		    }
		    
		    if (newText.length() == 7 || newText.charAt(7) != '.')
		    {
		    	newText.insert(7, ".");
		    	
		    	if (caret > 7 || (caret == 7 && end <= 7 && change.isDeleted()))
		    	{
		            caret++;
		        }
		        if (anchor > 7 || (anchor == 7 && end <= 7 && change.isDeleted()))
		        {
		            anchor++;
		        }
		    }
		    
		    while (newText.length() < 11)
		    {
		    	newText.append(" ");
		    }
		    
		    if (newText.length() == 11 || newText.charAt(11) != '-')
		    {
		    	newText.insert(11, "-");
		    	
		    	if (caret > 11 || (caret == 11 && end <= 11 && change.isDeleted()))
		    	{
		            caret++;
		        }
		        if (anchor > 11 || (anchor == 11 && end <= 11 && change.isDeleted()))
		        {
		            anchor++;
		        }
		    }

		    if (newText.length() > 14)
		    {
		        newText.delete(14, newText.length());
		    }
		    
		    int lastNumber = text.indexOf(" ");
		    int lastNumberOld = oldText.indexOf(" ");
		    int newPos = 0;
		    if (caret != lastNumber && caret > lastNumber)
		    {
		    	newPos = lastNumber;
		    }
		    else if (caret != lastNumber && caret < lastNumber)
		    {
		    	newPos = caret;
		    }
		    else if (caret == lastNumber)
		    {
		    	newPos = caret;
		    }
		    
		    if (lastNumber < 0)
		    	newPos = caret;
		    
		    if (newPos < 0)
		    	newPos = 0;
		    
		    int lastDigit = Utils.lastIndexOf(newText.toString(), "\\d");
		    lastDigit = (lastDigit < 0) ? 0 : lastDigit;
		    
		    if (lastDigit > 0)
		    {
		    	if (newText.charAt(lastDigit-1) == ' ' 
		    			|| (lastDigit > 1 
		    					&& (newText.charAt(lastDigit-1) == '-' || newText.charAt(lastDigit-1) == '.') 
		    					&& newText.charAt(lastDigit-2) == ' '))
			    {
			    	newText.replace(newText.indexOf(" "), newText.indexOf(" ")+1, newText.charAt(lastDigit) + "");
			    	newText.replace(lastDigit, lastDigit+1, " ");
			    	
			    	lastDigit = Utils.lastIndexOf(newText.toString(), "\\d");
			    	newPos = lastDigit+1;
			    }
		    }
		    
		    text = newText.toString();
		    anchor = Math.min(newPos, 14);
		    caret = Math.min(newPos, 14);
		    
		    change.setText(text);
		    
		    change.setAnchor(anchor);
		    change.setCaretPosition(caret);
		    
		    change.setRange(0, change.getControlText().length());

		    return change;
		});
		
		field.setTextFormatter(formatter);
	}
	
	public static void cnpjFormatter(TextField field)
	{
		TextFormatter<String> formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change ->
		{
		    if (!change.isContentChange() &&
		        !change.getControlNewText().isEmpty())
		    {

		        return change;
		    }

		    String oldText = change.getControlText();
		    String text = change.getControlNewText();
		    int start = change.getRangeStart();
		    int end = change.getRangeEnd();

		    int anchor = change.getAnchor();
		    int caret = change.getCaretPosition();
		    
		    for (int i = 0; i < text.length(); i++)
		    {
		    	if (Character.toString(text.charAt(i)).matches("[^\\d\\-\\.\\ \\/]"))
		    	{
		    		text = oldText;
		    		break;
		    	}
		    }
		    
		    StringBuilder newText = new StringBuilder(text);
		    
		    int dash;
		    while ((dash = newText.lastIndexOf("-")) >= start 
		    		|| (dash = newText.lastIndexOf(".")) >= start
		    		|| (dash = newText.lastIndexOf("/")) >= start) 
		    {
		        newText.deleteCharAt(dash);
		        if (caret > dash) 
		        {
		            caret--;
		        }
		        if (anchor > dash) 
		        {
		            anchor--;
		        }
		    }
		    
		    while (newText.length() < 2)
		    {
		    	newText.append(" ");
		    }
		    
		    if (newText.length() == 2 || newText.charAt(2) != '.')
		    {
		    	newText.insert(2, ".");
		    	
		    	if (caret > 2 || (caret == 2 && end <= 2 && change.isDeleted())) 
		    	{
		            caret++;
		        }
		        if (anchor > 2 || (anchor == 2 && end <= 2 && change.isDeleted())) 
		        {
		            anchor++;
		        }
		    }
		    
		    while (newText.length() < 6)
		    {
		    	newText.append(" ");
		    }
		    
		    if (newText.length() == 6 || newText.charAt(6) != '.')
		    {
		    	newText.insert(6, ".");
		    	
		    	if (caret > 6 || (caret == 6 && end <= 6 && change.isDeleted()))
		    	{
		            caret++;
		        }
		        if (anchor > 6 || (anchor == 6 && end <= 6 && change.isDeleted()))
		        {
		            anchor++;
		        }
		    }
		    
		    while (newText.length() < 10)
		    {
		    	newText.append(" ");
		    }
		    
		    if (newText.length() == 10 || newText.charAt(10) != '/')
		    {
		    	newText.insert(10, "/");
		    	
		    	if (caret > 10 || (caret == 10 && end <= 10 && change.isDeleted()))
		    	{
		            caret++;
		        }
		        if (anchor > 10 || (anchor == 10 && end <= 10 && change.isDeleted()))
		        {
		            anchor++;
		        }
		    }
		    
		    while (newText.length() < 15)
		    {
		    	newText.append(" ");
		    }
		    
		    if (newText.length() == 15 || newText.charAt(15) != '-')
		    {
		    	newText.insert(15, "-");
		    	
		    	if (caret > 15 || (caret == 15 && end <= 15 && change.isDeleted()))
		    	{
		            caret++;
		        }
		        if (anchor > 15 || (anchor == 15 && end <= 15 && change.isDeleted()))
		        {
		            anchor++;
		        }
		    }

		    if (newText.length() > 18)
		    {
		        newText.delete(18, newText.length());
		    }
		    
		    int lastNumber = text.indexOf(" ");
		    int lastNumberOld = oldText.indexOf(" ");
		    int newPos = 0;
		    if (caret != lastNumber && caret > lastNumber)
		    {
		    	newPos = lastNumber;
		    }
		    else if (caret != lastNumber && caret < lastNumber)
		    {
		    	newPos = caret;
		    }
		    else if (caret == lastNumber)
		    {
		    	newPos = caret;
		    }
		    
		    if (lastNumber < 0)
		    	newPos = caret;
		    
		    if (newPos < 0)
		    	newPos = 0;
		    
		    int lastDigit = Utils.lastIndexOf(newText.toString(), "\\d");
		    lastDigit = (lastDigit < 0) ? 0 : lastDigit;
		    
		    if (lastDigit > 0)
		    {
		    	if (newText.charAt(lastDigit-1) == ' ' 
		    			|| (lastDigit > 1 
		    					&& (newText.charAt(lastDigit-1) == '-' || newText.charAt(lastDigit-1) == '.' || newText.charAt(lastDigit-1) == '/') 
		    					&& newText.charAt(lastDigit-2) == ' '))
			    {
			    	newText.replace(newText.indexOf(" "), newText.indexOf(" ")+1, newText.charAt(lastDigit) + "");
			    	newText.replace(lastDigit, lastDigit+1, " ");
			    	
			    	lastDigit = Utils.lastIndexOf(newText.toString(), "\\d");
			    	newPos = lastDigit+1;
			    }
		    }
		    
		    text = newText.toString();
		    anchor = Math.min(newPos, 18);
		    caret = Math.min(newPos, 18);
		    
		    change.setText(text);
		    
		    change.setAnchor(anchor);
		    change.setCaretPosition(caret);
		    
		    change.setRange(0, change.getControlText().length());

		    return change;
		});
		
		field.setTextFormatter(formatter);
	}
	
	public static int lastIndexOf(String string, String regex)
	{
		Matcher matcher = Pattern.compile(regex).matcher(string);
	
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		while (matcher.find())
		{
			list.add(matcher.start());
		}
		
		if (list.size() == 0)
			return -1;
		
		return list.get(list.size()-1);
	}
	
	public static String formatName(String name, int linevalue)
	{
		StringBuilder ret = new StringBuilder(name);
		
		for(int i = 1; i <= ret.length()/linevalue; i++)
		{
			if (linevalue*i -1 <= ret.length())
				ret.insert(linevalue*i -1, "-\n");
		}
		
		return ret.toString();
	}
	
	public static String getFormattedDateTime(LocalDateTime dateTime)
	{
		return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
	}
	
	public static String getHashString(String string)
	{
		byte[] bytes = new byte[1];
		
		try
		{
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			digest.update(string.getBytes());
			bytes = digest.digest();
		} 
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) 
		{
			 int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
		     int parteBaixa = bytes[i] & 0xf;
		     if (parteAlta == 0) s.append('0');
		     s.append(Integer.toHexString(parteAlta | parteBaixa));
		}
		return s.toString();
	}
	
	public static <T> RecursiveTreeItem<TreeWrapper<T>> createRoot(ObservableList<TreeWrapper<T>> list)
	{
		RecursiveTreeItem<TreeWrapper<T>> root = new RecursiveTreeItem<TreeWrapper<T>>(list, new Callback<RecursiveTreeObject<TreeWrapper<T>>, ObservableList<TreeWrapper<T>>>()
		{
			@Override
			public ObservableList<TreeWrapper<T>> call(RecursiveTreeObject<TreeWrapper<T>> param)
			{
				return param.getChildren();
			}
		});
		
		return root;
	}
	
	public static boolean isCNPJ(String CNPJ) 
	{
		// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
		    if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
		        CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
		        CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
		        CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
		        CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
		       (CNPJ.length() != 14))
		       return(false);
		 
		    char dig13, dig14;
		    int sm, i, r, num, peso;
		 
		// "try" - protege o código para eventuais erros de conversao de tipo (int)
		    try {
		// Calculo do 1o. Digito Verificador
		      sm = 0;
		      peso = 2;
		      for (i=11; i>=0; i--) {
		// converte o i-ésimo caractere do CNPJ em um número:
		// por exemplo, transforma o caractere '0' no inteiro 0
		// (48 eh a posição de '0' na tabela ASCII)
		        num = (int)(CNPJ.charAt(i) - 48);
		        sm = sm + (num * peso);
		        peso = peso + 1;
		        if (peso == 10)
		           peso = 2;
		      }
		 
		      r = sm % 11;
		      if ((r == 0) || (r == 1))
		         dig13 = '0';
		      else dig13 = (char)((11-r) + 48);
		 
		// Calculo do 2o. Digito Verificador
		      sm = 0;
		      peso = 2;
		      for (i=12; i>=0; i--) {
		        num = (int)(CNPJ.charAt(i)- 48);
		        sm = sm + (num * peso);
		        peso = peso + 1;
		        if (peso == 10)
		           peso = 2;
		      }
		 
		      r = sm % 11;
		      if ((r == 0) || (r == 1))
		         dig14 = '0';
		      else dig14 = (char)((11-r) + 48);
		 
		// Verifica se os dígitos calculados conferem com os dígitos informados.
		      if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
		         return(true);
		      else return(false);
		    } catch (InputMismatchException erro) {
		        return(false);
		    }
	}
	
	public static boolean isCPF(String CPF) 
	{
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") ||
            CPF.equals("11111111111") ||
            CPF.equals("22222222222") || CPF.equals("33333333333") ||
            CPF.equals("44444444444") || CPF.equals("55555555555") ||
            CPF.equals("66666666666") || CPF.equals("77777777777") ||
            CPF.equals("88888888888") || CPF.equals("99999999999") ||
            (CPF.length() != 11))
            return(false);
          
        char dig10, dig11;
        int sm, i, r, num, peso;
          
        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
        // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {              
        // converte o i-esimo caractere do CPF em um numero:
        // por exemplo, transforma o caractere '0' no inteiro 0         
        // (48 eh a posicao de '0' na tabela ASCII)         
            num = (int)(CPF.charAt(i) - 48); 
            sm = sm + (num * peso);
            peso = peso - 1;
            }
          
            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48); // converte no respectivo caractere numerico
          
        // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
            num = (int)(CPF.charAt(i) - 48);
            sm = sm + (num * peso);
            peso = peso - 1;
            }
          
            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                 dig11 = '0';
            else dig11 = (char)(r + 48);
          
        // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                 return(true);
            else return(false);
                } catch (InputMismatchException erro) {
                return(false);
            }
      }
	
	public static void autoSizeColumn(JFXTreeTableView<?> tabela, int mainColumn)
	{
		tabela.setColumnResizePolicy(JFXTreeTableView.UNCONSTRAINED_RESIZE_POLICY);
		
		Runnable r = () ->
		{
			Platform.runLater(() ->
			{
				int columns = tabela.getColumns().size()-1+2;
				
				for(int i = 0; i < columns-1; i++)
				{
					tabela.getColumns().get(i).minWidthProperty().unbind();
					tabela.getColumns().get(i).maxWidthProperty().unbind();
					
					if (i == mainColumn)
					{
						tabela.getColumns().get(i).setMinWidth((tabela.getWidth()/columns)*2);
						tabela.getColumns().get(i).setMaxWidth((tabela.getWidth()/columns)*2);
					}
					else 
					{
						tabela.getColumns().get(i).setMinWidth(tabela.getWidth()/columns);
						tabela.getColumns().get(i).setMaxWidth(tabela.getWidth()/columns);
					}
					
					if (i == columns-2)
					{
						tabela.getColumns().get(i).setMinWidth(tabela.getWidth()/columns - 20);
						tabela.getColumns().get(i).setMaxWidth(tabela.getWidth()/columns - 20);
					}
				}
			});
		};
		
		if (tabela.getScene() != null)
			tabela.getScene().widthProperty().addListener(l -> r.run());
		
		r.run();
	}
	
	public static void onlyIntegerTextField(TextField field)
	{
//		field.textProperty().addListener((l, oldVal, newVal) ->
//		{
//			if (!newVal.matches("\\d*"))
//			{
//				field.setText(oldVal);
//			}
//			
//			if (newVal.length() > 10)
//			{
//				field.setText(oldVal);
//			}
//		});
		
		TextFormatter<Object> formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> 
		{
			String newtext = change.getControlNewText();
			String oldtext = change.getControlText();
			
			if (!newtext.matches("\\d*") || newtext.length() >  10)
			{
				change.setText(oldtext);
				change.setRange(0, oldtext.length());
			}
			
			return change;
		});
		
		field.setTextFormatter(formatter);
	}
	
	@SuppressWarnings("unchecked")
	public static void onlyDecimalTextField(TextField field)
	{
		Pattern pattern = Pattern.compile("(\\d+\\,\\d*)|\\d*");
		TextFormatter<Object> formatter = new TextFormatter<>((UnaryOperator<TextFormatter.Change>) change -> 
		{
			String newText = change.getControlNewText();
			String text = change.getControlText();
			
			int caret = change.getCaretPosition();
			int anchor = change.getAnchor();
			
			if (newText.length() < 3)
			{
				newText = "R$ ";
			}
			else if (!pattern.matcher(newText.substring(3)).matches())
			{
				newText = text;
				
				if (!newText.equals(text))
				{
					caret = newText.length();
					anchor = newText.length();
				}
			}
			
			if (caret < 3)
			{
				caret = (newText.length() > 2) ? 3: 2;
				anchor = caret;
			}
			
			change.setText(newText);
			change.setRange(0, change.getControlText().length());
			change.setCaretPosition(Math.min(caret, newText.length()));
			change.setAnchor(anchor);
			
		    return change;
		});
		
		field.setTextFormatter(formatter);
		
		field.textProperty().addListener((l, oldVal, newVal) ->
		{
			if (newVal.contains(",") && newVal.length() > 13)
			{
				field.setText(oldVal);
			}
			else if (!newVal.contains(",") && newVal.length() > 10)
			{
				field.setText(oldVal);
			}
		});
	}
	
	public static void closeQuietly(AutoCloseable closeable)
	{
		if (closeable != null)
		{
			try
			{
				closeable.close();
			} 
			catch (Exception e)
			{
			}
		}
	}
}
