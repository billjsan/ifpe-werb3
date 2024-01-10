/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ifpe.edu.br.controllers;

import com.ifpe.edu.br.dao.Repository;
import com.ifpe.edu.br.model.Pet;
import com.ifpe.edu.br.model.Postagem;
import com.ifpe.edu.br.model.Tutor;
import com.ifpe.edu.br.model.Video;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author wjeff
 */
@ManagedBean
@SessionScoped
public class PostagemController implements Serializable{
    
    private Postagem postagemCadastro;
    private Video videoTutor;
    private Video videoPet;

    private String tagVideoPet;
    private String tagVideoTutor;
    
    @PostConstruct
    public void init(){
        this.postagemCadastro = new Postagem();
        this.videoPet = new Video();
        this.videoTutor = new Video();
    }
    
    public Postagem getPostagemCadastro() {
        return postagemCadastro;
    }

    public void setPostagemCadastro(Postagem postagemCadastro) {
        this.postagemCadastro = postagemCadastro;
    }

    public String getTagVideoPet() {
        return tagVideoPet;
    }

    public void setTagVideoPet(String tagVideoPet) {
        this.tagVideoPet = tagVideoPet;
    }

    public String getTagVideoTutor() {
        return tagVideoTutor;
    }

    public void setTagVideoTutor(String tagVideoTutor) {
        this.tagVideoTutor = tagVideoTutor;
    }

    public Video getVideoTutor() {
        return videoTutor;
    }

    public void setVideoTutor(Video videoTutor) {
        this.videoTutor = videoTutor;
    }

    public Video getVideoPet() {
        return videoPet;
    }

    public void setVideoPet(Video videoPet) {
        this.videoPet = videoPet;
    }
    
     public void handleFileUploadPet(FileUploadEvent event) throws IOException {

        byte[] video = new byte[(int) event.getFile().getSize()];

        try {
            String nome = event.getFile().getFileName();
            String nomeArquivo = nome.split("\\.")[0];
            String extensao = nome.split("\\.")[1];
            
            event.getFile().getInputstream().read(video);

            this.videoPet.setNomeArquivo(nomeArquivo);
            this.videoPet.setExtensao(extensao);
            this.videoPet.setTamanho(event.getFile().getSize());
            this.videoPet.setArquivo(video);

            Repository.getInstance().insert(this.videoPet);
            this.postagemCadastro.setCodigoVideoPet(this.videoPet.getCodigo());

            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage("Video Pet Uploaded"));

             //gambiarra
            ((HttpSession)FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true)).setAttribute("video", this.videoPet.getArquivo());

            this.tagVideoPet = "http://localhost:8080/TicToguinho/ServletExibirVideo";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("formRegistro", 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro no upload do arquivo!",""));
            System.out.println(e);
        }
     }
    
     public void handleFileUploadTutor(FileUploadEvent event) throws IOException {

        byte[] video = new byte[(int) event.getFile().getSize()];

        try {
            String nome = event.getFile().getFileName();
            String nomeArquivo = nome.split("\\.")[0];
            String extensao = nome.split("\\.")[1];
            
            event.getFile().getInputstream().read(video);

            this.videoTutor.setNomeArquivo(nomeArquivo);
            this.videoTutor.setExtensao(extensao);
            this.videoTutor.setTamanho(event.getFile().getSize());
            this.videoTutor.setArquivo(video);

            Repository.getInstance().insert(this.videoTutor);
            this.postagemCadastro.setCodigoVideoTutor(this.videoTutor.getCodigo());

            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage("Video Tutor Uploaded"));
             //gambiarra
            ((HttpSession)FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true)).setAttribute("video"
                        ,this.videoTutor.getArquivo());
            this.tagVideoTutor = "http://localhost:8080/TicToguinho/ServletExibirVideo";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("formRegistro", 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro no upload do arquivo!",""));
            System.out.println(e);
        }
     }
    
    public void inserirNovaPostagem(){
        
        if (this.postagemCadastro.getCodigoVideoPet() <= -1 || this.postagemCadastro.getCodigoVideoTutor() <= -1) {
            FacesContext.getCurrentInstance().addMessage("formRegistro", 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Por favor, lembre de adicionar os dois videos!",""));
            
            return;
        }
        
        Pet pet = ((PetController)((HttpSession)FacesContext.getCurrentInstance().getExternalContext()
                 .getSession(true)).getAttribute("petController")).getSelection();

        Tutor tutor = ((LoginController)((HttpSession)FacesContext.getCurrentInstance().getExternalContext()
                 .getSession(true)).getAttribute("loginController")).getUsuarioLogado();

        Date dataDeCreação = new Date();
        
        this.postagemCadastro.setTutorQueInseriu(tutor);
        this.postagemCadastro.setPetRelacionado(pet);
        this.postagemCadastro.setCreated(dataDeCreação);
       
         Repository.getInstance().insert(this.postagemCadastro);
        
        this.postagemCadastro = new Postagem();
        
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage("Postagem cadastrada com sucesso!"));
        
        this.tagVideoPet = null;
        this.tagVideoTutor = null;
    }
    
    public List<Postagem> readPostagens() {
        List<Postagem> postagens = null;
        
        try {
            Pet pet = ((PetController)((HttpSession)FacesContext.getCurrentInstance().getExternalContext()
                 .getSession(true)).getAttribute("petController")).getSelection();
        
            postagens =  Repository.getInstance()
                .read("select p from Postagem p"
                            + " where p.petRelacionado.codigo = '" + pet.getCodigo()+"' order by p.created desc", Postagem.class);
            
        } catch (Exception e) {
            System.out.println(e);
            postagens = (List<Postagem>) new ArrayList<Postagem>();
        }
        
        return postagens;
    }
    
    public List<Postagem> readPostagensPetsSeguindo() {
        List<Postagem> postagens = null;
        
        try {
            Pet pet = ((PetController)((HttpSession)FacesContext.getCurrentInstance().getExternalContext()
                 .getSession(true)).getAttribute("petController")).getSelection();
            String values = pet.getSeguindoList().stream().map(str -> String.format("'%s'", str)).collect(Collectors.joining(","));
            System.out.println(values);
            postagens =  Repository.getInstance()
                .read("select p from Postagem p"
                            + " where p.petRelacionado.codigo in (" + values +") order by p.created desc", Postagem.class);
            
        } catch (Exception e) {
            System.out.println(e);
            postagens = (List<Postagem>) new ArrayList<Postagem>();
        }
        
        return postagens;
    }
}
