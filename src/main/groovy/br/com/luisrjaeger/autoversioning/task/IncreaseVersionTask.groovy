package br.com.luisrjaeger.autoversioning.task

import br.com.luisrjaeger.autoversioning.extension.Extension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.impldep.org.apache.maven.BuildFailureException

class IncreaseVersionTask extends DefaultTask {

    @Input
    Extension extension

    @Input
    Properties versionProps

    IncreaseVersionTask() { }

    @TaskAction
    def postMessage() {
        Integer major = extension.major
        Integer minor = extension.minor
        Integer patch = extension.patch
        Integer code = extension.code

        println "Increasing Version"

        println "---- current versionCode ${code} ----"

        code++

        println "---- current versionName $major.$minor.$patch ----"

        println " "
        println "----------------------"

        if (!extension.versionCycle) {
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

        if (extension.releaseNotesFileName) {
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

            println " "
            println "----------------------"
        }

        def versionPropsFile = project.file('version.properties')

        versionProps['VERSION_NAME_MAJOR'] = major.toString()
        versionProps['VERSION_NAME_MINOR'] = minor.toString()
        versionProps['VERSION_NAME_PATCH'] = patch.toString()
        versionProps['VERSION_CODE'] = code.toString()

        extension.major = major
        extension.minor = minor
        extension.patch = patch
        extension.code = code

        versionProps.store(versionPropsFile.newWriter(), null)
    }

}
