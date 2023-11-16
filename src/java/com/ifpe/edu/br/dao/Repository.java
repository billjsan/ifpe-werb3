/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ifpe.edu.br.dao;

import com.ifpe.edu.br.model.Tutor;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author wjeff
 */
public class Repository {
    private static Repository instance;

    public static Repository getInstance(){
        if(instance == null)
            instance = new Repository();
        return instance;
    }

    private EntityManagerFactory emf = null;

    private Repository(){
        this.emf = Persistence.createEntityManagerFactory("TicDoguinhoPU");
    }

    public void insert(Object o){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(o);
        em.flush();
        em.getTransaction().commit();
        em.close();
    }

    public void update(Object o){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(o);
        em.getTransaction().commit();
        em.close();
    }

    public List read(String query,Class c){
        EntityManager em = emf.createEntityManager();
        List returnedList = em.createQuery(query,c).getResultList();
        em.close();
        return returnedList;
    }

    public void delete(Object o){
        EntityManager em = emf.createEntityManager();
        Object oDelete = o;
        if(!em.contains(o)){
            oDelete = em.merge(o);
        }
        em.getTransaction().begin();
        em.remove(oDelete);
        em.getTransaction().commit();
        em.close();
    }
    
    public static void main(String args[]){
        Tutor u = new Tutor();
        u.setEmail("joaopaulorockfeler@gmail.com");
        u.setUsuario("rockfeler123");
        u.setSenha("1234");
        getInstance().insert(u);
    }
}