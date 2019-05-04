package br.com.mobilidade.lojasrenner.gradle.extension

class Criteria {

    String major = /((.|\n)*)((Grande Refatoração)|(Nova API)|(nova api)|(grande refatoração))((.|\n)*)/

    String minor = /((.|\n)*)((Nova funcionalidade)|(nova funcionalidade)|(Nova Funcionalidade))((.|\n)*)/

    String patch = /((.|\n)*)((Correção)|(Bug)|(BUG)|(correção)|(bug)|(ajuste)|(Ajuste)|(Melhoria)|(melhoria)|(Implementação parcial)|(Implementação Parcial))((.|\n)*)/

}
