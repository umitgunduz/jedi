/*
 *
 *  * Copyright 2015 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jedi.oracle;

import com.jedi.common.StoredFunction;

/**
 * @author umitgunduz
 */
public abstract class OracleFunction<T extends OracleParameters> extends OracleCall<T> {

    @Override
    public String getName() {
        StoredFunction storedFunction = this.getClass().getAnnotation(StoredFunction.class);
        String result = "";
        if (!storedFunction.schemaName().isEmpty()) {
            result += storedFunction.schemaName() + ".";
        }

        if (!storedFunction.packageName().isEmpty()) {
            result += storedFunction.packageName() + ".";
        }

        if (!storedFunction.name().isEmpty()) {
            result += storedFunction.name();
        }

        return result;
    }
}

