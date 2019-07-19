package br.com.luisrjaeger.autoversioning.task

import br.com.luisrjaeger.autoversioning.extension.Extension
import br.com.luisrjaeger.autoversioning.helper.FileHelper
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ReleaseNotesFromChangeLogTask extends DefaultTask {

    private static final VERSION_TITLE = "# Versão"
    private static final RELEASE_TITLE = VERSION_TITLE + " Release"

    Extension extension

    String changeLog = ""

    ReleaseNotesFromChangeLogTask() { }

    @TaskAction
    def buildReleaseNotes() {
        if (!extension.releaseNotesFileName) {
            if (extension.throwException) throw new Exception("Release notes not defined!")
            println "Release notes not defined!"
            return
        }

        if (!extension.changeLogFileName) {
            if (extension.throwException) throw new Exception("Change log not defined!")
            println "Change log not defined!"
            return
        }

        println "Getting Change Log"
        println "----------------------"

        readChangeLog()

        if (changeLog.isEmpty()) {
            if (extension.throwException) throw new Exception("Change log is empty!")
            println "Change log is empty!"
            return
        }

        createReleaseNoteFile()
        createChangeLogFile()

        println "Release notes and change log updated!"
    }

    private readChangeLog() {
        File file = new File("$project.rootProject.projectDir/${extension.changeLogFileName}")

        println "Reading lines..."
        println ""

        if (!file.canRead()) {
            throw new Exception("Change log file not found")
        }

        List<String> lines = file.readLines()
        for (String line in lines) {
            if (line.startsWith(RELEASE_TITLE)) break
            if (!line.startsWith(VERSION_TITLE) && !line.isAllWhitespace()) {
                changeLog = changeLog + line + "\n"
            }
        }

        println changeLog
        println "----------------------"
    }

    private createReleaseNoteFile() {
        String fileName = "$project.rootProject.projectDir/${extension.releaseNotesFileName}"
        println "Generating release notes - $fileName"
        FileHelper.createFile(fileName, buildReleaseText(), false)
    }

    private createChangeLogFile() {
        String fileName = "$project.rootProject.projectDir/${extension.changeLogFileName}"
        println "Generating change log - $fileName"
        FileHelper.createFile(fileName, buildReleaseText(), true)
    }

    private String buildReleaseText() {
        return """
# Versão Release $extension.major.$extension.minor.$extension.patch

${new String(changeLog.getBytes("UTF-8"), "UTF-8")}
"""
    }

}
