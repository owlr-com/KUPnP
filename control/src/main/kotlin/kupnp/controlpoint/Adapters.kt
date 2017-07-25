package kupnp.controlpoint

import io.reactivex.Maybe
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * Created by chris on 21/09/2016.
 */

interface DeviceService {

    /**
     * Gets the description of the passed in DeviceDescription url
     */
    @GET
    fun getDeviceDescription(
            @Url path: String
    ): Maybe<DeviceDescription>

    @GET
    fun getServiceDescription(
            @Url path: String
    ): Maybe<ServiceDescription>

    /**
     * Ask a set/get from a control point. These are discovered using `getDeviceDescription` and `getServiceDescription`
     *
     * @param controlURL this is the URL taken from the `Service.controlURL` field.
     * @param soapHeader  "urn:schemas-upnp-org:service:serviceType:v#actionName".
     *  - serviceType:v = `Service.serviceType`
     *  - actionName = `Action.name`
     */
    @POST
    fun postActionCommand(
            @Url controlURL: String,
            @Header("SOAPACTION") soapHeader: String,
            @Body actionRequest: ActionRequest
    ): Maybe<ActionResponse>
}

/**
 * Create a controlpoint service for the specified controlpoint endpoint
 */
fun getRetrofit(baseUrl: HttpUrl, scheduler: Scheduler = Schedulers.io()): Retrofit {
    return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(scheduler))
            .addConverterFactory(xmlConverter)
            .baseUrl(baseUrl)
            .build()
}
