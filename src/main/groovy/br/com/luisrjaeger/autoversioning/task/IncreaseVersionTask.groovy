package br.com.luisrjaeger.autoversioning.task

import br.com.luisrjaeger.autoversioning.extension.Extension
import br.com.luisrjaeger.autoversioning.helper.FileHelper
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class IncreaseVersionTask extends DefaultTask {

    Extension extension

    Properties versionProps

    Integer major

    Integer minor

    Integer patch

    Integer code

    IncreaseVersionTask() { }

    @TaskAction
    def increaseVersion() {
        major = extension.major
        minor = extension.minor
        patch = extension.patch
        code = extension.code

        println "Increasing Version"
        println "---- current versionCode ${code} ----"

        code++

        println "---- current versionName $major.$minor.$patch ----"
        println " "
        println "----------------------"

        if (!extension.versionCycle) {
            if (!upReleaseNoteVersion()) return
        } else {
            if (!upCyclingVersion()) return
        }

        println " "
        println "---- versionName to $major.$minor.$patch ----"

        if (extension.releaseNotesFileName) {
            createReleaseNoteFile()

            if (extension.changeLogFileName) {
                createChangeLogFile()
            }
        }

        extension.major = major
        extension.minor = minor
        extension.patch = patch
        extension.code = code

        saveProperties()
    }

    private saveProperties() {
        def versionPropsFile = project.file('version.properties')

        versionProps['VERSION_NAME_MAJOR'] = major.toString()
        versionProps['VERSION_NAME_MINOR'] = minor.toString()
        versionProps['VERSION_NAME_PATCH'] = patch.toString()
        versionProps['VERSION_CODE'] = code.toString()

        versionProps.store(versionPropsFile.newWriter(), null)
    }

    private upCyclingVersion() {
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
            if (extension.throwException) throw new Exception("Max cycle reached")
            return false
        }

        return true
    }

    private Boolean upReleaseNoteVersion() {
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
            if (extension.throwException) throw new Exception("No change found")
            return false
        }

        return true
    }

    private createReleaseNoteFile() {
        String fileName = "$project.rootProject.projectDir/${extension.releaseNotesFileName}"
        println "Generating release notes - $fileName"
        FileHelper.createFile(fileName, buildReleaseText(), false)
    }

    private createChangeLogFile() {
        String fileName = "$project.rootProject.projectDir/${extension.changeLogFileName}"
        println "Generating change log - $fileName"
        FileHelper.createFile(fileName, buildReleaseText (), true)
    }

    private String buildReleaseText() {
        return """
# Versão $major.$minor.$patch
${new String(extension.releaseNotes.getBytes("UTF-8"), "UTF-8")}
"""
    }

}
