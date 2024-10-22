package com.example.demo.service;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UsuarioService implements IUsuarioService {
    private final UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<Usuario> obtenerUsuarioXnombre(String nomusuario) {
        return usuarioRepository.findByUsername(nomusuario);
    }

    public Usuario register(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }
}