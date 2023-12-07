package com.davixavier.panes.historico;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.davixavier.application.MainController;
import com.davixavier.database.ConnectionFactory;
import com.davixavier.entidades.clientes.Cliente;
import com.davixavier.entidades.clientes.Telefone;
import com.davixavier.entidades.compras.ProdutoVenda;
import com.davixavier.entidades.compras.TipoPagamento;
import com.davixavier.entidades.compras.Venda;
import com.davixavier.entidades.compras.VendaDAO;
import com.davixavier.entidades.estoque.Produto;
import com.davixavier.entidades.estoque.ProdutoDAO;
import com.davixavier.nodes.materiallist.MaterialCellItem;
import com.davixavier.nodes.materiallist.MaterialCellNode;
import com.davixavier.nodes.materiallist.MaterialListCell;
import com.davixavier.nodes.materiallist.MaterialListView;
import com.davixavier.nodes.materiallist.NoButtonMaterialList;
import com.davixavier.panes.FlatJFXDialog;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.TreeWrapper;
import com.davixavier.utils.Utils;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.SortType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class HistoricoTabelaManager
{
	private static ObservableList<TreeWrapper<Venda>> vendaCache = FXCollections.observableArrayList();
	private static LocalDate lastStartData = null;
	private static LocalDate lastEndData = null;
	private static String lastTipoPagamento = "";
	private static String lastNomeProduto = "";
	
	public static void setUp(JFXTreeTableView<TreeWrapper<Venda>> tabela)
	{
		ExecuterServices.getExecutor().execute(() ->
		{
			vendaCache.clear();
			
			ObservableList<TreeWrapper<Venda>> vendas = FXCollections.observableArrayList();
			try 
			{
				VendaDAO.getInstance().getAll(ConnectionFactory.getOfflineConnection()).forEach((k, v) ->
				{
					TreeWrapper<Venda> wrapper = new TreeWrapper<>();
					wrapper.setValue(v);
					
					vendas.add(wrapper);
					vendaCache.add(wrapper);
				});
			} 
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			
			RecursiveTreeItem<TreeWrapper<Venda>> root = Utils.createRoot(vendas);
			
			Platform.runLater(() ->
			{
				tabela.getColumns().setAll(setUpColumns(tabela));
				tabela.getColumns().get(2).setSortType(SortType.ASCENDING);
				tabela.getSortOrder().add(tabela.getColumns().get(2));
				tabela.sort();
				
				tabela.setRoot(root);
				tabela.setShowRoot(false);		
				tabela.setEditable(false);
				
				Utils.autoSizeColumn(tabela, 1);
			});
		});
	}
	
	private static ObservableList<TreeTableColumn<TreeWrapper<Venda>, ?>> setUpColumns(JFXTreeTableView<TreeWrapper<Venda>> tabela)
	{
		JFXTreeTableColumn<TreeWrapper<Venda>, Integer> idColumn = new JFXTreeTableColumn<>("Número");
		JFXTreeTableColumn<TreeWrapper<Venda>, String> dataColumn = new JFXTreeTableColumn<>("Data | Hora ");
		JFXTreeTableColumn<TreeWrapper<Venda>, String> preçoColumn = new JFXTreeTableColumn<>("Preço  ");
		JFXTreeTableColumn<TreeWrapper<Venda>, String> descriçãoColumn = new JFXTreeTableColumn<>("Descrição");
		JFXTreeTableColumn<TreeWrapper<Venda>, JFXButton> produtosColumn = new JFXTreeTableColumn<>("Detalhes  ");
		
		idColumn.setStyle("-fx-alignment: center;");
		produtosColumn.setStyle("-fx-alignment: center");
		
		dataColumn.setStyle("-fx-alignment: center-right");
		preçoColumn.setStyle("-fx-alignment: center-right");
		
		descriçãoColumn.setStyle("-fx-alignment: center-left");
		
		idColumn.getStyleClass().add("centerAlignedColumn");
		produtosColumn.getStyleClass().add("centerAlignedColumn");
		
		dataColumn.getStyleClass().add("rightAlignedColumn");
		preçoColumn.getStyleClass().add("rightAlignedColumn");
		
		descriçãoColumn.getStyleClass().add("leftAlignedColumn");
		
		idColumn.setCellValueFactory(param ->
		{
			return new ReadOnlyObjectWrapper<Integer>(param.getValue().getValue().getValue().getId());
		});
		
		dataColumn.setCellValueFactory(param ->
		{
			LocalDateTime dateTime = param.getValue().getValue().getValue().getData().toLocalDateTime();
			return new ReadOnlyObjectWrapper<String>(Utils.getFormattedDateTime(dateTime));
		});
		
		preçoColumn.setCellValueFactory(param ->
		{
			String ret = String.format("%.2f", param.getValue().getValue().getValue().getPreço());
			return new ReadOnlyObjectWrapper<String>("R$" + ret);
		});
		
		descriçãoColumn.setCellValueFactory(param ->
		{
			return new ReadOnlyObjectWrapper<String>(param.getValue().getValue().getValue().getDescrição());
		});
		
		produtosColumn.setCellValueFactory(param ->
		{
			JFXButton button = new JFXButton("Ver Detalhes");
			button.setOnAction(e ->
			{
				produtosDialog(tabela, param.getValue().getValue().getValue()).getDialog().show();
			});
			
			return new ReadOnlyObjectWrapper<JFXButton>(button);
		});
		
		return FXCollections.observableArrayList(idColumn, descriçãoColumn, dataColumn, preçoColumn, produtosColumn);
	}
	
	public static void refreshTable(JFXTreeTableView<TreeWrapper<Venda>> tabela)
	{
		MainController.setLoading(true);
		
		ExecuterServices.getExecutor().execute(() ->
		{			
			ObservableList<TreeWrapper<Venda>> vendas = FXCollections.observableArrayList();
			vendaCache.clear();
			try 
			{
				VendaDAO.getInstance().getAll(ConnectionFactory.getOfflineConnection()).forEach((k, v) ->
				{
					TreeWrapper<Venda> wrapper = new TreeWrapper<>();
					wrapper.setValue(v);
					
					vendaCache.add(wrapper);
					vendas.add(wrapper);
				});
			} 
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			
			RecursiveTreeItem<TreeWrapper<Venda>> root = Utils.createRoot(vendas);
			Platform.runLater(() ->
			{
				tabela.setRoot(root);
				MainController.setLoading(false);
			});
		});
	}
	
	public static FlatJFXDialog produtosDialog(JFXTreeTableView<TreeWrapper<Venda>> tabela, Venda venda)
	{
		StackPane pane = (StackPane)tabela.getScene().lookup("#primaryStackPane");
		
		JFXButton fechar = new JFXButton("Fechar");
		
		String header = "Detalhes da venda";
		String body = "";
		FlatJFXDialog dialog = new FlatJFXDialog(pane, header, body, fechar);
		
		dialog.getLayout().setPrefWidth(500);
		
		Text clienteText = new Text("Cliente relacionado a essa venda");
		Text listText = new Text("Produtos presentes nessa venda");
		
		clienteText.setStyle("-fx-font-weight: bold;");
		listText.setStyle(clienteText.getStyle());
		
		NoButtonMaterialList<ProdutoVenda> listView = new NoButtonMaterialList<>();
		listView.setPrefHeight(400);
		
		for (ProdutoVenda produtoVenda : venda.getProdutos())
		{
			MaterialCellItem<ProdutoVenda> item = new MaterialCellItem<ProdutoVenda>();
			item.setLeftString(Utils.formatName(produtoVenda.getNome(), 50));
			item.setRightString("");
			item.setDescriptionString("Preço: " + "R$" + String.format("%.2f", produtoVenda.getPreço()));
			
			item.setObject(produtoVenda);
			
			MaterialCellNode quantidadeNode = new MaterialCellNode();
			quantidadeNode.getLeftLabel().setText("Quantidade: ");
			quantidadeNode.getRightLabel().setText(produtoVenda.getQuantidade() + " unidades");
			
			MaterialCellNode totalNode = new MaterialCellNode();
			totalNode.getLeftLabel().setText("Total: ");
			totalNode.getRightLabel().setText("R$" + String.format("%.2f", produtoVenda.getPreço() * produtoVenda.getQuantidade()));
			
			item.getItems().addAll(quantidadeNode);
			
			listView.getItems().add(item);
		}
		
		VBox vBox = new VBox(listText, listView);
		vBox.setSpacing(4);
		vBox.setAlignment(Pos.TOP_LEFT);
		
		if (venda.getCliente() != null)
		{
			NoButtonMaterialList<Cliente> clienteNode = new NoButtonMaterialList<Cliente>();
			clienteNode.setPrefHeight(120);
			clienteNode.setStyle(clienteNode.getStyle() + "-fx-background-color: white;");
			
			MaterialCellItem<Cliente> cellItem = new MaterialCellItem<Cliente>();
			cellItem.setLeftString(venda.getCliente().getNome());
			if (venda.getCliente().getNomeFantasia() != null && !venda.getCliente().getNomeFantasia().isEmpty())
			{
				cellItem.setLeftString(venda.getCliente().getNome() + " - " + venda.getCliente().getNomeFantasia());
			}
			
			cellItem.setDescriptionString("Endereço: " + Utils.formatName(venda.getCliente().getEndereço().toFormattedString(), 65));
			MaterialCellNode telefonesCellNode = new MaterialCellNode();
			telefonesCellNode.getLeftLabel().setText("Telefones: ");
			for (Telefone telefone : venda.getCliente().getTelefones())
			{
				telefonesCellNode.getRightLabel().setText(telefonesCellNode.getRightLabel().getText() + telefone.toFormattedString() + ",");
			}
			String newText = telefonesCellNode.getRightLabel().getText().substring(0, telefonesCellNode.getRightLabel().getText().length()-1);
			telefonesCellNode.getRightLabel().setText(newText);
			
			MaterialCellNode cpfCellNode = new MaterialCellNode();
			cpfCellNode.getLeftLabel().setText(venda.getCliente().getCpfCnpj().getType() + ": ");
			cpfCellNode.getRightLabel().setText(venda.getCliente().getCpfCnpj().toFormattedString());
			
			cellItem.getItems().addAll(telefonesCellNode, cpfCellNode);
			
			clienteNode.getItems().add(cellItem);
			
			vBox.getChildren().add(0, clienteNode);
			vBox.getChildren().add(0, clienteText);
		}
		
		dialog.getLayout().setBody(vBox);
		
		fechar.setOnAction(e ->
		{
			dialog.getDialog().close();
		});
		
		dialog.getLayout().setPrefWidth(600);
		return dialog;
	}
	
	public static void filterTable(JFXTreeTableView<TreeWrapper<Venda>> tabela, LocalDate startData, LocalDate endData, String tipopagamento, String nomeproduto)
	{
		MainController.setLoading(true);
		
		ExecuterServices.getExecutor().execute(() ->
		{
			ObservableList<TreeWrapper<Venda>> vendas = FXCollections.observableArrayList();
			vendaCache.forEach(v ->
			{
				vendas.add(v);
			});
			
			lastStartData = startData;
			lastEndData = endData;
			lastTipoPagamento = tipopagamento;
			lastNomeProduto = nomeproduto;
			
			if (startData != null && endData == null)
			{
				final Timestamp start = Timestamp.valueOf(startData.atStartOfDay());
				
				vendas.removeIf(v ->
				{
					boolean afterEqual = v.getValue().getData().after(start) || v.getValue().getData().equals(start);
					
					return !afterEqual;
				});
			}
			else if (startData == null && endData != null)
			{
				final Timestamp end = Timestamp.valueOf(endData.atTime(23, 59, 59));
				
				vendas.removeIf(v ->
				{
					boolean beforeEqual = v.getValue().getData().before(end) || v.getValue().getData().equals(end);
					
					return !beforeEqual;
				});
			}
			else if (startData != null && endData != null)
			{
				final Timestamp start = Timestamp.valueOf(startData.atStartOfDay());
				final Timestamp end = Timestamp.valueOf(endData.atTime(23, 59, 59));
				
				vendas.removeIf(p ->
				{
					boolean afterEqual = p.getValue().getData().after(start) || p.getValue().getData().equals(start);
					boolean beforeEqual = p.getValue().getData().before(end) || p.getValue().getData().equals(end);

					return !(afterEqual && beforeEqual);
				});
			}
			
			if (tipopagamento != null && !tipopagamento.isEmpty())
			{
				vendas.removeIf(p ->
				{
					if (tipopagamento.toLowerCase().equals("outros"))
					{
						boolean found = true;
						
						for (TipoPagamento tipo : TipoPagamento.values())
						{
							if (tipo != TipoPagamento.OUTROS)
							{
								if (p.getValue().getDescrição().toLowerCase().contains(tipo.toString().toLowerCase()))
								{
									found = false;
									break;
								}
							}
						}
						
						return !found;
					}
					
					return !p.getValue().getDescrição().toLowerCase().contains(tipopagamento.toLowerCase());
				});
			}
			
			if (nomeproduto != null && !nomeproduto.isEmpty())
			{
				vendas.removeIf(p ->
				{
					boolean found = false;
					
					for (ProdutoVenda produtoVenda : p.getValue().getProdutos())
					{
						if (produtoVenda.getNome().toLowerCase().contains(nomeproduto.toLowerCase()))
						{
							found = true;
						}
					}
					
					return !found;
				});
			}
			
			RecursiveTreeItem<TreeWrapper<Venda>> root = Utils.createRoot(vendas);
			Platform.runLater(() ->
			{
				tabela.setRoot(root);
				MainController.setLoading(false);
			});
		});
	}

	public static LocalDate getLastStartData() {
		return lastStartData;
	}

	public static LocalDate getLastEndData() {
		return lastEndData;
	}

	public static String getLastTipoPagamento() {
		return lastTipoPagamento;
	}

	public static String getLastNomeProduto() {
		return lastNomeProduto;
	}
}
