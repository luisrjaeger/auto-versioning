package br.com.mobilidade.lojasrenner.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project


class IncreaseAndroidVersionPlugin implements Plugin<Project>{

    void apply(Project project) {
        PluginLoader.load(project)
    }

}
