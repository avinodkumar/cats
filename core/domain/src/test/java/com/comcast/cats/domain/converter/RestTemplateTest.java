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
package com.comcast.cats.domain.converter;

import java.util.List;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.comcast.cats.domain.service.RestTemplateProducer;

/**
 * Test case for {@link RestTemplate} used in domain-api-impl project.
 * 
 * @author subinsugunan
 * 
 */
@ContextConfiguration( locations =
    { "classpath:application-config-test.xml" } )
@RunWith( SpringJUnit4ClassRunner.class )
public class RestTemplateTest
{
    private final Logger         logger = LoggerFactory.getLogger( getClass() );

    @Inject
    private RestTemplateProducer restTemplateProducer;

    @Test
    public void fileContextTest()
    {
        Assert.assertNotNull( "restTemplateProducer cannot be null", restTemplateProducer );
        Assert.assertNotNull( "restTemplateProducer.getRestTemplate() cannot be null",
                restTemplateProducer.getRestTemplate() );

        Assert.assertNotNull( "restTemplateProducer.getRestTemplate() cannot be null",
                restTemplateProducer.getRestTemplate() );

        List< HttpMessageConverter< ? > > messageConverters = restTemplateProducer.getRestTemplate()
                .getMessageConverters();

        Assert.assertNotNull( "messageConverters cannot be null", messageConverters );

        for ( HttpMessageConverter< ? > messageConverter : messageConverters )
        {
            Assert.assertNotNull( "messageConverter cannot be null", messageConverter );

            List< MediaType > supportedMediaTypes = messageConverter.getSupportedMediaTypes();

            Assert.assertNotNull( supportedMediaTypes );

            for ( MediaType mediaType : supportedMediaTypes )
            {
                Assert.assertNotNull( mediaType );
                logger.info( mediaType.toString() );
            }
        }
    }
}
