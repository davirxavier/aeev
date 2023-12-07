package http;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import com.davixavier.entidades.clientes.Cliente;
import com.davixavier.entidades.usuarios.Usuário;
import com.davixavier.utils.JSONEncoder;

public class JSONUsuárioHandler implements JSONEncoder<Usuário>
{
	private static JSONUsuárioHandler instance;
	
	private JSONUsuárioHandler() 
	{
		
	}
	
	public static JSONUsuárioHandler getInstance()
	{
		if (instance == null)
		{
			synchronized (JSONUsuárioHandler.class) 
			{
				if (instance == null)
				{
					instance = new JSONUsuárioHandler();
				}
			}
		}
		
		return instance;
	}
	
	public JSONObject encode(List<Usuário> usuários)
	{
		JSONObject object = new JSONObject();
		
		usuários.forEach(u ->
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
	
	public List<Usuário> decode(JSONObject object)
	{
		List<Usuário> usuários = new ArrayList<Usuário>();
		
		if (object.has("usuários"))
		{
			Object objectTest = object.get("usuários");
			if (objectTest == JSONObject.NULL || !(objectTest instanceof JSONObject))
				return usuários;
			
			JSONObject usuáriosObject = object.getJSONObject("usuários");
			
			Iterator<String> keys = usuáriosObject.keys();
			while(keys.hasNext())
			{
				String key = keys.next();
				
				JSONObject usuObject = usuáriosObject.getJSONObject(key);
				
				Usuário usuário = new Usuário();
				usuário.setId(usuObject.getInt("id"));
				usuário.setUsername(usuObject.getString("username"));
				usuário.setSenhaHash(usuObject.getString("password"));
				
				Object email = usuObject.get("email");
				usuário.setEmail(email.toString());
				if (email == JSONObject.NULL)
					usuário.setEmail(null);
				
				usuário.setType(usuObject.getString("type"));
				
				usuários.add(usuário);
			}
		}
		
		return usuários;
	}
}
