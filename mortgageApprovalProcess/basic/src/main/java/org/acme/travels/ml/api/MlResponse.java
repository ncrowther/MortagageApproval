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
    private Integer responseCode;
    private Boolean approved;

    public MlResponse(Integer responseCode, Boolean approved) {
        this.responseCode = responseCode;
        this.approved = approved;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public Boolean getApproved() {
        return approved;
    }

    @Override
    public String toString() {
        return "MlResponse [approved=" + approved + ", responseCode=" + responseCode + "]";
    }

    public static void main(Integer args[]) {

    }

}
