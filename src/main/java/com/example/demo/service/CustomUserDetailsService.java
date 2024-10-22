package com.example.demo.service;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService  implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String nomusuario) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findByUsername(nomusuario);
        if (usuario.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado con el nombre: " + nomusuario);
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(String.valueOf(usuario.get().getIdusuario()))
                .password(usuario.get().getPassword())
                .roles(usuario.get().getRol())
                .disabled(!usuario.get().getActivo())
                .build();
    }
    private UserDetails crearUserDetail(
            Usuario usuario, List<GrantedAuthority> authorityList
    ){
        return new User(
                usuario.getNomusuario(),
                usuario.getPassword(),
                usuario.getActivo(),
                true,
                true,
                true,
                authorityList);
    }
}