package com.alealogic.singleproxy.service

import com.alealogic.singleproxy.entity.Customer
import com.alealogic.singleproxy.entity.DesktopClient
import com.alealogic.singleproxy.entity.PremiumProxy
import com.alealogic.singleproxy.model.PortDto
import com.alealogic.singleproxy.repository.DesktopClientRepository
import com.alealogic.singleproxy.repository.PremiumProxyRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.LinkedBlockingQueue

@Service
class PremiumProxyService(private val desktopClientRepository: DesktopClientRepository) {

    private val premiumProxies = getPremiumProxyQueue()

    private fun getPremiumProxyQueue(): LinkedBlockingQueue<DesktopClient> =
        LinkedBlockingQueue(desktopClientRepository.findAllByActiveTrue())

    fun getNextPremiumPortForCustomer(customer: Customer): PortDto? =
        premiumProxies
            .remove()
            ?.also { premiumProxies.add(it) }
            ?.let { PortDto(it.port, it.ipId, it.id) }

    fun removeFailedProxy(premiumProxy: PremiumProxy) = premiumProxies.remove(premiumProxy)

    @Scheduled(fixedDelay = 240000)
    fun addNewPremiumProxies() {
        val allByActiveTrue = desktopClientRepository.findAllByActiveTrue()
        allByActiveTrue.removeAll(premiumProxies)
        premiumProxies.addAll(allByActiveTrue)
    }
}
