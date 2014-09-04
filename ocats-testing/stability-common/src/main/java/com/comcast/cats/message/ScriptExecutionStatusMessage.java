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
package com.comcast.cats.message;

import java.io.Serializable;

import com.comcast.cats.stability.ScriptExecutionStatus;

/**
 * 
 * @author maneshthomas
 * 
 */
public class ScriptExecutionStatusMessage implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private String                jobName;
    private ScriptExecutionStatus scriptExecutionStatus;

    public ScriptExecutionStatusMessage(ScriptExecutionStatus scriptExecutionStatus)
    {
        super();
        setScriptExecutionStatus(scriptExecutionStatus);
    }

    @Override
    public String toString()
    {
        return "ExecutionStatusMessage [jobName=" + getJobName() + ", ScriptExecutionStatus="
                + getScriptExecutionStatus() + "]";
    }

    public String getJobName()
    {
        return jobName;
    }

    public void setJobName( String jobName )
    {
        this.jobName = jobName;
    }

    public ScriptExecutionStatus getScriptExecutionStatus()
    {
        return scriptExecutionStatus;
    }

    public void setScriptExecutionStatus( ScriptExecutionStatus scriptExecutionStatus )
    {
        this.scriptExecutionStatus = scriptExecutionStatus;
    }
}
