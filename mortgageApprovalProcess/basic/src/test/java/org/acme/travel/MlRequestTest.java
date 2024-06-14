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
package org.acme.travel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.acme.travels.MortgageApplication;
import org.junit.jupiter.api.Test;
import org.kie.kogito.Model;
import org.kie.kogito.process.Process;
import org.kie.kogito.process.ProcessInstance;

import io.quarkus.test.junit.QuarkusTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class MlRequestTest {

    @Inject
    @Named("mortgageEligibility")
    Process<? extends Model> mortgageEligibilityProcess;

    @Test
    public void testMortgageNotEligible() {

        assertNotNull(mortgageEligibilityProcess);

        Model m = mortgageEligibilityProcess.createModel();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("mortgageapplication", new MortgageApplication(1000, 200000, 400000, "Reading", "UK", new Date(), new Date()));

        m.fromMap(parameters);

        ProcessInstance<?> processInstance = mortgageEligibilityProcess.createInstance(m);
        processInstance.start();
        assertEquals(org.kie.api.runtime.process.ProcessInstance.STATE_COMPLETED, processInstance.status());

        Model result = (Model) processInstance.variables();
        // assertEquals(2, result.toMap().size());
        MortgageApplication mortgageapplication = (MortgageApplication) result.toMap().get("mortgageapplication");
        assertNotNull(mortgageapplication);
        assertEquals(false, mortgageapplication.isApproved());
    }

    @Test
    public void testMortgageEligible() {

        assertNotNull(mortgageEligibilityProcess);

        Model m = mortgageEligibilityProcess.createModel();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("mortgageapplication", new MortgageApplication(100000, 200000, 400000, "Reading", "UK", new Date(), new Date()));

        m.fromMap(parameters);

        ProcessInstance<?> processInstance = mortgageEligibilityProcess.createInstance(m);
        processInstance.start();
        assertEquals(org.kie.api.runtime.process.ProcessInstance.STATE_COMPLETED, processInstance.status());

        Model result = (Model) processInstance.variables();
        // assertEquals(2, result.toMap().size());
        MortgageApplication mortgageapplication = (MortgageApplication) result.toMap().get("mortgageapplication");
        assertNotNull(mortgageapplication);
        assertEquals(true, mortgageapplication.isApproved());
    }
}
