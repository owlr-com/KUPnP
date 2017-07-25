package kupnp

import io.reactivex.Flowable
import kupnp.MulticastDiscovery.MulticastDiscoveryRequest

/**
 * Created by chris on 12/10/2016.
 */
object WsDiscoveryService {

    fun search(wsDiscoveryMessage: WsDiscoveryMessage = WsDiscoveryMessage()): Flowable<WsDiscoveryResponse> {
        val request = MulticastDiscoveryRequest(
                data = wsDiscoveryMessage.byteString(),
                multicastAddress = WsDiscoveryMessage.DEFAULT_IP_ADDRESS,
                port = WsDiscoveryMessage.DEFAULT_PORT,
                timeout = WsDiscoveryMessage.DEFAULT_TIMEOUT,
                responseSize = 4096
        )
        return MulticastDiscovery(request)
                .create()
                .map { WsDiscoveryResponse.parseResponse(it.data, it.address) }
                .cast(WsDiscoveryResponse::class.java)
                .distinct()
    }

}