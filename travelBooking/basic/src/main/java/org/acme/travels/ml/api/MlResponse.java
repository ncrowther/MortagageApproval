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
package org.acme.travels.ml.api;

public class MlResponse {
    private Boolean responseStatus;
    private String responsePayload;

    public MlResponse(String response) {
        this.responseStatus = true;
        this.responsePayload = response;
    }

    public Boolean getResponseStatus() {
        return responseStatus;
    }

    public String getResponsePayload() {
        return responsePayload;
    }

    @Override
    public String toString() {
        return "MlResponse [responsePayload=" + responsePayload + ", responseStatus=" + responseStatus + "]";
    }

    public static void main(String args[]) {

    }

}
