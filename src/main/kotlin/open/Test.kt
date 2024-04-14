import open.html.md.SSLSocketFactoryFacade
import open.html.md.SSLSocketFactoryWrapper
import open.html.md.TrustAllManager
import open.html.setUpWebDriver
import sun.security.ssl.SSLSocketFactoryImpl
import java.net.URL
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

fun main() {
    val s = "<span class=\"hljs-keyword\">fun BodyText(<span class=\"hljs-meta\">@StringRes id: int, ...)"
    //extracted(s)

//    val url= "https://www.baidu.com"
//    val url = "https://www.qidian.com"
//    val url= "https://stackoverflow.com/questions/63855202/java-httpsurlconnection-connection-reset"
//    val url= "https://www.medium.com"
    val url = "https://proandroiddev.com/media/77b76e8ee92649e696b2260559f59d65"

    System.setProperty("javax.net.debug", "all");

//    val driver = setUpWebDriver()
//    driver.get(url)
//    println(driver.pageSource)

    val re = getGistCode(url)
    println(re)
}

fun getSSLSocketFactory(url: URL): SSLSocketFactory? {
//    val trustAllCerts = arrayOfNulls<TrustManager>(1)
//    trustAllCerts[0] = TrustAllManager() as TrustManager
    val trustAllCerts = arrayOf(TrustAllManager())
    val sc: SSLContext = SSLContext.getInstance("SSL")
    sc.init(null, trustAllCerts, SecureRandom());

    val sslParameters = SSLParameters()

    val sniHostNames = listOf(SNIHostName(url.host))
    sslParameters.serverNames = sniHostNames

    val sslSocketFactoryWrapper = SSLSocketFactoryWrapper(sc.socketFactory, sslParameters)
    return sslSocketFactoryWrapper
}


fun getGistCode(url: String): String {
    val connect = URL(url).openConnection() as HttpsURLConnection
//    connect.hostnameVerifier = HostnameVerifier { s, sslSession ->
//        true
//    }
//    connect.sslSocketFactory = getSSLSocketFactory(URL(url))
//    connect.sslSocketFactory = SSLSocketFactoryFacade(connect.getURL().getHost());

    val content = connect.inputStream.bufferedReader().use {
        it.readText()
    }
    return content
}

private fun extracted(s: String) {
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
        println("slice==$slice")
        result = result.replace(slice, "")
    }
}

