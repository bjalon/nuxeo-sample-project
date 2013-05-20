/*
 * (C) Copyright ${year} Nuxeo SA (http://nuxeo.com/) and contributors.
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

package org.nuxeo.my.project.operation;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.collectors.DocumentModelCollector;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * @author bjalon
 */
@Operation(id = MyOperationForTest.ID, category = Constants.CAT_DOCUMENT, label = "MyOperation", description = "")
public class MyOperationForTest {

    public static final String ID = "MyOperation";

    @Context
    public CoreSession session;

    @Param(name = "save")
    protected boolean save;

    @OperationMethod(collector = DocumentModelCollector.class)
    public DocumentModel run(DocumentModel input) throws ClientException {
        input.setPropertyValue("dc:title", "Toto");
        if (save) {
            input = session.saveDocument(input);
            session.save();
        }
        return input;
    }

}
