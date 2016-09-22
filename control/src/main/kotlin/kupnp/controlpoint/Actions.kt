package kupnp.controlpoint

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root
import org.simpleframework.xml.convert.Convert
import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode

/**
 * Created by chris on 22/09/2016.
 */


@Root(name = "Envelope")
@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope", prefix = "s")
data class ActionRequest(
        @field:Element(name = "Body") var body: ActionBody? = null
)

@Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope")
data class ActionBody(val actionName: ActionName)

@Root
@Convert(ActionConverter::class)
data class ActionName(
        val actionName: String,
        val serviceType: String,
        val serviceVersion: String = "1"
) {

    val namespaceReference: String = "urn:schemas-upnp-org:service:$serviceType:$serviceVersion"
    val namespacePrefix = "u"

}

object ActionConverter : Converter<ActionName> {
    override fun read(node: InputNode?): ActionName {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun write(node: OutputNode, value: ActionName) {
        node.namespaces.setReference(value.namespaceReference, value.namespacePrefix)
        node.name = "${value.namespacePrefix}:${value.actionName}"
    }

}