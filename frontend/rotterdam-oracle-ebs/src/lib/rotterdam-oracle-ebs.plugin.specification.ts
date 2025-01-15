/*
 * Copyright 2015-2025 Ritense BV, the Netherlands.
 *
 * Licensed under EUPL, Version 1.2 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {PluginSpecification} from '@valtimo/plugin';
import {ROTTERDAM_ORACLE_EBS_PLUGIN_LOGO_BASE64} from './assets';
import {
    OracleEbsConfigurationComponent
} from "./components/oracle-ebs-configuration/oracle-ebs-configuration.component";

const RotterdamOracleEbsPluginSpecification: PluginSpecification = {
    pluginId: 'rotterdam-oracle-ebs',
    pluginConfigurationComponent: OracleEbsConfigurationComponent,
    pluginLogoBase64: ROTTERDAM_ORACLE_EBS_PLUGIN_LOGO_BASE64,
    functionConfigurationComponents: {
    },
    pluginTranslations: {
        nl: {
            title: 'Oracle E-Business Suite for Rotterdam',
            description: 'Oracle E-Business Suite plugin',
        },
        en: {
            title: 'Oracle E-Business Suite for Rotterdam',
            description: 'Oracle E-Business Suite plugin',
        },
        de: {
            title: 'Oracle E-Business Suite for Rotterdam',
            description: 'Oracle E-Business Suite plugin',
        },
    },
};

export {RotterdamOracleEbsPluginSpecification};
