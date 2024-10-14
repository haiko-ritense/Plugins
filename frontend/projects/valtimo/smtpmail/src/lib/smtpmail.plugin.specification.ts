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
import {SmtpMailPluginConfigurationComponent} from './components/smtp-mail-configuration/smtpmail-plugin-configuration.component';
import {SMTP_MAIL_PLUGIN_LOGO_BASE64} from './assets';
import {SendMailConfigurationComponent} from "./components/send-mail/send-mail-configuration.component";

const smtpmailPluginSpecification: PluginSpecification = {
  pluginId: 'smtp-mail',
  pluginConfigurationComponent: SmtpMailPluginConfigurationComponent,
  pluginLogoBase64: SMTP_MAIL_PLUGIN_LOGO_BASE64,
  functionConfigurationComponents: {
    'send-mail': SendMailConfigurationComponent
  },
  pluginTranslations: {
    nl: {
      title: "SMTP Mail",
      description: "Met deze plugin kan een e-mail via SMTP worden verzonden. De plugin haalt zijn e-mailinhoud en bijlagen op uit de Documenten API en werkt met proceskoppelingen",
      host: "Host",
      port: "Port",
      username: "Gebruikersnaam",
      password: "Wachtwoord",
      protocol: "Protocol",
      auth: "Authenticatie",
      debug: "Debug",
      startTlsEnable: "STARTTLS Inschakelen",
      sender: "Afzender",
      recipients: "Ontvanger",
      cc: "CC",
      bcc: "BCC",
      subject: "Onderwerp",
      attachments: "Bijlagen",
      content: "Inhoud"
    },
    en: {
      title: "SMTP Mail",
      description: "This plugin allows sending an email via SMTP. The plugin retrieves its email content and attachments from the Documenten API and works with process links.",
      host: "Host",
      port: "Port",
      username: "Username",
      password: "Password",
      protocol: "Protocol",
      auth: "Auth",
      debug: "Debug",
      startTlsEnable: "Enable STARTTLS",
      sender: "Sender",
      recipients: "Recipient",
      cc: "CC",
      bcc: "BCC",
      subject: "Subject",
      attachments: "Attachments",
      content: "Content"
    },
    de: {
      title: "SMTP Mail",
      description: "Mit diesem Plugin kann eine E-Mail über SMTP gesendet werden. Das Plugin bezieht seinen E-Mail-Inhalt und die Anhänge aus der Dokumenten-API und funktioniert mit Prozessverknüpfungen.",
      host: "Host",
      port: "Port",
      username: "Benutzername",
      password: "Passwort",
      protocol: "Protokoll",
      auth: "Authentifizierung",
      debug: "Debug",
      startTlsEnable: "STARTTLS aktivieren",
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

export {smtpmailPluginSpecification};
