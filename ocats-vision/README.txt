====
    Copyright 2014 Comcast Cable Communications Management, LLC

    This file is part of CATS.

    CATS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    CATS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CATS.  If not, see <http://www.gnu.org/licenses/>.
====

This README identifies how to execute CATS Vision from the command line for testing purposes

mvn exec:java -Dexec.mainClass=com.comcast.cats.vision.CATSVisionApplication -Dexec.args="--server=http://localhost:8080/ --mac=00:15:9A:B7:E8:D6 --authToken=8520 -d settopList.xml

The run.sh file included with this project provides an example of the above command for immediate use.  This can also be added to a .bat file for Window's machines.

On the end, -d tells CATS Vision to use dummy data. The local file settopList.xml is included as example for defining these settops.  Alter the mac address in run.sh to match a newly created settop so it is preloaded when CATS Vision starts.

Log output will be placed in ${CATS_HOME}/catsvision.log and trace information will be at ${CATS_HOME}/catsvision-trace.log


