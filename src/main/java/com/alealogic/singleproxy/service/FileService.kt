package com.alealogic.singleproxy.service

import com.alealogic.singleproxy.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

@Service
class FileService(
    private val customerRepository: CustomerRepository,
    private val portService: PortService,
    @Value("\${desktopclient.directory}") private val desktopClientDirectory: String
) {

    fun getBinaryForCustomer(sessionToken: String): ByteArray {
        val customer = customerRepository.findCustomerBySessionToken(sessionToken)
        buildDesktopClient(customer?.apiKey!!)
        val executableName = "desktopClient_release_1"
        renameExecutable(executableName)
        return Files.readAllBytes(Paths.get(desktopClientDirectory + executableName))
    }

    private fun buildDesktopClient(apiKey: String) {
        val nextAvailablePort = portService.nextAvailablePort
        buildDesktopClient(apiKey, nextAvailablePort)
    }

    private fun renameExecutable(name: String): String? {
        return try {
            val proc = ProcessBuilder("mv", "desktopClient", name)
                .directory(File(desktopClientDirectory))
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            proc.waitFor(60, TimeUnit.MINUTES)
            proc.inputStream.bufferedReader().readText()
        } catch(e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun buildDesktopClient(apiKey: String, port: Int): String? {
        return try {
            val proc = ProcessBuilder("go", "build", "-ldflags", "\"-X desktopClient/internal.Key=$apiKey -X desktopClient/internal.InjectedRemoteSshPort=$port\"")
                .directory(File(desktopClientDirectory))
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            proc.waitFor(60, TimeUnit.MINUTES)
            proc.inputStream.bufferedReader().readText()
        } catch(e: IOException) {
            e.printStackTrace()
            null
        }
    }
}