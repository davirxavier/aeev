package http;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import com.davixavier.entidades.clientes.Cliente;
import com.davixavier.entidades.estoque.Produto;
import com.davixavier.utils.JSONEncoder;

public class JSONProdutoHandler implements JSONEncoder<Produto>
{
	private static JSONProdutoHandler instance;
	
	private JSONProdutoHandler() 
	{
		
	}
	
	public static JSONProdutoHandler getInstance()
	{
		if (instance == null)
		{
			synchronized (JSONProdutoHandler.class) 
			{
				if (instance == null)
				{
					instance = new JSONProdutoHandler();
				}
			}
		}
		
		return instance;
	}
	
	public JSONObject encode(List<Produto> produtos)
	{
		JSONObject jsonObject = new JSONObject();
		
		produtos.forEach(p ->
		{
			JSONObject produtObject = new JSONObject();
			produtObject.put("id", p.getId());
			produtObject.put("nome", p.getNome());
			produtObject.put("preço", p.getPreço());
			produtObject.put("preço_compra", p.getPreçoCompra());
			produtObject.put("quantidade", p.getQuantidade());
			produtObject.put("codigo", p.getCodigo());
			
			jsonObject.put("" + p.getId(), produtObject);
		});
		
		return jsonObject;
	}
	
	public List<Produto> decode(JSONObject object)
	{
		List<Produto> list = new ArrayList<Produto>();
		
		if (object.has("produtos"))
		{
			Object objectTest = object.get("produtos");
			if (objectTest == JSONObject.NULL || !(objectTest instanceof JSONObject))
				return list;
			
			JSONObject produtosObject = object.getJSONObject("produtos");
			
			Iterator<String> keys = produtosObject.keys();
			while(keys.hasNext())
			{
				JSONObject produtoObject = produtosObject.getJSONObject(keys.next());
				
				Produto produto = new Produto();
				produto.setId(produtoObject.getInt("id"));
				produto.setNome(produtoObject.getString("nome"));
				produto.setPreço(produtoObject.getDouble("preço"));
				produto.setPreçoCompra(produtoObject.getDouble("preço_compra"));
				produto.setQuantidade(produtoObject.getInt("quantidade"));
				produto.setCodigo(produtoObject.getInt("codigo"));
				
				if (produto.getCodigo() == 0)
					produto.setCodigo(produto.getId());
				
				list.add(produto);
			}
		}
		
		return list;
	}
}	
