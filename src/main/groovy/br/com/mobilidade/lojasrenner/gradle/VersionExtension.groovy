package br.com.mobilidade.lojasrenner.gradle

class VersionExtension {

    String releaseNotes

    String releaseNotesFileName = 'RELEASE_NOTES.md'

    Integer major

    Integer minor

    Integer patch

    Integer code

    String criteriaMajor = /((.|\n)*)((Grande Refatoração)|(Nova API)|(nova api)|(grande refatoração))((.|\n)*)/

    String criteriaMinor = /((.|\n)*)((Nova funcionalidade)|(nova funcionalidade)|(Nova Funcionalidade))((.|\n)*)/

    String criteriaPatch = /((.|\n)*)((Correção)|(Bug)|(correção)|(bug)|(ajuste)|(Ajuste)|(Melhoria)|(melhoria))((.|\n)*)/

}
