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
package com.comcast.cats.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to instruct the Aspect ExclusiveAccessObserver to enforce
 * exclusive access to the annotated method. If an ExclusiveAccessProvider is
 * not provided the exclusive access check will be ignored.
 * 
 * When using this class it is important to not @Override the implementation
 * method. This will cause the annotation to be ignored during AOP processing.
 * If you must use an @Override to update an existing implementation then place
 * this annotation on the newly created class to be safe.
 * 
 * @author cfrede001
 * 
 */
@Target( ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface ExclusiveAccess
{

}
