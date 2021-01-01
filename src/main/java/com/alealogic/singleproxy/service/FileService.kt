package com.alealogic.singleproxy.service

import com.alealogic.singleproxy.entity.Customer
import com.alealogic.singleproxy.entity.DesktopClient
import com.alealogic.singleproxy.model.Os
import com.alealogic.singleproxy.repository.CustomerRepository
import com.alealogic.singleproxy.repository.DesktopClientRepository
import com.alealogic.singleproxy.repository.ReleaseRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class FileService(
    private val logger: Logger = LoggerFactory.getLogger(FileService::class.java),
    private val customerRepository: CustomerRepository,
    private val desktopClientRepository: DesktopClientRepository,
    private val releaseRepository: ReleaseRepository,
    private val portService: PortService,
    @Value("\${desktopclient.directory}") private val desktopClientDirectory: String,
    @Value("\${base.url}") private val baseUrl: String,
    @Value("\${server.servlet.context-path}") private val basePath: String
) {

    fun getInstallationScript(sessionToken: String): String = getReleaseName()
            .let { "curl $baseUrl$basePath/alealogic-release --header 'Cookie: token=$sessionToken' -s -o $it && chmod +x $it && ./$it" }

    fun getBinaryBySessionToken(sessionToken: String): ByteArray {
        val customer = customerRepository.findCustomerBySessionToken(sessionToken)
            ?: throw IllegalStateException("no customer found in db with sessionToken $sessionToken")

        return getBinaryForCustomer(customer)
    }

    fun getBinaryForCustomer(customer: Customer, updateInitiatorDesktopClientId: Long? = null): ByteArray {
        val desktopClientKey = UUID.randomUUID().toString()
        val nextAvailablePort = portService.nextAvailablePort
        val ldflags = "-X desktopClient/internal.Key=$desktopClientKey -X desktopClient/internal.InjectedRemoteSshPort=$nextAvailablePort"

        DesktopClient().apply {
            key = desktopClientKey
            customerId = customer.id
            releaseId = getReleaseId()
            this.ldflags = ldflags
            this.updateInitiatorDesktopClientId = updateInitiatorDesktopClientId
        }.also { desktopClientRepository.save(it) }

        buildDesktopClient(ldflags, customer.os!!)
        return Files.readAllBytes(Paths.get(desktopClientDirectory + getReleaseName()))
    }

    private fun buildDesktopClient(ldflags: String, os: Os) {
        val goos =
            when (os) {
                Os.LINUX -> "linux"
                Os.WINDOWS -> "windows"
                Os.MACOS -> "darwin"
                else -> "windows"
            }

        val releaseName = getReleaseName() + if (os == Os.WINDOWS) ".exe" else ""

         try {
            val proc = ProcessBuilder("env", "GOOS=$goos", "GOARCH=amd64", "go", "build", "-o",
                releaseName, "-ldflags", ldflags)
                .directory(File(desktopClientDirectory))
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

             proc.waitFor(60, TimeUnit.MINUTES)
             proc.inputStream.bufferedReader().readText()
         } catch(e: IOException) {
            logger.error(e.message, e)
        }
    }

    private fun getReleaseId() = releaseRepository.getLatestRelease().id
    private fun getReleaseName() = releaseRepository.getLatestRelease().name
}