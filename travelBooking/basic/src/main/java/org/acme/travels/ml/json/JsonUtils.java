/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.acme.travels.ml.json;

import org.json.JSONObject;

public class JsonUtils {

    public static Boolean getBoolean(String parameter, String jsonString) {
        JSONObject jsonObj = new JSONObject(jsonString);
        return (Boolean) jsonObj.get(parameter);
    }

    public static String getString(String parameter, String jsonString) {
        JSONObject jsonObj = new JSONObject(jsonString);
        return (String) jsonObj.get(parameter);
    }

    public static void main(String args[]) {

        String jsonStr = "{\"approved\": true}";

        Boolean approved = JsonUtils.getBoolean(jsonStr, "approved");

        System.out.println(approved);
    }
}
