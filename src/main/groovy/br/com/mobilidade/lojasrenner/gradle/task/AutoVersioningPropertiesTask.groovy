package br.com.mobilidade.lojasrenner.gradle.task

import br.com.mobilidade.lojasrenner.gradle.extension.Extension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class AutoVersioningPropertiesTask extends DefaultTask {

    Extension extension

    AutoVersioningPropertiesTask() { }

    @TaskAction
    def postMessage() {
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
