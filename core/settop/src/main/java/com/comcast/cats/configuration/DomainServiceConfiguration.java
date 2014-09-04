/**
 * Copyright 2014 Comcast Cable Communications Management, LLC
 *
 * This file is part of CATS.
 *
 * CATS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CATS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CATS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.comcast.cats.configuration;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.comcast.cats.domain.Domain;
import com.comcast.cats.domain.converter.JaxbListHttpMessageConverter;
import com.comcast.cats.domain.converter.JaxbSerializedHttpMessageConverter;

/**
 * 
 * @author subinsugunan
 * 
 */
@Configuration
public class DomainServiceConfiguration< T extends Domain > extends BaseConfiguration
{
    protected final Logger LOGGER = LoggerFactory.getLogger( getClass() );

    /**
     * To get rest template.
     * 
     * @return RestTemplate
     */
    @Bean
    public RestTemplate getRestTemplate()
    {
        RestTemplate restTemplate = new RestTemplate();
        List< HttpMessageConverter< ? >> messageConverters = new ArrayList< HttpMessageConverter< ? > >();
        messageConverters.add( new JaxbSerializedHttpMessageConverter< T >() );
        messageConverters.add( new JaxbListHttpMessageConverter() );
        messageConverters.add( new StringHttpMessageConverter() );
        restTemplate.setMessageConverters( messageConverters );
        return restTemplate;
    }
}