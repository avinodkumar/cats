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
package com.comcast.cats.provider;

import java.net.URI;

import com.comcast.cats.annotation.ExclusiveAccess;
import com.comcast.cats.provider.exceptions.PowerProviderException;

/**
 * Provide hard power capabilities on Settop.
 * 
 * @author jtyrre001
 */
public interface PowerProvider extends BaseProvider
{
    /**
     * Return locator for hardware that facilitates power handling. This will
     * point to the device used for Power handling on a rack.
     * 
     * @return URI containing power hardware definition.
     */
    public URI getPowerLocator();

    /**
     * Hard powers ON a settop device outlet using the WTI device.
     * 
     * @throws PowerProviderException
     */
    @ExclusiveAccess
    public void powerOn() throws PowerProviderException;

    /**
     * Hard powers OFF a settop device outlet using the WTI device.
     * 
     * @throws PowerProviderException
     */
    @ExclusiveAccess
    public void powerOff() throws PowerProviderException;

    /**
     * Hard power cycles a settop outlet OFF and then ON using the WTI device.
     * 
     * @throws PowerProviderException
     */
    @ExclusiveAccess
    public void reboot() throws PowerProviderException;

    /**
     * Return the ON or Off status state of a WTI device power outlet.
     * 
     * @return The power state returned by the WTI or Netboost power strips.
     */
    @ExclusiveAccess
    public String getPowerStatus();
}