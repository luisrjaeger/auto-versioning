package br.com.mobilidade.lojasrenner.gradle

import br.com.mobilidade.lojasrenner.gradle.extension.VersionExtension
import org.gradle.api.Project
import org.gradle.internal.impldep.org.apache.maven.BuildFailureException

class PluginLoader {

    static void load(Project project) {

        def extension = project.extensions.create("autoVersion", VersionExtension)

        Properties versionProps = new Properties()

        loadProperties(project, extension, versionProps)

        loadAllPropertiesTask(project, extension)

        loadIncreaseVersionTask(project, extension, versionProps)
    }

    static void loadProperties(Project project, def extension, Properties versionProps) {
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
    }

    static void loadAllPropertiesTask(Project project, def extension) {
        project.task('allReleaseVersionProperties') {
            doLast {
                println "CURRENT VERSION"
                println "code                 - ${extension.code}"
                println "versionName()        - ${extension.versionName()}"

                println "----------------------"

                println "CRITERIAS"
                println "criteriaMajor        - ${extension.criteria.major}"
                println "criteriaMinor        - ${extension.criteria.minor}"
                println "criteriaPatch        - ${extension.criteria.patch}"
                println " "
                println "versionCycle         - ${extension.versionCycle ?: "null"}"

                println "----------------------"

                println "OTHER"
                println "releaseNotesFileName - ${extension.releaseNotesFileName}"
                println "releaseNotes         - ${extension.releaseNotes}"
                println "throwException       - ${extension.throwException}"
            }
        }
    }

    static void loadIncreaseVersionTask(Project project, def extension, Properties versionProps) {
        project.task('increaseVersion') {

            def major = extension.major
            def minor = extension.minor
            def patch = extension.patch
            def code = extension.code

            doLast {
                println "Increasing Version"

                println "---- current versionCode ${code} ----"

                code++

                println "---- current versionName $major.$minor.$patch ----"

                println " "

                if (extension.versionCycle == null) {
                    println "RELEASE NOTES METHOD"
                    println "CRITERIAS"
                    println "major - ${new String(extension.criteria.major.getBytes("UTF-8"), "UTF-8")}"
                    println "minor - ${new String(extension.criteria.minor.getBytes("UTF-8"), "UTF-8")}"
                    println "patch - ${new String(extension.criteria.patch.getBytes("UTF-8"), "UTF-8")}"

                    println "----------------------"

                    if (extension.releaseNotes ==~ extension.criteria.major) {
                        println "Major change found!"
                        major++
                        minor = 0
                        patch = 0
                    } else if (extension.releaseNotes ==~ extension.criteria.minor) {
                        println "Minor change found!"
                        minor++
                        patch = 0
                    } else if (extension.releaseNotes ==~ extension.criteria.patch) {
                        println "Patch change found!"
                        patch++
                    } else {
                        println "No change found, release version increase won't happen!"
                        if (extension.throwException) throw new BuildFailureException("No change found")
                        return
                    }
                } else {
                    println "RELEASE CYCLING METHOD"
                    println "TOP VERSION - ${extension.versionCycle}"

                    println "----------------------"

                    if (patch < extension.versionCycle) {
                        patch++
                    } else if (minor < extension.versionCycle) {
                        minor++
                        patch = 0
                    } else if (major < extension.versionCycle) {
                        major++
                        minor = 0
                        patch = 0
                    } else {
                        println "Max cycle reached!"
                        if (extension.throwException) throw new BuildFailureException("Max cycle reached")
                        return
                    }
                }

                println " "
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

                def versionPropsFile = project.file('version.properties')

                versionProps['VERSION_NAME_MAJOR'] = major.toString()
                versionProps['VERSION_NAME_MINOR'] = minor.toString()
                versionProps['VERSION_NAME_PATCH'] = patch.toString()
                versionProps['VERSION_CODE'] = code.toString()
                versionProps.store(versionPropsFile.newWriter(), null)
            }
        }
    }

}
