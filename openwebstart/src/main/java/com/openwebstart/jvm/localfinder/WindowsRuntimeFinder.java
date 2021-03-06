package com.openwebstart.jvm.localfinder;

import com.openwebstart.func.ResultWithInput;
import com.openwebstart.jvm.os.OperationSystem;
import com.openwebstart.jvm.runtimes.LocalJavaRuntime;
import net.adoptopenjdk.icedteaweb.JavaSystemProperties;
import net.adoptopenjdk.icedteaweb.logging.Logger;
import net.adoptopenjdk.icedteaweb.logging.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class WindowsRuntimeFinder implements RuntimeFinder {
    private static final Logger LOG = LoggerFactory.getLogger(WindowsRuntimeFinder.class);

    private static final String JVM_BASEFOLDER_32 = System.getenv("ProgramFiles(X86)") + File.separatorChar + "java";
    private static final String JVM_BASEFOLDER_64 = System.getenv("ProgramFiles") + File.separatorChar + "java";
    public static final String CYGWIN_HOME = "cygwin64" + File.separatorChar + "home";

    @Override
    public List<ResultWithInput<Path, LocalJavaRuntime>> findLocalRuntimes() {
        LOG.debug("Searching for local runtimes");

        final Path systemPath32 = Paths.get(JVM_BASEFOLDER_32);
        final Path systemPath64 = Paths.get(JVM_BASEFOLDER_64);

        final Path correttoPath1 = Paths.get(System.getenv("ProgramFiles(X86)") + File.separatorChar + "Amazon Corretto");
        final Path correttoPath2 = Paths.get(System.getenv("ProgramFiles") + File.separatorChar + "Amazon Corretto");

        // This is based on the assumption that the windows installation and the cygwin installation left the
        // Windows' default user directory and the cygwin home directory pretty much to the defaults
        final String cygwinUserHome = JavaSystemProperties.getUserHome().replace("Users", CYGWIN_HOME);
        final Path sdkmanPath = Paths.get(cygwinUserHome + File.separatorChar + ".sdkman");

        return JdkFinder.findLocalJdks(systemPath32, systemPath64, sdkmanPath, correttoPath1, correttoPath2);
    }

    @Override
    public List<OperationSystem> getSupportedOperationSystems() {
        return Arrays.asList(OperationSystem.WIN32, OperationSystem.WIN64);
    }
}
