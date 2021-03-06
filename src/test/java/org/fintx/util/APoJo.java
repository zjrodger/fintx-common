/**
 *  Copyright 2017 FinTx
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
package org.fintx.util;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author bluecreator(qiang.x.wang@gmail.com)
 *
 */
@Getter
@Setter
public class APoJo extends PoJo {
  
    String anotherProperties;
    String anotherPropertiesToo;
    /**
     * @return the anotherProperties
     */
    public String getAnotherProperties() {
        return anotherProperties;
    }
    /**
     * @param anotherProperties the anotherProperties to set
     */
    public void setAnotherProperties(String anotherProperties) {
        this.anotherProperties = anotherProperties;
    }
    /**
     * @return the anotherPropertiesToo
     */
    public String getAnotherPropertiesToo() {
        return anotherPropertiesToo;
    }
    /**
     * @param anotherPropertiesToo the anotherPropertiesToo to set
     */
    public void setAnotherPropertiesToo(String anotherPropertiesToo) {
        this.anotherPropertiesToo = anotherPropertiesToo;
    }
   
    

}
