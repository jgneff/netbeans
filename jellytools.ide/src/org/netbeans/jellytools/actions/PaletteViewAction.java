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
package org.netbeans.jellytools.actions;

import org.netbeans.jellytools.Bundle;

/** Used to call "Window|IDE Tools|Palette" main menu item.
 * @see Action
 * @author mmirilovic@netbeans.org */
public class PaletteViewAction extends Action {
    private static final String projectMenu = Bundle.getStringTrimmed("org.netbeans.core.windows.resources.Bundle", "Menu/Window")
                                           + "|IDE Tools|"
                                           + Bundle.getStringTrimmed("org.netbeans.modules.palette.Bundle", "CTL_PaletteAction");

    /** creates new PaletteViewAction instance */    
    public PaletteViewAction() {
        super(projectMenu, null, "org.netbeans.modules.palette.ShowPaletteAction");
    }
}
