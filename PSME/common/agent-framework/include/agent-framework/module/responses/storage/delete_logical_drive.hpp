/*!
 * @copyright
 * Copyright (c) 2015-2017 Intel Corporation
 *
 * @copyright
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * @copyright
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * @copyright
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * @file responses/storage/delete_logical_drive.hpp
 * @brief Definition of storage DeleteLogicalDrive response
 * */

#pragma once



#include "agent-framework/module/utils/optional_field.hpp"
#include "agent-framework/module/model/attributes/oem.hpp"
#include "agent-framework/module/constants/command.hpp"
#include "json-wrapper/json-wrapper.hpp"

#include <string>

namespace agent_framework {
namespace model {
namespace responses {

/*! DeleteLogicalDrive response */
class DeleteLogicalDrive  {
public:
    using Oem = agent_framework::model::attribute::Oem;

    /*!
     * @brief explicit DeleteLogicalDrive response constructor
     */
    DeleteLogicalDrive(Oem oem = Oem{});


    static std::string get_command() {
        return literals::Command::DELETE_LOGICAL_DRIVE;
    }


    /*!
     * @brief Get task UUID
     * @return task UUID
     * */
    const OptionalField<std::string>& get_task() const {
        return m_task;
    }


    /*!
     * @brief Set task UUID
     * @param[in] task the task UUID
     * */
    void set_task(const OptionalField<std::string>& task) {
        m_task = task;
    }


    /*!
     * @brief Transform request to Json
     *
     * @return created Json value
     */
    json::Json to_json() const;


    /*!
     * @brief create DeleteLogicalDrive from Json
     *
     * @param[in] json the input argument
     *
     * @return new DeleteLogicalDrive
     */
    static DeleteLogicalDrive from_json(const json::Json& json);

private:
    OptionalField<std::string> m_task{};
    Oem m_oem{};
};

}
}
}
