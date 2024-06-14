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

public class MlRequest {

    private Integer salary = 0;
    private Integer loan = 0;
    private Integer propertyValue = 0;

    public Integer getLoan() {
        return loan;
    }

    public void setLoan(Integer loan) {
        this.loan = loan;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Integer getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Integer propertyValue) {
        this.propertyValue = propertyValue;
    }

    public static void main(String args[]) {

        MlRequest request = new MlRequest();
        request.setSalary(10000);
        request.setLoan(10000);
        request.setPropertyValue(10000);

        System.out.println("Request: " + request);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MlRequest{");
        sb.append("salary=").append(salary);
        sb.append(", loan=").append(loan);
        sb.append(", propertyValue=").append(propertyValue);
        sb.append('}');
        return sb.toString();
    }

}