/*
 * Copyright (c) 2016-2017 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intel.podm.business.redfish.services;

import com.intel.podm.business.ContextResolvingException;
import com.intel.podm.business.dto.RemoteTargetDto;
import com.intel.podm.business.dto.redfish.CollectionDto;
import com.intel.podm.business.entities.redfish.LogicalDrive;
import com.intel.podm.business.entities.redfish.RemoteTarget;
import com.intel.podm.business.entities.redfish.StorageService;
import com.intel.podm.business.redfish.EntityTreeTraverser;
import com.intel.podm.business.redfish.services.mappers.EntityToDtoMapper;
import com.intel.podm.business.services.context.Context;
import com.intel.podm.business.services.redfish.ReaderService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import static com.intel.podm.business.dto.redfish.CollectionDto.Type.REMOTE_TARGETS;
import static com.intel.podm.business.redfish.ContextCollections.getAsIdSet;
import static com.intel.podm.business.redfish.Contexts.toContext;
import static javax.transaction.Transactional.TxType.REQUIRED;

@RequestScoped
class RemoteTargetServiceImpl implements ReaderService<RemoteTargetDto> {
    @Inject
    private EntityTreeTraverser traverser;

    @Inject
    private EntityToDtoMapper entityToDtoMapper;

    @Transactional(REQUIRED)
    @Override
    public CollectionDto getCollection(Context serviceContext) throws ContextResolvingException {
        StorageService storageService = (StorageService) traverser.traverse(serviceContext);
        return new CollectionDto(REMOTE_TARGETS, getAsIdSet(storageService.getRemoteTargets()));
    }

    @Transactional(REQUIRED)
    @Override
    public RemoteTargetDto getResource(Context remoteTargetContext) throws ContextResolvingException {
        RemoteTarget remoteTarget = (RemoteTarget) traverser.traverse(remoteTargetContext);
        RemoteTargetDto dto = (RemoteTargetDto) entityToDtoMapper.map(remoteTarget);

        LogicalDrive logicalDrive = remoteTarget.getLogicalDrives().stream().findAny().orElse(null);
        if (logicalDrive != null) {
            Context driveContext = toContext(logicalDrive);
            dto.getAddresses().stream()
                .flatMap(addressWrapper -> addressWrapper.getIscsiAddressDto().getTargetLuns().stream())
                .forEach(lunDto -> lunDto.setLogicalDrive(driveContext));
        }
        return dto;
    }
}
