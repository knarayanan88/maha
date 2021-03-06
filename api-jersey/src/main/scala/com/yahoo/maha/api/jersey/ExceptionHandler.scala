// Copyright 2017, Yahoo Holdings Inc.
// Licensed under the terms of the Apache License 2.0. Please see LICENSE file in project root for terms.
package com.yahoo.maha.api.jersey

import javax.ws.rs.core.{MediaType, Response}
import javax.ws.rs.ext.{ExceptionMapper, Provider}

import grizzled.slf4j.Logging

import scala.beans.BeanProperty

@Provider
class GenericExceptionMapper extends ExceptionMapper[Throwable] with Logging {

  override def toResponse(e: Throwable): Response = {

    val response: Response = {
      e match {
        case iae: IllegalArgumentException => Response.status(Response.Status.BAD_REQUEST).entity(Error(iae.getMessage)).`type`(MediaType.APPLICATION_JSON).build()
        case NotFoundException(error) => Response.status(Response.Status.BAD_REQUEST).entity(error).`type`(MediaType.APPLICATION_JSON).build()
        case _ => Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Error(e.getMessage)).`type`(MediaType.APPLICATION_JSON).build()
      }
    }

    error(s"response status: ${response.getStatus} , response entity: ${response.getEntity}")
    response
  }

}

case class Error(@BeanProperty errorMsg: String)

case class NotFoundException(error: Error) extends Exception
