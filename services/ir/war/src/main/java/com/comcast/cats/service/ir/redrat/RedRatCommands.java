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
package com.comcast.cats.service.ir.redrat;

public interface RedRatCommands
{
    String IPADDRESS_ARGUMENT = "<ipAddress>";
    String KEYSET_ARGUMENT = "<keyset>";
    String KEY_ARGUMENT = "<key>";
    String PORT_ARGUMENT = "<port>";

    String BLACKLIST_ALL_IRNETBOXES = "hubQuery=\"blacklist all irnetboxes\"";
    String WHITLELIST_IRNETBOX = "hubQuery=\"whitelist redrat\" ip=\""+IPADDRESS_ARGUMENT+"\"";
    String BLACKLIST_IRNETBOX = "hubQuery=\"blacklist redrat\" ip=\"" + IPADDRESS_ARGUMENT + "\"";
    String ADD_IRNETBOX = "hubQuery=\"add irnetbox\" ip=\""+IPADDRESS_ARGUMENT+"\"";
    String REMOVE_IRNETBOX = "hubQuery=\"remove irnetbox\" ip=\"" + IPADDRESS_ARGUMENT + "\"";
    String IRNETBOX_IR_COMMAND = "ip=\""+IPADDRESS_ARGUMENT+"\" dataset=\""+KEYSET_ARGUMENT+"\" signal=\""+KEY_ARGUMENT+"\" output=\""+PORT_ARGUMENT+"\"";
    String LIST_REDRATS = "hubQuery=\"list redrats\"";
    String LIST_DATASETS = "hubQuery=\"list datasets\"";
}
