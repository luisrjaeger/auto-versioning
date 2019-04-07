package br.com.mobilidade.lojasrenner.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

class IncreaseVersion extends DefaultTask {
    @Internal
    String message = "Hello"

    @Internal
    String target = "World"

    @TaskAction
    void increase() {
        if (message.toLowerCase(Locale.ROOT).contains("bye")) {
            throw new GradleException("I can't let you do that, Starfox.")
        }

        println "${message}, ${target}!"
    }
}
