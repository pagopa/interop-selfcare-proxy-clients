/**
 * Party Management Micro Service V1
 * This service is the party manager
 *
 * The version of the OpenAPI document: v1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package it.pagopa.interop.selfcare.partymanagement.client.model

    import java.time.OffsetDateTime
    import java.util.UUID
import it.pagopa.interop.selfcare.partymanagement.client.invoker.ApiModel


            case class Relationship (
                id: UUID,
                    /* person ID */
                from: UUID,
                    /* institution ID */
                to: UUID,
                    /* path of the file containing the signed onboarding document */
                filePath: Option[String] = None,
                    /* name of the file containing the signed onboarding document */
                fileName: Option[String] = None,
                    /* content type of the file containing the signed onboarding document */
                contentType: Option[String] = None,
                    /* confirmation token identifier */
                tokenId: Option[UUID] = None,
                role: PartyRole,
                product: RelationshipProduct,
                state: RelationshipState,
                    /* pricing plan */
                pricingPlan: Option[String] = None,
                institutionUpdate: Option[InstitutionUpdate] = None,
                billing: Option[Billing] = None,
                createdAt: Option[OffsetDateTime] = None,
                updatedAt: Option[OffsetDateTime] = None
            ) extends ApiModel



