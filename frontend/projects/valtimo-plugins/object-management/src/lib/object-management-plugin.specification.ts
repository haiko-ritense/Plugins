/*
 * Copyright 2015-2022 Ritense BV, the Netherlands.
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
import {ObjectManagementConfigurationComponent} from "./components/object-management-configuration/object-management-configuration.component";
import {OBJECT_MANAGEMENT_PLUGIN_LOGO_BASE64} from "./assets/object-management-plugin-logo";
import {CreateObjectConfigurationComponent} from "./components/create-object/create-object-configuration.component";

const objectManagementPluginSpecification: PluginSpecification = {
  pluginId: 'object-management',
  pluginConfigurationComponent: ObjectManagementConfigurationComponent,
  pluginLogoBase64: OBJECT_MANAGEMENT_PLUGIN_LOGO_BASE64,
  functionConfigurationComponents: {
    'create-object': CreateObjectConfigurationComponent
  },
  pluginTranslations: {
    nl: {
      title: 'Object management',
      url: 'Object management',
      description: 'Plugin voor het uitvoeren van CRUD acties in de Object registratie',
      configurationTitle: 'Configuratienaam',
    },
    en: {
      title: 'Object management',
      url: 'Object management',
      description: 'Plugin for CRUD actions in the Objects registration',
      configurationTitle: 'Configuration Name',
    }
  },
};

export {objectManagementPluginSpecification};
