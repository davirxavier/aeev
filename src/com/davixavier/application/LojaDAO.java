package com.davixavier.application;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import com.davixavier.application.img.IconsPath;
import com.davixavier.database.ConnectionFactory;
import com.davixavier.entidades.clientes.Telefone;
import com.davixavier.utils.Utils;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class LojaDAO
{
	public static Loja get(int idusuário, Connection connection)
	{
		Loja loja = Loja.getInstance();
		
		String query = "SELECT * FROM lojas WHERE idusuário = ?";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			statement.setInt(1, idusuário);
			ResultSet resultSet = statement.executeQuery();
			
			if (resultSet.next())
			{
				loja.setIdusuário(idusuário);
				loja.setNome(resultSet.getString("nomeloja"));
				loja.setTelefone1(Telefone.fromString(resultSet.getString("telefone1")));
				loja.setTelefone2(Telefone.fromString(resultSet.getString("telefone2")));
				
				Image image = new Image(IconsPath.ICON.getPath());
				
				InputStream inputStream = resultSet.getBlob("userimage").getBinaryStream();
				BufferedImage bufferedImage = ImageIO.read(inputStream);
				
				image = SwingFXUtils.toFXImage(bufferedImage, null);
				
				loja.setLogo(image);
			}
		} 
		catch (SQLException | IOException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
		
		return loja;
	}
	
	public static Loja get(Connection connection)
	{
		String query = "SELECT * FROM lojas";
		
		Loja loja = Loja.getInstance();
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			
			resultSet.next();
			//if (!resultSet.next())
				//return loja;
			
			loja.setNome(resultSet.getString("nomeloja"));
			loja.setTelefone1(Telefone.fromString(resultSet.getString("telefone1")));
			loja.setTelefone2(Telefone.fromString(resultSet.getString("telefone2")));
			
			Image image = new Image(IconsPath.ICON.getPath());
			
			InputStream inputStream = resultSet.getBlob("userimage").getBinaryStream();
			BufferedImage bufferedImage = ImageIO.read(inputStream);
			
			image = SwingFXUtils.toFXImage(bufferedImage, null);
			
			loja.setLogo(image);
		} 
		catch (SQLException | IOException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			Utils.closeQuietly(statement);
		}
		
		return loja;
	}
	
	public static boolean insert(Connection connection)
	{
		String query = "INSERT INTO lojas(userimage, nomeloja, telefone1, telefone2, idusuário) "
				     + "VALUES (?, ?, ?, ?, ?)";
		
		Loja loja = Loja.getInstance();
		
		PreparedStatement statement = null;
		try
		{
			statement = connection.prepareStatement(query);
			
			statement.setBlob(1, LojaDAO.class.getResourceAsStream(loja.getLogoPath()));
			statement.setString(2, loja.getNome());
			statement.setString(3, loja.getTelefone1().toString());
			statement.setString(4, loja.getTelefone2().toString());
			statement.setInt(5, loja.getIdusuário());
			
			statement.execute();
			
			return true;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			
			return false;
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
	}
	
	public static boolean update(Connection connection)
	{
		String query = "UPDATE lojas "
					 + "SET userimage = ?, "
					 + "nomeloja = ?,"
					 + "telefone1 = ?,"
					 + "telefone2 = ?,"
					 + "idusuário = ? ";
		
		Loja loja = Loja.getInstance();
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			
			statement.setBlob(1, LojaDAO.class.getResourceAsStream(loja.getLogoPath()));
			statement.setString(2, loja.getNome());
			statement.setString(3, loja.getTelefone1().toString());
			statement.setString(4, loja.getTelefone2().toString());
			statement.setInt(5, loja.getIdusuário());
			
			statement.execute();
			
			return true;
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
	}
	
	public static boolean exists(Connection connection)
	{
		String query = "SELECT * FROM lojas";
		
		PreparedStatement statement = null;
		try 
		{
			statement = connection.prepareStatement(query);
			
			if (statement.executeQuery().next())
				return true;
			else
				return false;
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			
			return false;
		}
		finally 
		{
			Utils.closeQuietly(statement);
		}
	}
}
