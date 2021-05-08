package com.alealogic.singleproxy.service

import com.alealogic.singleproxy.repository.DesktopClientRepository
import org.springframework.stereotype.Service

@Service
class RegistrationService(private val desktopClientRepository: DesktopClientRepository) {

    fun registerDesktopClient(desktopClientKey: String) {
        val desktopClient = desktopClientRepository.findByKey(desktopClientKey)
        if (desktopClient != null) {
            desktopClient
                .apply { registered = true }
                .also { desktopClientRepository.save(it) }
        } else throw IllegalStateException("no DesktopClient found in db with desktopClientKey $desktopClientKey")

        desktopClient.updateInitiatorDesktopClientId?.let {
            desktopClientRepository.findByUpdateInitiatorDesktopClientId(it)
        }?.apply { obsolete = true }?.also { desktopClientRepository.save(it) }
    }
}
