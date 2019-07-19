package br.com.luisrjaeger.autoversioning

import br.com.luisrjaeger.autoversioning.extension.Extension
import br.com.luisrjaeger.autoversioning.task.AutoVersioningPropertiesTask
import br.com.luisrjaeger.autoversioning.task.IncreaseVersionTask
import br.com.luisrjaeger.autoversioning.task.ReleaseNotesFromChangeLogTask
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

            group "auto versioning"
            description "Show all DSL properties used on Auto Versioning Plugin"
        }

        project.tasks.create("increaseVersion", IncreaseVersionTask) { task ->
            task.extension = extension
            task.versionProps = versionProps

            group "auto versioning"
            description "Increase application version based on configured criteria"
        }

        project.tasks.create("releaseNotesFromChangeLog", ReleaseNotesFromChangeLogTask) { task ->
            task.extension = extension

            group "auto versioning"
            description "Generate release notes based on change log file"
        }
    }

    static void loadProperties(Project project, def extension, Properties versionProps) {
        def versionPropsFile = project.file('version.properties')

        if (versionPropsFile.canRead()) {
            versionProps.load(new FileInputStream(versionPropsFile))

            try {
                extension.major = versionProps['VERSION_NAME_MAJOR'].toInteger()
                extension.minor = versionProps['VERSION_NAME_MINOR'].toInteger()
                extension.patch = versionProps['VERSION_NAME_PATCH'].toInteger()
                extension.code = versionProps['VERSION_CODE'].toInteger()
            } catch(Exception ex) {
                throw new Exception("Unable to get application version!")
            }
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
