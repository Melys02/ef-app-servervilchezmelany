package com.example.demo.controller;

import com.example.demo.dto.UsuarioResponseDto;
import com.example.demo.model.Usuario;
import com.example.demo.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

}

