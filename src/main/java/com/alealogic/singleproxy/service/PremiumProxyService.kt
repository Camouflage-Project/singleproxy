package com.alealogic.singleproxy.service

import com.alealogic.singleproxy.entity.Customer
import com.alealogic.singleproxy.entity.PremiumProxy
import com.alealogic.singleproxy.model.PortDto
import com.alealogic.singleproxy.repository.PremiumProxyRepository
import org.springframework.stereotype.Service
import java.util.concurrent.LinkedBlockingQueue

@Service
class PremiumProxyService(private val premiumProxyRepository: PremiumProxyRepository) {

    private val premiumProxies = getPremiumProxyQueue()

    private fun getPremiumProxyQueue(): LinkedBlockingQueue<PremiumProxy?> = LinkedBlockingQueue(premiumProxyRepository.findAll())

    fun getNextPremiumPortForCustomer(customer: Customer): PortDto? =
        premiumProxies
            .remove()
            ?.also { premiumProxies.add(it) }
            ?.let { PortDto(it.port, it.ipId) }

}