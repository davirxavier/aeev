package com.davixavier.panes.usuarios;

import com.davixavier.entidades.usuarios.Usuário;
import com.davixavier.utils.TreeWrapper;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class UsuáriosController
{

    @FXML
    private BorderPane primaryPane;

    @FXML
    private JFXTreeTableView<TreeWrapper<Usuário>> usuariosTable;

    @FXML
    private JFXTextField pesquisaField;

    @FXML
    private ImageView cancelimg;

    @FXML
    void adicionarGerente(ActionEvent event) 
    {

    }

    @FXML
    void adicionarVendedor(ActionEvent event) 
    {

    }

    @FXML
    void limparPesquisa(MouseEvent event) 
    {

    }

    @FXML
    void pesquisar(ActionEvent event) 
    {

    }

}
