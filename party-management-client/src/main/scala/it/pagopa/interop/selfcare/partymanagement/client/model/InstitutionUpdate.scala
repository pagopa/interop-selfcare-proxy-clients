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

import it.pagopa.interop.selfcare.partymanagement.client.invoker.ApiModel


            case class InstitutionUpdate (
                    /* The type of the institution */
                institutionType: Option[String] = None,
                description: Option[String] = None,
                digitalAddress: Option[String] = None,
                address: Option[String] = None,
                    /* institution tax code */
                taxCode: Option[String] = None
            ) extends ApiModel



