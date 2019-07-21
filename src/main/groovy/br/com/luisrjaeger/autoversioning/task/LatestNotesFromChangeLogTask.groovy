package br.com.luisrjaeger.autoversioning.task

import br.com.luisrjaeger.autoversioning.extension.Extension
import br.com.luisrjaeger.autoversioning.helper.FileHelper
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class LatestNotesFromChangeLogTask extends DefaultTask {

    private static final VERSION_TITLE = "# Versão"
    private static final RELEASE_TITLE = VERSION_TITLE + " Release"

    private static final ERROR_LATEST_NOTES = "Latest notes not defined!"
    private static final ERROR_CHANGE_LOG = "Change log not defined!"
    private static final ERROR_CHANGE_LOG_EMPTY = "Change log is empty!"
    private static final ERROR_CHANGE_LOG_NOT_FOUND = "Change log file not found!"

    Extension extension

    String changeLog = ""

    LatestNotesFromChangeLogTask() { }

    @TaskAction
    def buildReleaseNotes() {
        if (!extension.latestNotesFileName) {
            if (extension.throwException) throw new Exception(ERROR_LATEST_NOTES)
            println ERROR_LATEST_NOTES
            return
        }

        if (!extension.changeLogFileName) {
            if (extension.throwException) throw new Exception(ERROR_CHANGE_LOG)
            println ERROR_CHANGE_LOG
            return
        }

        println "Getting Change Log"
        println "----------------------"

        readChangeLog()

        if (changeLog.isEmpty()) {
            if (extension.throwException) throw new Exception(ERROR_CHANGE_LOG_EMPTY)
            println ERROR_CHANGE_LOG_EMPTY
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
            throw new Exception(ERROR_CHANGE_LOG_NOT_FOUND)
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
        String fileName = "$project.rootProject.projectDir/${extension.latestNotesFileName}"
        println "Generating final release notes - $fileName"
        FileHelper.createFile(fileName, buildLatestText(), false)
    }

    private createChangeLogFile() {
        String fileName = "$project.rootProject.projectDir/${extension.changeLogFileName}"
        println "Generating change log - $fileName"
        FileHelper.createFile(fileName, buildLatestText(), true)
    }

    private String buildLatestText() {
        return """
# Versão Release $extension.major.$extension.minor.$extension.patch

${new String(changeLog.getBytes("UTF-8"), "UTF-8")}
"""
    }

}
