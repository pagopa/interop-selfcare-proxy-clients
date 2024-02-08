package it.pagopa.interop.selfcare.userregistry.client.invoker

    import java.time.{LocalDate, LocalDateTime, OffsetDateTime, ZoneId}
    import java.time.format.DateTimeFormatter
import org.json4s.{Serializer, CustomSerializer, JNull}
import org.json4s.ext.JavaTypesSerializers
import org.json4s.JsonAST.JString

import scala.util.Try

            import it.pagopa.interop.selfcare.userregistry.client.model.Field

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




            object FieldSerializer
            extends CustomSerializer[Field](formats =>
            (
            {
                    case JString("birthDate") => Field.birthDate
                    case JString("email") => Field.email
                    case JString("familyName") => Field.familyName
                    case JString("fiscalCode") => Field.fiscalCode
                    case JString("name") => Field.name
                    case JString("workContacts") => Field.workContacts
            },
            {
                    case Field.birthDate => JString("birthDate")
                    case Field.email => JString("email")
                    case Field.familyName => JString("familyName")
                    case Field.fiscalCode => JString("fiscalCode")
                    case Field.name => JString("name")
                    case Field.workContacts => JString("workContacts")
            }
            )
            )



val enumSerializers = Seq(
            FieldSerializer,
)

def all: Seq[Serializer[_]] = JavaTypesSerializers.all ++ enumSerializers :+ DateTimeSerializer :+ LocalDateSerializer

}