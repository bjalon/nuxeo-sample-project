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
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.VersioningOption;
import org.nuxeo.ecm.core.schema.DocumentType;
import org.nuxeo.ecm.core.schema.SchemaManager;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features(CoreFeature.class)
public class CoreRepositorySampleTest {

    @Inject
    public CoreSession session;

    @Test
    public void documentManagementInTestCasesExample() throws ClientException {
        SchemaManager typeService = Framework.getLocalService(SchemaManager.class);

        DocumentType[] types = typeService.getDocumentTypes();
        for (DocumentType type : types) {
            System.out.println(type.getName());
        }

        DocumentModel mydoc = session.createDocumentModel("File");
        // DocumentModel mydoc = session.createDocumentModel("/", "toto",
        // "File");

        mydoc.setPathInfo("/", "toto");
        mydoc.setPropertyValue("dc:title", "Toto");
        mydoc = session.createDocument(mydoc);
        session.save();

        DocumentModelList docs = session.query("SELECT * FROM Document WHERE dc:title = 'Toto'");
        assertEquals(1, docs.size());
        mydoc = docs.get(0);
        assertEquals("toto", mydoc.getName());
        assertEquals("project", mydoc.getCurrentLifeCycleState());

        for (String state : mydoc.getAllowedStateTransitions()) {
            System.out.println("Transition : " + state);
        }

        // session.followTransition(mydoc.getRef(), "approve");
        mydoc.followTransition("approve");

        mydoc.setPropertyValue("dc:description", "My Description");
        mydoc = session.saveDocument(mydoc);
        session.save();

        assertEquals("approved", mydoc.getCurrentLifeCycleState());
        assertEquals("0.0", mydoc.getVersionLabel());
        assertEquals(0, session.getVersions(mydoc.getRef()).size());

        session.checkIn(mydoc.getRef(), VersioningOption.MINOR, "");
        mydoc = session.getDocument(mydoc.getRef());
        assertEquals("0.1", mydoc.getVersionLabel());
        assertEquals(1, session.getVersions(mydoc.getRef()).size());

        DocumentModel folder = session.createDocumentModel("/", "folder",
                "Folder");
        folder.setPropertyValue("dc:title", "Folder");
        folder = session.createDocument(folder);
        session.save();

        assertEquals(0, session.getChildren(folder.getRef()).size());
        session.publishDocument(mydoc, folder);
        assertEquals(1, session.getChildren(folder.getRef()).size());


        DocumentModel folder2 = session.createDocumentModel("/", "folder2",
                "Folder");
        folder2.setPropertyValue("dc:title", "Folder2");
        folder2 = session.createDocument(folder2);
        session.save();

        DocumentModel proxy = session.createProxy(mydoc.getRef(), folder2.getRef());

        assertEquals("Toto", proxy.getPropertyValue("dc:title"));
        mydoc.setPropertyValue("dc:title", "Tutu");
        session.saveDocument(mydoc);
        session.save();

        proxy = session.getDocument(proxy.getRef());
        assertEquals("Tutu", proxy.getPropertyValue("dc:title"));

        proxy.setPropertyValue("dc:title", "Tata");
        session.saveDocument(proxy);
        session.save();

        mydoc = session.getDocument(mydoc.getRef());
        assertEquals("Tata", mydoc.getPropertyValue("dc:title"));

    }

}
