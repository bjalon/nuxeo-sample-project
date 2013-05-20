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

package org.nuxeo.my.project.operation.fetch;

import java.util.List;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;

/**
 * @author bjalon
 */
@Operation(id=FetchVersions.ID, category=Constants.CAT_DOCUMENT, label="FetchVersions", description="Fetch all versions of the input document. If the version label is set, this operation will returned only the version specified if it exists ot null if not")
public class FetchVersions {

    public static final String ID = "Fetch.Versions";

    @Context
    public CoreSession session;

    @Param(name="version label", required = false)
    public String versionLabel = null;


    @OperationMethod
    public DocumentModelList run(DocumentModel input) throws ClientException {
        List<DocumentModel> versions = session.getVersions(input.getRef());

        if (versionLabel == null || versionLabel.isEmpty()) {
            return new DocumentModelListImpl(versions);
        }

        for (DocumentModel version : versions) {
            if (versionLabel.equals(version.getVersionLabel())) {
                DocumentModelList result = new DocumentModelListImpl();
                result.add(version);
                return result;
            }
        }
        return null;
    }

}
