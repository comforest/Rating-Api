package com.nomean.rating.api.security


import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Role
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.WebSecurityConfigurer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.FilterChainProxy
import org.springframework.security.web.SecurityFilterChain
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*


/**
 * OAuth Authorization Server Configuration.
 *
 * @author Steve Riesenberg
 */
@Configuration
@EnableWebSecurity
open class OAuth2AuthorizationServerSecurityConfiguration :WebSecurityConfigurerAdapter() {

//    @Bean(name = ["springSecurityFilterChain"])
//    open fun getFilterChain(): FilterChainProxy {
//        println("Test Proxy")
//        return FilterChainProxy(listOf())
//    }

    override fun configure(http: HttpSecurity) {
//        super.configure(http)
        http.authorizeRequests()
//            .antMatchers(HttpMethod.POST, "/v1/auth/login").permitAll()
            .antMatchers("/v1/auth/**","/docs/**").permitAll()
//            .antMatchers("/admin/**").hasAnyRole("        ADMIN")
//            .antMatchers("/order/**").hasAnyRole("USER")
            .anyRequest().authenticated()
            .and()
            .csrf().disable();
    }

//    @Bean
//    @Order(1)
//    @Throws(Exception::class)
//    open fun authorizationServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
//
//        return http.formLogin(Customizer.withDefaults()).build()
//    }
//
//    @Bean
//    @Order(2)
//    @Throws(Exception::class)
//    open fun standardSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
//        // @formatter:off
//        http
//            .authorizeHttpRequests { authorize ->
//                authorize
//                    .anyRequest().authenticated()
//            }
//            .formLogin(Customizer.withDefaults())
//        // @formatter:on
//        return http.build()
//    }
//
//    @Bean
//    open fun registeredClientRepository(): RegisteredClientRepository {
//        // @formatter:off
//        val loginClient: RegisteredClient = RegisteredClient.withId(UUID.randomUUID().toString())
//            .clientId("login-client")
//            .clientSecret("{noop}openid-connect")
//            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//            .redirectUri("http://127.0.0.1:8080/login/oauth2/code/login-client")
//            .redirectUri("http://127.0.0.1:8080/authorized")
//            .scope(OidcScopes.OPENID)
//            .scope(OidcScopes.PROFILE)
//            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
//            .build()
//        val registeredClient: RegisteredClient = RegisteredClient.withId(UUID.randomUUID().toString())
//            .clientId("messaging-client")
//            .clientSecret("{noop}secret")
//            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//            .scope("message:read")
//            .scope("message:write")
//            .build()
//        // @formatter:on
//        return InMemoryRegisteredClientRepository(loginClient, registeredClient)
//    }
//
//    @Bean
//    open fun jwkSource(keyPair: KeyPair): JWKSource<SecurityContext> {
//        val publicKey = keyPair.public as RSAPublicKey
//        val privateKey = keyPair.private as RSAPrivateKey
//        // @formatter:off
//        val rsaKey: RSAKey = Builder(publicKey)
//            .privateKey(privateKey)
//            .keyID(UUID.randomUUID().toString())
//            .build()
//        // @formatter:on
//        val jwkSet = JWKSet(rsaKey)
//        return ImmutableJWKSet(jwkSet)
//    }
//
//    @Bean
//    open fun jwtDecoder(keyPair: KeyPair): JwtDecoder {
//        return NimbusJwtDecoder.withPublicKey(keyPair.public as RSAPublicKey).build()
//    }
//
//    @Bean
//    open fun providerSettings(): ProviderSettings {
//        return ProviderSettings.builder().issuer("http://localhost:9000").build()
//    }
//
//    @Bean
//    open fun userDetailsService(): UserDetailsService {
//        // @formatter:off
//        val userDetails: UserDetails = User.withDefaultPasswordEncoder()
//            .username("user")
//            .password("password")
//            .roles("USER")
//            .build()
//        // @formatter:on
//        return InMemoryUserDetailsManager(userDetails)
//    }
//
//    @Bean
//    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
//    open fun generateRsaKey(): KeyPair {
//        val keyPair: KeyPair
//        keyPair = try {
//            val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
//            keyPairGenerator.initialize(2048)
//            keyPairGenerator.generateKeyPair()
//        } catch (ex: Exception) {
//            throw IllegalStateException(ex)
//        }
//        return keyPair
//    }
}