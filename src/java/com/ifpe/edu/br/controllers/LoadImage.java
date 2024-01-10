/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ifpe.edu.br.controllers;

import com.ifpe.edu.br.dao.Repository;
import com.ifpe.edu.br.model.Foto;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author wjeff
 */
@WebServlet(name = "LoadImage", urlPatterns = {"/LoadImage"})
public class LoadImage extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) {
       try {
        int codigo = Integer.parseInt(request.getParameter("codFoto"));
        System.out.println("Código da imagem: " + codigo);

        Foto foto = (Foto) Repository.getInstance()
                .read("SELECT f FROM Foto f WHERE f.codigo = " + codigo, Foto.class).get(0);

        if (foto != null) {
            response.setContentType("image/jpeg");
            response.getOutputStream().write(foto.getArquivo());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } else {
            System.out.println("Foto não encontrada para o código: " + codigo);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    } catch (IOException | NumberFormatException | IndexOutOfBoundsException ex) {
        Logger.getLogger(LoadImage.class.getName()).log(Level.SEVERE, null, ex);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
