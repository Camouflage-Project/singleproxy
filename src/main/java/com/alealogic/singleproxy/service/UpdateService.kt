package com.alealogic.singleproxy.service

import com.alealogic.singleproxy.repository.CustomerRepository
import com.alealogic.singleproxy.repository.DesktopClientRepository
import com.alealogic.singleproxy.repository.ReleaseRepository
import org.springframework.stereotype.Service

@Service
class UpdateService(
    private val customerRepository: CustomerRepository,
    private val desktopClientRepository: DesktopClientRepository,
    private val releaseRepository: ReleaseRepository,
    private val fileService: FileService
) {

    fun getBinary(key: String): Pair<String, ByteArray> {
        val desktopClient = desktopClientRepository.findByKey(key)
            ?: throw IllegalStateException("no desktopClient found in db with key $key")

        val customer = customerRepository.findById(desktopClient.customerId!!)
            .orElseThrow { throw IllegalStateException("no customer found in db with customerId ${desktopClient.customerId}") }

        return fileService.getBinaryForCustomer(customer!!, desktopClient.id)
    }

    fun getNewestVersion(key: String) =
        desktopClientRepository.findByKey(key)
            ?.updateToReleaseId
            ?.let { releaseRepository.findById(it) }
            ?.name

}