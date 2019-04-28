package br.com.mobilidade.lojasrenner.gradle


import org.gradle.api.Project

class PluginLoader {

    static void load(Project project) {

        def extension = project.extensions.create("androidVersion", VersionExtension)

        Properties versionProps = new Properties()

        def versionPropsFile = project.file('version.properties')

        if (versionPropsFile.canRead()) {
            versionProps.load(new FileInputStream(versionPropsFile))

            extension.major = versionProps['VERSION_NAME_MAJOR'].toInteger()
            extension.minor = versionProps['VERSION_NAME_MINOR'].toInteger()
            extension.patch = versionProps['VERSION_NAME_PATCH'].toInteger()
            extension.code = versionProps['VERSION_CODE'].toInteger()
        } else {
            extension.major = 0
            extension.minor = 0
            extension.patch = 0
            extension.code = 0

            versionProps['VERSION_NAME_MAJOR'] = extension.major.toString()
            versionProps['VERSION_NAME_MINOR'] = extension.minor.toString()
            versionProps['VERSION_NAME_PATCH'] = extension.patch.toString()
            versionProps['VERSION_CODE'] = extension.code.toString()

            versionProps.store(versionPropsFile.newWriter(), null)
        }

        project.task('allReleaseVersionProperties') {
            doLast {
                println "CURRENT VERSION"
                println "code                 - ${extension.code}"
                println "versionName()        - ${extension.versionName()}"

                println "----------------------"

                println "CRITERIAS"
                println "criteriaMajor        - ${extension.criteriaMajor}"
                println "criteriaMinor        - ${extension.criteriaMinor}"
                println "criteriaPatch        - ${extension.criteriaPatch}"

                println "----------------------"

                println "OTHER"
                println "releaseNotesFileName - ${extension.releaseNotesFileName}"
                println "releaseNotes         - ${extension.releaseNotes}"
            }
        }

        project.task('increaseVersion') {

            def major = extension.major
            def minor = extension.minor
            def patch = extension.patch
            def code = extension.code

            doLast {
                println "CRITERIAS"
                println "major - ${new String(extension.criteriaMajor.getBytes("UTF-8"), "UTF-8")}"
                println "minor - ${new String(extension.criteriaMinor.getBytes("UTF-8"), "UTF-8")}"
                println "patch - ${new String(extension.criteriaPatch.getBytes("UTF-8"), "UTF-8")}"

                println "----------------------"

                println "Increasing Version"

                println "---- versionCode to ${++code} ----"

                if (extension.releaseNotes ==~ extension.criteriaMajor) {
                    println "Major change found!"
                    major++
                    minor = 0
                    patch = 0
                } else if (extension.releaseNotes ==~ extension.criteriaMinor) {
                    println "Minor change found!"
                    minor++
                    patch = 0
                } else if (extension.releaseNotes ==~ extension.criteriaPatch) {
                    println "Patch change found!"
                    patch++
                } else {
                    println "No change found, release version increase won't happen!"
                    return
                }

                println "---- versionName to $major.$minor.$patch ----"

                if (extension.releaseNotesFileName != null) {
                    String fileName = "$project.rootProject.projectDir/${extension.releaseNotesFileName}"
                    println "Generating release notes - $fileName"

                    def file = new File(fileName)

                    String fileText =
"""
# Versão $major.$minor.$patch
${new String(extension.releaseNotes.getBytes("UTF-8"), "UTF-8")}
"""
                    file.withWriter('UTF-8') { writer ->
                        writer.write(fileText)
                    }

                    println fileText
                }

                versionProps['VERSION_NAME_MAJOR'] = major.toString()
                versionProps['VERSION_NAME_MINOR'] = minor.toString()
                versionProps['VERSION_NAME_PATCH'] = patch.toString()
                versionProps['VERSION_CODE'] = code.toString()
                versionProps.store(versionPropsFile.newWriter(), null)
            }
        }

        /*
        * A task using a project property for configuration.
        * Reference:
        * https://docs.gradle.org/4.6/userguide/build_environment.html#sec:gradle_configuration_properties
        *
        project.task('helloTarget') {
            group = "Greeting"
            description = "Greets the user. Target configured through properties."

            doLast {
                String target = project.findProperty("target") ?: "default-user"
                println "Hello, $target!"
            }
        }
        */
    }

}
