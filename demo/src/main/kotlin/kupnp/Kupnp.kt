package kupnp

import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import kupnp.controlpoint.DeviceDescription
import kupnp.controlpoint.ServiceDescription
import kupnp.controlpoint.getDeviceService
import okhttp3.HttpUrl
import java.util.Scanner

fun main(args: Array<String>) {

    info("Starting search")

    val ssdp = SSDPService.msearch()
            .flatMapMaybe {
                val location = it.headers[SsdpMessage.HEADER_LOCATION] ?: ""
                val url = HttpUrl.parse(location)?.newBuilder()?.encodedPath("/")?.build() ?: return@flatMapMaybe Maybe.empty<ServiceDescription>()
                //debug("SearchLocation: $location")
                getDeviceService(url).getDeviceDescription(location).onExceptionResumeNext(Maybe.empty<DeviceDescription>())
            }
            .doOnNext { debug("DeviceDescription: $it") }
            .doOnComplete { info("Completed SSDP Discovery") }
            .subscribeOn(Schedulers.io())

    val search = WsDiscoveryMessage().apply { addType("NetworkVideoTransmitter") }
    val ws = WsDiscoveryService.search(search)
            .doOnNext { debug("WSSearch Result: ${it.packetAddress} ${it.hardware}") }
            .doOnComplete { info("Completed WS-Discovery") }
            .subscribeOn(Schedulers.io())

    Flowable.merge(ssdp, ws).subscribe({}, { it.printStackTrace() })
//    val sub = ws.subscribe({}, { it.printStackTrace() })
    Scanner(System.`in`).nextLine()
}

