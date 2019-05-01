package br.com.mobilidade.lojasrenner.gradle

class VersionExtension {

    String releaseNotes

    String releaseNotesFileName = 'RELEASE_NOTES.md'

    Integer major

    Integer minor

    Integer patch

    Integer code

    Boolean throwException = true

    Integer versionCycle

    String criteriaMajor = /((.|\n)*)((Grande Refatoração)|(Nova API)|(nova api)|(grande refatoração))((.|\n)*)/

    String criteriaMinor = /((.|\n)*)((Nova funcionalidade)|(nova funcionalidade)|(Nova Funcionalidade))((.|\n)*)/

    String criteriaPatch = /((.|\n)*)((Correção)|(Bug)|(BUG)|(correção)|(bug)|(ajuste)|(Ajuste)|(Melhoria)|(melhoria)|(Implementação parcial)|(Implementação Parcial))((.|\n)*)/

    String versionName() {
        return "$major.$minor.$patch"
    }

}
