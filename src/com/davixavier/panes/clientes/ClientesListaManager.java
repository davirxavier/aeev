package com.davixavier.panes.clientes;

import java.sql.SQLException;
import java.util.function.Consumer;

import com.davixavier.database.ConnectionFactory;
import com.davixavier.entidades.clientes.Cliente;
import com.davixavier.entidades.clientes.ClienteDAO;
import com.davixavier.entidades.clientes.Endereço;
import com.davixavier.entidades.clientes.IdentificaçãoFactory;
import com.davixavier.entidades.clientes.Telefone;
import com.davixavier.nodes.materiallist.MaterialCellItem;
import com.davixavier.nodes.materiallist.MaterialCellNode;
import com.davixavier.nodes.materiallist.MaterialListCell;
import com.davixavier.nodes.materiallist.MaterialListView;
import com.davixavier.panes.FlatJFXDialog;
import com.davixavier.utils.FollowableTextFieldGroup;
import com.davixavier.utils.ExecuterServices;
import com.davixavier.utils.Utils;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class ClientesListaManager
{
	public static MaterialListView<Cliente> setUp()
	{
		final MaterialListView<Cliente> listView = new MaterialListView<Cliente>();
		
		listView.setCellFactory(new Callback<ListView<MaterialCellItem<Cliente>>, ListCell<MaterialCellItem<Cliente>>>() 
		{
			@Override
			public ListCell<MaterialCellItem<Cliente>> call(ListView<MaterialCellItem<Cliente>> arg0) 
			{
				MaterialListCell<Cliente> cell = new MaterialListCell<Cliente>()
				{
					@Override
					protected void updateItem(MaterialCellItem<Cliente> persistentObject, boolean empty)
					{
						super.updateItem(persistentObject, empty);
						
						if (getDeleteButton() != null)
						{
							getDeleteButton().setOnAction(e -> removerDialog(listView, persistentObject.getObject()).getDialog().show());
							getDeleteButton().setBackgroundInHover("derive(white, -20%)");
						}
						
						if (getEditButton() != null)
						{
							getEditButton().setOnAction(e -> editarDialog(listView, persistentObject.getObject()).getDialog().show());
							getEditButton().setBackgroundInHover("derive(white, -20%)");
						}
					}
				};
				
				return cell;
			}
		});
		
		refresh(listView, "", "", "");
		
		return listView;
	}
	
	public static void refresh(MaterialListView<Cliente> listView, String nomefiltro, String cpfFiltro, String cnpjFiltro)
	{
		listView.getItems().clear();
		
		try 
		{
			ClienteDAO.getInstance().getAll().forEach((k, v) ->
			{
				if (nomefiltro != null && !nomefiltro.isEmpty())
				{
					if (!v.getNome().toLowerCase().contains(nomefiltro.toLowerCase()))
						return;
				}
				if (cpfFiltro != null && !cpfFiltro.isEmpty())
				{
					if (!v.getCpfCnpj().toString().startsWith(cpfFiltro) || !v.getCpfCnpj().getType().equals("CPF"))
						return;
				}
				if (cnpjFiltro != null && !cnpjFiltro.isEmpty())
				{
					if (!v.getCpfCnpj().toString().startsWith(cnpjFiltro) || !v.getCpfCnpj().getType().equals("CNPJ"))
						return;
				}
				
				MaterialCellItem<Cliente> item = new MaterialCellItem<>();
				item.setLeftString(v.getNome());
				if (v.getNomeFantasia() != null && !v.getNomeFantasia().isEmpty())
					item.setLeftString(v.getNome() + " - " + v.getNomeFantasia());
				
				item.setRightString("Cliente número: " + v.getId());
				item.setDescriptionString("Endereço: " + v.getEndereço().toFormattedString());
				
				item.setObject(v);
				
				MaterialCellNode telefonesCellNode = new MaterialCellNode();
				telefonesCellNode.getLeftLabel().setText("Telefones: ");
				for (Telefone telefone : v.getTelefones())
				{
					telefonesCellNode.getRightLabel().setText(telefonesCellNode.getRightLabel().getText() + telefone.toFormattedString() + ",");
				}
				String newText = telefonesCellNode.getRightLabel().getText().substring(0, telefonesCellNode.getRightLabel().getText().length()-1);
				telefonesCellNode.getRightLabel().setText(newText);
				
				MaterialCellNode cpfCellNode = new MaterialCellNode();
				cpfCellNode.getLeftLabel().setText(v.getCpfCnpj().getType() + ": ");
				cpfCellNode.getRightLabel().setText(v.getCpfCnpj().toFormattedString());
				
				item.getItems().addAll(telefonesCellNode, cpfCellNode);
				
				listView.getItems().add(item);
			});
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	private static FlatJFXDialog editarDialog(MaterialListView<Cliente> listView, Cliente cliente)
	{
		StackPane pane = (StackPane)listView.getScene().lookup("#primaryStackPane");
    	
    	JFXButton confirmarButton = new JFXButton("Confirmar");
    	
    	String header = "Edite as informações do cliente";
    	String body = "";
    	FlatJFXDialog dialog = new FlatJFXDialog(pane, header, body, confirmarButton);
    	
    	Text nomeText = new Text("Insira o nome do cliente");
    	JFXTextField nomeField = new JFXTextField();
    	nomeField.setPromptText("Nome do cliente");
    	nomeField.textProperty().addListener((l, oldVal, newVal) ->
    	{
    		if (newVal.length() > 100)
    			nomeField.setText(oldVal);
    	});
    	
    	Text cpfText = new Text("Insira o CPF ou CPNJ do cliente\n(somente os números, sem ponto ou traço)");
    	JFXTextField cpfField = new JFXTextField();
    	cpfField.setPromptText("CPF ou CNPJ do cliente");
    	
    	Text endereçoText = new Text("Insira as informações do endereço do cliente");
    	JFXTextField endereçoField = new JFXTextField();
    	endereçoField.setPromptText("Insira a rua do endereço do cliente");
    	JFXTextField numeroField = new JFXTextField();
    	numeroField.setPromptText("Insira o número do endereço cliente");
    	JFXTextField bairroField = new JFXTextField();
    	bairroField.setPromptText("Insira o bairro do endereço do cliente");
    	JFXTextField cidadeField = new JFXTextField();
    	cidadeField.setPromptText("Insira a cidade do endereço do cliente");
    	JFXTextField estadoField = new JFXTextField();
    	estadoField.setPromptText("Insira o estado do endereço do cliente");
    	JFXTextField complementoField = new JFXTextField();
    	complementoField.setPromptText("Insira um complemento para o endereço se necessário");
    	
    	nomeField.setStyle("");
    	endereçoField.styleProperty().bind(nomeField.styleProperty());
    	numeroField.styleProperty().bind(nomeField.styleProperty());
    	bairroField.styleProperty().bind(nomeField.styleProperty());
    	cidadeField.styleProperty().bind(nomeField.styleProperty());
    	estadoField.styleProperty().bind(nomeField.styleProperty());
    	complementoField.styleProperty().bind(nomeField.styleProperty());
    	cpfField.styleProperty().bind(nomeField.styleProperty());
    	
    	Text telefonesText = new Text("Insira um ou dois números de telefone para o cliente");
    	Text telefone1Text = new Text("Telefone 1");
    	Text telefone2Text = new Text("Telefone 2");
    	JFXTextField telefone1Field = new JFXTextField();
    	telefone1Field.setPromptText("Telefone 1");
    	JFXTextField telefone2Field = new JFXTextField();
    	telefone2Field.setPromptText("Telefone 2");
    	telefone1Field.styleProperty().bind(nomeField.styleProperty());
    	telefone2Field.styleProperty().bind(nomeField.styleProperty());
    	
    	Utils.applyPhoneFormatter(telefone1Field);
    	Utils.applyPhoneFormatter(telefone2Field);
    	
    	JFXTextField cnpjField = new JFXTextField();
    	cnpjField.setVisible(false);
    	cnpjField.setPromptText("CNPJ do cliente");
    	
    	Utils.cpfFormatter(cpfField);
    	Utils.cnpjFormatter(cnpjField);
    	
    	JFXComboBox<String> comboBox = new JFXComboBox<String>();
    	comboBox.getItems().add("CPF");
    	comboBox.getItems().add("CNPJ");
    	comboBox.setStyle("-fx-background-color: transparent;");
    	
    	comboBox.getSelectionModel().selectedItemProperty().addListener((l, oldval, newval) ->
    	{
    		if (newval.equals("CPF"))
    		{
    			String text = cnpjField.getText().replace(".", "");
    			text = text.replace("-", "").replace("/", "");
    			
    			cpfField.setText(text);
    			
    			cpfField.setVisible(true);
    			cnpjField.setVisible(false);
    		}
    		else 
    		{
    			String text = cpfField.getText().replace(".", "");
    			text = text.replace("-", "");
    			
    			cnpjField.setText(text);
    			
    			cnpjField.setVisible(true);
    			cpfField.setVisible(false);
			}
    	});
    	
    	if (cliente.getCpfCnpj().getType() == "CPF")
    	{
    		comboBox.getSelectionModel().select(0);
    	}
    	else 
    	{
    		comboBox.getSelectionModel().select(1);
		}
    	
    	cpfField.setText(cliente.getCpfCnpj().toString());
    	cnpjField.setText(cliente.getCpfCnpj().toString());
    	
    	StackPane cpfcnpjPane = new StackPane(cpfField, cnpjField, comboBox);
    	cpfcnpjPane.prefHeightProperty().bind(cpfField.heightProperty());
    	cpfcnpjPane.prefWidthProperty().bind(cpfField.widthProperty());
    	cpfcnpjPane.setAlignment(Pos.CENTER_RIGHT);
    	
    	nomeField.setText(cliente.getNome());
    	endereçoField.setText(cliente.getEndereço().getRua());
    	numeroField.setText(cliente.getEndereço().getNumero());
    	bairroField.setText(cliente.getEndereço().getBairro());
    	cidadeField.setText(cliente.getEndereço().getCidade());
    	estadoField.setText(cliente.getEndereço().getEstado());
    	complementoField.setText(cliente.getEndereço().getComplemento());
    	
    	Telefone telefone1 = cliente.getTelefones().get(0);
    	telefone1Field.setText(telefone1.getDdd() + telefone1.getNúmero());
    	if (cliente.getTelefones().size() > 1) 
    	{
    		Telefone telefone2 = cliente.getTelefones().get(1);
			telefone2Field.setText(telefone2.getDdd() + telefone2.getNúmero());
		}
    	
    	Text erroText = new Text();
    	erroText.setFill(Color.RED);
    	
    	VBox vBox = new VBox(nomeText, nomeField, cpfText, cpfcnpjPane, endereçoText, endereçoField, numeroField, 
    			bairroField, cidadeField, estadoField, complementoField, telefonesText,
    			telefone1Text, telefone1Field, telefone2Text, telefone2Field);
    	vBox.setAlignment(Pos.TOP_LEFT);
    	vBox.setSpacing(15);
    	
    	ScrollPane scrollPane = new ScrollPane(vBox);
    	scrollPane.setFitToHeight(true);
    	scrollPane.setFitToWidth(true);
    	scrollPane.setStyle("-fx-background-color:transparent;");
    	
    	VBox mainvBox = new VBox(scrollPane, erroText);
    	
    	FollowableTextFieldGroup textFieldGroup = new FollowableTextFieldGroup();
    	textFieldGroup.getNodes().setAll(nomeField, cpfField, numeroField, bairroField, cidadeField, estadoField, complementoField,
    			telefone1Field, telefone2Field);
    	
    	dialog.getLayout().setBody(mainvBox);
    	
    	confirmarButton.setOnAction(e ->
    	{
    		if (endereçoField.getText().isEmpty() || cpfField.getText().isEmpty() || bairroField.getText().isEmpty() 
    				|| numeroField.getText().isEmpty() || cidadeField.getText().isEmpty() || nomeField.getText().isEmpty() 
    				|| estadoField.getText().isEmpty())
    		{
    			erroText.setText("Erro: campo(s) vazio(s)."); 
    			erroText.setVisible(true);
    			return;
    		}
    		
    		String CPF = cpfField.getText().replace(".", "").replace("-", "");
    		String CNPJ = cnpjField.getText().replace(".", "").replace("-", "").replace("/", "");
    		
    		if (comboBox.getSelectionModel().getSelectedIndex() == 0 && !Utils.isCPF(CPF))
    		{
    			erroText.setText("Erro: CPF inválido.");
    			erroText.setVisible(true);
    			return;
    		}
    		
    		if (comboBox.getSelectionModel().getSelectedIndex() == 1 && !Utils.isCNPJ(CNPJ))
    		{
    			erroText.setText("Erro: CNPJ inválido.");
    			erroText.setVisible(true);
    			return;
    		}
    		
    		
    		if ((telefone1Field.getText().contains(" ") && telefone2Field.getText().contains(" ")))
    		{
    			erroText.setText("Erro: número(s) de telefone(s) inválido(s).");
    			erroText.setVisible(true);
    			return;
    		}
    		erroText.setVisible(false);
    		
    		cliente.setNome(nomeField.getText());
    		cliente.setEndereço(new Endereço(endereçoField.getText(), numeroField.getText(), bairroField.getText(), 
    				cidadeField.getText(), estadoField.getText(), complementoField.getText()));

    		if (comboBox.getSelectionModel().getSelectedIndex() == 0)
    		{
    			cliente.setCpfCnpj(IdentificaçãoFactory.identificávelFromString(CPF));
    		}
    		else 
    		{
    			cliente.setCpfCnpj(IdentificaçãoFactory.identificávelFromString(CNPJ));
			}
    		
    		if (complementoField.getText().isEmpty())
    		{
    			cliente.getEndereço().setComplemento("Sem complemento");
    		}
    		
    		ObservableList<Telefone> telefones = FXCollections.observableArrayList();
    		
    		Consumer<JFXTextField> consumer = field ->
    		{
    			String[] split = field.getText().replace("(", " ").replace(")", " ").replace("-", " ").split("\\ ");
    			
    			String pais = "0055";
    			String ddd = split[1];
    			String numero = split[2] + split[3];
    			
    			telefones.add(new Telefone(pais, ddd, numero));
    		};
    		
    		if (!telefone1Field.getText().contains(" "))
    		{
    			consumer.accept(telefone1Field); 
    		}
    		if (!telefone2Field.getText().contains(" "))
    		{
    			consumer.accept(telefone2Field);
    		}
    		
    		cliente.setTelefones(telefones);
    		
    		dialog.getLayout().getBody().clear();
    		dialog.getLayout().getActions().clear();
    		dialog.setHeader("Processando...");
    		dialog.getLayout().setBody(new JFXSpinner());
    		
    		ExecuterServices.getExecutor().execute(() ->
    		{
    			SimpleBooleanProperty ret = new SimpleBooleanProperty();
    			try 
    			{
					ret.set(ClienteDAO.getInstance().update(cliente, ConnectionFactory.getOfflineConnection()));
				}
    			catch (Exception e2) 
    			{
					e2.printStackTrace();
					ret.set(false);
				}
    			
    			Platform.runLater(() ->
    			{
    				if (ret.get())
    				{
    					dialog.setHeader("Sucesso");
    					dialog.setBody("Operação concluída com sucesso.");
    				}
    				else
    				{
    					dialog.setHeader("Falha");
    					dialog.setBody("Falha ao realizar a operação, tente novamente mais tarde.");
    				}
    				
    				ClientesListaManager.refresh(listView, "", "", "");
    			});
    		});
    	});
    	textFieldGroup.lastEventHandlerProperty().bind(confirmarButton.onActionProperty());
    	
    	return dialog;
	}
	
	private static FlatJFXDialog removerDialog(MaterialListView<Cliente> listView, Cliente cliente)
    {
    	JFXButton simButton = new JFXButton("Sim");
    	JFXButton naoButton = new JFXButton("Não");
    	
    	StackPane pane = (StackPane)listView.getScene().lookup("#primaryStackPane");
    	
    	String header = "Confirme sua ação";
    	String body = "Tem certeza que deseja excluir todos os produtos selecionados? (isso não poderá ser desfeito)";
    	FlatJFXDialog dialog = new FlatJFXDialog(pane, header, body, simButton, naoButton);
    	
    	simButton.setOnAction(e ->
    	{
    		dialog.setHeader("Processando...");
    		dialog.getLayout().setBody(new JFXSpinner());
    		dialog.getLayout().getActions().clear();
    		
    		ExecuterServices.getExecutor().execute(() ->
    		{
    			SimpleBooleanProperty success = new SimpleBooleanProperty(true); 
    			
    			try 
    			{
					if (!ClienteDAO.getInstance().remove(cliente, ConnectionFactory.getOfflineConnection()))
						success.set(false);
				} 
    			catch (SQLException e1)
    			{
					e1.printStackTrace();
					success.set(false);
				}
    			
    			Platform.runLater(() ->
    			{
    				if(success.get())
    				{
    					dialog.setHeader("Sucesso");
    					dialog.setBody("Operação realizada com sucesso!");
    				}
    				else 
    				{
    					dialog.setHeader("Falha");
    					dialog.setBody("Ocorreu um erro na operação, tente novamente.");
					}
    				
    				ClientesListaManager.refresh(listView, "", "", "");
    			});
    		});
    	});
    	
    	naoButton.setOnAction(e -> 
    	{
    		dialog.getDialog().close();
    	});
    	
    	return dialog;
    }
}
