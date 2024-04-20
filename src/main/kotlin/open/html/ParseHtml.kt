package open.html

import open.html.md.*
import org.apache.commons.text.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.openqa.selenium.By
import org.openqa.selenium.By.ByTagName
import org.openqa.selenium.By.className
import org.openqa.selenium.chrome.ChromeDriver
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.net.URL
import java.util.HashSet
import javax.net.ssl.HttpsURLConnection

fun main() {
    val url =
//        "https://proandroiddev.com/mastering-android-viewmodels-essential-dos-and-donts-part-2-%EF%B8%8F-2b49281f0029"
//        "https://medium.com/androiddevelopers/understanding-nested-scrolling-in-jetpack-compose-eb57c1ea0af0"
        "https://proandroiddev.com/kotlins-sealed-interfaces-in-android-0882a3d2afd1"
//        "https://proandroiddev.com/jetpack-compose-ktor-and-koin-di-unlocking-mad-skills-05b9f28b4cd8"

    val html = seleniumGetPageHtml(url)
    val content = parseMedium(html) { error ->
        println("parse error:===$error")
    }
    val desPath = "G:\\resource\\桌面\\medium.md"
    writeToFile(content, desPath)
}

fun writeToFile(content: String, path: String): String {
    val file = File(path)
    if (!file.exists()) {
        file.createNewFile()
    }
    OutputStreamWriter(FileOutputStream(file), "utf-8").use {
        it.write(content)
    }
    return path
}

fun parseMedium(html: String, error: (String?) -> Unit): String {
    try {
        val driver = setUpWebDriver()

        // val fileurl = "G:/resource/桌面/2.mhtml"
//        val fileurl = "G:/resource/桌面/3.html"
        //val fileurl = "G:/resource/桌面/4.html"
//        val fileurl = "G:/resource/桌面/5.html"
//        val fileurl = "G:/resource/桌面/spancode.html"
//        val input = File(fileurl)


        var doc = Jsoup.parse(html)

        val hSet = HashSet<String>()
        hSet.add("Follow")
        hSet.add("ProAndroidDev")

        val sb = StringBuilder()

        var allElements = doc.allElements
        // get the first article element then parse to doc
        for (e in allElements) {
            val tagName = e.tagName()
            when (tagName) {
                "article" -> {
                    val articleHtml = e.html()
                    doc = Jsoup.parse(articleHtml)
                    break;
                }
            }
        }

        allElements = doc.allElements
        for (e in allElements) {
            val tagName = e.tagName()
            when (tagName) {
                "button" -> {
                    hSet.add(e.text())
                }

                "div" -> {
                    val attr: String? = e.attr("role")
                    if (attr != null && attr.equals("separator")) {
                        sb.append(separator()).br().br()
                    }
                    val aChild: Element? = e.firstElementChild()
                    if (aChild != null && aChild.tagName() == "a") {
                        val atext: String? = aChild.text()
                        if (aChild.attr("data-testid") == "publicationName" || atext == null || atext.isEmpty()) {
                            continue
                        }
                        val h2e: Elements? = e.getElementsByTag("h2")
                        val h2Text = h2e?.text()

                        var link = aChild.attr("href")
                        if (!link.startsWith("https://")) {
                            link = "https://proandroiddev.com" + link
                        }
                        val a = a(h2Text ?: link, link)
                        sb.append(a).br().br()

                        hSet.add(h2Text ?: "")

                        val h3e = e.getElementsByTag("h3")
                        hSet.add(h3e.text())

                        val pe = e.getElementsByTag("p")
                        hSet.add(pe.text())
                    }
                }

                "h1" -> {
                    val h1 = h1(e.text())
                    sb.append(h1).br().br()
                }

                "h2" -> {
                    if (hSet.contains(e.text())) {
                        continue
                    }
                    val h2 = h2(e.text())
                    sb.append(h2).br().br()
                }

                "h3" -> {
                    if (hSet.contains(e.text())) {
                        continue
                    }
                    val h3 = h3(e.text())
                    sb.append(h3).br().br()
                }

                "p" -> {
                    val pe: Element? = e.firstElementChild()
                    if (pe != null && pe.tagName() == "button") {
                        continue
                    }
                    if (e.parent()?.tagName().equals("blockquote") ||
                        hSet.contains(e.text())
                    ) {
                        continue
                    }
                    parseParagraph(sb, e, e.text()).br().br()
                }

                "span" -> {
                    val hasAttr = e.hasAttr("data-selectable-paragraph")
                    if (hasAttr) {
                        val result = parseSpanCode(e.html())
                        sb.append(formatCode(result)).br().br()
                    }
                }

                "blockquote" -> {
                    val quoteText = e.text()
                    sb.append(blockquote(quoteText)).br().br()
                }

                "ol" -> {
                    e.children().forEachIndexed { index, li ->
                        val litext = li.text()
                        val mdliString = ol(index + 1, litext)
                        parseParagraph(sb, li, mdliString).br().br()
                    }
                }

                "ul" -> {
                    e.children().forEachIndexed { _, li ->
                        val litext = li.text()
                        val mdliString = li(litext)
                        parseParagraph(sb, li, mdliString).br().br()
                    }
                }

                "figure" -> {
                    val imgElement = e.getElementsByTag("img")
                    val imgUrl = imgElement.attr("src")
                    if (imgUrl.isNotEmpty()) {
                        val imgText = img("", imgUrl)
                        sb.append(imgText).br()

                        sb.append(e.text()).br().br()
                    }
                }

                "iframe" -> {
                    val frameSrc = e.attr("src")
                    if (!frameSrc.contains("www.google.com") && frameSrc.isNotEmpty()) {
                        val code = getGistFormatCode(driver, frameSrc)
                        sb.append(code).br().br()
                    }
                }
            }
        }
        driver.quit()
        //println(sb.toString())
        return sb.toString()
    } catch (e: Exception) {
        error(e.message)
        return ""
    }
}

fun parseParagraph(sb: StringBuilder, e: Element, content: String): StringBuilder {
    var content2 = content
    val codeSb = StringBuilder()

    val children = e.children()
    for (child in children) {
        val tName = child.tagName()
        val originalText = child.text()
        when (tName) {
            // [**'text'**](link) code->strong->link
            // ['text'](link) code->link
            "code" -> {
                val codeText = code(originalText)

                content2 = composeString(codeSb, content2, originalText, codeText)

                val strongTag: Elements? = child.getElementsByTag("strong")
                var strongText: String? = null
                if (strongTag != null) {
                    val text = strongTag.text()
                    if (text.isNotEmpty()) {
                        strongText = bold(codeText)
                        val s = codeSb.toString().replace(codeText, strongText)
                        codeSb.clearAndAppend(s)
                    }
                }

                val atag: Elements? = child.getElementsByTag("a")
                if (atag != null) {
                    val text = atag.text()
                    if (text.isNotEmpty()) {
                        val link = atag.attr("href")
                        val innerText = strongText ?: codeText
                        val alink = a(innerText, link)
                        val s = codeSb.toString().replace(innerText, alink)
                        codeSb.clearAndAppend(s)
                    }
                }
            }
            // [**text**](link) strong->link
            "strong" -> {
                val strongText = bold(originalText)
                content2 = composeString(codeSb, content2, originalText, strongText)

                val atag: Elements? = child.getElementsByTag("a")
                if (atag != null) {
                    val text = atag.text()
                    if (text.isNotEmpty()) {
                        val link = atag.attr("href")
                        val alink = a(strongText, link)
                        val s = codeSb.toString().replace(strongText, alink)
                        codeSb.clearAndAppend(s)
                    }
                }
            }
            // [text](link)
            "a" -> {
                val link = child.attr("href")
                val a = a(originalText, link)
                content2 = composeString(codeSb, content2, originalText, a)
            }
        }
    }
    sb.append(codeSb)
    return sb.append(content2)
}

fun composeString(codeSb: StringBuilder, content: String, originalText: String, formatText: String): String {
    val index = content.indexOf(originalText)
    val preString = content.substring(0, index)
    codeSb.append(preString).append(formatText)

    return content.substring(index + originalText.length)
}


fun getGistFormatCode(driver: ChromeDriver, frameSrc: String): String {
    driver.get(frameSrc)

    Thread.sleep(3000)

    val gistElement = driver.findElement(className("gist-meta"))
    val ae = gistElement.findElement(ByTagName("a"))
    val alink = ae.getAttribute("href")

    driver.get(alink)

    Thread.sleep(3000)
    val e = driver.findElement(By.tagName("pre"))
    val formatCode = formatCode(e.text)

    return formatCode
}

fun getGistCodeBlock(url: String): String {
    val connect = URL(url).openConnection() as HttpsURLConnection
    val content = connect.inputStream.bufferedReader().use {
        it.readText()
    }
    return content
}

fun parseSpanCode(html: String): String {
    val spanList = html.split("<br>")
    val sb = StringBuilder()
    spanList.forEachIndexed { index, s ->
        s.split("</span>").forEach { ss ->
            val code = StringEscapeUtils.unescapeHtml4(removeSpan(ss))
            sb.append(code)
        }
        if (index != (spanList.size - 1)) {
            sb.br()
        }
    }
    return sb.toString()
}

private fun removeSpan(s: String): String {
    val arrayIndex = ArrayList<Array<Int>>()
    var indexArray = Array(2) { -1 }
    s.forEachIndexed { index, c ->
        if (c == '<') {
            indexArray = Array(2) { -1 }
            indexArray[0] = index
        } else if (c == '>') {
            indexArray[1] = index
            arrayIndex.add(indexArray)
        }
    }
    var result = s
    arrayIndex.forEach { intArray ->
        val slice = s.slice(IntRange(intArray[0], intArray[1]))
        result = result.replace(slice, "")
    }
    return result
}