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
import {PRINTSTRAAT_PLUGIN_LOGO_BASE64} from './assets';
import {
  PrintstraatPluginConfigurationComponent
} from "./components/printstraat-configuration/printstraat-plugin-configuration.component";

const printstraatPluginSpecification: PluginSpecification = {
  pluginId: 'printstraat',
  pluginLogoBase64: PRINTSTRAAT_PLUGIN_LOGO_BASE64,
  pluginConfigurationComponent: PrintstraatPluginConfigurationComponent,
  functionConfigurationComponents: {
  },
  pluginTranslations: {
    nl: {
      title: "printstraat",
      description: "Met deze plugin kan je documenten naar de printstraat versturen",
      host: "host",
      username: "gebruikersnaam",
      password: "wachtwoord",
      protocol: "protocol",
      auth: "authenticatie",
      debug: "debug",
      startTlsEnable: "startTls inschakelen",
      sender: "afzender",
      recipients: "ontvanger",
      cc: "cc",
      bcc: "bcc",
      subject: "onderwerp",
      attachments: "bijlagen",
      content: "inhoud"
    },
    en: {
      title: "printstraat",
      description: "With this plugin you can send documents to the printing station",
      host: "host",
      username: "username",
      password: "password",
      protocol: "protocol",
      auth: "auth",
      debug: "debug",
      startTlsEnable: "startTlsEnable",
      sender: "sender",
      recipients: "recipient",
      cc: "cc",
      bcc: "bcc",
      subject: "subject",
      attachments: "attachments",
      content: "content"
    },
    de: {
      title: "printstraat",
      description: "Mit diesem Plugin können Sie Dokumente an die Druckstation senden",
      host: "Host",
      username: "Benutzername",
      password: "Passwort",
      protocol: "Protokoll",
      auth: "Authentifizierung",
      debug: "Debug",
      startTlsEnable: "StartTLS aktivieren",
      sender: "Absender",
      recipients: "Empfänger",
      cc: "CC",
      bcc: "BCC",
      subject: "Betreff",
      attachments: "Anhänge",
      content: "Inhalt"
    },
  },
};

export {printstraatPluginSpecification};
