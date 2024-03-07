/*
 * Copyright 2015-2024. Ritense BV, the Netherlands.
 *
 * Licensed under EUPL, Version 1.2 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import {PluginSpecification} from '@valtimo/plugin';
import {BerkelybridgeTextgeneratorConfigurationComponent} from './components/berkelybridge-textgenerator-configuration/berkelybridge-textgenerator-configuration.component';
import {AMSTERDAM_EMAILAPI_PLUGIN_LOGO_BASE64} from './assets';
import {TextGenerationConfigurationComponent} from "./components/text-generation/text-generation-configuration.component";

const berkelybridgeTextgeneratorPluginSpecification: PluginSpecification = {
  pluginId: 'bbtextgenerator',
  pluginConfigurationComponent: BerkelybridgeTextgeneratorConfigurationComponent,
  pluginLogoBase64: AMSTERDAM_EMAILAPI_PLUGIN_LOGO_BASE64,
  functionConfigurationComponents: {
    'genereer-tekst': TextGenerationConfigurationComponent
  },
  pluginTranslations: {
    nl: {
      title: 'Berkely Bridge tekst en PDF generator',
      description:
          'Met de Berkely Bridge tekst en PDF generator plugin kun je in een process tekst of PDF\'s genereren',
      configurationTitle: 'Configuratienaam',
      configurationTitleTooltip:
          'Onder deze naam zal de plugin te herkennen zijn in de rest van de applicatie',
      berkelybridgeBaseUrl: 'Berkely Bridge base url',
      berkelybridgeBaseUrlTooltip:
          'Vul hier url in waarop Berkely Bridge is te bereiken.',
      modelId: 'Model ID',
      modelIdTooltip: 'Vul het Berkely Bridge model ID in',
      templateId: 'Template ID',
      templateIdTooltip: 'Vul het template ID in om een tekst of PDF te genereren',
      naam: 'Naam',
      nameTooltip: 'Vul hier de naam in van de te genereren file.',
      format: "Formaat",
      formatTooltip: 'Vul hier het formaat in van de te genereren tekst of file',
      parameters: "Parameters",
      parametersTooltip: 'Vul hier de parameters die worden gebruikt in de template',
      key: "Formaat",
      keyTooltip: 'Vul hier het formaat in van de te genereren tekst of file',
      value: "Formaat",
      valueTooltip: 'Vul hier het formaat in van de te genereren tekst of file',
      addParameter: 'Voeg een parameter toe',
    },
    en: {
      title: 'Berkely Bridge text and PDF generator',
      description:
          'With the Berkely Bridge tekst and PDF generator plugin you can generate a text or PDF in a process step',
      configurationTitle: 'Configuratienaam',
      configurationTitleTooltip:
          'Onder deze naam zal de plugin te herkennen zijn in de rest van de applicatie',
      berkelybridgeBaseUrl: 'Berkely Bridge base url',
      berkelybridgeBaseUrlTooltip:
          'Vul hier url in waarop Berkely Bridge is te bereiken.',
      modelId: 'Model ID',
      modelIdTooltip: 'Vul het Berkely Bridge model ID in',
      templateId: 'Template ID',
      templateIdTooltip: 'Vul het template ID in om een tekst of PDF te genereren',
      naam: 'Naam',
      nameTooltip: 'Vul hier de naam in van de te genereren file.',
      format: "Formaat",
      formatTooltip: 'Vul hier het formaat in van de te genereren tekst of file',
      parameters: "Parameters",
      parametersTooltip: 'Vul hier de parameters die worden gebruikt in de template',
      key: "Formaat",
      keyTooltip: 'Vul hier het formaat in van de te genereren tekst of file',
      value: "Formaat",
      valueTooltip: 'Vul hier het formaat in van de te genereren tekst of file',
      addParameter: 'Voeg een parameter toe',
    },
    de: {
      title: 'Berkely Bridge tekst en PDF generator',
      description:
          'Met de Berkely Bridge tekst en PDF generator plugin kun je in een process tekst of PDF\'s genereren',
      configurationTitle: 'Configuratienaam',
      configurationTitleTooltip:
          'Onder deze naam zal de plugin te herkennen zijn in de rest van de applicatie',
      berkelybridgeBaseUrl: 'Berkely Bridge base url',
      berkelybridgeBaseUrlTooltip:
          'Vul hier url in waarop Berkely Bridge is te bereiken.',
      modelId: 'Model ID',
      modelIdTooltip: 'Vul het Berkely Bridge model ID in',
      templateId: 'Template ID',
      templateIdTooltip: 'Vul het template ID in om een tekst of PDF te genereren',
      naam: 'Naam',
      nameTooltip: 'Vul hier de naam in van de te genereren file.',
      format: "Formaat",
      formatTooltip: 'Vul hier het formaat in van de te genereren tekst of file',
      parameters: "Parameters",
      parametersTooltip: 'Vul hier de parameters die worden gebruikt in de template',
      key: "Key",
      keyTooltip: 'Vul hier de key van property zoals in de template vermeldt',
      value: "Waarde",
      valueTooltip: 'Vul hier de waarde in voor de property.',
      addParameter: 'Voeg een parameter toe',
    },
  },
};

export {berkelybridgeTextgeneratorPluginSpecification};
