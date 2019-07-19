package br.com.luisrjaeger.autoversioning.extension

class Criteria {

    String major = /((.|\n)*)((Grande Refatoração)|(Nova API)|(Nova Api)|(Nova api)|(nova api)|(grande refatoração)|(Grande refatoração))((.|\n)*)/

    String minor = /((.|\n)*)((Nova funcionalidade)|(nova funcionalidade)|(Nova Funcionalidade))((.|\n)*)/

    String patch = /((.|\n)*)((Correção)|(Bug)|(BUG)|(correção)|(bug)|(ajuste)|(Ajuste)|(Melhoria)|(melhoria)|(Implementação parcial)|(Implementação Parcial))((.|\n)*)/

}
