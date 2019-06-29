package io.github.erfangc.sdk.generator.java.general;

import com.samskivert.mustache.Mustache;
import io.github.erfangc.sdk.generator.IOUtils;
import io.github.erfangc.sdk.generator.java.JavaOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PomProcessor {

    private static List<License> licenses;
    private static Logger logger = LoggerFactory.getLogger(PomProcessor.class);

    static {
        licenses = new ArrayList<>();
        licenses.add(new License().setId("mit").setName("MIT License").setUrl("http://www.opensource.org/licenses/mit-license"));
        licenses.add(new License().setId("apache2").setName("The Apache Software License, Version 2.0").setUrl("https://www.apache.org/licenses/LICENSE-2.0.txt"));
        licenses.add(new License().setId("bsd3").setName("The 3-Clause BSD License").setUrl("https://opensource.org/licenses/BSD-3-Clause"));
        licenses.add(new License().setId("bsd2").setName("The 2-Clause BSD License").setUrl("https://opensource.org/licenses/BSD-2-Clause"));
        licenses.add(new License().setId("gpl2").setName("GNU General Public License version 2").setUrl("https://opensource.org/licenses/GPL-2.0"));
        licenses.add(new License().setId("gpl3").setName("GNU General Public License version 3").setUrl("https://opensource.org/licenses/GPL-3.0"));
    }

    public OutputFile processPom(JavaOptions options, Context context) {
        String outputDirectory = options.getSdkOutputDirectory();
        //
        // populate context with license if applicable
        //
        final String license = options.getLicense();
        if (license != null) {
            final Optional<License> first = licenses.stream().filter(l -> l.getId().equals(license)).findFirst();
            first.ifPresent(context::setLicense);
        }
        //
        // populate context with scm
        //
        if (options.getScmConnection() != null && options.getScmDeveloperConnection() != null && options.getScmUrl() != null) {
            final Scm scm = new Scm()
                    .setConnection(options.getScmConnection())
                    .setDeveloperConnection(options.getScmDeveloperConnection())
                    .setUrl(options.getScmUrl());
            context.setScm(scm);
        } else {
            logger.info("No all of connection, developerConnection and url are specified for scm, skipping scm generation");
        }
        return new OutputFile()
                .setPath(outputDirectory + "/pom.xml")
                .setContent(Mustache.compiler().compile(IOUtils.readResourceFully("java/pom.mustache")).execute(context));
    }

}
