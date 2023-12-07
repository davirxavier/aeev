package http.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.logging.Level;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.davixavier.application.logging.Logger;

import http.ResponseData;

public class HttpClientController 
{
	private static final int RETRY_TIMES = 5;
	
	private static final Logger LOGGER = Logger.getInstance();
	private CookieStore cookieStore;
	private HttpContext httpContext;
	
	private static HttpClientController instance;
	
	private HttpClientController() 
	{
		cookieStore = new BasicCookieStore();
		
		httpContext = new BasicHttpContext();
		httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
	}
	
	public static HttpClientController getInstance()
	{
		if (instance == null)
		{
			synchronized (HttpClientController.class) 
			{
				if (instance == null)
				{
					instance = new HttpClientController();
				}
			}
		}
		
		return instance;
	}
	
	public ResponseData getRequest(String url) throws ClientProtocolException, IOException
	{
		HttpGet request = new HttpGet(url);
		
		CloseableHttpClient client = getCustomClient();
		
		return httpTry(client, request, url, "GET");
	}
	
	public ResponseData postRequest(String url, String requestBody, String contentType) throws ParseException, ClientProtocolException, IOException
	{
		HttpPost request = new HttpPost(url);

		StringEntity entity = new StringEntity(requestBody, "UTF-8");
		
		request.setEntity(entity);
		request.addHeader(HttpHeaders.CONTENT_TYPE, contentType);
		
		CloseableHttpClient client = getCustomClient();
		
		return httpTry(client, request, url, "POST");
	}
	
	public ResponseData postLoginRequest(String url, String username, String password) throws ParseException, ClientProtocolException, IOException
	{
		HttpPost request = new HttpPost(url);
		request.setEntity(new StringEntity("{\"user\":\"" + username +"\", \"pass\":\"" + password + "\"}"));
		request.addHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
		
		CloseableHttpClient client = getCustomClient();
		
		return httpTry(client, request, url, "LOGIN POST");
	}
	
	public CloseableHttpClient getCustomClient()
	{
		return HttpClients.custom().setDefaultCookieStore(cookieStore).build();
	}
	
	private ResponseData httpTry(CloseableHttpClient client, HttpRequestBase request, String url, String name) throws ParseException, ClientProtocolException, IOException
	{
		try 
		{
			LOGGER.log("HTTP " + name + " to url '" + url + "'...", Level.INFO);
			ResponseData responseData = fillResponseData(client.execute(request));
			
			for(int i = 0; i < RETRY_TIMES; i++)
			{
				if (responseData.getStatus() == 404 || responseData.getStatus() == 500)
				{
					LOGGER.log("HTTP " + name +" para o url '" + url + "' não teve sucesso, tentando novamente...", Level.WARNING);
					request.releaseConnection();
					client.close();
					client = getCustomClient();
					
					responseData = fillResponseData(client.execute(request));
				}
				else 
				{
					break;
				}
			}
			
			if (responseData.getStatus() == 404 || responseData.getStatus() == 500)
			{
				LOGGER.log("HTTP request falhou.", Level.SEVERE);
			}
			else if (responseData.getStatus() >= 200 && responseData.getStatus() < 300) 
			{
				LOGGER.log("HTTP request realizada com sucesso.", Level.INFO);
			}
			
			return responseData;
		} 
		catch (Exception e) 
		{
			throw e;
		}
		finally 
		{
			client.close();
			request.releaseConnection();
		}
	}
	
	private ResponseData fillResponseData(CloseableHttpResponse response) throws ParseException, IOException
	{
		ResponseData data = new ResponseData();
		
		data.setStatus(response.getStatusLine().getStatusCode());
		data.setStatusMessage(response.getStatusLine().getReasonPhrase());
		HttpEntity entity = response.getEntity();
		if (entity != null)
		{
			data.setBody(EntityUtils.toString(entity));
		}
		
		EntityUtils.consume(entity);
		response.close();
		return data;
	}
}
