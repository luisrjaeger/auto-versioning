package br.com.mobilidade.lojasrenner.gradle


import org.gradle.api.Project

class PluginLoader {

    static void load(Project project) {

        project.extensions.create("AndroidVersion", IncreaseAndroidVersionExtension)

        /*
        * Clever trick so users don't have to reference a custom task class by its fully qualified name.
        * Reference:
        * https://discuss.gradle.org/t/how-to-create-custom-gradle-task-type-and-not-have-to-specify-full-path-to-type-in-build-gradle/6059/4
        */
        project.ext.IncreaseVersion = br.com.mobilidade.lojasrenner.gradle.IncreaseVersion

        /*
        * A task that uses an extension for configuration.
        * Reference:
        * https://docs.gradle.org/4.6/userguide/custom_plugins.html#sec:getting_input_from_the_build
        */
        project.task('helloWorld') {
            group = "Greeting"
            description = "Greets the world. Greeting configured in the 'greeting' extension."

            doLast {
                String greeting = project.extensions.greeting.alternativeGreeting ?: "Hello"
                println "$greeting, world!"
            }
        }

        /*
        * A task using a project property for configuration.
        * Reference:
        * https://docs.gradle.org/4.6/userguide/build_environment.html#sec:gradle_configuration_properties
        */
        project.task('helloTarget') {
            group = "Greeting"
            description = "Greets the user. Target configured through properties."

            doLast {
                String target = project.findProperty("target") ?: "default-user"
                println "Hello, $target!"
            }
        }
    }

}
