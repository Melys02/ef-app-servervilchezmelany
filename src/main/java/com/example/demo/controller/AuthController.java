package com.example.demo.controller;

import com.example.demo.dto.UsuarioResponseDto;
import com.example.demo.model.Usuario;
import com.example.demo.service.UsuarioService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.register(usuario));
    }

    @GetMapping("/user/nomusuario")
    public ResponseEntity<Optional<Usuario>> getUser(@PathVariable String nomusuario) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioXnombre(nomusuario);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }
}

