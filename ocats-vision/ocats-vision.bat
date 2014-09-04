@REM
@REM Copyright 2014 Comcast Cable Communications Management, LLC
@REM
@REM This file is part of CATS.
@REM
@REM CATS is free software: you can redistribute it and/or modify
@REM it under the terms of the GNU General Public License as published by
@REM the Free Software Foundation, either version 3 of the License, or
@REM (at your option) any later version.
@REM
@REM CATS is distributed in the hope that it will be useful,
@REM but WITHOUT ANY WARRANTY; without even the implied warranty of
@REM MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
@REM GNU General Public License for more details.
@REM
@REM You should have received a copy of the GNU General Public License
@REM along with CATS.  If not, see <http://www.gnu.org/licenses/>.
@REM

mvn clean compile install exec:java -Dexec.mainClass=com.comcast.cats.vision.CATSVisionApplication -Dexec.args="--server=http://localhost:8080/ --mac=12:12:12:12:12:12

