/*
 * (C) Copyright 2013 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     bjalon
 */
package org.nuxeo.my.project.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.jaxrs.impl.HttpAutomationClient;
import org.nuxeo.ecm.automation.client.jaxrs.spi.auth.PortalSSOAuthInterceptor;
import org.nuxeo.ecm.automation.client.model.Document;

/**
 * @author bjalon
 *
 */
public class CallRestAutomationRequestSample {

    private static final Log log = LogFactory.getLog(CallRestAutomationRequestSample.class);

    public static void main(String[] args) {

        HttpAutomationClient client = new HttpAutomationClient(
                "http://192.168.0.115:8080/nuxeo/site/automation");

        // if Portal SSO call
        client.setRequestInterceptor(new PortalSSOAuthInterceptor("12345678", "acostache"));
        Session session = client.getSession();
        // if standard call
        // Session session = client.getSession("user", "password");

        for(String operation :session.getOperations().keySet()) {
            System.out.println("Operation: " +  operation);
        }
        try {
            Document doc = (Document) session.newRequest("Document.Fetch").setContextProperty("value",
                    "/default-domain").execute();
            for (String key : doc.getProperties().getKeys()) {
                System.out.println(key + ":" + doc.getProperties().getString(key));
            }
        } catch (Exception e) {
            log.error(e, e);
        }
        client.shutdown();
    }
}
