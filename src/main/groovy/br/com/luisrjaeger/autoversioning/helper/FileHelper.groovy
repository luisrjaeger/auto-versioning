package br.com.luisrjaeger.autoversioning.helper

class FileHelper {

    static createFile(String fileName, String text, Boolean keepText) {
        File file = new File(fileName)
        String old = file.canRead() ? file.getText('UTF-8') : ""
        String newText = text

        if (keepText) newText += old

        file.withWriter('UTF-8') { writer ->
            writer.write(newText)
        }

        println "Done!"
        println "----------------------"
    }

}
