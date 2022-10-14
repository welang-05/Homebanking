package com.mindhub.homebanking.Configuration;

import com.mindhub.homebanking.Services.ClientServices;
import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    ClientServices clientServices;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(inputName -> {

           Client client = clientServices.findClientByEmail(inputName);

            if (client != null) {

                if (client.getEmail().equals("admin@admin.com")){
                    return new User(client.getEmail(), client.getPassword(),

                            AuthorityUtils.createAuthorityList("ADMIN"));
                } else {
                    return new User(client.getEmail(), client.getPassword(),

                            AuthorityUtils.createAuthorityList("CLIENT"));
                }

            } else {

                throw new UsernameNotFoundException("Unknown user: " + inputName);

            }

        });

    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();

    }

}
