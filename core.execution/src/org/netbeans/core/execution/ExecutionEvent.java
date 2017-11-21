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

package org.netbeans.core.execution;

/** Informs about process state
*
* @author Ales Novak
*/
public class ExecutionEvent extends java.util.EventObject {
    /** generated Serialized Version UID */
    static final long serialVersionUID = -9181112840849353114L;
    /** the process that the event notifies about*/
    private DefaultSysProcess proc;

    /**
    * @param source is a source of the event
    * @param proc is a Process that this event notifies about
    */
    public ExecutionEvent(Object source, DefaultSysProcess proc) {
        super(source);
        this.proc = proc;
    }

    /**
    * @return Process from the event
    */
    public DefaultSysProcess getProcess() {
        return proc;
    }
    
}
