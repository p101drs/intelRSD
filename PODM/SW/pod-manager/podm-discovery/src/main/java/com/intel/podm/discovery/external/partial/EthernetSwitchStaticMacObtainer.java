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

package com.intel.podm.discovery.external.partial;

import com.intel.podm.business.entities.dao.DiscoverableEntityDao;
import com.intel.podm.business.entities.redfish.EthernetSwitchStaticMac;
import com.intel.podm.business.entities.redfish.ExternalService;
import com.intel.podm.client.WebClient;
import com.intel.podm.client.WebClientBuilder;
import com.intel.podm.client.WebClientRequestException;
import com.intel.podm.client.resources.redfish.EthernetSwitchStaticMacResource;
import com.intel.podm.common.types.Id;
import com.intel.podm.mappers.redfish.EthernetSwitchStaticMacMapper;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.net.URI;

@Dependent
public class EthernetSwitchStaticMacObtainer {
    @Inject
    private WebClientBuilder webClientBuilder;

    @Inject
    private DiscoverableEntityDao discoverableEntityDao;

    @Inject
    private EthernetSwitchStaticMacMapper mapper;

    public EthernetSwitchStaticMac discover(ExternalService service, URI uri) throws WebClientRequestException {
        try (WebClient webClient = webClientBuilder.newInstance(service.getBaseUri()).retryable().build()) {
            EthernetSwitchStaticMacResource resource = (EthernetSwitchStaticMacResource) webClient.get(uri);

            return readResource(service, uri, resource);
        }
    }

    private EthernetSwitchStaticMac readResource(ExternalService service, URI uri, EthernetSwitchStaticMacResource resource) {
        URI sourceUri = URI.create(uri.getPath());
        Id entityId = resource.getGlobalId(service.getId(), sourceUri);

        EthernetSwitchStaticMac entity = discoverableEntityDao.findOrCreateEntity(service, entityId, sourceUri, EthernetSwitchStaticMac.class);

        mapper.map(resource, entity);

        return entity;
    }
}
