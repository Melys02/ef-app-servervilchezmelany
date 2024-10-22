package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonTypeId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "usuario")
public class Usuario {

}