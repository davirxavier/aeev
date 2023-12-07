package com.davixavier.panes.historico;

import java.sql.Timestamp;
import java.time.LocalDate;

import com.davixavier.application.img.IconsPath;
import com.davixavier.entidades.compras.TipoPagamento;
import com.davixavier.entidades.compras.Venda;
import com.davixavier.nodes.SearchBar;
import com.davixavier.nodes.ToolbarButton;
import com.davixavier.utils.TreeWrapper;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTreeTableView;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class HistoricoController 
{

    @FXML
    private BorderPane primaryPane;
    
    @FXML
    private AnchorPane toolbarPane;

    @FXML
    private JFXTreeTableView<TreeWrapper<Venda>> tabelaHistorico;

    @FXML
    private JFXDatePicker dataInicioPicker;

    @FXML
    private JFXDatePicker dataFimPicker;
    
    @FXML
    private JFXComboBox<String> tipopagamentoCombobox;
    
    private SearchBar searchBar;
    
    private String tipoPagamentoSearch;
    private String nomeProdutoSearch;
    
    @FXML
    void initialize()
    {
    	tipoPagamentoSearch = "";
    	nomeProdutoSearch = "";
//    	dataInicioPicker.setValue(LocalDate.of(2019, 01, 01));
//    	dataFimPicker.setValue(LocalDate.now());
    	
    	dataInicioPicker.focusedProperty().addListener(l ->
    	{
    		if (dataInicioPicker.isFocused() && !dataInicioPicker.isShowing())
    			dataInicioPicker.show();
    	});
    	dataInicioPicker.setOnHidden(e ->
    	{
    		Platform.runLater(() -> toolbarPane.requestFocus());
    	});
    	dataFimPicker.focusedProperty().addListener(l ->
    	{
    		if (dataFimPicker.isFocused() && !dataFimPicker.isShowing())
    			dataFimPicker.show();
    	});
    	dataFimPicker.setOnHidden(e ->
    	{
    		Platform.runLater(() -> toolbarPane.requestFocus());
    	});
    	
    	dataInicioPicker.setOnAction(e ->
    	{
    		if (dataInicioPicker.getValue() != null && dataFimPicker.getValue() != null 
    				&& dataInicioPicker.getValue().isAfter(dataFimPicker.getValue()))
    		{
    			dataInicioPicker.setValue(dataFimPicker.getValue());
    		}
    		
    		filtrar(null);
    	});
    	dataFimPicker.onActionProperty().bind(dataInicioPicker.onActionProperty());
    	
    	HistoricoTabelaManager.setUp(tabelaHistorico);
    	
    	setUpCombobox();
    	
    	searchBar = new SearchBar();
    	searchBar.getTextField().setPromptText("Pesquisar por nome de produto...");
    	toolbarPane.getChildren().add(searchBar);
    	AnchorPane.setTopAnchor(searchBar, 9.0);
    	AnchorPane.setLeftAnchor(searchBar, 115.0);
    	AnchorPane.setRightAnchor(searchBar, 442.0);
    	
    	searchBar.getTextField().setOnAction(e ->
    	{
    		nomeProdutoSearch = searchBar.getTextField().getText();
    		filtrar(null);
    	});
    	searchBar.getClearButton().setOnAction(e ->
    	{
    		searchBar.getTextField().clear();
    		
    		nomeProdutoSearch = "";
    		
    		filtrar(null);
    	});
    	
    	ToolbarButton clearDataButton = new ToolbarButton(IconsPath.CLOSE18PXWHITE.getImage());
    	clearDataButton.setBackgroundInHover("derive(-buttoncolorlight, 40%)");
    	clearDataButton.setBackgroundInUnhover("derive(-buttoncolorlight, 25%)");
    	toolbarPane.getChildren().add(clearDataButton);
    	AnchorPane.setTopAnchor(clearDataButton, 10.0);
    	AnchorPane.setRightAnchor(clearDataButton, 175.0);
    	
    	clearDataButton.setOnAction(e ->
    	{
    		dataInicioPicker.setValue(null);
    		dataFimPicker.setValue(null);
    	});  	
    }

    @FXML
    void filtrar(ActionEvent event) 
    {
    	HistoricoTabelaManager.filterTable(tabelaHistorico, 
				dataInicioPicker.getValue(),
				dataFimPicker.getValue(), 
				tipoPagamentoSearch, 
				nomeProdutoSearch);
    }
    
    private void setUpCombobox()
    {
    	tipopagamentoCombobox.setEditable(false);
    	
    	tipopagamentoCombobox.getItems().add("Todos");
    	
    	for (TipoPagamento tipo : TipoPagamento.values())
    	{
    		tipopagamentoCombobox.getItems().add(tipo.toString());
    	}
    	
    	tipopagamentoCombobox.setPromptText("  Tipo de pagamento");
    	
    	tipopagamentoCombobox.getSelectionModel().selectedItemProperty().addListener((l, oldval, newval) ->
    	{
    		if (newval == "Todos" || newval == null)
    		{
    			tipoPagamentoSearch = "";
    			filtrar(null);
    			
    			return;
    		}
    		
    		tipoPagamentoSearch = newval;
    		filtrar(null);
    	});
    	
    }

}
