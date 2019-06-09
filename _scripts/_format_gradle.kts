import java.io.File
import java.util.Stack

fun File.children(): Sequence<File> {
    return walkTopDown()
        .filter { this.path != it.path }
}

fun format(file: File) {
    println(file.absolutePath)
    val lines = file.readLines()
        .filter { it.isNotBlank() }
    val root = getBlocks(lines)
    val buffer = arrayListOf<String>()
    root.print(buffer, 0)
    val formatted = buffer.joinToString("")
    file.writeText(formatted)
}

fun getBlocks(lines: List<String>): Block {
    val stack = Stack<Block>()
    stack.push(RootBlock())
    var i = 0
    while (i < lines.size) {
        val currentLine = lines[i].trim()
        if (currentLine.contains(Regex(" \\{$"))) {
            val block = Block(currentLine.replace("{", "").trim())
            stack.peek().blocks.add(block)
            stack.push(block)
        } else if (currentLine == "}") {
            stack.pop()
        } else {
            stack.peek().lines.add(currentLine)
        }
        i++
    }
    return stack.pop()
}

class RootBlock : Block() {
    override fun print(buffer: ArrayList<String>, offset: Int) {
        buffer.add(lines.joinToString("\n"))
        blocks.forEach {
            it.print(buffer, 0)
        }
    }
}

open class Block(var name: String = "") {

    val blocksToSort = arrayListOf(
        "dependencies",
        "lintOptions",
        "defaultConfig"
    )

    val lines = arrayListOf<String>()
    val blocks = arrayListOf<Block>()

    open fun print(buffer: ArrayList<String>, offset: Int) {
        val rootOffset = "".padStart(offset, ' ')
        val linesOffset = "".padStart(offset + 4, ' ')
        buffer.add("\n$rootOffset$name {")
        if (name in blocksToSort) {
            val sortedLines = lines.asSequence().sorted()
                .sortedBy { it.contains("implementation") }
                .sortedBy { !it.contains("implementation(project") }
                .sortedBy { !it.contains("implementation(kotlin") }
                .sortedBy { !it.contains("implementation(fileTree") }
                .sortedBy { it.contains("api") }
                .sortedBy { it.contains("kapt") }
                .sortedBy { it.contains("testImplementation") }
                .sortedBy { it.contains("androidTestImplementation") }
                .toMutableList()
            if (name == "dependencies") {
                for (i in sortedLines.size - 2 downTo 0) {
                    val prev = sortedLines[i + 1].substringBefore("(\"")
                    val curr = sortedLines[i].substringBefore("(\"")
                    if (prev != curr) sortedLines.add(i + 1, "\n")
                }
            }
            buffer.add(
                sortedLines
                    .joinToString("") { "${if (it.isNotBlank()) "\n" else ""}$linesOffset$it" }
            )
        } else {
            buffer.add(lines.joinToString("") { "\n$linesOffset$it" })
        }
        blocks.forEach {
            it.print(buffer, offset + 4)
        }
        buffer.add("\n$rootOffset}")
    }
}

File("../").children()
    .filter { it.name == "build.gradle.kts" }
    .forEach {
        format(it)
    }