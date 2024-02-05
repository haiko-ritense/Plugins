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
import {PublictaskPluginConfigurationComponent} from './components/public-task-configuration/publictask-plugin-configuration.component';
import {PUBLIC_TASK_PLUGIN_LOGO_BASE64} from './assets';
import {CreatePublicTaskConfigurationComponent} from "./components/create-public-task/create-public-task-configuration.component";

const publictaskPluginSpecification: PluginSpecification = {
  pluginId: 'public-task',
  pluginConfigurationComponent: PublictaskPluginConfigurationComponent,
  pluginLogoBase64: PUBLIC_TASK_PLUGIN_LOGO_BASE64,
  functionConfigurationComponents: {
    'create-public-task': CreatePublicTaskConfigurationComponent
  },
  pluginTranslations: {
    nl: {
      title: "public-task",
      description: "Met deze plugin kan een e-mail via SMTP worden verzonden. De plugin haalt zijn e-mailinhoud en bijlagen op uit de Documenten API en werkt met proceskoppelingen",
      pvTaskHandler: "the process variable in which the taskhandler is saved. Start with pv:",
      ttl: "Time To Live of the URL. Default is 28 days"
    },
    en: {
      title: "public-task",
      description: "This plugin allows sending an email via SMTP. The plugin retrieves its email content and attachments from the Documenten API and works with process links.",
      pvTaskHandler: "the process variable in which the taskhandler is saved. Start with pv:",
      ttl: "Time To Live of the URL. Default is 28 days"
    },
    de: {
      title: "public-task",
      description: "Mit diesem Plugin kann eine E-Mail über SMTP gesendet werden. Das Plugin bezieht seinen E-Mail-Inhalt und die Anhänge aus der Dokumenten-API und funktioniert mit Prozessverknüpfungen.",
      pvTaskHandler: "the process variable in which the taskhandler is saved. Start with pv:",
      ttl: "Time To Live of the URL. Default is 28 days"
    }
  },
};

export {publictaskPluginSpecification};
