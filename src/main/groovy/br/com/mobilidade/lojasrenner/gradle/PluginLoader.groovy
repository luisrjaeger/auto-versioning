package br.com.mobilidade.lojasrenner.gradle


import org.gradle.api.Project

class PluginLoader {

    static void load(Project project) {

        def extension = project.extensions.create("androidVersion", VersionExtension)

        Properties versionProps = new Properties()

        def versionPropsFile = project.file('version.properties')
        versionProps.load(new FileInputStream(versionPropsFile))

        extension.major = versionProps['VERSION_NAME_MAJOR'].toInteger()
        extension.minor = versionProps['VERSION_NAME_MINOR'].toInteger()
        extension.patch = versionProps['VERSION_NAME_PATCH'].toInteger()
        extension.code = versionProps['VERSION_CODE'].toInteger()

        project.task('increaseVersion') {

            def major = extension.major
            def minor = extension.minor
            def patch = extension.patch
            def code = extension.code

            doLast {
                println "CRITERIAS"
                println "major - ${extension.criteriaMajor}"
                println "minor - ${extension.criteriaMinor}"
                println "patch - ${extension.criteriaPatch}"

                println "RELEASE-NOTES"
                println extension.releaseNotes
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
                }

                println "---- versionName to $major.$minor.$patch ----"

                if (extension.releaseNotesFileName != null) {
                    String fileName = "$project.rootProject.projectDir/${extension.releaseNotesFileName}"
                    println "Generating release notes - $fileName"

                    def file = new File(fileName)

                    file.text =
"""
# Versão $major.$minor.$patch
${extension.releaseNotes}
"""
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
