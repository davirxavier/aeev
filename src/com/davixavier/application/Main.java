package com.davixavier.application;
	
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.davixavier.application.img.IconsPath;
import com.davixavier.application.logging.Logger;
import com.davixavier.autoupdate.AutoUpdate;
import com.davixavier.autoupdate.VersionConfig;
import com.davixavier.database.ConnectionFactory;
import com.davixavier.database.DBOperationType;
import com.davixavier.entidades.clientes.CNPJ;
import com.davixavier.entidades.clientes.CPF;
import com.davixavier.entidades.clientes.Cliente;
import com.davixavier.entidades.clientes.Endereço;
import com.davixavier.entidades.clientes.IdentificaçãoFactory;
import com.davixavier.entidades.clientes.Telefone;
import com.davixavier.entidades.compras.ProdutoVenda;
import com.davixavier.entidades.compras.Venda;
import com.davixavier.entidades.estoque.Produto;
import com.davixavier.entidades.usuarios.Usuário;
import com.davixavier.nodes.materiallist.MaterialListCell;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.Utils;
import com.davixavier.utils.pdf.EstoquePDF;
import com.davixavier.utils.pdf.PDFCreator;
import com.davixavier.utils.pdf.ProdutoPDFTable;
import com.davixavier.utils.pdf.ProdutoVendaPDFTRow;
import com.davixavier.utils.pdf.ProdutoVendaPDFTable;
import com.davixavier.utils.pdf.VendaPDF;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.xml.simpleparser.NewLineHandler;

import http.GenericDAO;
import http.HttpConsts;
import http.JSONClienteHandler;
import http.JSONProdutoHandler;
import http.JSONUsuárioHandler;
import http.JSONVendaHandler;
import http.client.HttpClientController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

public class Main extends Application
{
	public static final String TITLE = "AEEV - Agenda Eletrônica de Estoque e Vendas";
	public static final String CURRENTVERSION = "0.4";
	
	@Override
	public void start(Stage primaryStage)
	{
		try 
		{
			ExecuterServices.getExecutor().execute(() -> 
			{
				if (Utils.checkConnection())
					new VersionConfig().checkVersions();
			});
			
			AnchorPane root = FXMLLoader.load(getClass().getResource("application.fxml"));
			Scene scene = new Scene(root, 800, 600);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.setTitle(TITLE);
			primaryStage.setMinHeight(600);
			primaryStage.setMinWidth(800);
			primaryStage.getIcons().add(IconsPath.ICON.getImage());
			primaryStage.setMaximized(true);
			primaryStage.show();
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() throws Exception
	{
		super.stop();
		
		HttpClientController.getInstance().postRequest(HttpConsts.REQUEST_URL + "/logout", "", "");
		
		ConnectionFactory.closeQuietly();
		ExecuterServices.getExecutor().shutdownNow();
		ExecuterServices.getCacheExecutor().shutdownNow();
		
		Logger.getInstance().close();
	}
	
	public static void main(String[] args) throws DocumentException, ClientProtocolException, IOException 
	{
		try 
		{
			Logger.getInstance().open();
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		
		System.setProperty("prism.dirtyopts", "false");
		System.setProperty("textdb.allow_full_path", "true");
		
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() 
		{
			@Override
			public void uncaughtException(Thread t, Throwable e)
			{
				System.err.println("exception " + e + " from thread " + t + " trace: ");
				e.printStackTrace();
			}
		});
		
		launch(args);
	}
}
