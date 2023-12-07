package http;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import com.davixavier.entidades.clientes.Cliente;
import com.davixavier.entidades.compras.ProdutoVenda;
import com.davixavier.entidades.compras.Venda;
import com.davixavier.utils.JSONEncoder;

public class JSONVendaHandler implements JSONEncoder<Venda>
{
	private static JSONVendaHandler instance;
	
	private JSONVendaHandler() 
	{
		
	}
	
	public static JSONVendaHandler getInstance()
	{
		if (instance == null)
		{
			synchronized (JSONVendaHandler.class) 
			{
				if (instance == null)
				{
					instance = new JSONVendaHandler();
				}
			}
		}
		
		return instance;
	}
	
	public JSONObject encode(List<Venda> vendas)
	{
		JSONObject object = new JSONObject();
		
		vendas.forEach(v ->
		{
			JSONObject vendaObject = new JSONObject();
			
			vendaObject.put("id", v.getId());
			vendaObject.put("data", v.getData().toString());
			vendaObject.put("preço", v.getPreço());
			vendaObject.put("descrição", v.getDescrição());
			vendaObject.put("produtos", v.getProdutosString());
			
			String cliente = "";
			if (v.getCliente() != null)
			{
				cliente = v.getCliente().toString();
			}
			vendaObject.put("cliente", cliente);
			
			object.put("" + v.getId(), vendaObject);
		});
		
		return object;
	}
	
	public List<Venda> decode(JSONObject object)
	{
		List<Venda> vendas = new ArrayList<Venda>();
		
		if (object.has("vendas"))
		{
			Object objectTest = object.get("vendas");
			if (objectTest == JSONObject.NULL || !(objectTest instanceof JSONObject))
				return vendas;
			
			JSONObject vendasObject = object.getJSONObject("vendas");
			
			Iterator<String> keys = vendasObject.keys();
			while(keys.hasNext())
			{
				String key = keys.next();
				
				JSONObject vendaObject = vendasObject.getJSONObject(key);
				
				Venda venda = new Venda();
				venda.setId(vendaObject.getInt("id"));
				venda.setData(Timestamp.valueOf(vendaObject.getString("data")));
				venda.setDescrição(vendaObject.getString("descrição"));
				venda.setPreço(vendaObject.getDouble("preço"));
				venda.setProdutos(ProdutoVenda.parseArray(vendaObject.getString("produtos").split(";")));
				venda.setCliente(Cliente.fromString(vendaObject.getString("cliente")));
				
				vendas.add(venda);
			}
		}
		
		return vendas;
	}
}
