package com.alealogic.singleproxy.service

import com.alealogic.singleproxy.model.Os
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
        val customer = customerRepository.findCustomerBySessionToken(sessionToken)!!
        buildDesktopClient(customer.apiKey!!, customer.os!!)
        return Files.readAllBytes(Paths.get(desktopClientDirectory + getReleaseName()))
    }

    private fun buildDesktopClient(apiKey: String, os: Os) {
        val port = portService.nextAvailablePort
        val goos =
            when (os) {
                Os.LINUX -> "linux"
                Os.WINDOWS -> "windows"
                Os.MACOS -> "darwin"
                else -> "windows"
            }

        val releaseName = getReleaseName() + if (os == Os.WINDOWS) ".exe" else ""

         try {
            val proc = ProcessBuilder("env", "GOOS=$goos", "GOARCH=amd64" ,"go", "build", "-o",
                releaseName, "-ldflags", "\"-X desktopClient/internal.Key=$apiKey -X desktopClient/internal.InjectedRemoteSshPort=$port\"")
                .directory(File(desktopClientDirectory))
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            proc.waitFor(60, TimeUnit.MINUTES)
            proc.inputStream.bufferedReader().readText()
        } catch(e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getReleaseName() = "desktopClient_release_1"
}