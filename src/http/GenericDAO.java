package http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import http.client.HttpClientController;

public class GenericDAO 
{
	public static JSONObject get(String path, int id) throws ClientProtocolException, IOException
	{
		String response = HttpClientController.getInstance().getRequest(HttpConsts.REQUEST_URL + path + "/" + id).getBody();
		
		return new JSONObject(response);
	}
	
	public static JSONObject getAll(String path) throws ClientProtocolException, IOException
	{
		String response = HttpClientController.getInstance().getRequest(HttpConsts.REQUEST_URL + path + "/").getBody();
		
		return new JSONObject(response);
	}
	
	public static boolean insert(JSONObject object, String path) throws ParseException, ClientProtocolException, IOException
	{
		int response = HttpClientController.getInstance().postRequest(HttpConsts.REQUEST_URL + path + "/", 
				object.toString(), "application/json;charset=UTF-8").getStatus();
		
		boolean ret = (response >= 200 && response < 300);
		
		if (!ret)
		{
			throw new IOException("Insert failed with HTTP status: " + response);
		}
		
		return ret;
	}
	
	public static boolean insertBatch(JSONObject object, String path) throws ParseException, ClientProtocolException, IOException
	{
		ResponseData response = HttpClientController.getInstance().postRequest(HttpConsts.REQUEST_URL + path + "-collection/insert", 
				object.toString(), "application/json;charset=UTF-8");
		
		boolean ret = (response.getStatus() >= 200 && response.getStatus() < 300);
		
		if (!ret)
		{
			throw new IOException("Insert batch failed with HTTP status: " + response.getStatus());
		}
		
		return ret;
	}
	
	public static boolean update(JSONObject object, int id, String path) throws ParseException, ClientProtocolException, IOException
	{
		ResponseData response = HttpClientController.getInstance().postRequest(HttpConsts.REQUEST_URL + path + "/" + id, 
				object.toString(), "application/json;charset=UTF-8");
		
		boolean ret = (response.getStatus() >= 200 && response.getStatus() < 300);
		
		if (!ret)
		{
			throw new IOException("Update failed with HTTP status: " + response.getStatus());
		}
		
		return ret;
	}
	
	public static boolean updateBatch(JSONObject object, String path) throws ParseException, ClientProtocolException, IOException
	{
		ResponseData response = HttpClientController.getInstance().postRequest(HttpConsts.REQUEST_URL + path + "-collection/update", 
				object.toString(), "application/json;charset=UTF-8");
		
		boolean ret = (response.getStatus() >= 200 && response.getStatus() < 300);
		
		if (!ret)
		{
			throw new IOException("Update batch failed with HTTP status: " + response.getStatus());
		}
		
		return ret;
	}
	
	public static boolean delete(JSONObject idsJson, String path) throws ParseException, ClientProtocolException, IOException
	{
		ResponseData response = HttpClientController.getInstance().postRequest(HttpConsts.REQUEST_URL + path + "/delete/", 
				idsJson.toString(), "application/json;charset=UTF-8");
		
		boolean ret = (response.getStatus() >= 200 && response.getStatus() < 300);
		
		if (!ret)
		{
			throw new IOException("Delete failed with HTTP status: " + response.getStatus());
		}
		
		return ret;
	}
	
	public static JSONObject deleteFormatIds(JSONObject obsjectsJsonObject)
	{
		JSONObject formattedIds = new JSONObject();
		JSONArray idsArray = new JSONArray();
		
		Iterator<String> keys = obsjectsJsonObject.keys();
		while (keys.hasNext())
		{
			String key = keys.next();
			if (key.equals("ids"))
				continue;
			
			idsArray.put(Integer.parseInt(key));
		}
		
		formattedIds.put("ids", idsArray);
		
		return formattedIds;
	}
}
