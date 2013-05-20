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

import static org.junit.Assert.assertNotNull;
import static org.nuxeo.my.project.Constants.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.RuntimeFeature;

/**
 * @author bjalon
 *
 */
@RunWith(FeaturesRunner.class)
@Features(RuntimeFeature.class)
@Deploy(BUNDLE_SYMBOLIC_NAME)
public class StudioProjectDeploymentTestCase {

    @Test
    public void shouldFindStudioProjectComponent() {
        Object component = Framework.getRuntime().getComponent(
                BUNDLE_SYMBOLIC_NAME);
        assertNotNull(
                "Should change the name of the bundle symbolic name in Constants#BUNDLE_SYMBOLIC_NAME in core project",
                component);
    }

}
