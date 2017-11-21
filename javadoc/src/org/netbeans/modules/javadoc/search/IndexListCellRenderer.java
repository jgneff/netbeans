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

package org.netbeans.modules.javadoc.search;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.DefaultListCellRenderer;

/** Just sets the right icon to IndexItem

 @author Petr Hrebejk
*/
class IndexListCellRenderer extends DefaultListCellRenderer {

    static final long serialVersionUID =543071118545614229L;
    public Component getListCellRendererComponent( JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        JLabel cr = (JLabel)super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );

        cr.setIcon( DocSearchIcons.getIcon( ((DocIndexItem)value).getIconIndex() ) );

        try {
            if (  ((DocIndexItem)value).getURL() == null )
                setForeground (java.awt.SystemColor.textInactiveText);
        }
        catch ( java.net.MalformedURLException e ) {
            setForeground (java.awt.SystemColor.textInactiveText);
        }
        return cr;
    }
}
