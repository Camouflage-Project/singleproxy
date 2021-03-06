package com.alealogic.singleproxy.service

import com.alealogic.singleproxy.repository.DesktopClientRepository
import com.alealogic.singleproxy.util.Hasher
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class HeartbeatService(private val desktopClientRepository: DesktopClientRepository) {

    fun heartbeat(key: String, ip: String) =
        desktopClientRepository.findByKey(key)
            ?.apply {
                lastHeartbeat = LocalDateTime.now()
                if (lastIp != ip) ipId = Hasher.getHash(ip)
                lastIp = ip
            }?.also { desktopClientRepository.save(it) }

}
