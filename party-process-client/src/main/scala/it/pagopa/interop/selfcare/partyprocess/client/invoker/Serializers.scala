package it.pagopa.interop.selfcare.partyprocess.client.invoker

    import java.time.{LocalDate, LocalDateTime, OffsetDateTime, ZoneId}
    import java.time.format.DateTimeFormatter
import org.json4s.{Serializer, CustomSerializer, JNull}
import org.json4s.ext.JavaTypesSerializers
import org.json4s.JsonAST.JString

import scala.util.Try

            import it.pagopa.interop.selfcare.partyprocess.client.model.PartyRole
            import it.pagopa.interop.selfcare.partyprocess.client.model.ProductState
            import it.pagopa.interop.selfcare.partyprocess.client.model.RelationshipState

object Serializers {

    case object DateTimeSerializer extends CustomSerializer[OffsetDateTime]( _ => ( {
    case JString(s) =>
    Try(OffsetDateTime.parse(s, DateTimeFormatter.ISO_OFFSET_DATE_TIME)) orElse
    Try(LocalDateTime.parse(s).atZone(ZoneId.systemDefault()).toOffsetDateTime) getOrElse null
    }, {
    case d: OffsetDateTime =>
    JString(d.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
    }))

    case object LocalDateSerializer extends CustomSerializer[LocalDate]( _ => ( {
    case JString(s) => LocalDate.parse(s)
    }, {
    case d: LocalDate =>
    JString(d.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }))




            object PartyRoleSerializer
            extends CustomSerializer[PartyRole](formats =>
            (
            {
                    case JString("MANAGER") => PartyRole.MANAGER
                    case JString("DELEGATE") => PartyRole.DELEGATE
                    case JString("SUB_DELEGATE") => PartyRole.SUB_DELEGATE
                    case JString("OPERATOR") => PartyRole.OPERATOR
            },
            {
                    case PartyRole.MANAGER => JString("MANAGER")
                    case PartyRole.DELEGATE => JString("DELEGATE")
                    case PartyRole.SUB_DELEGATE => JString("SUB_DELEGATE")
                    case PartyRole.OPERATOR => JString("OPERATOR")
            }
            )
            )




            object ProductStateSerializer
            extends CustomSerializer[ProductState](formats =>
            (
            {
                    case JString("PENDING") => ProductState.PENDING
                    case JString("ACTIVE") => ProductState.ACTIVE
            },
            {
                    case ProductState.PENDING => JString("PENDING")
                    case ProductState.ACTIVE => JString("ACTIVE")
            }
            )
            )




            object RelationshipStateSerializer
            extends CustomSerializer[RelationshipState](formats =>
            (
            {
                    case JString("PENDING") => RelationshipState.PENDING
                    case JString("ACTIVE") => RelationshipState.ACTIVE
                    case JString("SUSPENDED") => RelationshipState.SUSPENDED
                    case JString("DELETED") => RelationshipState.DELETED
                    case JString("REJECTED") => RelationshipState.REJECTED
            },
            {
                    case RelationshipState.PENDING => JString("PENDING")
                    case RelationshipState.ACTIVE => JString("ACTIVE")
                    case RelationshipState.SUSPENDED => JString("SUSPENDED")
                    case RelationshipState.DELETED => JString("DELETED")
                    case RelationshipState.REJECTED => JString("REJECTED")
            }
            )
            )



val enumSerializers = Seq(
            PartyRoleSerializer,
            ProductStateSerializer,
            RelationshipStateSerializer,
)

def all: Seq[Serializer[_]] = JavaTypesSerializers.all ++ enumSerializers :+ DateTimeSerializer :+ LocalDateSerializer

}