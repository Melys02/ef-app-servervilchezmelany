package com.example.demo.service;

import com.example.demo.model.Usuario;

import java.util.Optional;

public interface IUsuarioService {
    Optional<Usuario> obtenerUsuarioXnombre(String nomusuario);

}
