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
 * 
 * @author SSugun00c
 * 
 */
public interface JMSConstants
{
    String PROPERTY_DESTINATION_TYPE        = "destinationType";
    String JMS_TOPIC                        = "javax.jms.Topic";
    String PROPERTY_DESTINATION             = "destination";

    String CATS_JMS_TOPIC_NAME              = "topic/cats";
    String CATS_JNDI_TOPIC_LOOKUP_NAME      = "jms/topic/cats";
    String CATS_JMS_TOPIC_ENTRY_NAME        = "java:jboss/exported/" + CATS_JNDI_TOPIC_LOOKUP_NAME;

    String CATS_JMS_TOPIC_SELECTOR_PROPERTY = "job";
    String CATS_JMS_STATUS                  = "status";
    String SCRIPT_EXECUTION_STATUS          = "scriptExecutionStatus";
    String CATS_JMS_ACTION                  = "action";
    String CATS_MESSAGE                     = "catsMessage";
    String JOB_NAME                         = "jobName";
    String CATS_JMS_TOPIC_STATUS_SELECTOR   = "topicStatus";

}
