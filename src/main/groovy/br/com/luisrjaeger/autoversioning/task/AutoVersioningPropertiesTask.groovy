package br.com.luisrjaeger.autoversioning.task

import br.com.luisrjaeger.autoversioning.extension.Extension
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

        println " "
        println "----------------------"

        println "CRITERIAS"
        println "criteria.major       - ${extension.criteria.major}"
        println "criteria.minor       - ${extension.criteria.minor}"
        println "criteria.patch       - ${extension.criteria.patch}"
        println " "
        println "versionCycle         - ${extension.versionCycle}"

        println " "
        println "----------------------"

        println "OTHER"
        println "throwException       - ${extension.throwException}"
        println "releaseNotesFileName - ${extension.releaseNotesFileName}"
        println "releaseNotes         - ${extension.releaseNotes}"
        println "changeLogFileName    - ${extension.changeLogFileName}"

        println " "
        println "----------------------"
    }

}
