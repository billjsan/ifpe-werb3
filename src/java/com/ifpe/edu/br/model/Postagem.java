/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ifpe.edu.br.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author wjeff
 */
@Entity
public class Postagem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int codigo;
}
