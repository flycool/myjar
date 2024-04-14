package open.html.md

import java.io.IOException
import java.io.InputStream
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException
import java.util.*
import javax.net.ssl.*


class SSLSocketFactoryWrapper(
    private val wrappedFactory: SSLSocketFactory,
    private val sslParameters: SSLParameters
) : SSLSocketFactory() {
    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(host: String, port: Int): Socket {
        val socket = wrappedFactory.createSocket(host, port) as SSLSocket
        setParameters(socket)
        return socket
    }

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(host: String, port: Int, localHost: InetAddress, localPort: Int): Socket {
        val socket = wrappedFactory.createSocket(host, port, localHost, localPort) as SSLSocket
        setParameters(socket)
        return socket
    }


    @Throws(IOException::class)
    override fun createSocket(host: InetAddress, port: Int): Socket {
        val socket = wrappedFactory.createSocket(host, port) as SSLSocket
        setParameters(socket)
        return socket
    }

    @Throws(IOException::class)
    override fun createSocket(address: InetAddress, port: Int, localAddress: InetAddress, localPort: Int): Socket {
        val socket = wrappedFactory.createSocket(address, port, localAddress, localPort) as SSLSocket
        setParameters(socket)
        return socket
    }

    @Throws(IOException::class)
    override fun createSocket(): Socket {
        val socket = wrappedFactory.createSocket() as SSLSocket
        setParameters(socket)
        return socket
    }

    override fun getDefaultCipherSuites(): Array<String> {
        return wrappedFactory.defaultCipherSuites
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return wrappedFactory.supportedCipherSuites
    }

    @Throws(IOException::class)
    override fun createSocket(s: Socket, host: String, port: Int, autoClose: Boolean): Socket {
        val socket = wrappedFactory.createSocket(s, host, port, autoClose) as SSLSocket
        setParameters(socket)
        return socket
    }

    private fun setParameters(socket: SSLSocket) {
        socket.sslParameters = sslParameters
    }
}

class SSLSocketFactoryFacade(hostName: String?) : SSLSocketFactory() {
    private var sslsf: SSLSocketFactory? = null
    private var sslParameters: SSLParameters? = null

    init {
        sslParameters = SSLParameters()
        sslParameters!!.serverNames = Arrays.asList<SNIServerName>(SNIHostName(hostName))
        sslsf = getDefault() as SSLSocketFactory
    }

    @Throws(IOException::class)
    override fun createSocket(): Socket {
        val socket = sslsf!!.createSocket()
        (socket as SSLSocket).sslParameters = sslParameters
        return socket
    }

    @Throws(IOException::class)
    override fun createSocket(arg0: InetAddress?, arg1: Int, arg2: InetAddress?, arg3: Int): Socket {
        val socket = sslsf!!.createSocket(arg0, arg1, arg2, arg3)
        (socket as SSLSocket).sslParameters = sslParameters
        return socket
    }

    @Throws(IOException::class)
    override fun createSocket(arg0: InetAddress?, arg1: Int): Socket {
        val socket = sslsf!!.createSocket(arg0, arg1)
        (socket as SSLSocket).sslParameters = sslParameters
        return socket
    }

    @Throws(IOException::class)
    override fun createSocket(arg0: Socket?, arg1: InputStream?, arg2: Boolean): Socket {
        val socket = sslsf!!.createSocket(arg0, arg1, arg2)
        (socket as SSLSocket).sslParameters = sslParameters
        return socket
    }

    @Throws(IOException::class)
    override fun createSocket(arg0: Socket?, arg1: String?, arg2: Int, arg3: Boolean): Socket {
        val socket = sslsf!!.createSocket(arg0, arg1, arg2, arg3)
        (socket as SSLSocket).sslParameters = sslParameters
        return socket
    }

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(arg0: String?, arg1: Int, arg2: InetAddress?, arg3: Int): Socket {
        val socket = sslsf!!.createSocket(arg0, arg1, arg2, arg3)
        (socket as SSLSocket).sslParameters = sslParameters
        return socket
    }

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(arg0: String?, arg1: Int): Socket {
        val socket = sslsf!!.createSocket(arg0, arg1)
        (socket as SSLSocket).sslParameters = sslParameters
        return socket
    }

    override fun getDefaultCipherSuites(): Array<String> {
        return sslsf!!.defaultCipherSuites
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return sslsf!!.supportedCipherSuites
    }
}