/*
 * Copyright (c) 2015-2017 Intel Corporation
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

package com.intel.podm.redfish.resources;

import com.intel.podm.business.dto.EventServiceDto;
import com.intel.podm.business.services.context.Context;
import com.intel.podm.business.services.redfish.ReaderService;
import com.intel.podm.redfish.json.templates.RedfishResourceAmazingWrapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static com.intel.podm.business.services.context.Context.contextOf;
import static com.intel.podm.business.services.context.ContextType.EVENT_SERVICE;
import static com.intel.podm.common.types.Id.id;
import static com.intel.podm.common.types.redfish.ResourceNames.EVENT_SUBSCRIPTION_RESOURCE_NAME;
import static com.intel.podm.redfish.OptionsResponseBuilder.newOptionsForResourceBuilder;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@RequestScoped
@Produces(APPLICATION_JSON)
public class EventServiceResource extends BaseResource {
    @Inject
    private ReaderService<EventServiceDto> readerService;

    @GET
    @Override
    public RedfishResourceAmazingWrapper get() {
        Context context = contextOf(id(""), EVENT_SERVICE);
        EventServiceDto eventServiceDto = getOrThrow(() -> readerService.getResource(context));
        return new RedfishResourceAmazingWrapper(context, eventServiceDto);
    }

    @Path(EVENT_SUBSCRIPTION_RESOURCE_NAME)
    public EventSubscriptionCollectionResource getSubscriptions() {
        return getResource(EventSubscriptionCollectionResource.class);
    }

    @Override
    protected Response createOptionsResponse() {
        return newOptionsForResourceBuilder().build();
    }
}
