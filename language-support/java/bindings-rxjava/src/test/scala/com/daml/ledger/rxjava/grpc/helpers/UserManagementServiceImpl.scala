// Copyright (c) 2022 Digital Asset (Switzerland) GmbH and/or its affiliates. All rights reserved.
// SPDX-License-Identifier: Apache-2.0

package com.daml.ledger.rxjava.grpc.helpers

import com.daml.ledger.api.auth.Authorizer
import com.daml.ledger.api.auth.services.UserManagementServiceAuthorization
import com.daml.ledger.api.v1.admin.user_management_service.UserManagementServiceGrpc.UserManagementService
import com.daml.ledger.api.v1.admin.user_management_service._
import com.daml.logging.LoggingContext
import io.grpc.ServerServiceDefinition

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

final class UserManagementServiceImpl extends UserManagementService with FakeAutoCloseable {

  private val log = mutable.ArrayBuffer.empty[Any]

  def requests(): Array[Any] = {
    log.toArray
  }

  private def record[Result](request: Any)(result: Result): Future[Result] = {
    log += request
    Future.successful(result)
  }

  override def createUser(request: CreateUserRequest): Future[User] =
    record(request)(User.defaultInstance)

  override def getUser(request: GetUserRequest): Future[User] =
    record(request)(User.defaultInstance)

  override def deleteUser(request: DeleteUserRequest): Future[DeleteUserResponse] =
    record(request)(DeleteUserResponse.defaultInstance)

  override def listUsers(request: ListUsersRequest): Future[ListUsersResponse] =
    record(request)(ListUsersResponse.defaultInstance)

  override def grantUserRights(request: GrantUserRightsRequest): Future[GrantUserRightsResponse] =
    record(request)(GrantUserRightsResponse.defaultInstance)

  override def revokeUserRights(
      request: RevokeUserRightsRequest
  ): Future[RevokeUserRightsResponse] = record(request)(RevokeUserRightsResponse.defaultInstance)

  override def listUserRights(request: ListUserRightsRequest): Future[ListUserRightsResponse] =
    record(request)(ListUserRightsResponse.defaultInstance)
}

object UserManagementServiceImpl {

  // for testing only
  private[helpers] def createWithRef(
      authorizer: Authorizer
  )(implicit ec: ExecutionContext): (ServerServiceDefinition, UserManagementServiceImpl) = {
    implicit val loggingContext: LoggingContext = LoggingContext.ForTesting
    val impl = new UserManagementServiceImpl
    val authImpl = new UserManagementServiceAuthorization(impl, authorizer)
    (UserManagementServiceGrpc.bindService(authImpl, ec), impl)
  }

}