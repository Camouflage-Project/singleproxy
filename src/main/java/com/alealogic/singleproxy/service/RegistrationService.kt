package com.alealogic.singleproxy.service

import com.alealogic.singleproxy.repository.DesktopClientRepository
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.math.log

@Service
class RegistrationService(private val desktopClientRepository: DesktopClientRepository) {

    fun registerDesktopClient(desktopClientKey: String) {
        val desktopClient = desktopClientRepository.findByKey(desktopClientKey)
        if (desktopClient != null) {
            desktopClient
                .apply { registered = true }
                .also { desktopClientRepository.save(it) }
        } else throw IllegalStateException("no DesktopClient found in db with desktopClientKey $desktopClientKey")

        desktopClient.updateInitiatorDesktopClientId?.also { updater ->
            desktopClientRepository
                .findByUpdateInitiatorDesktopClientId(updater)
                ?.apply { obsolete = true }
                ?.also { desktopClientRepository.save(it) }
        }
    }
}