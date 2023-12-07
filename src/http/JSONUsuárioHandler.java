package http;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import com.davixavier.entidades.clientes.Cliente;
import com.davixavier.entidades.usuarios.Usu�rio;
import com.davixavier.utils.JSONEncoder;

public class JSONUsu�rioHandler implements JSONEncoder<Usu�rio>
{
	private static JSONUsu�rioHandler instance;
	
	private JSONUsu�rioHandler() 
	{
		
	}
	
	public static JSONUsu�rioHandler getInstance()
	{
		if (instance == null)
		{
			synchronized (JSONUsu�rioHandler.class) 
			{
				if (instance == null)
				{
					instance = new JSONUsu�rioHandler();
				}
			}
		}
		
		return instance;
	}
	
	public JSONObject encode(List<Usu�rio> usu�rios)
	{
		JSONObject object = new JSONObject();
		
		usu�rios.forEach(u ->
		{
			JSONObject usuObject = new JSONObject();
			
			usuObject.put("id", u.getId());
			usuObject.put("username", u.getUsername());
			usuObject.put("password", u.getSenhaHash());
			
			if (u.getEmail() != null)
			{
				usuObject.put("email", u.getEmail());
			}
			else 
			{
				usuObject.put("email", JSONObject.NULL);
			}
			
			usuObject.put("type", u.getType());
			
			object.put("" + u.getId(), usuObject);
		});
		
		return object;
	}
	
	public List<Usu�rio> decode(JSONObject object)
	{
		List<Usu�rio> usu�rios = new ArrayList<Usu�rio>();
		
		if (object.has("usu�rios"))
		{
			Object objectTest = object.get("usu�rios");
			if (objectTest == JSONObject.NULL || !(objectTest instanceof JSONObject))
				return usu�rios;
			
			JSONObject usu�riosObject = object.getJSONObject("usu�rios");
			
			Iterator<String> keys = usu�riosObject.keys();
			while(keys.hasNext())
			{
				String key = keys.next();
				
				JSONObject usuObject = usu�riosObject.getJSONObject(key);
				
				Usu�rio usu�rio = new Usu�rio();
				usu�rio.setId(usuObject.getInt("id"));
				usu�rio.setUsername(usuObject.getString("username"));
				usu�rio.setSenhaHash(usuObject.getString("password"));
				
				Object email = usuObject.get("email");
				usu�rio.setEmail(email.toString());
				if (email == JSONObject.NULL)
					usu�rio.setEmail(null);
				
				usu�rio.setType(usuObject.getString("type"));
				
				usu�rios.add(usu�rio);
			}
		}
		
		return usu�rios;
	}
}
