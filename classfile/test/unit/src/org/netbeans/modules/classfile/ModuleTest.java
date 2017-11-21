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
package org.netbeans.modules.classfile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * @author Tomas Zezula
 */
public class ModuleTest extends TestCase {
    private static final String JDK9_HOME = null;
    private static final Logger LOG = Logger.getLogger(ModuleTest.class.getName());

    public void testJavaBaseModule() throws IOException {
        doTest("java.base", Level.FINER);    //NOI18N
    }

    public void testJavaDesktopModule() throws IOException {
        doTest("java.desktop", Level.FINER);    //NOI18N
    }

    public void testAll() throws IOException {
        final Path root = getModulesRoot();
        if (root != null) {
            Files.list(root)
                    .filter((p) -> Files.isDirectory(p))
                    .forEach((p) -> {
                        try {
                            doTest(p.getName(p.getNameCount()-1).toString(), Level.FINE);
                        } catch (IOException ioe) {
                            throw new RuntimeException(ioe);
                        }
                    });
        }
    }

    public void testModulePackages() throws IOException {
        final Path root = getModulesRoot();
        if (root != null) {
            Files.list(root)
                    .filter((p) -> Files.isDirectory(p))
                    .forEach((p) -> {
                        try {
                            final String moduleName = p.getName(p.getNameCount()-1).toString();
                            final Path moduleInfo = root.resolve(String.format("%s/module-info.class", moduleName));   //NOI18N
                            assertTrue(Files.exists(moduleInfo));
                            try (InputStream in = Files.newInputStream(moduleInfo)) {
                                final ClassFile cf = new ClassFile(in, true);
                                assertNotNull(cf);
                                assertTrue(cf.isModule());
                                final List<String> pkgs = cf.getModulePackages();
                                assertNotNull("No module packages for: " + moduleName, pkgs);   //NOI18N
                            }
                        } catch (IOException ioe) {
                            throw new RuntimeException(ioe);
                        }
                    });
        }
    }

    public void testModuleTarget() throws IOException {
        final Path root = getModulesRoot();
        if (root != null) {
            Files.list(root)
                    .filter((p) -> Files.isDirectory(p))
                    .forEach((p) -> {
                        try {
                            final String moduleName = p.getName(p.getNameCount()-1).toString();
                            final Path moduleInfo = root.resolve(String.format("%s/module-info.class", moduleName));   //NOI18N
                            assertTrue(Files.exists(moduleInfo));
                            try (InputStream in = Files.newInputStream(moduleInfo)) {
                                final ClassFile cf = new ClassFile(in, true);
                                assertNotNull(cf);
                                assertTrue(cf.isModule());
                                final ModuleTarget target = cf.getModuleTarget();
                                assertNotNull(target);
                            }
                        } catch (IOException ioe) {
                            throw new RuntimeException(ioe);
                        }
                    });
        }
    }

    public void testModuleMainClass() throws IOException {
        final Path root = getModulesRoot();
        if (root != null) {
            Files.list(root)
                    .filter((p) -> Files.isDirectory(p))
                    .forEach((p) -> {
                        try {
                            final String moduleName = p.getName(p.getNameCount()-1).toString();
                            final Path moduleInfo = root.resolve(String.format("%s/module-info.class", moduleName));   //NOI18N
                            assertTrue(Files.exists(moduleInfo));
                            try (InputStream in = Files.newInputStream(moduleInfo)) {
                                final ClassFile cf = new ClassFile(in, true);
                                assertNotNull(cf);
                                assertTrue(cf.isModule());
                                final ClassName main = cf.getModuleMainClass();
                                if (main != null) {
                                    System.out.println(moduleName + "/" + main.getExternalName());
                                }
                            }
                        } catch (IOException ioe) {
                            throw new RuntimeException(ioe);
                        }
                    });
        }
    }

    public void testOldModuleInfoCrash() throws IOException {
        try(final InputStream in = this.getClass().getResourceAsStream("datafiles/old_version_module-info.clz")) {
            final ClassFile cf = new ClassFile(in);
            assertNull(cf.getModule());
            //Check other attributes are not broken
            assertEquals("module-info.java", cf.getSourceFileName());
        }
    }

    private void doTest(
            final String moduleName,
            final Level logLevel) throws IOException {
        final Path modulesRoot = getModulesRoot();
        if (modulesRoot != null) {
            final Path moduleInfo = modulesRoot.resolve(String.format("%s/module-info.class", moduleName));   //NOI18N
            assertTrue(Files.exists(moduleInfo));
            try (InputStream in = Files.newInputStream(moduleInfo)) {
                final ClassFile cf = new ClassFile(in, true);
                assertNotNull(cf);
                assertTrue(cf.isModule());
                final Module mod = cf.getModule();
                assertNotNull(mod);
                if (Level.FINE.intValue() >= logLevel.intValue()) {
                    System.out.println(mod.getName() + " " + Integer.toBinaryString(mod.getFlags()) + " " + mod.getVersion());
                }
                assertEquals(moduleName, mod.getName());
                if (Level.FINER.intValue() >= logLevel.intValue()) {
                    for (Module.RequiresEntry req : mod.getRequiresEntries()) {
                        System.out.printf("%s%n", req);
                    }
                    for (Module.ExportsEntry exp : mod.getExportsEntries()) {
                        System.out.printf("%s%n", exp);
                    }
                    for (Module.OpensEntry exp : mod.getOpensEntries()) {
                        System.out.printf("%s%n", exp);
                    }
                    for (ClassName u : mod.getUses()) {
                        System.out.printf("uses %s%n", u.getExternalName());
                    }
                    for (Module.ProvidesEntry p : mod.getProvidesEntries()) {
                        System.out.printf("%s%n", p);
                    }
                }
            }
        }
    }

    private static Path getModulesRoot() {
        try {
            final File javaHome = new File(JDK9_HOME == null ? System.getProperty("java.home") : JDK9_HOME);    //NOI18N
            final File jrtProvider = new File(new File(javaHome, "lib"), "jrt-fs.jar");  //NOI18N
            if (!jrtProvider.exists()) {
                return null;
            }
            final ClassLoader cl = new URLClassLoader(
                    new URL[]{jrtProvider.toURI().toURL()},
                    ModuleTest.class.getClassLoader());
            FileSystemProvider provider = null;
            for (FileSystemProvider p : ServiceLoader.load(FileSystemProvider.class, cl)) {
                if ("jrt".equals(p.getScheme())) {  //NOI18N
                    provider = p;
                    break;
                }
            }
            if (provider == null) {
                return null;
            }
            final Path jimageRoot = provider.getPath(URI.create("jrt:///"));    //NOI18N
            final Path modules = jimageRoot.resolve("modules");
            return Files.exists(modules) ? modules : jimageRoot;
        } catch (IOException ioe) {
            LOG.log(Level.WARNING, "Cannot load jrt nio provider.", ioe);   //NOI18N
            return null;
        }
    }
}
