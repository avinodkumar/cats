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
package com.comcast.cats.domain.service.it;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.comcast.cats.domain.SettopDesc;
import com.comcast.cats.domain.User;
import com.comcast.cats.domain.service.SettopDomainService;

/**
 * Integrations test cases for criteria based search APIs in
 * {@link SettopDomainService}.
 * 
 * <pre>
 * <b>Prerequisites:</b>
 * 
 * 1. You should have a valid reservation in the configuration management system.
 * 2. You should have to provide the following properties in <b>/src/test/resources/test.props</b>. 
 *  
 *    test.property.one           :   First property of {@link SettopDesc}
 *    test.value.one              :   Value for first property
 *    test.property.two           :   Second property of {@link SettopDesc}
 *    test.value.two              :   Value for second property
 *    test.offset                 :   Offset
 *    test.count                  :   Count  
 *    
 *    E.g.
 *    test.property.one           =   Manufacturer
 *    test.value.one              =   Pace
 *    test.property.two           =   Model
 *    test.value.two              =   DC50Xu-Micro
 *    test.offset                 =   0
 *    test.count                  =   1
 *    
 * 3. You should have cats.props file under $CATS_HOME directory with at least the following entries.
 * 
 *   cats.config.url            :   REST interface url of the configuration management system.
 *   cats.user.authToken        :   Authentication token of the {@link User} in the configuration management system.
 *   
 *   E.g.
 *   cats.config.url            =   http://192.168.160.201:8080/rest/cats/
 *   cats.user.authToken        =   567468
 * </pre>
 * 
 * @author subinsugunan
 * 
 */
public class SettopDomainServiceBasedOnCriteriaIT extends BaseSettopDomainServiceIT
{
    @Test( expected = IllegalArgumentException.class )
    public void findAllByCriteriaNull()
    {
        settopDomainService.findAllByCriteria( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAllByCriteriaEmpty()
    {
        Map< String, String > criteria = Collections.emptyMap();
        settopDomainService.findAllByCriteria( criteria );
    }

    @Test
    public void findAllByCriteriaOneValue()
    {
        Map< String, String > criteria = new LinkedHashMap< String, String >();
        criteria.put( testProperties.getPropertyOne(), testProperties.getPropertyValueOne() );

        List< SettopDesc > settops = settopDomainService.findAllByCriteria( criteria );
        logResult( settops.size() );
    }

    @Test
    public void findAllByCriteriaTwoValue()
    {
        Map< String, String > criteria = new LinkedHashMap< String, String >();
        criteria.put( testProperties.getPropertyOne(), testProperties.getPropertyValueOne() );
        criteria.put( testProperties.getPropertyTwo(), testProperties.getPropertyValueTwo() );

        List< SettopDesc > settops = settopDomainService.findAllByCriteria( criteria );
        logResult( settops.size() );
    }

    @Test
    public void findAllByCriteriaOffset()
    {
        Map< String, String > criteria = new LinkedHashMap< String, String >();
        criteria.put( testProperties.getPropertyOne(), testProperties.getPropertyValueOne() );
        criteria.put( testProperties.getPropertyTwo(), testProperties.getPropertyValueTwo() );

        List< SettopDesc > settops = settopDomainService.findAllByCriteria( criteria, offset, count );
        logResult( settops.size() );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByCriteriaNull()
    {
        settopDomainService.findAvailableByCriteria( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void findAvailableByCriteriaEmpty()
    {
        Map< String, String > criteria = Collections.emptyMap();
        settopDomainService.findAvailableByCriteria( criteria );
    }

    @Test
    public void findAvailableByCriteriaOneValue()
    {
        Map< String, String > criteria = new LinkedHashMap< String, String >();
        criteria.put( testProperties.getPropertyOne(), testProperties.getPropertyValueOne() );

        List< SettopDesc > settops = settopDomainService.findAvailableByCriteria( criteria );
        logResult( settops.size() );
    }

    @Test
    public void findAvailableByCriteriaTwoValue()
    {
        Map< String, String > criteria = new LinkedHashMap< String, String >();
        criteria.put( testProperties.getPropertyOne(), testProperties.getPropertyValueOne() );
        criteria.put( testProperties.getPropertyTwo(), testProperties.getPropertyValueTwo() );

        List< SettopDesc > settops = settopDomainService.findAvailableByCriteria( criteria );
        logResult( settops.size() );

    }

    @Test
    public void findAvailableByCriteriaOffset()
    {
        Map< String, String > criteria = new LinkedHashMap< String, String >();
        criteria.put( testProperties.getPropertyOne(), testProperties.getPropertyValueOne() );
        List< SettopDesc > settops = settopDomainService.findAvailableByCriteria( criteria, offset, count );
        logResult( settops.size() );
    }

}
