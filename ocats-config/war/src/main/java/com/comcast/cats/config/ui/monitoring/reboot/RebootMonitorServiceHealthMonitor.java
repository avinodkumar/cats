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
package com.comcast.cats.config.ui.monitoring.reboot;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * Performs health check of the snmp-reboot-monitor service.
 */
@ManagedBean
@ApplicationScoped
public class RebootMonitorServiceHealthMonitor
{
    @Inject
    RebootMonitorService                rebootMonitorService;

    public boolean checkIfSNMPServiceIsUp(){
        boolean isServiceUp = rebootMonitorService.isSNMPRebootServiceIsReachable();
        if(!isServiceUp){
            FacesContext.getCurrentInstance().addMessage("response", new FacesMessage(FacesMessage.SEVERITY_WARN,"", "Could not communicate with SNMP reboot monitor service."));
        }

        return isServiceUp;
    }
}
