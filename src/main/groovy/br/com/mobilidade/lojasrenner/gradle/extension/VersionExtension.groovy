package br.com.mobilidade.lojasrenner.gradle.extension

class VersionExtension {

    String releaseNotes

    String releaseNotesFileName = 'RELEASE_NOTES.md'

    Integer major

    Integer minor

    Integer patch

    Integer code

    Boolean throwException = true

    Integer versionCycle

    Criteria criteria = new Criteria()

    String versionName() {
        return "$major.$minor.$patch"
    }

    def criteria(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = criteria
        closure()
    }

}
