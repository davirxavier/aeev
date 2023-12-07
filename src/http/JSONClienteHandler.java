package http;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import com.davixavier.entidades.clientes.Cliente;
import com.davixavier.entidades.clientes.Endereço;
import com.davixavier.entidades.clientes.IdentificaçãoFactory;
import com.davixavier.entidades.clientes.Telefone;
import com.davixavier.utils.JSONEncoder;

public class JSONClienteHandler implements JSONEncoder<Cliente>
{
	private static JSONClienteHandler instance;
	
	private JSONClienteHandler() 
	{
		
	}
	
	public static JSONClienteHandler getInstance()
	{
		if (instance == null)
		{
			synchronized (JSONClienteHandler.class) 
			{
				if (instance == null)
				{
					instance = new JSONClienteHandler();
				}
			}
		}
		
		return instance;
	}
	
	public JSONObject encode(List<Cliente> clientes)
	{
		JSONObject object = new JSONObject();
		
		clientes.forEach(c ->
		{
			JSONObject clienteObject = new JSONObject();
			clienteObject.put("id", c.getId());
			clienteObject.put("nome", c.getNome());
			
			if (c.getNomeFantasia() != null)
			{
				clienteObject.put("nomefantasia", c.getNomeFantasia());
			}
			else 
			{
				clienteObject.put("nomefantasia", JSONObject.NULL);
			}
			
			clienteObject.put("telefones", c.getTelefonesString());
			clienteObject.put("cpf", c.getCpfCnpj().toString());
			clienteObject.put("endereço", c.getEndereço());
			
			object.put("" + c.getId(), clienteObject);
		});
		
		return object;
	}
	
	public List<Cliente> decode(JSONObject object)
	{
		List<Cliente> clientes = new ArrayList<Cliente>();
		
		if (object.has("clientes"))
		{
			Object objectTest = object.get("clientes");
			if (objectTest == JSONObject.NULL || !(objectTest instanceof JSONObject))
				return clientes;
			
			JSONObject clientesObject = object.getJSONObject("clientes");
			
			Iterator<String> keys = clientesObject.keys();
			while (keys.hasNext())
			{
				String key = keys.next();
				
				JSONObject clienteObject = clientesObject.getJSONObject(key);
				Cliente cliente = new Cliente();
				cliente.setId(clienteObject.getInt("id"));
				cliente.setNome(clienteObject.getString("nome"));
				
				Object nomefantasia = clienteObject.get("nomefantasia");
				cliente.setNomeFantasia(nomefantasia.toString());
				if (nomefantasia == JSONObject.NULL)
				{
					cliente.setNomeFantasia(null);
				}
				
				cliente.setTelefones(Cliente.telefonesFromString(clienteObject.getString("telefones")));
				cliente.setCpfCnpj(IdentificaçãoFactory.identificávelFromString(clienteObject.getString("cpf")));
				cliente.setEndereço(Endereço.fromString(clienteObject.getString("endereço")));
				
				clientes.add(cliente);
			}
		}
		
		return clientes;
	}
}
