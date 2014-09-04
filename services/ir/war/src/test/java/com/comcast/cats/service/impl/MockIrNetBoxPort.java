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
package com.comcast.cats.service.impl;

import com.comcast.cats.ir.commands.CatsCommand;
import com.comcast.cats.service.WebServiceReturn;
import com.comcast.cats.service.WebServiceReturnEnum;
import com.comcast.cats.service.ir.redrat.IrNetBoxPro;
import com.comcast.cats.service.ir.redrat.IrNetBoxProPort;

public class MockIrNetBoxPort extends IrNetBoxProPort{

    boolean normalState = true;
    
    public MockIrNetBoxPort( int portNumber, IrNetBoxPro irDevice   )
    {
        super( portNumber, irDevice );
    }
    
    public void setErrorState(){
        normalState = false;
    }
    
    public void setNormalState(){
        normalState = true;
    }
    
    @Override
    public WebServiceReturn sendCommand( CatsCommand catsCommand )
    {
        WebServiceReturn response  = new WebServiceReturn();
        if(normalState){
            response.setResult( WebServiceReturnEnum.SUCCESS );
        }
        
        return  response;
    }
}