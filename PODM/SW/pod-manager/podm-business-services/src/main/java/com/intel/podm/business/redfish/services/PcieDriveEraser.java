/*
 * Copyright (c) 2017 Intel Corporation
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

import com.intel.podm.business.BusinessApiException;
import com.intel.podm.business.ResourceStateMismatchException;
import com.intel.podm.business.entities.redfish.Drive;
import com.intel.podm.business.redfish.EntityTreeTraverser;
import com.intel.podm.business.redfish.services.actions.PcieDriveActionsInvoker;
import com.intel.podm.business.services.context.Context;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.Transactional;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

@Dependent
public class PcieDriveEraser {
    @Inject
    private EntityTreeTraverser traverser;

    @Inject
    private PcieDriveActionsInvoker pcieDriveActionsInvoker;

    @Transactional(REQUIRES_NEW)
    public void secureErase(Context context) throws BusinessApiException {
        Drive drive = (Drive) traverser.traverse(context);

        if (!drive.isPresent()) {
            throw new ResourceStateMismatchException("SecureErase action cannot be performed on PCIeDrive which is not in 'Present' state");
        }

        if (drive.getMetadata().isAllocated()) {
            throw new ResourceStateMismatchException("SecureErase action cannot be performed on allocated PCIeDrive");
        }

        pcieDriveActionsInvoker.secureErase(drive);
    }
}
