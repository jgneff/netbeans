/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.netbeans.modules.refactoring.java.ui.scope;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.Icon;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.modules.refactoring.api.Scope;
import org.netbeans.modules.refactoring.spi.ui.ScopeProvider;
import org.netbeans.modules.refactoring.spi.ui.ScopeReference;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Ralph Benjamin Ruijs <ralphbenjamin@netbeans.org>
 */
@ScopeProvider.Registration(id = "current-project", displayName = "#LBL_CurrentProject", position = 200)
@ScopeReference(path = "org-netbeans-modules-refactoring-java-ui-WhereUsedPanel")
@Messages({"LBL_CurrentProject=Current Project"})
public final class CurrentJavaProjectScopeProvider extends ScopeProvider {

    private String detail;
    private Scope scope;
    private Icon icon;

    @Override
    public boolean initialize(Lookup context, AtomicBoolean cancel) {
        FileObject file = context.lookup(FileObject.class);
        Project selected = null;
        if (file != null) {
            selected = FileOwnerQuery.getOwner(file);
        }
        if (selected == null) {
            selected = context.lookup(Project.class);
            if (selected == null) {
                SourceGroup sg = context.lookup(SourceGroup.class);
                if (sg != null) {
                    selected = FileOwnerQuery.getOwner(sg.getRootFolder());
                }
            }
            if (selected == null) {
                DataFolder df = context.lookup(DataFolder.class);
                if (df != null) {
                    selected = FileOwnerQuery.getOwner(df.getPrimaryFile());
                }
            }
        }
        if (selected == null || !OpenProjects.getDefault().isProjectOpen(selected)) {
            return false;
        }

        ProjectInformation pi = ProjectUtils.getInformation(selected);
        final SourceGroup[] sourceGroups = ProjectUtils.getSources(pi.getProject()).getSourceGroups(JavaProjectConstants.SOURCES_TYPE_JAVA);
        FileObject[] projectSources = new FileObject[sourceGroups.length];
        for (int i = 0; i < sourceGroups.length; i++) {
            projectSources[i] = sourceGroups[i].getRootFolder();
        }
        scope = Scope.create(Arrays.asList(projectSources), null, null);

        detail = pi.getDisplayName();
        icon = pi.getIcon();
        return true;
    }

    @Override
    public String getDetail() {
        return detail;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public Scope getScope() {
        return scope;
    }
}
