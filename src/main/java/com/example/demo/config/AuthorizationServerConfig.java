package com.example.demo.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

@Configuration

public class AuthorizationServerConfig {

    private KeyPair keyPair;

    public AuthorizationServerConfig(){
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator
                    .getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }catch (Exception ex){
            throw new RuntimeException("Error a generar claves RSA", ex);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    //Este metodo realiza la configuracion predeterminada para lo que es OauthAutorization
    //esto incluye manejo y proteccion de las rutas utilizando token y ademas
    //SecurityFilterChain sirve para administrar o asegurar los filtros e seguridad que
    //que se aplicaran en las entradas de las solicitudes
    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity httpSecurity) throws Exception{
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);
        return httpSecurity.build();
    }

    //este metodo se va a encargar de definir detalles puntuales del cliente Oauth
    //como el clientid el clientsecret, que tipo de autenticacion vamos a utilizar, que son credenciales del cliente
    //los alcances o ambitos si van a ser de lectura o escritura y tmb se determina el timepo de vida del token
    //y todo esto lo va a almacenar en memoria utilizando esta clase
    @Bean
    public RegisteredClientRepository registeredClientRepository(){
        RegisteredClient registeredClient =
                RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("client")
                        .clientSecret(passwordEncoder().encode("secret"))
                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .scope("read")
                        .scope("write")
                        .tokenSettings(TokenSettings.builder()
                                .accessTokenTimeToLive(Duration.ofMinutes(30)).build())
                        .clientSettings(ClientSettings.builder().build())
                        .build();
        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    //este motodo se va a encargar de configurar el JwtEncoder
    @Bean
    public JwtEncoder jwtEncoder(){
        return new NimbusJwtEncoder(jwkSource());
    }
    //definimos una llave en la parte superior, nos va ayudar para poder firmar el token
    //jwt que haremos
    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(
                (RSAPublicKey) keyPair.getPublic()).build();
    }
    @Bean
    public AuthorizationServerSettings authorizationServerSettings(){
        return AuthorizationServerSettings.builder()
                .jwkSetEndpoint("/.well-known/jwks.json")
                .build();
    }

    @Bean
    public KeyPair jwkSetKeyPair(){
        return keyPair;
    }
    //Crea el JWKSource a partir de una Key
    @Bean
    public JWKSource<SecurityContext> jwkSource(){
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return ((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
    }

}
