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
package org.acme.travels;

import java.util.Date;

public class MortgageApplication {

    private String city;
    private String country;
    private Integer loan;
    private Integer salary;
    private Integer propertyValue;
    private Date begin;
    private Date end;
    private boolean approved;
    private String incentive;

    public MortgageApplication() {
    }

    public MortgageApplication(Integer loan, Integer salary, Integer propertyValue, String city, String country, Date begin, Date end) {
        super();
        this.loan = loan;
        this.salary = salary;
        this.propertyValue = propertyValue;
        this.city = city;
        this.country = country;
        this.begin = begin;
        this.end = end;
        this.approved = false;
        this.incentive = "No incentive";
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getIncentive() {
        return incentive;
    }

    public void setIncentive(String incentive) {
        this.incentive = incentive;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Integer getLoan() {
        return loan;
    }

    public void setLoan(Integer loan) {
        this.loan = loan;
    }

    public Integer getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Integer propertyValue) {
        this.propertyValue = propertyValue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MortgageApplication{");
        sb.append("city=").append(city);
        sb.append(", country=").append(country);
        sb.append(", loan=").append(loan);
        sb.append(", salary=").append(salary);
        sb.append(", propertyValue=").append(propertyValue);
        sb.append(", begin=").append(begin);
        sb.append(", end=").append(end);
        sb.append(", approved=").append(approved);
        sb.append(", incentive=").append(incentive);
        sb.append('}');
        return sb.toString();
    }

}
