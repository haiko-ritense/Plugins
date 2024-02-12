package com.ritense.valtimo.backend.plugin.config

import com.ritense.valtimo.contract.security.config.HttpConfigurerConfigurationException
import com.ritense.valtimo.contract.security.config.HttpSecurityConfigurer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import javax.ws.rs.HttpMethod

class PublicTaskSecurityConfigurer: HttpSecurityConfigurer {

    override fun configure(http: HttpSecurity) {
        try {
            http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/v1/public-task/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/public-task/**").permitAll()
        } catch (e: Exception) {
            throw HttpConfigurerConfigurationException(e)
        }
    }
}