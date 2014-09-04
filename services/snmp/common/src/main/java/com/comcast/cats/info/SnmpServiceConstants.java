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
package com.comcast.cats.info;

/**
 * Constants related to SnmpService.
 * 
 * @author TATA
 */
public interface SnmpServiceConstants
{
    /**
     * SnmpCoreService related constants
     */
    String SNMP_CORE_SERVICE                        = "snmp-core";
    String SNMP_CORE_SERVICE_NAME                   = "SnmpCoreService";
    String SNMP_CORE_SERVICE_LOCAL_PART_NAME        = "SnmpCoreServiceImplService";
    String SNMP_CORE_SERVICE_PORT_NAME              = "SnmpCoreServicePort";
    String SNMP_CORE_SERVICE_MAPPED_NAME            = "cats/services/SnmpCoreService";
    String SNMP_CORE_SERVICE_ENDPOINT_INTERFACE     = "com.comcast.cats.service.SnmpCoreService";
    String SNMP_CORE_SERVICE_WSDL_LOCATION          = "snmp-service/SnmpCoreService?wsdl";

    /**
     * SnmpCoreService related constants
     */
    String SNMP_SERVICE                             = "snmp";
    String SNMP_SERVICE_NAME                        = "SnmpService";
    String SNMP_SERVICE_LOCAL_PART_NAME             = "SnmpServiceImplService";
    String SNMP_SERVICE_PORT_NAME                   = "SnmpServicePort";
    String SNMP_SERVICE_MAPPED_NAME                 = "cats/services/SnmpService";
    String SNMP_SERVICE_ENDPOINT_INTERFACE          = "com.comcast.cats.service.SnmpService";
    String SNMP_SERVICE_WSDL_LOCATION               = "snmp-service/SnmpService?wsdl";

    String DEFAULT_SNMP_READ_COMMUNITY_STRING       = "public";
    String DEFAULT_SNMP_READ_WRITE_COMMUNITY_STRING = "public";

    String CATS_SNMP_PROXY_IP                       = "cats.snmp.proxy.ip";
    String DEFAULT_HOST_URL                         = "localhost:8080";

    /**
     * OID type
     */
    String OID_TYPE_INTEGER                         = "Integer";

    String OID_TYPE_STRING                          = "String";

    /*
     * The name space of the SnmpService.
     */
    String SNMP_SERVICE_NAMESPACE                   = "urn:com:comcast:cats:snmp";
    /**
     * The host and port to use for directory service. Constant used is specfic
     * for JBoss. For weblogic "t3://localhost:7001" needs to be used.
     */
    String PROVIDER_URL                             = "jnp://192.168.161.15:1099";
    /**
     * The jboss specific value for java.naming.factory.url.pkgs.
     */
    String JNDI_FACTORY_PACKAGE_PREFIX              = "org.jboss.naming:org.jnp.interfaces";
    /**
     * The initial context implementation to use. For Weblogic, use
     * "weblogic.jndi.WLInitialContextFactory";
     */
    String INITIAL_CONTEXT_FACTORY                  = "org.jnp.interfaces.NamingContextFactory";
    /**
     * JNDI naming convention is vendor specific. For weblogic,
     * (mappedName#fully_qualified_name). For JBoss, either used EAR-name/EJB
     * Name/remote or local. You can also specify the bean name in the
     * jboss.xml. This can avoid the dependency on the ear name.
     */
    String JNDI_BEAN_NAME                           = "cats/services/SnmpService";

    /**
     * The jboss specific EJB value for java.naming.factory.url.pkgs
     */
    String JBOSS_URL_PKG_PREFIXES                   = "org.jboss.ejb.client.naming";

    String SNMP_GET                                 = "/get";

    String SNMP_SET                                 = "/set";

}
