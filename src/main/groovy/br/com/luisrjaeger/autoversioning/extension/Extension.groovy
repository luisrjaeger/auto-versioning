package br.com.luisrjaeger.autoversioning.extension

class Extension {

    String releaseNotes

    String releaseNotesFileName = 'RELEASE_NOTES.md'

    String changeLogFileName = 'CHANGE_LOG.md'

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
