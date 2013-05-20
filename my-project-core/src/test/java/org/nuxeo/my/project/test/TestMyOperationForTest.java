/*
 * (C) Copyright 2013 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Benjamin JALON<bjalon@nuxeo.com>
 */
package org.nuxeo.my.project.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.my.project.operation.MyOperationForTest;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;

import com.google.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features(CoreFeature.class)
@Deploy({ "org.nuxeo.ecm.automation.core", "my-project-core" })
@LocalDeploy("my-project-core:org.nuxeo.my.project.operation.MyOperationForTest.xml")
public class TestMyOperationForTest {

    @Inject
    AutomationService service;

    @Inject
    CoreSession session;

    @Test
    public void shouldExecuteOperationDefinedInMyTest() throws Exception {

        DocumentModel doc = session.createDocumentModel("File");
        doc.setPropertyValue("dc:title", "Nothing");
        doc = session.createDocument(doc);
        session.save();

        // initialize Operation Chain Context with the document as input
        OperationContext ctx = new OperationContext(session);
        ctx.setInput(doc);

        // Create an operation chain with only my operation inside
        OperationChain chain = new OperationChain("myNewOperationChain");
        chain.add(MyOperationForTest.ID).set("save", true);

        // Execute the operation chain
        service.run(ctx, chain);

        doc = session.getDocument(doc.getRef());
        assertEquals("Toto", doc.getPropertyValue("dc:title"));

    }

}
