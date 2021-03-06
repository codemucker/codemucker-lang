/*
 * Copyright 2011 Bert van Brakel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codemucker.lang;

import java.lang.reflect.Type;

public class ClassNameUtil extends StringUtil {

	public static String extractPkgPartOrNull(String fullClassName){
    	int dot = fullClassName.lastIndexOf('.');
    	if( dot != -1 ){
    		return fullClassName.substring(0, dot);
    	}
    	return null;
    }

    public static String extractSimpleClassNamePart(String fullClassName){
    	int dot = fullClassName.lastIndexOf('.');
    	if( dot != -1 ){
    		return fullClassName.substring(dot+1);
    	}
    	return fullClassName;
    }

    public static String safeToClassName(Class<?> type){
        return type==null?null:type.getName();
    }
    
	public static String safeToClassName(Type type){
    	return type==null?null:type.getClass().getName();
    }
}
