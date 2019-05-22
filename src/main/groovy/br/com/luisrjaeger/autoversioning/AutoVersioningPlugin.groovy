package br.com.luisrjaeger.autoversioning

import br.com.luisrjaeger.autoversioning.extension.Extension
import br.com.luisrjaeger.autoversioning.task.AutoVersioningPropertiesTask
import br.com.luisrjaeger.autoversioning.task.IncreaseVersionTask
import org.gradle.api.Plugin
import org.gradle.api.Project


class AutoVersioningPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        def extension = project.extensions.create("autoVersion", Extension)

        Properties versionProps = new Properties()
        loadProperties(project, extension, versionProps)

        project.tasks.create("autoVersioningProperties", AutoVersioningPropertiesTask) { task ->
            task.extension = extension

            description "Show all DSL properties used on Auto Versioning Plugin"
        }

        project.tasks.create("increaseVersion", IncreaseVersionTask) { task ->
            task.extension = extension
            task.versionProps = versionProps

            description "Increase Application version base on configured criteria"
        }
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

}
