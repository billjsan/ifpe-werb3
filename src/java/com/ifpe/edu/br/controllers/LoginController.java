/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ifpe.edu.br.controllers;

import com.ifpe.edu.br.dao.Repository;
import com.ifpe.edu.br.model.Tutor;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author wjeff
 */
@ManagedBean
@SessionScoped

public class LoginController  implements Serializable  {
    private Tutor usuarioLogado;
    
    public String realizarLogin(String login, String senha) {
        System.out.println("login:" + login + " senha:" + senha);
        try {
            Tutor uLogin = (Tutor) Repository.getInstance()
                    .read("select u from Tutor u"
                            + " where u.usuario = '" + login
                            + "' and u.senha = '" + senha+"'", Tutor.class)
                    .get(0);
            this.usuarioLogado = uLogin;
            return "indexTutor";
        } catch (Exception e) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, 
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao Logar","Usuário e/ou senha estão incorretos"));
            return null;
        }
    }
    
    public String realizarCadastro()  {
        return "irParaTelaDeCadastro";
    }
    
    public String logout(){
        this.usuarioLogado = null; 
        return "tg_login";
    }
    
    public Tutor getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Tutor usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }
}
